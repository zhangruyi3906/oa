package com.lh.oa.framework.env.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 环境配置
 *
 * @author
 */
@ConfigurationProperties(prefix = "oa.env")
@Data
public class EnvProperties {

    public static final String TAG_KEY = "oa.env.tag";

    /**
     * 环境标签
     */
    private String tag;

}
