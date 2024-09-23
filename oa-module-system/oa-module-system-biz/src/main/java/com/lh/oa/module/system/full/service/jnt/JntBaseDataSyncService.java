package com.lh.oa.module.system.full.service.jnt;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Joiner;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.system.controller.admin.project.vo.JntMaterialVO;
import com.lh.oa.module.system.controller.admin.project.vo.JntWarehouseProjectVO;
import com.lh.oa.module.system.controller.admin.project.vo.JntWarehouseVO;
import com.lh.oa.module.system.full.constants.ISysConstant;
import com.lh.oa.module.system.full.entity.jnt.JntDept;
import com.lh.oa.module.system.full.entity.jnt.JntOrg;
import com.lh.oa.module.system.full.entity.jnt.JntPost;
import com.lh.oa.module.system.full.entity.jnt.JntProject;
import com.lh.oa.module.system.full.entity.jnt.JntProjectUser;
import com.lh.oa.module.system.full.entity.jnt.JntUser;
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Component
public class JntBaseDataSyncService {

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
    @Value("${jnt.sync-project-api}")
    private String syncProjectApi;
    @Value("${jnt.sync-org-api}")
    private String syncOrgApi;
    @Value("${jnt.sync-dept-api}")
    private String syncDeptApi;
    @Value("${jnt.sync-post-api}")
    private String syncPostApi;
    @Value("${jnt.sync-user-api}")
    private String syncUserApi;
    @Value("${jnt.sync-user-state-api}")
    private String syncUserStateApi;
    @Value("${jnt.sync-user-project-api}")
    private String syncUserProjectApi;
    @Value("${jnt.query-user-byOa-api}")
    private String queryUserByOaApi;
    @Value("${jnt.query-project-api}")
    private String queryProjectApi;
    @Value("${jnt.query-material-by-project-api}")
    private String queryMaterialByProjectApi;
    @Value("${jnt.query-warehouse-by-project-api}")
    private String queryWarehouseByProjectApi;

    @Value("${jnt.query-projectByIds-api}")
    private String queryProjectByIdsApi;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void syncProject(JntProject jntProject) {
        String jntAccessToken = this.getJntAccessToken();
        String jntProjectApiUrl = platformUrl + syncProjectApi;
        HttpRequest httpRequest;
        if (jntProject.getOperateType() == OperateTypeEnum.DELETE) {
            httpRequest = HttpRequest.delete(jntProjectApiUrl)
                    .form("ids", jntProject.getIds())
                    .header("X-OA-Operate-Type", OperateTypeEnum.DELETE.name());
        } else if (jntProject.getOperateType() == OperateTypeEnum.UPDATE) {
            httpRequest = HttpRequest.put(jntProjectApiUrl)
                    .body(JSONUtil.toJsonStr(jntProject));
        } else {
            httpRequest = HttpRequest.post(jntProjectApiUrl)
                    .body(JSONUtil.toJsonStr(jntProject));
        }
        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("项目同步失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("项目同步失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("项目同步失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
        }
    }


    public void syncOrg(JntOrg jntOrg) {
        String jntAccessToken = this.getJntAccessToken();
        String jntOrgApiUrl = platformUrl + syncOrgApi;
        HttpRequest httpRequest;
        if (jntOrg.getOperateType() == OperateTypeEnum.DELETE) {
            httpRequest = HttpRequest.delete(jntOrgApiUrl)
                    .form("ids", jntOrg.getIds())
                    .header("X-OA-Operate-Type", OperateTypeEnum.DELETE.name());
        } else if (jntOrg.getOperateType() == OperateTypeEnum.UPDATE) {
            httpRequest = HttpRequest.put(jntOrgApiUrl)
                    .body(JSONUtil.toJsonStr(jntOrg));
        } else {
            httpRequest = HttpRequest.post(jntOrgApiUrl)
                    .body(JSONUtil.toJsonStr(jntOrg));
        }
        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("组织架构同步失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("组织架构同步失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("组织架构同步失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
        }
    }

    public void syncDept(JntDept jntDept) {
        String jntAccessToken = this.getJntAccessToken();
        String jntDeptApiUrl = platformUrl + syncDeptApi;
        HttpRequest httpRequest;
        if (jntDept.getOperateType() == OperateTypeEnum.DELETE) {
            httpRequest = HttpRequest.delete(jntDeptApiUrl)
                    .form("ids", jntDept.getIds())
                    .header("X-OA-Operate-Type", OperateTypeEnum.DELETE.name());
        } else if (jntDept.getOperateType() == OperateTypeEnum.UPDATE) {
            httpRequest = HttpRequest.put(jntDeptApiUrl)
                    .body(JSONUtil.toJsonStr(jntDept));
        } else {
            httpRequest = HttpRequest.post(jntDeptApiUrl)
                    .body(JSONUtil.toJsonStr(jntDept));
        }
        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("组织架构同步失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("组织架构同步失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("组织架构同步失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
        }
    }

    public void syncPost(JntPost jntPost) {
        String jntAccessToken = this.getJntAccessToken();
        String jntPostApiUrl = platformUrl + syncPostApi;
        HttpRequest httpRequest;
        if (jntPost.getOperateType() == OperateTypeEnum.DELETE) {
            httpRequest = HttpRequest.delete(jntPostApiUrl)
                    .form("ids", jntPost.getIds())
                    .header("X-OA-Operate-Type", OperateTypeEnum.DELETE.name());
        } else if (jntPost.getOperateType() == OperateTypeEnum.UPDATE) {
            httpRequest = HttpRequest.put(jntPostApiUrl)
                    .body(JSONUtil.toJsonStr(jntPost));
        } else {
            httpRequest = HttpRequest.post(jntPostApiUrl)
                    .body(JSONUtil.toJsonStr(jntPost));
        }
        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("岗位同步失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("岗位同步失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("岗位同步失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
        }
    }

    public void syncUser(JntUser jntUser) {
        String jntAccessToken = this.getJntAccessToken();
        String jntUserApiUrl = platformUrl + syncUserApi;
        HttpRequest httpRequest;
        if (jntUser.getOperateType() == OperateTypeEnum.DELETE) {
            httpRequest = HttpRequest.delete(jntUserApiUrl)
                    .form("ids", jntUser.getIds())
                    .header("X-OA-Operate-Type", OperateTypeEnum.DELETE.name());
        } else if (jntUser.getOperateType() == OperateTypeEnum.UPDATE) {
            httpRequest = HttpRequest.put(jntUserApiUrl)
                    .body(JSONUtil.toJsonStr(jntUser));
        } else {
            httpRequest = HttpRequest.post(jntUserApiUrl)
                    .body(JSONUtil.toJsonStr(jntUser));
        }
        log.info("向PMS同步用户信息，url:{}, 请求参数:{}", httpRequest.getUrl(), JsonUtils.toJsonString(jntUser));
        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        log.info("向PMS同步用户信息，返回结果:{}", httpResponse.body());
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

    public String getSysUserByOaUserId(int userId) {
        String jntAccessToken = this.getJntAccessToken();
        String jntUserApiUrl = platformUrl + queryUserByOaApi;
        HttpRequest httpRequest;

        httpRequest = HttpRequest.get(jntUserApiUrl)
                .form("oaUserId", userId);

        log.info("用户查询:{}", jntUserApiUrl + "?oaUserId=" + userId);

        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("用户查询失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("用户查询失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("用户查询失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
            Object data = body.get("data");
            Map<String, Object> sysUserMap = JsonUtils.covertObject2Map(data);
            Object account = sysUserMap.get("account");
            if (Objects.isNull(account)) {
                log.info("用户查询失败, " + JSONUtil.toJsonStr(body));
            }
            return account.toString();
        }
    }

    public List<JntWarehouseProjectVO> getWarehouseProject() {
        String jntAccessToken = this.getJntAccessToken();
        String jntProjectApiUrl = platformUrl + queryProjectApi;
        HttpRequest httpRequest;

        httpRequest = HttpRequest.get(jntProjectApiUrl).form("userId",getLoginUserId());

        log.info("项目查询:{}", jntProjectApiUrl);

        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("项目查询失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("项目查询失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("项目查询失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
            Object data = body.get("data");
            String s = com.alibaba.fastjson.JSONObject.toJSONString(data);
            List<JntWarehouseProjectVO> warehouseProjectVOS = com.alibaba.fastjson.JSONObject.parseArray(s, JntWarehouseProjectVO.class);
            if(ObjectUtils.isEmpty(warehouseProjectVOS)){
                log.info("项目查询失败, " + JSONUtil.toJsonStr(body));
            }
            return warehouseProjectVOS;
        }
    }

    public Map<String, String> getJNTProjectByIds(String jntProjectIds) {
        String jntAccessToken = this.getJntAccessToken();
        String jntProjectApiUrl = platformUrl + queryProjectByIdsApi;
        HttpRequest httpRequest;

        httpRequest = HttpRequest.get(jntProjectApiUrl).form("projectIds", jntProjectIds);

        log.info("根据ids查询JNT项目信息:{}", jntProjectApiUrl);

        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("根据ids查询JNT项目信息失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("查询JNT项目信息失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("根据ids查询JNT项目信息失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
            Object data = body.get("data");
            Map<String, String> result = JsonUtils.covertObject(data, new TypeReference<Map<String, String>>() {});
            if(ObjectUtils.isEmpty(result)){
                log.info("查询JNT项目信息失败, " + JSONUtil.toJsonStr(body));
            }
            return result;
        }
    }

    public void syncUserProject(JntProjectUser jntProjectUser) {
        String jntAccessToken = this.getJntAccessToken();
        String jntProjectUserApiUrl = platformUrl + syncUserProjectApi;
        HttpRequest httpRequest;
        if (jntProjectUser.getOperateType() == OperateTypeEnum.DELETE) {
            httpRequest = HttpRequest.delete(jntProjectUserApiUrl)
                    .form("id", jntProjectUser.getOaUserId())
                    .form("projectId", jntProjectUser.getOaProjectId())
                    .header("X-OA-Operate-Type", OperateTypeEnum.DELETE.name());
        } else if (jntProjectUser.getOperateType() == OperateTypeEnum.UPDATE) {
            httpRequest = HttpRequest.put(jntProjectUserApiUrl)
                    .body(JSONUtil.toJsonStr(jntProjectUser));
        } else {
            httpRequest = HttpRequest.post(jntProjectUserApiUrl)
                    .body(JSONUtil.toJsonStr(jntProjectUser));
        }
        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("项目人员同步失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("项目人员同步失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("项目人员同步失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
        }
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

    public List<JntMaterialVO> getWarehouseMaterialByProject(Long projectId) {
        String jntAccessToken = this.getJntAccessToken();
        String jntMaterialProjectApiUrl = platformUrl + queryMaterialByProjectApi;
        HttpRequest httpRequest;

        httpRequest = HttpRequest.get(jntMaterialProjectApiUrl)
                .form("projectId", projectId);

        log.info("物料查询:{}", jntMaterialProjectApiUrl + "?projectId=" + projectId);

        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("物料查询失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("物料查询失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("物料查询失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
            Object data = body.get("data");
            String s = com.alibaba.fastjson.JSONObject.toJSONString(data);
            List<JntMaterialVO> jntMaterialVOS = com.alibaba.fastjson.JSONObject.parseArray(s, JntMaterialVO.class);
            if(ObjectUtils.isEmpty(jntMaterialVOS)){
                log.info("项目查询失败, " + JSONUtil.toJsonStr(body));
            }
            return jntMaterialVOS;
        }
    }

    public List<JntWarehouseVO> getWarehouseByProjectId(Long projectId) {
        log.info("根据项目id获取仓库列表,项目id：{}", projectId);
        String jntAccessToken = this.getJntAccessToken();
        String jntMaterialProjectApiUrl = platformUrl + queryWarehouseByProjectApi;
        HttpRequest httpRequest;

        httpRequest = HttpRequest.get(jntMaterialProjectApiUrl)
                .form("projectId", projectId);

        log.info("建能通仓库查询:{}", jntMaterialProjectApiUrl + "?projectId=" + projectId);

        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("建能通仓库查询失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("建能通仓库查询失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("建能通仓库查询失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
            Object data = body.get("data");
            String s = com.alibaba.fastjson.JSONObject.toJSONString(data);
            List<JntWarehouseVO> jntMaterialVOS = com.alibaba.fastjson.JSONObject.parseArray(s, JntWarehouseVO.class);
            if(ObjectUtils.isEmpty(jntMaterialVOS)){
                log.info("建能通仓库查询失败, " + JSONUtil.toJsonStr(body));
            }
            return jntMaterialVOS;
        }
    }

    public void syncUserState(JntUser jntUser) {
        String jntAccessToken = this.getJntAccessToken();
        String jntOrgApiUrl = platformUrl + syncUserStateApi;
        HttpRequest httpRequest = HttpRequest.put(jntOrgApiUrl)
                    .body(JSONUtil.toJsonStr(jntUser));
        HttpResponse httpResponse = httpRequest.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("同步用户状态失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("调用项目管理平台接口失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("同步用户状态失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
        }
    }
}
