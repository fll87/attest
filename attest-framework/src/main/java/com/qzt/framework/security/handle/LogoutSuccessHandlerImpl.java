package com.qzt.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.qzt.common.constant.Constants;
import com.qzt.common.core.domain.AjaxResult;
import com.qzt.common.core.domain.model.LoginUser;
import com.qzt.common.utils.ServletUtils;
import com.qzt.common.utils.StringUtils;
import com.qzt.framework.manager.AsyncManager;
import com.qzt.framework.manager.factory.AsyncFactory;
import com.qzt.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String username = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLoginLog(username, Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.writeJSON(response, JSON.toJSONString(AjaxResult.success("退出成功")));
    }
}
