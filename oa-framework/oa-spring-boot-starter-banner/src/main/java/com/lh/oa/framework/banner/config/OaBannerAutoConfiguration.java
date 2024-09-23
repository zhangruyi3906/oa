package com.lh.oa.framework.banner.config;

//import com.lh.oa.framework.banner.core.BannerApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Banner 的自动配置类
 *
 * @author
 */
@AutoConfiguration
public class OaBannerAutoConfiguration {

    @Bean
    public void bannerApplicationRunner() {
    }
//    public BannerApplicationRunner bannerApplicationRunner() {
//        return new BannerApplicationRunner();
//    }

}
