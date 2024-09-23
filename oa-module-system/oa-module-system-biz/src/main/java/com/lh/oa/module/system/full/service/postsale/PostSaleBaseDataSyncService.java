package com.lh.oa.module.system.full.service.postsale;

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
import com.lh.oa.module.system.full.entity.postsale.PostSaleUser;
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
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
public class PostSaleBaseDataSyncService {
    @Value("${post.sale.platform-url}")
    private String platformUrl;
    @Value("${post.sale.access_token}")
    private String appAccessToken;
    @Value("${post.sale.username}")
    private String username;

    @Value("${post.sale.password}")
    private String password;

    @Value("${post.sale.client}")
    private String client;
    @Value("${post.sale.grant_type}")
    private String grantType;
    @Value("${post.sale.sync-user}")
    private String syncUserUrl;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    public void syncUser(PostSaleUser user){
        String accessToken = getPostSaleAccessToken();
        String userApiUrl = platformUrl + syncUserUrl;
        HttpRequest httpRequest;
        if (user.getOperateType() == OperateTypeEnum.DELETE) {
            httpRequest = HttpRequest.delete(userApiUrl)
                    .form("id", user.getId())
                    .header("X-OA-Operate-Type", OperateTypeEnum.DELETE.name());
        } else if (user.getOperateType() == OperateTypeEnum.UPDATE) {
            httpRequest = HttpRequest.put(userApiUrl)
                    .body(JSONUtil.toJsonStr(user));
        } else {
            httpRequest = HttpRequest.post(userApiUrl)
                    .body(JSONUtil.toJsonStr(user)).bearerAuth(accessToken);
        }
        log.info("向售后服务平台同步用户信息，url:{}, 请求参数:{}", httpRequest.getUrl(), JsonUtils.toJsonString(user));
        HttpResponse httpResponse = httpRequest.bearerAuth(accessToken).timeout(1000 * 60).execute();
        log.info("向售后服务平台同步用户信息，返回结果:{}", httpResponse.body());
        if (!httpResponse.isOk()) {
            log.error("用户同步失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("用户同步失败-系统内部错误");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("用户同步失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
        }
    }

    private String getPostSaleAccessToken() {
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

    private JSONObject getPostSaleAuthInfo() {
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

}
