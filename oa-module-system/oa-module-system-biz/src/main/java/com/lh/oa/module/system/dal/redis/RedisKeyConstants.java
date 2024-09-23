package com.lh.oa.module.system.dal.redis;

import com.lh.oa.framework.redis.core.RedisKeyDefine;
import com.lh.oa.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;

import java.time.Duration;

import static com.lh.oa.framework.redis.core.RedisKeyDefine.KeyTypeEnum.STRING;

/**
 * System Redis Key 枚举类
 *
 * @author
 */
public interface RedisKeyConstants {

    RedisKeyDefine OAUTH2_ACCESS_TOKEN = new RedisKeyDefine("访问令牌的缓存",
            "oauth2_access_token:%s", // 参数为访问令牌 token
            STRING, OAuth2AccessTokenDO.class, RedisKeyDefine.TimeoutTypeEnum.DYNAMIC);

    RedisKeyDefine SOCIAL_AUTH_STATE = new RedisKeyDefine("社交登陆的 state", // 注意，它是被 JustAuth 的 justauth.type.prefix 使用到
            "social_auth_state:%s", // 参数为 state
            STRING, String.class, Duration.ofHours(24)); // 值为 state

}
