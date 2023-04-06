package com.qzt.module.system.domain;

import com.qzt.common.core.domain.BaseEntity;
import com.qzt.common.sms.SmsSceneEnum;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 短信发送日志
 * idx_mobile 索引：基于 {@link #mobile} 字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysSmsSendLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Integer id;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 短信内容
     */
    private String content;
    /**
     * 发送场景
     * <p>
     * 枚举 {@link SmsSceneEnum}
     */
    private Integer scene;
    /**
     * 创建 IP
     */
    private String createIp;
    /**
     * 今日发送的第几条
     */
    private Integer todayIndex;
}
