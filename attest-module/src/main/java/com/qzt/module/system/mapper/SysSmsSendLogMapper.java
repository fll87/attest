package com.qzt.module.system.mapper;

import com.qzt.module.system.domain.SysSmsSendLog;

import java.util.List;

public interface SysSmsSendLogMapper {

    public List<SysSmsSendLog> selectList(SysSmsSendLog sysSmsSendLog);

    public SysSmsSendLog selectById(Long id);

    int save(SysSmsSendLog smsRecord);

    SysSmsSendLog selectLastByMobile(String mobile);

}
