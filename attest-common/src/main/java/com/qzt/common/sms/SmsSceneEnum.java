package com.qzt.common.sms;

import cn.hutool.core.util.ArrayUtil;
import com.qzt.common.core.domain.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 用户短信发送场景的枚举
 *
 * @author
 */
@Getter
@AllArgsConstructor
public enum SmsSceneEnum implements IntArrayValuable {

    REGISTER_AUDIT(1, "sms-register-audit", "注册审核结果"),
    ACCOUNT_STATE(2, "sms-account-state", "账号冻结/解冻"),
    CERT_STATE(3, "sms-cert-state", "证书失效或临近失效"),
    VERIFICATION_CODE_REGISTER(5, "sms-verification-code", "验证码-注册"),
    VERIFICATION_CODE_LOGIN(6, "sms-verification-code", "验证码-登录"),
    VERIFICATION_CODE_FORGOT_PWD(7, "sms-verification-code", "验证码-忘记密码"),
    VERIFICATION_CODE_UPDATE_MOBILE(8, "sms-verification-code", "验证码-修改手机号"),
    SYS_MSG(9, "sms-sys-msg", "平台消息"),
    ATTEST_STATE(21, "sms-attest-state", "认证流程相关");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(SmsSceneEnum::getScene).toArray();

    /**
     * 验证场景的编号
     */
    private final Integer scene;
    /**
     * 模版编码
     */
    private final String templateCode;
    /**
     * 描述
     */
    private final String description;

    @Override
    public int[] array() {
        return ARRAYS;
    }

    public static SmsSceneEnum getCodeByScene(Integer scene) {
        return ArrayUtil.firstMatch(sceneEnum -> sceneEnum.getScene().equals(scene),
                values());
    }

}
