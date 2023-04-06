package com.qzt.common.constant;

import com.qzt.common.core.domain.ErrorCode;

/**
 * 企业前端 错误码枚举类
 *
 * app 系统，使用 2-001-000-000 段
 */
public interface AppErrorCodeConstants {

    // ========== 注册 模块 2001000000 ==========
    ErrorCode REG_CLOSED = new ErrorCode(1002000000, "当前系统没有开启注册功能");
    ErrorCode REG_USERNAME_EXISTS = new ErrorCode(1002003001, "注册账号已存在");

}
