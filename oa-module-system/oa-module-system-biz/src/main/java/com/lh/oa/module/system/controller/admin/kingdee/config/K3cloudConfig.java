package com.lh.oa.module.system.controller.admin.kingdee.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:k3cloud.properties")
@ConfigurationProperties(prefix="k3cloud")
public class K3cloudConfig {
    private static String url;
    private static String dbid;
    private static String user;
    private static String pwd;
    private static int lcid;

    public static String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        K3cloudConfig.url = url;
    }

    public static String getDbid() {
        return dbid;
    }

    public void setDbid(String dbid) {
        K3cloudConfig.dbid = dbid;
    }

    public static String getUser() {
        return user;
    }

    public void setUser(String user) {
        K3cloudConfig.user = user;
    }

    public static String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        K3cloudConfig.pwd = pwd;
    }

    public static int getLcid() {
        return lcid;
    }

    public void setLcid(int lcid) {
        K3cloudConfig.lcid = lcid;
    }
}
