package com.qzt.module.system.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel("管理后台 - 用户导入 Response VO")
@Data
@Builder
public class UserImportRespVO {

    @ApiModelProperty(name = "创建成功的用户名数组", required = true)
    private List<String> createUsernames;

    @ApiModelProperty(name = "更新成功的用户名数组", required = true)
    private List<String> updateUsernames;

    @ApiModelProperty(name = "导入失败的用户集合,key 为用户名，value 为失败原因", required = true)
    private Map<String, String> failureUsernames;

}
