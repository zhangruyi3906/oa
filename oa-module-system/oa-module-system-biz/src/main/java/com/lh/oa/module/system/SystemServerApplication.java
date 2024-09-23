package com.lh.oa.module.system;

import com.lh.oa.framework.quartz.config.OaXxlJobAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目的启动类
 *
 *
 * @author
 */
@Slf4j
@SpringBootApplication
@Import(OaXxlJobAutoConfiguration.class)
@EnableScheduling
public class SystemServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemServerApplication.class, args);
        log.info("\n\n\tsystem-server start up completed\n");
    }


}
