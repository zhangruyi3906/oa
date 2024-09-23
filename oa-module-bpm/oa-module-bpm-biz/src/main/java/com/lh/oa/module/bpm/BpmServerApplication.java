package com.lh.oa.module.bpm;

import com.lh.oa.framework.quartz.config.OaXxlJobAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * 项目的启动类
 *
 *
 * @author
 */
@Slf4j
@SpringBootApplication
@Import(OaXxlJobAutoConfiguration.class)
public class BpmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BpmServerApplication.class, args);
        log.info("\n\n\tbpm-server start up completed\n");
    }

}
