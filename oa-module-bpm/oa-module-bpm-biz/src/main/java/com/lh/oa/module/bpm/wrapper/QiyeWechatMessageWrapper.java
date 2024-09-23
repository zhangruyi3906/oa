package com.lh.oa.module.bpm.wrapper;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSONObject;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Component
public class QiyeWechatMessageWrapper {
    public final String POST_SALE_OAUTH_TOKEN_KEY = "POST_SALE_OAUTH_TOKEN";
    @Value("${post.sale.todo-message}")
    private String todoMessageApi;
    @Value("${post.sale.platform-url}")
    private String platformUrlApi;
    @Value("${post.sale.username}")
    private String username;
    @Value("${post.sale.password}")
    private String password;
    @Value("${post.sale.access_token}")
    private String accessTokenApi;
    @Value("${post.sale.grant_type}")
    private String grantType;
    @Value("${post.sale.client}")
    private String client;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void pushQiyeWechatMessage(JSONObject params) {
        String accessToken = getPostSaleAccessToken();
        HttpRequest httpRequest;
        httpRequest = HttpRequest.post(platformUrlApi+todoMessageApi)
                .body(JSONUtil.toJsonStr(params)).bearerAuth(accessToken);
        log.info("推送售后流程待办企业微信消息，url:{}, 请求参数:{}", httpRequest.getUrl(), JsonUtils.toJsonString(params));
        HttpResponse httpResponse;
        try {
            httpResponse = httpRequest.bearerAuth(accessToken).timeout(1000 * 60).execute();
            log.info("推送售后流程待办企业微信消息，返回结果:{}", httpResponse.body());
        } catch (Exception e) {
            log.error("推送消息失败：{}", e.getMessage());
            throw new BusinessException("推送消息失败");
        }
        if (!httpResponse.isOk()) {
            log.error("消息发送失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("消息发送失败-系统内部错误");
        } else {
            cn.hutool.json.JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("消息发送失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            } else {
                log.info("消息发送成功");
            }
        }
    }

    private String getPostSaleAccessToken() {
        String accessToken = null;
        try {
            accessToken = stringRedisTemplate.opsForValue().get(POST_SALE_OAUTH_TOKEN_KEY);
            log.info("获取售后认证信息-获取缓存值, accessToken:{}", accessToken);
            if (hasText(accessToken)) {
                JWT jwt = JWTUtil.parseToken(accessToken);
                JWTPayload payload = jwt.getPayload();
                cn.hutool.json.JSONObject claimsJson = payload.getClaimsJson();
                if (claimsJson.getInt("exp") <= System.currentTimeMillis() / 1000) { //1min
                    cn.hutool.json.JSONObject authInfo = this.getPostSaleAuthInfo();
                    accessToken = authInfo.getStr("access_token");
                }
            } else {
                cn.hutool.json.JSONObject authInfo = this.getPostSaleAuthInfo();
                accessToken = authInfo.getStr("data");
            }
        } catch (Exception e) {
            log.error("获取售后服务平台认证信息失败/解析有误", e);
        }
        return accessToken;
    }

    private cn.hutool.json.JSONObject getPostSaleAuthInfo() {
        HttpRequest httpRequest = HttpRequest.post(platformUrlApi + accessTokenApi)
                .form("username", username)
                .form("password", password)
                .form("grant_type", grantType)
                .form("client", client)
                .timeout(1000 * 60); //1min
        httpRequest.header("Content-Type", "application/x-www-form-urlencoded");
        httpRequest.header("Authorization", "Basic UE1TOmxhbmhhaTg4OA=="); //固定值
        log.info("获取售后认证信息，url:{}, 请求参数:{}", httpRequest.getUrl(), JsonUtils.toJsonString(httpRequest.form()));
        HttpResponse httpResponse = httpRequest.execute();
        log.info("获取售后认证信息，获得返回值:{}", httpResponse.body());
        if (!httpResponse.isOk()) {
            log.error("售后服务平台系统认证失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("系统异常，请联系管理员");
        } else {
            cn.hutool.json.JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK)
                throw new BusinessException(body.getStr("message"));
            Integer expiresIn = body.getInt("expires_in");
            if (Objects.nonNull(expiresIn) && expiresIn > 0) {
                stringRedisTemplate.opsForValue().set(POST_SALE_OAUTH_TOKEN_KEY, body.getStr("access_token"), expiresIn, TimeUnit.SECONDS);
            }
            return body;
        }
    }
}
