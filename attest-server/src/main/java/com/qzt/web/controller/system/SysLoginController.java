package com.qzt.web.controller.system;

import com.qzt.common.constant.UserConstants;
import com.qzt.common.core.domain.AjaxResult;
import com.qzt.common.core.domain.entity.SysMenu;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.utils.SecurityUtils;
import com.qzt.common.utils.bean.BeanUtils;
import com.qzt.framework.web.service.SysLoginService;
import com.qzt.framework.web.service.SysPermissionService;
import com.qzt.module.system.domain.vo.user.PwdLoginVO;
import com.qzt.module.system.domain.vo.user.LoginVO;
import com.qzt.module.system.domain.vo.user.SmsLoginVO;
import com.qzt.module.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Api(tags = "登录")
@RestController
@Validated
@Slf4j
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @PostMapping("/login")
    @ApiOperation(value = "使用用户名/手机/邮箱 + 密码登录")
    public AjaxResult login(@RequestBody @Valid PwdLoginVO reqVO) {
        LoginVO vo = new LoginVO();
        BeanUtils.copyBeanProp(vo, reqVO);
        vo.setType(UserConstants.USER_LOGIN_PWD);
        return AjaxResult.success(loginService.login(vo));
    }


    @PostMapping("/sms-login")
    @ApiOperation(value = "使用手机 + 验证码登录")
    public AjaxResult smsLogin(@RequestBody @Valid SmsLoginVO reqVO) {
        LoginVO vo = new LoginVO();
        BeanUtils.copyBeanProp(vo, reqVO);
        vo.setUsername(reqVO.getMobile());
        vo.setType(UserConstants.USER_LOGIN_SMS);
        return AjaxResult.success(loginService.login(vo));
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        return AjaxResult.success(null);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
