package com.lh.oa.framework.redis.template;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author dengxiaolin
 * @since 2021/06/17
 */
public class RedisLockTemplate implements LockTemplate {
    @Autowired
    private RedisMultiLockTemplate redisMultiLockTemplate;

    @Override
    public <T> T tryLockWithReturn(String key, Supplier<T> supplier) {
        Set<String> keySet = new HashSet<>(Arrays.asList(key));
        return redisMultiLockTemplate.tryLockWithReturn(keySet, supplier);
    }

    @Override
    public void tryLock(String key, Runnable runnable) {
        Set<String> keySet = new HashSet<>(Arrays.asList(key));
        redisMultiLockTemplate.tryLock(keySet, runnable);
    }

    @Override
    public <T> T lockWithReturn(String key, Supplier<T> supplier) {
        Set<String> keySet = new HashSet<>(Arrays.asList(key));
        return redisMultiLockTemplate.lockWithReturn(keySet, supplier);
    }

    @Override
    public void lock(String key, Runnable runnable) {
        Set<String> keySet = new HashSet<>(Arrays.asList(key));
        redisMultiLockTemplate.lock(keySet, runnable);
    }
}
