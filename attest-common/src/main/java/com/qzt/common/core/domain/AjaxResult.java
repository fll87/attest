package com.qzt.common.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qzt.common.constant.GlobalErrorCodeConstants;
import com.qzt.common.exception.ServerException;
import com.qzt.common.exception.ServiceException;
import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;

/**
 * 操作消息提醒
 *
 * @author
 */
@Data
public class AjaxResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     *
     * @see ErrorCode#getCode()
     */
    private Integer code;
    /**
     * 返回数据
     */
    private Object data;
    /**
     * 错误提示，用户可阅读
     *
     * @see ErrorCode#getMsg() ()
     */
    private String msg;

    /**
     * 返回错误消息
     * @param code
     * @param message
     * @return
     */
    public static AjaxResult error(Integer code, String message) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(code), "code 必须是错误的！");
        AjaxResult result = new AjaxResult();
        result.code = code;
        result.msg = message;
        return result;
    }

    /**
     * 返回错误消息
     * @param errorCode
     * @return
     */
    public static AjaxResult error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    /**
     * 返回错误消息
     * @param serviceException
     * @return
     */
    public static AjaxResult error(ServiceException serviceException) {
        return error(serviceException.getCode(), serviceException.getMessage());
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        AjaxResult result = new AjaxResult();
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data = data;
        result.msg = "";
        return result;
    }

    public static boolean isSuccess(Integer code) {
        return Objects.equals(code, GlobalErrorCodeConstants.SUCCESS.getCode());
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isSuccess() {
        return isSuccess(code);
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isError() {
        return !isSuccess();
    }

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     */
    public void checkError() throws ServiceException {
        if (isSuccess()) {
            return;
        }
        // 服务端异常
        if (GlobalErrorCodeConstants.isServerErrorCode(code)) {
            throw new ServerException(code, msg);
        }
        // 业务异常
        throw new ServiceException(code, msg);
    }

}
