package com.qzt.module.system.domain.vo.user;

import com.qzt.common.validation.InEnum;
import com.qzt.common.validation.Mobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("获取短信验证码")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsCodeVO {

    /**
     * 手机号
     */
    @ApiModelProperty(name = "手机号", required = true)
    @Mobile
    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    /**
     * 发送场景
     */
    @ApiModelProperty(name = "验证码场景", notes = "(5注册 6手机号登陆 7忘记密码 8修改手机号)", access = "5,6,7,8", required = true, example = "6")
    @NotNull(message = "发送场景不能为空")
    @InEnum(intValues = {5, 6, 7, 8})
    private Integer scene;


}
