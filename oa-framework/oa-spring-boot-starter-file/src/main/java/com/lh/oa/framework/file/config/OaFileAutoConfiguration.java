package com.lh.oa.framework.file.config;

import com.lh.oa.framework.file.core.client.FileClientFactory;
import com.lh.oa.framework.file.core.client.FileClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 文件配置类
 *
 * @author
 */
@AutoConfiguration
public class OaFileAutoConfiguration {

    @Bean
    public FileClientFactory fileClientFactory() {
        return new FileClientFactoryImpl();
    }

}
