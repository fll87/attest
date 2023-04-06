package com.qzt.common.email;

import lombok.Data;

import java.util.Map;

/**
 * 邮件信息
 * @author kf
 */
@Data
public class MailSenderInfo {
    /**邮件接收者的地址*/
    private String[] toAddress;

    /**邮件抄送人的地址*/
    private String[] ccAddress;

    /**邮件密送人的地址*/
    private String[] bccAddress;

    /**邮件主题*/
    private String subject;

    /**邮件内容*/
    private String content;

    /**邮件附件*/
    private Map<String, String> attachFiles;

    /**发送富文本，默认*/
    public static int SEND_TYPE_HTML=0;

    /**发送纯文本*/
    public static int SEND_TYPE_TEXT=1;

    /**邮件发送类型*/
    private int sendType = SEND_TYPE_HTML;

    public void setToAddress(String... toAddress) {
        this.toAddress = toAddress;
    }

    public void setCcAddress(String... ccAddress) {
        this.ccAddress = ccAddress;
    }

    public void setBccAddress(String... bccAddress) {
        this.bccAddress = bccAddress;
    }
}
