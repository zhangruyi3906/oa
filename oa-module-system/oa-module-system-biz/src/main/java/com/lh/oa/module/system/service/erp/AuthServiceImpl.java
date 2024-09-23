package com.lh.oa.module.system.service.erp;

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

@Component
@Slf4j
public class AuthServiceImpl {
    @Value("${post.sale.platform-url}")
    private String platformUrl;
    @Value("${post.sale.access_token}")
    private String appAccessToken;

    @Value("${post.sale.client}")
    private String client;
    @Value("${post.sale.grant_type}")
    private String grantType;


    @Value("${information.pre_url}")
    private String preUrl;
    @Value("${information.warehouse}")
    private String warehouse;
    @Value("${information.oauth_token}")
    private String authApi;
    @Value("${information.username}")
    private String erpUsername;
    @Value("${information.password}")
    private String erpPassword;
    @Value("${information.header_name1}")
    private String headerName1;
    @Value("${information.header_value1}")
    private String headerValue1;
    @Value("${information.auth_token_validity_seconds}")
    private Long authTokenValiditySeconds;
    @Value("${post.sale.username}")
    private String username;
    @Value("${post.sale.password}")
    private String password;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public String getPostSaleAccessToken() {
        String accessToken = null;
        try {
            accessToken = stringRedisTemplate.opsForValue().get(ISysConstant.POST_SALE_OAUTH_TOKEN_KEY);
            log.info("获取售后认证信息-获取缓存值, accessToken:{}", accessToken);
            if (hasText(accessToken)) {
                JWT jwt = JWTUtil.parseToken(accessToken);
                JWTPayload payload = jwt.getPayload();
                JSONObject claimsJson = payload.getClaimsJson();
                if (claimsJson.getInt("exp") <= System.currentTimeMillis() / 1000) { //1min
                    JSONObject authInfo = this.getPostSaleAuthInfo();
                    accessToken = authInfo.getStr("access_token");
                }
            } else {
                JSONObject authInfo = this.getPostSaleAuthInfo();
                accessToken = authInfo.getStr("data");
            }
        } catch (Exception e) {
            log.error("获取售后服务平台认证信息失败/解析有误", e);
        }
        return accessToken;
    }

    public JSONObject getPostSaleAuthInfo() {
        HttpRequest httpRequest = HttpRequest.post(platformUrl + appAccessToken)
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
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK)
                throw new BusinessException(body.getStr("message"));
            Integer expiresIn = body.getInt("expires_in");
            if (Objects.nonNull(expiresIn) && expiresIn > 0) {
                stringRedisTemplate.opsForValue().set(ISysConstant.POST_SALE_OAUTH_TOKEN_KEY, body.getStr("access_token"), expiresIn, TimeUnit.SECONDS);
            }
            return body;
        }
    }


    /**
     * 获取oa系统授权
     */
    public String getErpToken() {
        log.info("开始获取信息化系统授权");
        String accessToken = null;
        try {
            accessToken = stringRedisTemplate.opsForValue().get(ISysConstant.INFORMATION_OAUTH_TOKEN_KEY);
            log.info("获取信息化系统认证信息-获取缓存值, accessToken:{}", accessToken);
            if (hasText(accessToken)) {
                JWT jwt = JWTUtil.parseToken(accessToken);
                JWTPayload payload = jwt.getPayload();
                JSONObject claimsJson = payload.getClaimsJson();
                if (claimsJson.getInt("exp") != null && claimsJson.getInt("exp") <= System.currentTimeMillis() / 1000) { //1min
                    accessToken = this.getErpAuthInfo();
                }
            } else {
                accessToken = this.getErpAuthInfo();
            }
        } catch (Exception e) {
            log.error("获取信息化系统认证信息失败/解析有误", e);
        }
        return accessToken;
    }

    public String getErpAuthInfo() {
        HttpRequest httpRequest = HttpRequest.post(preUrl + authApi)
                .form("username", erpUsername)
                .form("password", erpPassword)
                .timeout(1000 * 60); //1min
        httpRequest.header(headerName1, headerValue1);
        log.info("获取信息化系统认证信息，url:{}, 请求参数:{}", httpRequest.getUrl(), JsonUtils.toJsonString(httpRequest.form()));
        HttpResponse httpResponse = httpRequest.execute();
        log.info("获取信息化系统认证信息，获得返回值:{}", httpResponse.body());
        if (!httpResponse.isOk()) {
            log.error("信息化系统认证失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("系统异常，请联系管理员");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK)
                throw new BusinessException(body.getStr("message"));
            Object data = body.get("data");
            String accessToken = JSONUtil.parseObj(data).getStr("accessToken");
            stringRedisTemplate.opsForValue().set(ISysConstant.INFORMATION_OAUTH_TOKEN_KEY, accessToken, 7200, TimeUnit.SECONDS);
            return accessToken;
        }
    }
}
