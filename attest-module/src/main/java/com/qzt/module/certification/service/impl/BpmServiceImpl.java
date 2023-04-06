package com.qzt.module.certification.service.impl;

import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.exception.ServiceException;
import com.qzt.flowpack.TemplateManager;
import com.qzt.flowpack.common.vo.Result;
import com.qzt.flowpack.model.template.Template;
import com.qzt.module.certification.service.IBpmService;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class BpmServiceImpl implements IBpmService {

    @Autowired
    TemplateManager templateManager;
/*    @Autowired*/
/*    ProcessManager processManager;*/
/*    @Autowired*/
/*    TaskManager taskManager;*/

    /**
     * 创建认证任务
     *
     * @return
     */
    @Override
    public void createTask() {
        String templateName = "企业认证模板";
        String resourceName = "C:\\Users\\DELL\\Desktop\\认证流程.bpmn20.xml";
        byte[] tempBytes = null;
        try {
            tempBytes = FileUtil.readAsByteArray(new File(resourceName));
        } catch (IOException e) {
            throw new ServiceException(SysErrorCodeConstants.FILE_NOT_EXISTS);
        }
        Result<Template> template = templateManager.createTemplate("认证流程", resourceName, tempBytes);

        System.out.println(template.getStatus());
        template.getData().getKey();
    }
}
