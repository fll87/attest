package com.qzt.module.system.domain.vo.user;

import com.qzt.common.validation.Mobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@ApiModel("用户 手机 + 验证码登录")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsLoginVO {

    @ApiModelProperty(name = "手机号", required = true)
    @NotEmpty(message = "手机号不能为空")
    @Mobile
    private String mobile;

    @ApiModelProperty(name = "手机验证码", required = true)
    @NotEmpty(message = "手机验证码不能为空")
    @Length(min = 4, max = 6, message = "手机验证码长度为 4-6 位")
    @Pattern(regexp = "^[0-9]+$", message = "手机验证码必须都是数字")
    private String code;

    @ApiModelProperty(name = "唯一标识", notes = "用于校验验证码", required = true)
    @NotEmpty(message = "唯一标识不能为空")
    private String uuid;
}
