package com.qzt.module.system.domain.vo.user;

import com.qzt.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户 Excel 导入 VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class UserImportExcelVO {

    @Excel(name = "登录名称")
    private String username;

    @Excel(name = "用户名称")
    private String nickname;

    @Excel(name = "用户邮箱")
    private String email;

    @Excel(name = "手机号码")
    private String mobile;

    @Excel(name = "用户性别", dictConvert = "system_user_sex")
    private Integer sex;

    @Excel(name = "账号状态", dictConvert = "common_status")
    private Integer status;

}
