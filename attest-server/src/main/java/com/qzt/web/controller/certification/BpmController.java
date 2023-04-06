package com.qzt.web.controller.certification;

import com.qzt.common.annotation.Anonymous;
import com.qzt.common.core.domain.AjaxResult;
import com.qzt.module.certification.service.IBpmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

@Api("工作流")
@RestController
@RequestMapping("/bpm")
public class BpmController {

    @Autowired
    IBpmService bpmService;

    @PostMapping("/create")
    @PermitAll
    @ApiOperation("创建模板")
    @Anonymous
    public AjaxResult create() {
        bpmService.createTask();
        return AjaxResult.success(null);
    }
}
