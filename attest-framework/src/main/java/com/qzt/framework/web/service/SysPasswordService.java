package com.qzt.framework.web.service;

import com.qzt.common.constant.CacheConstants;
import com.qzt.common.constant.Constants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.redis.RedisCache;
import com.qzt.common.utils.MessageUtils;
import com.qzt.common.utils.SecurityUtils;
import com.qzt.framework.manager.AsyncManager;
import com.qzt.framework.manager.factory.AsyncFactory;
import com.qzt.framework.security.context.AuthenticationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登录密码方法
 *
 * @author
 */
@Component
public class SysPasswordService
{
    @Autowired
    private RedisCache redisCache;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user)
    {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        Integer retryCount = redisCache.getCacheObject(getCacheKey(user.getUsername()));

        if (retryCount == null)
        {
            retryCount = 0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
        {
            AsyncManager.me().execute(AsyncFactory.recordLoginLog(user.getUsername(), Constants.LOGIN_FAIL,
                    MessageUtils.doFormat(SysErrorCodeConstants.LOGIN_FAIL_LOCK, maxRetryCount, lockTime)));
            throw MessageUtils.exception(SysErrorCodeConstants.LOGIN_FAIL_LOCK,maxRetryCount, lockTime);
        }

        if (!matches(user, password))
        {
            retryCount = retryCount + 1;
            AsyncManager.me().execute(AsyncFactory.recordLoginLog(user.getUsername(), Constants.LOGIN_FAIL,
                    MessageUtils.doFormat(SysErrorCodeConstants.LOGIN_PWD_ERROR, retryCount)));
            redisCache.setCacheObject(getCacheKey(user.getUsername()), retryCount, lockTime, TimeUnit.MINUTES);
            throw MessageUtils.exception(SysErrorCodeConstants.LOGIN_PWD_ERROR,maxRetryCount);
        }
        else
        {
            clearLoginRecordCache(user.getUsername());
        }
    }

    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (redisCache.hasKey(getCacheKey(loginName)))
        {
            redisCache.deleteObject(getCacheKey(loginName));
        }
    }

}
