package com.qzt.web.controller.common;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.Producer;
import com.qzt.common.config.AttestConfig;
import com.qzt.common.constant.CacheConstants;
import com.qzt.common.constant.Constants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.domain.AjaxResult;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.redis.RedisCache;
import com.qzt.common.core.text.StrFormatter;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.sms.SMSManager;
import com.qzt.common.sms.SmsSceneEnum;
import com.qzt.common.utils.MessageUtils;
import com.qzt.common.utils.ip.IpUtils;
import com.qzt.common.utils.sign.Base64;
import com.qzt.common.utils.uuid.IdUtils;
import com.qzt.module.system.domain.SysSmsSendLog;
import com.qzt.module.system.domain.vo.user.SmsCodeVO;
import com.qzt.module.system.service.ISysConfigService;
import com.qzt.module.system.service.ISysSmsSendLogService;
import com.qzt.module.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.util.RandomUtil.randomInt;

@Api(tags = "验证码")
@RestController
@Validated
@RequestMapping("code")
public class CodeController {
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysSmsSendLogService smsSendLogService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 生成验证码
     */
    @PostMapping("/captcha")
    @ApiOperation(value = "生成图形验证码", notes = "data参数说明：<br/>" +
            "captchaEnabled： 是否开启验证码<br/>" +
            "uuid： 唯一标识<br/>" +
            "img： 图片流")
    public AjaxResult getCode() {
        Map data = new HashMap<>();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        data.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return AjaxResult.success(data);
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = AttestConfig.getCaptchaType();
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxResult.error(MessageUtils.exception(SysErrorCodeConstants.CAPTCHA_GEN_ERROR, e.getMessage()));
        }

        data.put("uuid", uuid);
        data.put("img", Base64.encode(os.toByteArray()));
        return AjaxResult.success(data);
    }

    /**
     * 生成手机验证码
     */
    @PostMapping("/sms")
    @ApiOperation(value = "生成手机验证码")
    public AjaxResult getSmsCode(@RequestBody @Valid SmsCodeVO reqVO) {
        Map data = new HashMap<>();
        boolean smsEnabled = configService.selectSmsEnabled();
        if (!smsEnabled) {
            return AjaxResult.success("短信发送功能已禁用");
        }
        SmsSceneEnum sceneEnum = SmsSceneEnum.getCodeByScene(reqVO.getScene());
        if (Objects.isNull(sceneEnum)) {
            return AjaxResult.error(MessageUtils.exception(SysErrorCodeConstants.SMS_SCENE_NOT_FOUND, reqVO.getScene()));
        }
        // 校验是否可以发送验证码
        SysSmsSendLog lastSmsLog = validSendSms(reqVO.getMobile(),sceneEnum);
        boolean hasLog = lastSmsLog != null && DateUtil.isSameDay(lastSmsLog.getCreateTime(), new Date());
        // 生成验证码
        String code = String.valueOf(randomInt(1000, 9999));
        String template = configService.selectSmsTemplateByCode(sceneEnum.getTemplateCode());
        if (StrUtil.isEmpty(template)) {
            throw new ServiceException(SysErrorCodeConstants.SMS_SEND_TEMPLATE_IS_EMPTY);
        }
        String content = StrFormatter.format(template,code);

        // 发送短信
        SMSManager.sendSMS(reqVO.getMobile(),content);

        //记录短信发送日志
        SysSmsSendLog smsRecord = SysSmsSendLog.builder().mobile(reqVO.getMobile()).content(content).scene(reqVO.getScene())
                .todayIndex( hasLog ? lastSmsLog.getTodayIndex() + 1 : 1)
                .createIp(IpUtils.getIpAddr()).build();
        smsSendLogService.save(smsRecord);

        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.SMS_CODE_KEY + uuid;
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        data.put("uuid",uuid);
        return AjaxResult.success(data);
    }

    private SysSmsSendLog validSendSms(String mobile, SmsSceneEnum sceneEnum) {
        //验证手机号
        SysUser sysUser = sysUserService.selectUserByUsername(mobile);
        if(SmsSceneEnum.VERIFICATION_CODE_REGISTER.equals(sceneEnum) || SmsSceneEnum.VERIFICATION_CODE_UPDATE_MOBILE.equals(sceneEnum)){
            if(Objects.nonNull(sysUser)){
                throw new ServiceException(SysErrorCodeConstants.MOBILE_IS_USED);
            }
        }else{
            if(Objects.isNull(sysUser)){
                throw new ServiceException(SysErrorCodeConstants.MOBILE_NOT_EXISTS);
            }
        }

        SysSmsSendLog lastSmsLog = smsSendLogService.selectLastByMobile(mobile);
        if (lastSmsLog != null) {
            // 发送过于频繁
            if (DateUtil.between(lastSmsLog.getCreateTime(), DateUtil.date(), DateUnit.MINUTE) < Constants.CAPTCHA_EXPIRATION) {
                throw new ServiceException(SysErrorCodeConstants.SMS_CODE_SEND_TOO_FAST);
            }
            // 超过当天发送的上限
            if (DateUtil.isSameDay(lastSmsLog.getCreateTime(), new Date()) && lastSmsLog.getTodayIndex() >= 20) {
                throw new ServiceException(SysErrorCodeConstants.SMS_CODE_EXCEED_SEND_MAXIMUM_QUANTITY_PER_DAY);
            }
            // TODO 每个 IP 每天可发送数量
            // TODO 每个 IP 每小时可发送数量
        }
        return lastSmsLog;
    }
}
