package com.qzt.module.system.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@ApiModel("企业注册")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterVO {
    @ApiModelProperty(name = "用户名", required = true)
    @NotEmpty(message = "用户名不能为空")
    private String username;
}
