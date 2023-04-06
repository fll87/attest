package com.qzt.module.system.service.impl;

import com.qzt.module.system.domain.SysSmsSendLog;
import com.qzt.module.system.mapper.SysConfigMapper;
import com.qzt.module.system.mapper.SysSmsSendLogMapper;
import com.qzt.module.system.service.ISysSmsSendLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysSmsSendLogServiceImpl implements ISysSmsSendLogService {

    @Autowired
    private SysSmsSendLogMapper sysSmsSendLogMapper;

    /**
     * 按手机号查询最后一条日志记录
     *
     * @param mobile 手机号
     * @return 短信日志记录
     */
    @Override
    public SysSmsSendLog selectLastByMobile(String mobile) {
        return sysSmsSendLogMapper.selectLastByMobile(mobile);
    }

    /**
     * 保存短信发送日志
     *
     * @param smsRecord
     * @return
     */
    @Override
    public int save(SysSmsSendLog smsRecord) {
        return sysSmsSendLogMapper.save(smsRecord);
    }
}
