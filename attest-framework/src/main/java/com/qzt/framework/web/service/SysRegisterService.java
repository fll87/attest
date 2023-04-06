package com.qzt.framework.web.service;

import com.qzt.common.constant.AppErrorCodeConstants;
import com.qzt.common.constant.Constants;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.redis.RedisCache;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.MessageUtils;
import com.qzt.framework.manager.AsyncManager;
import com.qzt.framework.manager.factory.AsyncFactory;
import com.qzt.module.system.domain.vo.user.RegisterVO;
import com.qzt.module.system.service.ISysConfigService;
import com.qzt.module.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 注册校验方法
 *
 * @author
 */
@Component
public class SysRegisterService {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册
     */
    public void register(RegisterVO reqVO) {
        String username = reqVO.getUsername();
        //todo
        if (Objects.nonNull(userService.selectUserByUsername(username))) {
            throw new ServiceException(AppErrorCodeConstants.REG_USERNAME_EXISTS);
        }

        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setNickname(username);
        userService.registerUser(sysUser);
        AsyncManager.me().execute(AsyncFactory.recordLoginLog(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
    }

}
