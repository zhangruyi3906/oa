package com.lh.oa.module.system.materialApply.service;

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
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
import com.lh.oa.module.system.service.erp.AuthServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.StringUtils.hasText;

@Service
@Slf4j
public class MaterialApplyStorageServiceImpl implements MaterialApplyStorageService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
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
    @Value("${jnt.material-storage}")
    private String materialStorageApi;

    @Override
    public String materialApplyStorage(Map<String, Object> variableInstances) {
        log.info("物料申请流程入库，表单信息：{}", JsonUtils.toJsonString(variableInstances));
        //获取表单数据
        JSONObject formInfo = getFormInfo(variableInstances);
        //建能通物料入库
        return jntMaterialStorage(formInfo);
    }

    private JSONObject getFormInfo(Map<String, Object> variableInstances) {
        String projectId = String.valueOf(variableInstances.get("costCompanyName"));//项目
        String userId = String.valueOf(variableInstances.get("userId"));//申请人
        String warehouse = String.valueOf(variableInstances.get("warehouse"));//仓库
        Object table = variableInstances.get("table");
        List<JSONObject> inventoryMaterialList = JsonUtils.parseArray(JsonUtils.toJsonString(table), JSONObject.class);//库存物料
        Object newMaterialTable = variableInstances.get("xinzengtable");
        List<JSONObject> newMaterialList = JsonUtils.parseArray(JsonUtils.toJsonString(newMaterialTable), JSONObject.class);//新增物料
        JSONObject formInfo = new JSONObject();
        formInfo.set("projectId", projectId);
        formInfo.set("userId", userId);
        formInfo.set("warehouse", warehouse);
        formInfo.set("inventoryMaterialList", inventoryMaterialList);
        formInfo.set("newMaterialList", newMaterialList);
        log.info("---------------------物料申请流程表单数据：{}", formInfo);
        return formInfo;
    }

    private String jntMaterialStorage(JSONObject formInfo) {
        String message = "";
        String jntToken = getJntAccessToken();
        HttpRequest httpRequest = HttpRequest.post(platformUrl + materialStorageApi)
                .body(JSONUtil.toJsonStr(formInfo)).bearerAuth(jntToken);

        log.info("物料申请流程完成，建能通自动入库，url:{}, 请求参数:{}", httpRequest.getUrl(), JsonUtils.toJsonString(formInfo));
        HttpResponse httpResponse = httpRequest.bearerAuth(jntToken).timeout(1000 * 60).execute();
        log.info("物料申请流程完成，建能通自动入库返回结果:{}", httpResponse.body());
        if (!httpResponse.isOk()) {
            log.error("建能通自动入库, " + JSONUtil.toJsonStr(httpResponse));
            message = "建能通自动入库失败,请到建能通手动入库";
//            throw new BusinessException("建能通自动入库失败-系统内部错误：" + JSONUtil.toJsonStr(httpResponse));
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("自动入库失败, " + JSONUtil.toJsonStr(body));
//                throw new BusinessException(body.getStr("message"));
                message = "建能通自动入库失败,请到建能通手动入库";
            } else {
                log.info("自动入库完成");
                message = "您的物料申请流程已完成，物料已自动入库";
            }
        }
        return message;

    }

    private String getJntAccessToken() {
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
