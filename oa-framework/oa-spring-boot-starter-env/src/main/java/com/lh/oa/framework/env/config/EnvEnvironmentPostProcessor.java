package com.lh.oa.framework.env.config;

import cn.hutool.core.util.StrUtil;
import com.lh.oa.framework.common.util.collection.SetUtils;
import com.lh.oa.framework.env.core.util.EnvUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Set;

/**
 * 多环境的 {@link EnvEnvironmentPostProcessor} 实现类
 * 将 oa.env.tag 设置到 dubbo、nacos 等组件对应的 tag 配置项，当且仅当它们不存在时
 *
 * @author
 */
public class EnvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final Set<String> TARGET_TAG_KEYS = SetUtils.asSet(
            "spring.cloud.nacos.discovery.metadata.tag", // Nacos 注册中心
            "dubbo.provider.tag", // Dubbo 服务提供者的 tag
            "dubbo.consumer.tag" // Dubbo 服务消费者的 tag
            // MQ TODO
    );

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 0. 设置 ${HOST_NAME} 兜底的环境变量
        String hostNameKey = StrUtil.subBetween(EnvUtils.HOST_NAME_VALUE, "{", "}");
        if (!environment.containsProperty(hostNameKey)) {
            environment.getSystemProperties().put(hostNameKey, EnvUtils.getHostName());
        }

        // 1.1 如果没有 oa.env.tag 配置项，则不进行配置项的修改
        String tag = EnvUtils.getTag(environment);
        if (StrUtil.isEmpty(tag)) {
            return;
        }
        // 1.2 需要修改的配置项
        for (String targetTagKey : TARGET_TAG_KEYS) {
            String targetTagValue = environment.getProperty(targetTagKey);
            if (StrUtil.isNotEmpty(targetTagValue)) {
                continue;
            }
            environment.getSystemProperties().put(targetTagKey, tag);
        }
    }

}
