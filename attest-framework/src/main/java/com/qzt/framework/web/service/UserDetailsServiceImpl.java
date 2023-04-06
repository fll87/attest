package com.qzt.framework.web.service;

import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.constant.UserConstants;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.domain.model.LoginUser;
import com.qzt.common.enums.UserStatus;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.MessageUtils;
import com.qzt.common.utils.StringUtils;
import com.qzt.module.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String param) throws UsernameNotFoundException
    {
        String username = StringUtils.substringBefore(param, "_");
        String type = StringUtils.substringAfterLast(param, "_");
        SysUser user = userService.selectUserByUsername(username);
        if (StringUtils.isNull(user))
        {
            throw new ServiceException(SysErrorCodeConstants.USER_NOT_EXISTS);
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDeleted()))
        {
            throw MessageUtils.exception(SysErrorCodeConstants.USER_IS_DELETED,username);
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            throw MessageUtils.exception(SysErrorCodeConstants.USER_IS_DISABLE,username);
        }

        if(UserConstants.USER_LOGIN_PWD.equals(type)){
            passwordService.validate(user);
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user.getUserId(), user, permissionService.getMenuPermission(user));
    }
}
