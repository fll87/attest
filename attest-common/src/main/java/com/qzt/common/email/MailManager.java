package com.qzt.common.email;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;


/**
 * 发邮件
 *
 * @author kf
 */
@Component
public class MailManager {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发件人邮箱
     */
    @Value("${spring.mail.username}")
    private String username;

    /**
     * 发送简单邮件
     *
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param to      发送地址
     * @return 0、正常发送成功 1、发送异常
     */
    public int sendSimpleMail(String subject, String content, String[] to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
            return 0;
        } catch (MailException e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 发送简单邮件
     *
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param to      发送地址
     * @return 0、正常发送成功 1、发送异常
     */
    public int sendComplexMail(String subject, String content, String[] to) {
        MailSenderInfo info = new MailSenderInfo();
        info.setToAddress(to);
        info.setSubject(subject);
        info.setContent(content);
        return sendComplexMail(info);
    }

    /**
     * 发送邮件
     *
     * @param mailInfo 邮件对象
     * @return 0、正常发送成功<br/>
     * 1、接收者地址不正确<br/>
     * 2、 邮件消息设置异常<br/>
     * 3、发送内容没有数据<br/>
     */
    public int sendComplexMail(MailSenderInfo mailInfo) {
        try {
            // 创建一个邮件消息
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //设置发送邮箱和发送人
            helper.setFrom(username, username);
            //收件人邮箱地址
            helper.setTo(mailInfo.getToAddress());
            // 设置邮件抄送人
            if (!StrUtil.isBlankIfStr(mailInfo.getCcAddress())) {
                helper.setCc(mailInfo.getCcAddress());
            }
            // 设置邮件密送
            if (!StrUtil.isBlankIfStr(mailInfo.getBccAddress())) {
                helper.setBcc(mailInfo.getBccAddress());
            }
            //设置邮件主题
            helper.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            helper.setSentDate(new Date());
            //正文
            String mailContent = mailInfo.getContent();
            if (StrUtil.isBlank(mailContent)) {
                return 3;
            }
            helper.setText(mailContent, true);
            //添加附件，并使用MimeUtility解决附件名称中文乱码
            Map<String, String> attachFiles = mailInfo.getAttachFiles();
            if (MapUtil.isNotEmpty(attachFiles)) {
                for (Map.Entry<String, String> entry : attachFiles.entrySet()) {
                    File file = new File(entry.getValue());
                    if (file.exists()) {
                        helper.addAttachment(MimeUtility.encodeWord(entry.getKey()), file);
                    }
                }
            }

            //发送邮件
            mailSender.send(mimeMessage);
            return 0;
        } catch (MailException e) {
            e.printStackTrace();
            return 1;
        } catch (MessagingException e) {
            e.printStackTrace();
            return 2;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 2;
        }

    }

}
