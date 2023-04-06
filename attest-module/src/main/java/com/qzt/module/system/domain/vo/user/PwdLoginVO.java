package com.qzt.module.system.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@ApiModel("用户 用户名/手机/邮箱 + 密码登录")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PwdLoginVO {
    @ApiModelProperty(name = "登录账号（用户名/手机/邮箱）", required = true)
    @NotEmpty(message = "登录账号不能为空")
    private String username;

    @ApiModelProperty(name = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    @Length(min = 5, max = 20, message = "密码长度为 5-20 位")
    private String password;

    @ApiModelProperty(name = "验证码", required = true)
    private String code;

    @ApiModelProperty(name = "唯一标识",notes = "用于校验验证码",required = true)
    @NotEmpty(message = "唯一标识不能为空")
    private String uuid;

}
