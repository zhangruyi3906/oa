package com.lh.oa.framework.redis.config;

import java.net.UnknownHostException;

import com.lh.oa.framework.redis.template.CommonRedisLock;
import com.lh.oa.framework.redis.template.RedisLockTemplate;
import com.lh.oa.framework.redis.template.RedisMultiLockTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author dengxiaolin
 * @since 2021/08/25
 */
@Configuration
@Import(RedisAutoConfiguration.class)
public class RedisLockConfig {
    @Bean
    @ConditionalOnMissingBean(
            name = {"stringRedisTemplate"}
    )
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public CommonRedisLock commonRedisLock() {
        return new CommonRedisLock();
    }

    @Bean
    public RedisLockTemplate redisLockTemplate() {
        return new RedisLockTemplate();
    }

    @Bean
    public RedisMultiLockTemplate redisMultiLockTemplate() {
        return new RedisMultiLockTemplate();
    }
}
