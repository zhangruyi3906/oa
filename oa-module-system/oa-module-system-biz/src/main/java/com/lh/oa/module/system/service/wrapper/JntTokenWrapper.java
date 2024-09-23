package com.lh.oa.module.system.service.wrapper;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.system.full.constants.ISysConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.StringUtils.hasText;

/**
 * @author Rick
 * @since 2024/2/20
 */

@Slf4j
@Component
public class JntTokenWrapper {

    @Value("${jnt.platform-url}")
    private String platformUrl;

    @Value("${jnt.app-access-token}")
    private String appAccessToken;

    @Value("${jnt.app-id}")
    private String appId;

    @Value("${jnt.app-secret}")
    private String appSecret;

    @Value("${jnt.client}")
    private String client;

    @Value("${jnt.grant-type}")
    private String grantType;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取JNT项目的访问token
     *
     * @return jnt项目的token
     */
    public String getJntAccessToken() {
        String jntAccessToken = null;
        try {
            jntAccessToken = stringRedisTemplate.opsForValue().get(ISysConstant.JNT_OAUTH_TOKEN_KEY);
            log.info("获取PMS认证信息-获取缓存值, jntAccessToken:{}", jntAccessToken);
            if (hasText(jntAccessToken)) {
                JWT jwt = JWTUtil.parseToken(jntAccessToken);
                JWTPayload payload = jwt.getPayload();
                JSONObject claimsJson = payload.getClaimsJson();
                if (claimsJson.getInt("exp") <= System.currentTimeMillis() / 1000) { //1min
                    JSONObject jntAuthInfo = this.getJntAuthInfo();
                    jntAccessToken = jntAuthInfo.getStr("access_token");
                }
            } else {
                JSONObject jntAuthInfo = this.getJntAuthInfo();
                jntAccessToken = jntAuthInfo.getStr("access_token");
            }
        } catch (Exception e) {
            log.error("获取建能通系统认证信息失败/解析有误", e);
        }
        return jntAccessToken;
    }

    private JSONObject getJntAuthInfo() {
        HttpRequest httpRequest = HttpRequest.post(platformUrl + appAccessToken)
                .form("app_id", appId)
                .form("app_secret", appSecret)
                .form("grant_type", grantType)
                .form("client", client)
                .timeout(1000 * 60); //1min
        httpRequest.header("Content-Type", "application/x-www-form-urlencoded");
        httpRequest.header("Authorization", "Basic UE1TOmxhbmhhaTg4OA=="); //固定值
        log.info("获取PMS认证信息，url:{}, 请求参数:{}", httpRequest.getUrl(), JsonUtils.toJsonString(httpRequest.form()));
        HttpResponse httpResponse = httpRequest.execute();
        log.info("获取PMS认证信息，获得返回值:{}", httpResponse.body());
        if (!httpResponse.isOk()) {
            log.error("建能通系统认证失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("系统异常，请联系管理员");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK)
                throw new BusinessException(body.getStr("message"));
            Integer expiresIn = body.getInt("expires_in");
            if (Objects.nonNull(expiresIn) && expiresIn > 0) {
                stringRedisTemplate.opsForValue().set(ISysConstant.JNT_OAUTH_TOKEN_KEY, body.getStr("access_token"), expiresIn, TimeUnit.SECONDS);
            }
            return body;
        }
    }

}
