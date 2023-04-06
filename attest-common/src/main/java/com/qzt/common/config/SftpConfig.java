package com.qzt.common.config;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 读取SFTP相关配置
 *
 * @author
 */
@Component
@ConfigurationProperties(prefix = "sftp")
public class SftpConfig {
    /**
     * 基础路径
     */
    @NotEmpty(message = "基础路径不能为空")
    private static String basePath;

    /**
     * 自定义域名
     */
    @NotEmpty(message = "domain 不能为空")
    @URL(message = "domain 必须是 URL 格式")
    private static String domain;

    /**
     * 主机地址
     */
    @NotEmpty(message = "host 不能为空")
    private static String host;

    /**
     * 主机端口
     */
    @NotNull(message = "port 不能为空")
    private static Integer port;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private static String username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    private static String password;

    public static String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        SftpConfig.basePath = basePath;
    }

    public static String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        SftpConfig.domain = domain;
    }

    public static String getHost() {
        return host;
    }

    public void setHost(String host) {
        SftpConfig.host = host;
    }

    public static Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        SftpConfig.port = port;
    }

    public static String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        SftpConfig.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        SftpConfig.password = password;
    }
}
