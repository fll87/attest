package com.qzt.common.utils.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ssh.Sftp;
import com.qzt.common.config.AttestConfig;
import com.qzt.common.config.SftpConfig;
import com.qzt.common.constant.Constants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.exception.UtilException;
import com.qzt.common.utils.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传工具类
 *
 * @author
 */
public class SftpUtils {

    private Sftp sftp;

    public SftpUtils() {
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        this.sftp = new Sftp(SftpConfig.getHost(), SftpConfig.getPort(), SftpConfig.getUsername(), SftpConfig.getPassword());
    }

    /**
     * 上传
     * @param localFile
     * @param remoteFile
     * @return
     */
    public boolean upload(String localFile, String remoteFile) {
        String localFilePath = AttestConfig.getProfile() + StringUtils.substringAfter(localFile, Constants.RESOURCE_PREFIX);
        File file = new File(localFilePath);
        if(!file.exists()){
            throw new ServiceException(SysErrorCodeConstants.FILE_NOT_EXISTS);
        }
        return sftp.upload(getFilePath(remoteFile), file);
    }

    public void delete(String path) {
        String filePath = getFilePath(path);
        sftp.delFile(filePath);
    }

    public byte[] down(String path) {
        String filePath = getFilePath(path);
        File destFile = null;
        try {
            destFile = FileUtils.createTempFile();
        } catch (IOException e) {
            throw new UtilException("创建空文件异常");
        }
        sftp.download(filePath, destFile);
        return FileUtil.readBytes(destFile);
    }

    private String getFilePath(String path) {
        String basePath = SftpConfig.getBasePath();
        if (!basePath.endsWith(File.separator)) {
            basePath += File.separator;
        }
        return basePath + path;
    }

}
