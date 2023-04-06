-- ----------------------------
-- 空表 用于验证
-- ----------------------------
DROP TABLE IF EXISTS "dual";
CREATE TABLE "dual" ();

-- ----------------------------
-- 参数配置表
-- ----------------------------
DROP TABLE IF EXISTS "sys_config";
CREATE TABLE "sys_config" (
  "config_id" SERIAL PRIMARY KEY,
  "config_type" int2 NOT NULL,
  "config_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "config_key" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "config_value" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6) DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0
);
COMMENT ON COLUMN "sys_config"."config_id" IS '参数主键';
COMMENT ON COLUMN "sys_config"."config_type" IS '系统内置（1是 2否）';
COMMENT ON COLUMN "sys_config"."config_name" IS '参数名称';
COMMENT ON COLUMN "sys_config"."config_key" IS '参数键名';
COMMENT ON COLUMN "sys_config"."config_value" IS '参数键值';
COMMENT ON COLUMN "sys_config"."remark" IS '备注';
COMMENT ON COLUMN "sys_config"."creator" IS '创建者';
COMMENT ON COLUMN "sys_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_config"."updater" IS '更新者';
COMMENT ON COLUMN "sys_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_config"."deleted" IS '是否删除';
COMMENT ON TABLE "sys_config" IS '参数配置表';

-- ----------------------------
-- 参数配置表初始化数据
-- ----------------------------
INSERT INTO "sys_config" VALUES (1, 1, '登陆验证码的开关', 'sys.account.captchaEnabled', 'true', '', 'admin', now(), '', NULL, 0);
INSERT INTO "sys_config" VALUES (2, 1, '用户登录-黑名单列表', 'sys.login.blackIPList', '', '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）', 'admin', now(), '', NULL, 0);
INSERT INTO "sys_config" VALUES (3, 1, '短信发送的开关', 'sys.account.smsEnabled', 'true', '', 'admin', now(), '', NULL, 0);
INSERT INTO "sys_config" VALUES (4, 1, '验证码模板', 'sms-verification-code', '【安全认证】 您好， 本次验证码为：{},有效期为五分钟，请尽快验证。', '', 'admin', now(), '', NULL, 0);


-- ----------------------------
-- 企业-用户关联表
-- ----------------------------
DROP TABLE IF EXISTS "sys_customer_user";
CREATE TABLE "sys_customer_user" (
  "customer_id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  CONSTRAINT "sys_customer_user_pkey" PRIMARY KEY ("customer_id", "user_id")
);
COMMENT ON TABLE "sys_customer_user" IS '企业-用户表';

-- ----------------------------
-- 字典数据表
-- ----------------------------
DROP TABLE IF EXISTS "sys_dict_data";
CREATE TABLE "sys_dict_data" (
  "dict_code" SERIAL PRIMARY KEY,
  "dict_sort" int4 NOT NULL,
  "dict_label" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "dict_value" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "dict_type" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL,
  "list_class" varchar(100) COLLATE "pg_catalog"."default",
  "css_class" varchar(100) COLLATE "pg_catalog"."default",
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6) DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0,
  "is_default" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'N'::bpchar
);
COMMENT ON COLUMN "sys_dict_data"."dict_code" IS '字典编码';
COMMENT ON COLUMN "sys_dict_data"."dict_sort" IS '字典排序';
COMMENT ON COLUMN "sys_dict_data"."dict_label" IS '字典标签';
COMMENT ON COLUMN "sys_dict_data"."dict_value" IS '字典键值';
COMMENT ON COLUMN "sys_dict_data"."dict_type" IS '字典类型';
COMMENT ON COLUMN "sys_dict_data"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "sys_dict_data"."list_class" IS '表格回显样式';
COMMENT ON COLUMN "sys_dict_data"."css_class" IS '样式属性（其他样式扩展）';
COMMENT ON COLUMN "sys_dict_data"."remark" IS '备注';
COMMENT ON COLUMN "sys_dict_data"."creator" IS '创建者';
COMMENT ON COLUMN "sys_dict_data"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_dict_data"."updater" IS '更新者';
COMMENT ON COLUMN "sys_dict_data"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_dict_data"."deleted" IS '是否删除';
COMMENT ON COLUMN "sys_dict_data"."is_default" IS '是否默认（Y是 N否）';
COMMENT ON TABLE "sys_dict_data" IS '字典数据表';

-- ----------------------------
-- 字典数据表数据
-- ----------------------------
INSERT INTO "sys_dict_data" VALUES (1, 1, '男', '1', 'system_user_sex', 0, 'default', 'A', '性别男', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (2, 2, '女', '2', 'system_user_sex', 1, 'success', '', '性别女', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (3, 0, '其它', '0', 'system_operate_type', 0, 'default', '', '其它操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (4, 1, '查询', '1', 'system_operate_type', 0, 'info', '', '查询操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (5, 2, '新增', '2', 'system_operate_type', 0, 'primary', '', '新增操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (6, 3, '修改', '3', 'system_operate_type', 0, 'warning', '', '修改操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (7, 4, '删除', '4', 'system_operate_type', 0, 'danger', '', '删除操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (8, 5, '导出', '5', 'system_operate_type', 0, 'default', '', '导出操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (9, 6, '导入', '6', 'system_operate_type', 0, 'default', '', '导入操作', 'admin', now(), '', NULL, 0, 'N');

-- ----------------------------
-- 字典类型表
-- ----------------------------
DROP TABLE IF EXISTS "sys_dict_type";
CREATE TABLE "sys_dict_type" (
  "dict_id" SERIAL PRIMARY KEY,
  "dict_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "dict_type" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6) DEFAULT NULL,
  "deleted_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0
);
COMMENT ON COLUMN "sys_dict_type"."dict_id" IS '字典主键';
COMMENT ON COLUMN "sys_dict_type"."dict_name" IS '字典名称';
COMMENT ON COLUMN "sys_dict_type"."dict_type" IS '字典类型';
COMMENT ON COLUMN "sys_dict_type"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "sys_dict_type"."remark" IS '备注';
COMMENT ON COLUMN "sys_dict_type"."creator" IS '创建者';
COMMENT ON COLUMN "sys_dict_type"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_dict_type"."updater" IS '更新者';
COMMENT ON COLUMN "sys_dict_type"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_dict_type"."deleted_time" IS '删除时间';
COMMENT ON COLUMN "sys_dict_type"."deleted" IS '是否删除';
COMMENT ON TABLE "sys_dict_type" IS '字典类型表';

-- ----------------------------
-- 字典类型表数据
-- ----------------------------
INSERT INTO "sys_dict_type" VALUES (1, '用户性别', 'system_user_sex', 0, NULL, 'admin', now(), '', NULL, NULL, 0);
INSERT INTO "sys_dict_type" VALUES (2, '操作类型', 'system_operate_type', 0, NULL, 'admin', now(), '', NULL, NULL, 0);

-- ----------------------------
-- 系统访问记录表
-- ----------------------------
DROP TABLE IF EXISTS "sys_login_log";
CREATE TABLE "sys_login_log" (
  "info_id" SERIAL PRIMARY KEY,
  "username" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "ipaddr" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "login_location" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "browser" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "os" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "status" int2 DEFAULT 0,
  "msg" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "login_time" date
);
COMMENT ON COLUMN "sys_login_log"."info_id" IS '访问ID';
COMMENT ON COLUMN "sys_login_log"."username" IS '用户账号';
COMMENT ON COLUMN "sys_login_log"."ipaddr" IS '登录IP地址';
COMMENT ON COLUMN "sys_login_log"."login_location" IS '登录地点';
COMMENT ON COLUMN "sys_login_log"."browser" IS '浏览器类型';
COMMENT ON COLUMN "sys_login_log"."os" IS '操作系统';
COMMENT ON COLUMN "sys_login_log"."status" IS '登录状态（0成功 1失败）';
COMMENT ON COLUMN "sys_login_log"."msg" IS '提示消息';
COMMENT ON COLUMN "sys_login_log"."login_time" IS '访问时间';
COMMENT ON TABLE "sys_login_log" IS '系统访问记录';

-- ----------------------------
-- 菜单权限表
-- ----------------------------
DROP TABLE IF EXISTS "sys_menu";
CREATE TABLE "sys_menu" (
  "menu_id" SERIAL PRIMARY KEY,
  "menu_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "perms" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "type" int2 NOT NULL,
  "sort" int4 NOT NULL,
  "parent_id" int8 NOT NULL,
  "path" varchar(200) COLLATE "pg_catalog"."default",
  "icon" varchar(100) COLLATE "pg_catalog"."default",
  "order_num" varchar(255) COLLATE "pg_catalog"."default",
  "status" int2 NOT NULL,
  "visible" bool NOT NULL,
  "is_cache" bool NOT NULL,
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6) DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0,
  "is_frame" int2
);
COMMENT ON COLUMN "sys_menu"."menu_id" IS '菜单ID';
COMMENT ON COLUMN "sys_menu"."menu_name" IS '菜单名称';
COMMENT ON COLUMN "sys_menu"."perms" IS '权限标识';
COMMENT ON COLUMN "sys_menu"."type" IS '菜单类型';
COMMENT ON COLUMN "sys_menu"."sort" IS '显示顺序';
COMMENT ON COLUMN "sys_menu"."parent_id" IS '父菜单ID';
COMMENT ON COLUMN "sys_menu"."path" IS '路由地址';
COMMENT ON COLUMN "sys_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "sys_menu"."order_num" IS '组件路径';
COMMENT ON COLUMN "sys_menu"."status" IS '菜单状态';
COMMENT ON COLUMN "sys_menu"."visible" IS '是否可见';
COMMENT ON COLUMN "sys_menu"."is_cache" IS '是否缓存';
COMMENT ON COLUMN "sys_menu"."creator" IS '创建者';
COMMENT ON COLUMN "sys_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_menu"."updater" IS '更新者';
COMMENT ON COLUMN "sys_menu"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_menu"."deleted" IS '是否删除';
COMMENT ON COLUMN "sys_menu"."is_frame" IS '是否为外链（0是 1否）';
COMMENT ON TABLE "sys_menu" IS '菜单权限表';

-- ----------------------------
-- 菜单权限表数据
-- ----------------------------

-- ----------------------------
-- 通知公告表
-- ----------------------------
DROP TABLE IF EXISTS "sys_notice";
CREATE TABLE "sys_notice" (
  "notice_id" SERIAL PRIMARY KEY,
  "notice_title" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "notice_content" text COLLATE "pg_catalog"."default" NOT NULL,
  "notice_type" int2 NOT NULL,
  "status" int2 NOT NULL,
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6) DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0
);
COMMENT ON COLUMN "sys_notice"."notice_id" IS '公告ID';
COMMENT ON COLUMN "sys_notice"."notice_title" IS '公告标题';
COMMENT ON COLUMN "sys_notice"."notice_content" IS '公告内容';
COMMENT ON COLUMN "sys_notice"."notice_type" IS '公告类型（1通知 2公告）';
COMMENT ON COLUMN "sys_notice"."status" IS '公告状态（0正常 1关闭）';
COMMENT ON COLUMN "sys_notice"."creator" IS '创建者';
COMMENT ON COLUMN "sys_notice"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_notice"."updater" IS '更新者';
COMMENT ON COLUMN "sys_notice"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_notice"."deleted" IS '是否删除';
COMMENT ON TABLE "sys_notice" IS '通知公告表';

-- ----------------------------
-- 用户操作日志表
-- ----------------------------
DROP TABLE IF EXISTS "sys_oper_log";
CREATE TABLE "sys_oper_log" (
  "oper_id" SERIAL PRIMARY KEY,
  "title" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "business_type" int2 DEFAULT 0,
  "method" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "request_method" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "operator_type" int2 DEFAULT 0,
  "oper_name" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_url" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_ip" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_location" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_param" varchar(2000) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "json_result" varchar(2000) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "status" int2 DEFAULT 0,
  "error_msg" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_time" date,
  "cost_time" int8 DEFAULT 0
);
COMMENT ON COLUMN "sys_oper_log"."oper_id" IS '日志主键';
COMMENT ON COLUMN "sys_oper_log"."title" IS '模块标题';
COMMENT ON COLUMN "sys_oper_log"."business_type" IS '业务类型（字典system_operate_type）';
COMMENT ON COLUMN "sys_oper_log"."method" IS '方法名称';
COMMENT ON COLUMN "sys_oper_log"."request_method" IS '请求方式';
COMMENT ON COLUMN "sys_oper_log"."operator_type" IS '操作类别（0其它 1后台用户 2企业端用户）';
COMMENT ON COLUMN "sys_oper_log"."oper_name" IS '操作人员';
COMMENT ON COLUMN "sys_oper_log"."oper_url" IS '请求URL';
COMMENT ON COLUMN "sys_oper_log"."oper_ip" IS '主机地址';
COMMENT ON COLUMN "sys_oper_log"."oper_location" IS '操作地点';
COMMENT ON COLUMN "sys_oper_log"."oper_param" IS '请求参数';
COMMENT ON COLUMN "sys_oper_log"."json_result" IS '返回参数';
COMMENT ON COLUMN "sys_oper_log"."status" IS '操作状态（0正常 1异常）';
COMMENT ON COLUMN "sys_oper_log"."error_msg" IS '错误消息';
COMMENT ON COLUMN "sys_oper_log"."oper_time" IS '操作时间';
COMMENT ON COLUMN "sys_oper_log"."cost_time" IS '消耗时间';

-- ----------------------------
-- 角色信息表
-- ----------------------------
DROP TABLE IF EXISTS "sys_role";
CREATE TABLE "sys_role" (
  "role_id" SERIAL PRIMARY KEY,
  "role_name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "role_key" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "role_sort" int4 NOT NULL,
  "data_scope" int2 NOT NULL,
  "status" int2 NOT NULL,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6) DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0
);
COMMENT ON COLUMN "sys_role"."role_id" IS '角色ID';
COMMENT ON COLUMN "sys_role"."role_name" IS '角色名称';
COMMENT ON COLUMN "sys_role"."role_key" IS '角色权限字符串';
COMMENT ON COLUMN "sys_role"."role_sort" IS '显示顺序';
COMMENT ON COLUMN "sys_role"."data_scope" IS '数据范围（1：全部数据权限 2：自定数据权限）';
COMMENT ON COLUMN "sys_role"."status" IS '角色状态（0正常 1停用）';
COMMENT ON COLUMN "sys_role"."remark" IS '备注';
COMMENT ON COLUMN "sys_role"."creator" IS '创建者';
COMMENT ON COLUMN "sys_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_role"."updater" IS '更新者';
COMMENT ON COLUMN "sys_role"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_role"."deleted" IS '是否删除';
COMMENT ON TABLE "sys_role" IS '角色信息表';

-- ----------------------------
-- 角色信息表数据
-- ----------------------------
INSERT INTO "sys_role" VALUES (1, '超级管理员', 'super_admin', 1, 1, 0, '超级管理员', 'admin', now(), '', NULL, 0);
INSERT INTO "sys_role" VALUES (2, '管理员', 'admin', 2, 2, 0, '管理员', 'admin', now(), '', NULL, 0);

-- ----------------------------
-- 角色和菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS "sys_role_menu";
CREATE TABLE "sys_role_menu" (
  "role_id" int8 NOT NULL,
  "menu_id" int8 NOT NULL,
  CONSTRAINT "sys_role_menu_pkey" PRIMARY KEY ("role_id", "menu_id")
);
COMMENT ON COLUMN "sys_role_menu"."role_id" IS '角色ID';
COMMENT ON COLUMN "sys_role_menu"."menu_id" IS '菜单ID';
COMMENT ON TABLE "sys_role_menu" IS '角色和菜单关联表';

-- ----------------------------
-- 角色和菜单关联表数据
-- ----------------------------
INSERT INTO "sys_role_menu" VALUES (1, 1);
INSERT INTO "sys_role_menu" VALUES (1, 2);
INSERT INTO "sys_role_menu" VALUES (2, 1);
INSERT INTO "sys_role_menu" VALUES (2, 2);

-- ----------------------------
-- 用户信息表
-- ----------------------------
DROP TABLE IF EXISTS "sys_user";
CREATE TABLE "sys_user" (
  "user_id" SERIAL PRIMARY KEY,
  "username" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "nickname" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(500) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "email" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "mobile" varchar(11) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "sex" int2 DEFAULT 0,
  "avatar" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "status" int2 NOT NULL,
  "login_ip" varchar(50) COLLATE "pg_catalog"."default",
  "login_date" timestamp(6),
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6) DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0,
  "user_type" int2 NOT NULL DEFAULT 0,
  "pwd_update_time" timestamp(6)
)
;
COMMENT ON COLUMN "sys_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "sys_user"."username" IS '用户账号';
COMMENT ON COLUMN "sys_user"."password" IS '密码';
COMMENT ON COLUMN "sys_user"."nickname" IS '用户昵称';
COMMENT ON COLUMN "sys_user"."remark" IS '备注';
COMMENT ON COLUMN "sys_user"."email" IS '用户邮箱';
COMMENT ON COLUMN "sys_user"."mobile" IS '手机号码';
COMMENT ON COLUMN "sys_user"."sex" IS '用户性别';
COMMENT ON COLUMN "sys_user"."avatar" IS '头像地址';
COMMENT ON COLUMN "sys_user"."status" IS '帐号状态（0正常 1停用）';
COMMENT ON COLUMN "sys_user"."login_ip" IS '最后登录IP';
COMMENT ON COLUMN "sys_user"."login_date" IS '最后登录时间';
COMMENT ON COLUMN "sys_user"."creator" IS '创建者';
COMMENT ON COLUMN "sys_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_user"."updater" IS '更新者';
COMMENT ON COLUMN "sys_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_user"."deleted" IS '是否删除';
COMMENT ON COLUMN "sys_user"."user_type" IS '用户类型（0管理端 1企业端）';
COMMENT ON COLUMN "sys_user"."pwd_update_time" IS '最后修改密码时间';
COMMENT ON TABLE "sys_user" IS '用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "sys_user" VALUES (1, 'admin', '35c7ff6501485904d817343c10c0bcd9', '管理员', '管理员', 'admin@126.com', '13333333333', 1, '', 0, '', NULL, 'admin', now(), NULL, NULL, 0, 0, NULL);

-- ----------------------------
-- 用户和角色关联表
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_role";
CREATE TABLE "sys_user_role" (
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  CONSTRAINT "sys_user_role_pkey" PRIMARY KEY ("user_id", "role_id")
);
COMMENT ON COLUMN "sys_user_role"."user_id" IS '用户ID';
COMMENT ON COLUMN "sys_user_role"."role_id" IS '角色ID';
COMMENT ON TABLE "sys_user_role" IS '用户和角色关联表';

-- ----------------------------
-- 用户和角色关联表数据
-- ----------------------------
INSERT INTO "sys_user_role" VALUES (1, 1);


-- ----------------------------
-- 索引
-- ----------------------------
CREATE UNIQUE INDEX "dict_type" ON "sys_dict_type" USING btree (
  "dict_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_status" ON "sys_login_log" USING btree (
  "status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_time" ON "sys_login_log" USING btree (
  "login_time" "pg_catalog"."date_ops" ASC NULLS LAST
);
CREATE INDEX "idx_oper_bus" ON "sys_oper_log" USING btree (
  "business_type" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_oper_status" ON "sys_oper_log" USING btree (
  "status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_oper_time" ON "sys_oper_log" USING btree (
  "oper_time" "pg_catalog"."date_ops" ASC NULLS LAST
);
CREATE INDEX "idx_userType" ON "sys_user" USING btree (
  "user_type" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "idx_username" ON "sys_user" USING btree (
  "username" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "update_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST,
  "user_type" "pg_catalog"."int2_ops" ASC NULLS LAST
);

-- ----------------------------
-- 短信发送记录表
-- ----------------------------
DROP TABLE IF EXISTS "sys_sms_send_log";
CREATE TABLE sys_sms_send_log (
  "id" SERIAL PRIMARY KEY,
  "mobile" varchar(11) COLLATE "pg_catalog"."default" NOT NULL,
  "content" varchar(500) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "scene" int2 NOT NULL,
  "create_ip" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "today_index" int4 NOT NULL DEFAULT 0,
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6)
);

CREATE INDEX "idx_mobile" ON "sys_sms_send_log" USING btree (
  "mobile" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

COMMENT ON COLUMN "sys_sms_send_log"."id" IS '主键';
COMMENT ON COLUMN "sys_sms_send_log"."mobile" IS '手机号';
COMMENT ON COLUMN "sys_sms_send_log"."content" IS '短信内容';
COMMENT ON COLUMN "sys_sms_send_log"."scene" IS '发送场景（参考SmsSceneEnum）';
COMMENT ON COLUMN "sys_sms_send_log"."create_ip" IS 'IP';
COMMENT ON COLUMN "sys_sms_send_log"."today_index" IS '今日发送的第几条';
COMMENT ON COLUMN "sys_sms_send_log"."creator" IS '创建者';
COMMENT ON COLUMN "sys_sms_send_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_sms_send_log"."updater" IS '更新者';
COMMENT ON COLUMN "sys_sms_send_log"."update_time" IS '更新时间';
COMMENT ON TABLE "sys_sms_send_log" IS '短信发送记录表';
