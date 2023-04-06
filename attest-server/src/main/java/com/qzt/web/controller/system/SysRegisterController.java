package com.qzt.web.controller.system;

import com.qzt.common.constant.AppErrorCodeConstants;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.AjaxResult;
import com.qzt.framework.web.service.SysRegisterService;
import com.qzt.module.system.domain.vo.user.RegisterVO;
import com.qzt.module.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 *
 * @author
 */
@RestController
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterVO reqVO)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return AjaxResult.error(AppErrorCodeConstants.REG_CLOSED);
        }
        registerService.register(reqVO);
        return success();
    }
}
