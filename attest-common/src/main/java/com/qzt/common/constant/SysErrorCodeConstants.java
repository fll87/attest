package com.qzt.common.constant;

import com.qzt.common.core.domain.ErrorCode;

/**
 * System 错误码枚举类
 *
 * system 系统，使用 1-002-000-000 段
 */
public interface SysErrorCodeConstants {

    // ========== 登录注册 模块 1002000000 ==========
    ErrorCode LOGIN_BAD_CREDENTIALS = new ErrorCode(1002000000, "登录失败，账号密码不正确");
    ErrorCode LOGIN_FAIL_LOCK = new ErrorCode(1002000001, "密码输入错误{}次，帐户锁定{}分钟");
    ErrorCode LOGIN_PWD_ERROR = new ErrorCode(1002000002, "密码输入错误{}次");
    ErrorCode CAPTCHA_GEN_ERROR = new ErrorCode(1002000003, "验证码生成失败，原因：{}");
    ErrorCode CAPTCHA_EXPIRED = new ErrorCode(1002000004, "验证码已失效");
    ErrorCode CAPTCHA_CODE_ERROR = new ErrorCode(1002000005, "验证码不正确");
    ErrorCode USER_PASSWORD_FAILED = new ErrorCode(1002000006, "用户密码校验失败");
    ErrorCode USER_IS_DISABLE = new ErrorCode(1002000007, "用户【{}】已被禁用");
    ErrorCode USER_IS_DELETED = new ErrorCode(1002000008, "用户【{}】已被删除");
    ErrorCode MOBILE_NOT_EXISTS = new ErrorCode(1002000009, "手机号不存在");
    ErrorCode MOBILE_IS_USED = new ErrorCode(1002000010, "手机号已使用");
    ErrorCode USER_IS_BLACK = new ErrorCode(1002000011, "访问IP已被列入系统黑名单");
    ErrorCode AUTH_TOKEN_EXPIRED = new ErrorCode(1002000012, "Token 已经过期");



    // ========== 菜单模块 1002001000 ==========
    ErrorCode MENU_NAME_DUPLICATE = new ErrorCode(1002001000, "已经存在该名字的菜单");
    ErrorCode MENU_PARENT_NOT_EXISTS = new ErrorCode(1002001001, "父菜单不存在");
    ErrorCode MENU_PARENT_ERROR = new ErrorCode(1002001002, "不能设置自己为父菜单");
    ErrorCode MENU_NOT_EXISTS = new ErrorCode(1002001003, "菜单不存在");
    ErrorCode MENU_EXISTS_CHILDREN = new ErrorCode(1002001004, "存在子菜单，无法删除");
    ErrorCode MENU_PARENT_NOT_DIR_OR_MENU = new ErrorCode(1002001005, "父菜单的类型必须是目录或者菜单");
    ErrorCode MENU_PATH_NOT_HTTP = new ErrorCode(1002001006, "外链菜单地址必须以http(s)://开头");
    ErrorCode MENU_EXIST_ROLE = new ErrorCode(1002001007, "菜单已分配,不允许删除");


    // ========== 角色模块 1002002000 ==========
    ErrorCode ROLE_NOT_EXISTS = new ErrorCode(1002002000, "角色不存在");
    ErrorCode ROLE_NAME_DUPLICATE = new ErrorCode(1002002001, "已经存在名为【{}】的角色");
    ErrorCode ROLE_CODE_DUPLICATE = new ErrorCode(1002002002, "已经存在编码为【{}】的角色");
    ErrorCode ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE = new ErrorCode(1002002003, "不能操作系统内置的角色");
    ErrorCode ROLE_IS_DISABLE = new ErrorCode(1002002004, "名字为【{}】的角色已被禁用");
    ErrorCode ROLE_ADMIN_CODE_ERROR = new ErrorCode(1002002005, "编码【{}】不能使用");
    ErrorCode ROLE_EXITS_CHILDREN = new ErrorCode(1002004006, "角色已分配用户,无法删除");
    ErrorCode ROLE_KEY_DUPLICATE = new ErrorCode(1002004007, "角色权限已存在");

    // ========== 用户模块 1002003000 ==========
    ErrorCode USER_USERNAME_EXISTS = new ErrorCode(1002003000, "用户账号已经存在");
    ErrorCode USER_MOBILE_EXISTS = new ErrorCode(1002003001, "手机号已经存在");
    ErrorCode USER_EMAIL_EXISTS = new ErrorCode(1002003002, "邮箱已经存在");
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1002003003, "用户不存在");
    ErrorCode USER_IMPORT_LIST_IS_EMPTY = new ErrorCode(1002003004, "导入用户数据不能为空！");
    ErrorCode USER_PASSWORD_NO_CHANGE = new ErrorCode(1002003008, "用户新旧密码一致");
    ErrorCode USER_DELETE_SELF = new ErrorCode(1002003003, "当前用户不能删除");

    // ========== 字典类型 1002004000 ==========
    ErrorCode DICT_TYPE_NOT_EXISTS = new ErrorCode(1002004001, "当前字典类型不存在");
    ErrorCode DICT_TYPE_NOT_ENABLE = new ErrorCode(1002004002, "字典类型不处于开启状态，不允许选择");
    ErrorCode DICT_TYPE_NAME_DUPLICATE = new ErrorCode(1002004003, "已经存在该名字的字典类型");
    ErrorCode DICT_TYPE_TYPE_DUPLICATE = new ErrorCode(1002004004, "已经存在该类型的字典类型");
    ErrorCode DICT_TYPE_HAS_CHILDREN = new ErrorCode(1002004005, "无法删除，该字典类型还有字典数据");

    // ========== 字典数据 1002005000 ==========
    ErrorCode DICT_DATA_NOT_EXISTS = new ErrorCode(1002005001, "当前字典数据不存在");
    ErrorCode DICT_DATA_NOT_ENABLE = new ErrorCode(1002005002, "字典数据({})不处于开启状态，不允许选择");
    ErrorCode DICT_DATA_VALUE_DUPLICATE = new ErrorCode(1002005003, "已经存在该值的字典数据");

    // ========== 通知公告 1002006000 ==========
    ErrorCode NOTICE_NOT_FOUND = new ErrorCode(1002006001, "当前通知公告不存在");

    // ========== 参数配置 1002007000 ==========
    ErrorCode CONFIG_IS_BUILTIN = new ErrorCode(1002007001, "内置参数不允许删除");
    ErrorCode CONFIG_KEY_EXISTS = new ErrorCode(1002007001, "参数键名已存在");

    // ========== 代码生成 1002008000 ==========
    ErrorCode GEN_TABLE_NOT_EXISTS = new ErrorCode(1002008001, "原表结构不存在");
    ErrorCode GEN_RENDER_TEMPLATE_FAIL = new ErrorCode(1002008002, "渲染模板失败");
    ErrorCode GEN_TREE_ID_NULL = new ErrorCode(1002008003, "树编码字段不允许为空");
    ErrorCode GEN_TREE_FID_NULL = new ErrorCode(1002008004, "树父编码字段不允许为空");
    ErrorCode GEN_TREE_NAME_NULL = new ErrorCode(1002008005, "树名称字段不允许为空");
    ErrorCode GEN_SUB_TABLE_NULL = new ErrorCode(1002008006, "关联子表的表名不允许为空");

    // ========== 文件相关 1002009000 ==========
    ErrorCode FILE_NOT_EXISTS = new ErrorCode(1002009000, "文件不存在");
    ErrorCode FILE_UPLOAD_FAIL = new ErrorCode(1002009001, "文件上传异常");

    // ========== 短信验证码 1002014000 ==========
    ErrorCode SMS_SCENE_NOT_FOUND = new ErrorCode(1002014000, "验证码场景({}) 查找不到配置");
    ErrorCode SMS_CODE_SEND_TOO_FAST = new ErrorCode(1002014001, "短信发送过于频率");
    ErrorCode SMS_CODE_EXCEED_SEND_MAXIMUM_QUANTITY_PER_DAY = new ErrorCode(1002014002, "超过每日短信发送数量");
    ErrorCode SMS_SEND_TEMPLATE_IS_EMPTY = new ErrorCode(1002014003, "短信模板内容为空");
    ErrorCode SMS_SEND_FAIL = new ErrorCode(1002014004, "短信发送失败");

}
