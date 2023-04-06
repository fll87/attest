package com.qzt.module.system.service;

import com.qzt.module.system.domain.SysSmsSendLog;

/**
 * 短信发送日志业务层
 *
 * @author
 */
public interface ISysSmsSendLogService {
    /**
     * 按手机号查询最后一条日志记录
     *
     * @param mobile 手机号
     * @return 短信日志记录
     */
    public SysSmsSendLog selectLastByMobile(String mobile);

    /**
     * 保存短信发送日志
     * @param smsRecord
     * @return
     */
    int save(SysSmsSendLog smsRecord);
}
