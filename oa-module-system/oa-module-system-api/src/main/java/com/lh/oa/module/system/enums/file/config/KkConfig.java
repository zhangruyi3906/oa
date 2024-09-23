package com.lh.oa.module.system.enums.file.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * kkFileView配置
 *
 * @author Rz Liu
 * @date 2023-02-15
 */
@Component
@ConfigurationProperties(prefix = "kk")
public class KkConfig {
    public static final String SEP_URL = "?url=";
    public static final String SEP_WATERMARK = "&watermark=";

    private static String server;

    public void setServer(String server) {
        KkConfig.server = server;
    }

    public static String server() {
        return server;
    }

}
