package com.qzt.common.utils;

import com.qzt.common.core.domain.ErrorCode;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 消息处理
 *
 * @author
 */
@Slf4j
public class MessageUtils {
    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * 格式化异常信息
     *
     * @param errorCode
     * @param params
     * @return
     */
    public static ServiceException exception(ErrorCode errorCode, Object... params) {
        String message = doFormat(errorCode.getCode(), errorCode.getMsg(), params);
        return new ServiceException(errorCode.getCode(), message);
    }

    /**
     * 将错误编号对应的消息使用 params 进行格式化。
     *
     * @param errorCode 错误码
     * @param params    参数
     * @return 格式化后的提示
     */
    public static String doFormat(ErrorCode errorCode, Object... params) {
        return doFormat(errorCode.getCode(), errorCode.getMsg(), params);
    }

    /**
     * 将错误编号对应的消息使用 params 进行格式化。
     *
     * @param code           错误编号
     * @param messagePattern 消息模版
     * @param params         参数
     * @return 格式化后的提示
     */
    public static String doFormat(int code, String messagePattern, Object... params) {
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        int i = 0;
        int j;
        int l;
        for (l = 0; l < params.length; l++) {
            j = messagePattern.indexOf("{}", i);
            if (j == -1) {
                log.error("[doFormat][参数过多：错误码({})|错误内容({})|参数({})", code, messagePattern, params);
                if (i == 0) {
                    return messagePattern;
                } else {
                    sbuf.append(messagePattern.substring(i));
                    return sbuf.toString();
                }
            } else {
                sbuf.append(messagePattern, i, j);
                sbuf.append(params[l]);
                i = j + 2;
            }
        }
        if (messagePattern.indexOf("{}", i) != -1) {
            log.error("[doFormat][参数过少：错误码({})|错误内容({})|参数({})", code, messagePattern, params);
        }
        sbuf.append(messagePattern.substring(i));
        return sbuf.toString();
    }
}
