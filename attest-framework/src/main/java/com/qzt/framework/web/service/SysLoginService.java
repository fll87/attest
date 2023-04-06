package com.qzt.framework.web.service;

import com.qzt.common.constant.CacheConstants;
import com.qzt.common.constant.Constants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.constant.UserConstants;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.domain.model.LoginUser;
import com.qzt.common.core.redis.RedisCache;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.DateUtils;
import com.qzt.common.utils.MessageUtils;
import com.qzt.common.utils.StringUtils;
import com.qzt.common.utils.ip.IpUtils;
import com.qzt.framework.manager.AsyncManager;
import com.qzt.framework.manager.factory.AsyncFactory;
import com.qzt.framework.security.context.AuthenticationContextHolder;
import com.qzt.module.system.domain.vo.user.LoginVO;
import com.qzt.module.system.service.ISysConfigService;
import com.qzt.module.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 *
 * @author
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录验证
     *
     * @param vo
     * @return 结果
     */
    public String login(LoginVO vo) {
        // 验证码校验
        String redisKeyPrefix = UserConstants.USER_LOGIN_PWD.equals(vo.getType()) ? CacheConstants.CAPTCHA_CODE_KEY : CacheConstants.SMS_CODE_KEY;
        validateCode(vo.getUsername(), vo.getCode(), vo.getUuid(), redisKeyPrefix);
        // 登录前置校验
        loginPreCheck(vo.getUsername());
        // 用户验证
        Authentication authentication = null;
        try {
            String principal = vo.getUsername() + "_" + vo.getType();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, vo.getPassword());
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            AsyncManager.me().execute(AsyncFactory.recordLoginLog(vo.getUsername(), Constants.LOGIN_FAIL, e.getMessage()));
            throw new ServiceException(SysErrorCodeConstants.LOGIN_BAD_CREDENTIALS);
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLoginLog(vo.getUsername(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     *
     * @param username       用户名
     * @param code           验证码
     * @param uuid           唯一标识
     * @param redisKeyPrefix redis Key前缀
     * @return 结果
     */
    public void validateCode(String username, String code, String uuid, String redisKeyPrefix) {
        boolean bool = true;
        if (UserConstants.USER_LOGIN_PWD.equals(redisKeyPrefix)) {
            bool = configService.selectCaptchaEnabled();
        } else if (UserConstants.USER_LOGIN_SMS.equals(redisKeyPrefix)) {
            configService.selectSmsEnabled();
        }
        if (bool) {
            String verifyKey = redisKeyPrefix + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, SysErrorCodeConstants.CAPTCHA_EXPIRED.getMsg()));
                throw new ServiceException(SysErrorCodeConstants.CAPTCHA_EXPIRED);
            }
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, SysErrorCodeConstants.CAPTCHA_CODE_ERROR.getMsg()));
                throw new ServiceException(SysErrorCodeConstants.CAPTCHA_CODE_ERROR);
            }
        }
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     */
    public void loginPreCheck(String username) {
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            AsyncManager.me().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, SysErrorCodeConstants.USER_IS_BLACK.getMsg()));
            throw new ServiceException(SysErrorCodeConstants.USER_IS_BLACK);
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }


}
