package com.qzt.web.controller.system;

import com.qzt.common.annotation.Log;
import com.qzt.common.config.AttestConfig;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.AjaxResult;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.domain.model.LoginUser;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.utils.SecurityUtils;
import com.qzt.common.utils.StringUtils;
import com.qzt.common.utils.file.FileUploadUtils;
import com.qzt.common.utils.file.MimeTypeUtils;
import com.qzt.framework.web.service.TokenService;
import com.qzt.module.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户个人中心 业务处理
 *
 * @author
 */
@Api(tags = "用户个人中心")
@RestController
@RequestMapping("user/profile")
public class SysProfileController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/get")
    @ApiOperation("获得登录用户信息")
    public AjaxResult profile()
    {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        Map data = new HashMap<>();
        data.put("user",user);
        data.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        return AjaxResult.success(data);
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user)
    {
        LoginUser loginUser = getLoginUser();
        SysUser sysUser = loginUser.getUser();
        user.setUsername(sysUser.getUsername());
        user.setUserId(sysUser.getUserId());
        user.setPassword(null);
        user.setAvatar(null);
        if (userService.updateUserProfile(user) > 0)
        {
            // 更新缓存用户信息
            sysUser.setNickname(user.getNickname());
            sysUser.setMobile(user.getMobile());
            sysUser.setEmail(user.getEmail());
            sysUser.setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
        }
        return success();
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword)
    {
        LoginUser loginUser = getLoginUser();
        if (userService.resetUserPwd(loginUser.getUserId(), oldPassword,newPassword)>0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
        }
        return success();
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(AttestConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar))
            {
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return AjaxResult.success(avatar);
            }
        }
        return AjaxResult.error(SysErrorCodeConstants.FILE_UPLOAD_FAIL);
    }
}
