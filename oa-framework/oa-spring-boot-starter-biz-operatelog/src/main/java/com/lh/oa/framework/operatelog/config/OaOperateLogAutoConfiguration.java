package com.lh.oa.framework.operatelog.config;

import com.lh.oa.framework.operatelog.core.aop.OperateLogAspect;
import com.lh.oa.framework.operatelog.core.service.OperateLogFrameworkService;
import com.lh.oa.framework.operatelog.core.service.OperateLogFrameworkServiceImpl;
import com.lh.oa.module.system.api.logger.OperateLogApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class OaOperateLogAutoConfiguration {

    @Bean
    public OperateLogAspect operateLogAspect() {
        return new OperateLogAspect();
    }

    @Bean
    public OperateLogFrameworkService operateLogFrameworkService(OperateLogApi operateLogApi) {
        return new OperateLogFrameworkServiceImpl(operateLogApi);
    }

}
