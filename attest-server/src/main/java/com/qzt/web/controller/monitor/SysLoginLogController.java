package com.qzt.web.controller.monitor;

import com.qzt.common.annotation.Log;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.AjaxResult;
import com.qzt.common.core.page.TableDataInfo;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.utils.poi.ExcelUtil;
import com.qzt.framework.web.service.SysPasswordService;
import com.qzt.module.system.domain.SysLoginLog;
import com.qzt.module.system.service.ISysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统访问记录
 *
 * @author
 */
@RestController
@RequestMapping("/monitor/loginLog")
public class SysLoginLogController extends BaseController
{
    @Autowired
    private ISysLoginLogService loginLogService;

    @Autowired
    private SysPasswordService passwordService;

    @PreAuthorize("@ss.hasPermi('monitor:loginLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginLog loginLog)
    {
        startPage();
        List<SysLoginLog> list = loginLogService.selectLoginLogList(loginLog);
        return getDataTable(list);
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:loginLog:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoginLog loginLog)
    {
        List<SysLoginLog> list = loginLogService.selectLoginLogList(loginLog);
        ExcelUtil<SysLoginLog> util = new ExcelUtil<SysLoginLog>(SysLoginLog.class);
        util.exportExcel(response, list, "登录日志");
    }

    @PreAuthorize("@ss.hasPermi('monitor:loginLog:remove')")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds)
    {
        loginLogService.deleteLoginLogByIds(infoIds);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:loginLog:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        loginLogService.cleanLoginLog();
        return success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:loginLog:unlock')")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{username}")
    public AjaxResult unlock(@PathVariable("username") String username)
    {
        passwordService.clearLoginRecordCache(username);
        return success();
    }
}
