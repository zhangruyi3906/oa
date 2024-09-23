package com.lh.oa.module.system.service.oauth2;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.string.StrUtils;
import com.lh.oa.module.system.controller.admin.oauth2.vo.client.OAuth2ClientCreateReqVO;
import com.lh.oa.module.system.controller.admin.oauth2.vo.client.OAuth2ClientPageReqVO;
import com.lh.oa.module.system.controller.admin.oauth2.vo.client.OAuth2ClientUpdateReqVO;
import com.lh.oa.module.system.convert.auth.OAuth2ClientConvert;
import com.lh.oa.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import com.lh.oa.module.system.dal.mysql.oauth2.OAuth2ClientMapper;
import com.google.common.annotations.VisibleForTesting;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * OAuth2.0 Client Service 实现类
 *
 * @author
 */
@Service
@Validated
@Slf4j
public class OAuth2ClientServiceImpl implements OAuth2ClientService {

    /**
     * 客户端缓存
     * key：客户端编号 {@link OAuth2ClientDO#getClientId()} ()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter
    private volatile Map<String, OAuth2ClientDO> clientCache;

    @Resource
    private OAuth2ClientMapper oauth2ClientMapper;

    /**
     * 初始化 {@link #clientCache} 缓存
     */
    @Override
    @PostConstruct
    public void initLocalCache() {
        List<OAuth2ClientDO> clients = oauth2ClientMapper.selectList();
        log.info("[initLocalCache][缓存 OAuth2 客户端，数量为:{}]", clients.size());

        clientCache = convertMap(clients, OAuth2ClientDO::getClientId);
    }

    @Override
    public Long createOAuth2Client(OAuth2ClientCreateReqVO createReqVO) {
        validateClientIdExists(null, createReqVO.getClientId());
        OAuth2ClientDO oauth2Client = OAuth2ClientConvert.INSTANCE.convert(createReqVO);
        oauth2ClientMapper.insert(oauth2Client);
//        oauth2ClientProducer.sendOAuth2ClientRefreshMessage();
        ThreadUtil.execAsync(this::initLocalCache);
        return oauth2Client.getId();
    }

    @Override
    public void updateOAuth2Client(OAuth2ClientUpdateReqVO updateReqVO) {
        validateOAuth2ClientExists(updateReqVO.getId());
        validateClientIdExists(updateReqVO.getId(), updateReqVO.getClientId());

        OAuth2ClientDO updateObj = OAuth2ClientConvert.INSTANCE.convert(updateReqVO);
        oauth2ClientMapper.updateById(updateObj);
//        oauth2ClientProducer.sendOAuth2ClientRefreshMessage();
        ThreadUtil.execAsync(this::initLocalCache);
    }

    @Override
    public void deleteOAuth2Client(Long id) {
        validateOAuth2ClientExists(id);
        oauth2ClientMapper.deleteById(id);
//        oauth2ClientProducer.sendOAuth2ClientRefreshMessage();
        ThreadUtil.execAsync(this::initLocalCache);
    }

    private void validateOAuth2ClientExists(Long id) {
        if (oauth2ClientMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_NOT_EXISTS);
        }
    }

    @VisibleForTesting
    void validateClientIdExists(Long id, String clientId) {
        OAuth2ClientDO client = oauth2ClientMapper.selectByClientId(clientId);
        if (client == null) {
            return;
        }
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_EXISTS);
        }
        if (!client.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_EXISTS);
        }
    }

    @Override
    public OAuth2ClientDO getOAuth2Client(Long id) {
        return oauth2ClientMapper.selectById(id);
    }

    @Override
    public PageResult<OAuth2ClientDO> getOAuth2ClientPage(OAuth2ClientPageReqVO pageReqVO) {
        return oauth2ClientMapper.selectPage(pageReqVO);
    }

    @Override
    public OAuth2ClientDO validOAuthClientFromCache(String clientId, String clientSecret,
                                                    String authorizedGrantType, Collection<String> scopes, String redirectUri) {
        OAuth2ClientDO client = clientCache.get(clientId);
        if (client == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_NOT_EXISTS);
        }
        if (ObjectUtil.notEqual(client.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_DISABLE);
        }

        if (StrUtil.isNotEmpty(clientSecret) && ObjectUtil.notEqual(client.getSecret(), clientSecret)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_CLIENT_SECRET_ERROR);
        }
        if (StrUtil.isNotEmpty(authorizedGrantType) && !CollUtil.contains(client.getAuthorizedGrantTypes(), authorizedGrantType)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_AUTHORIZED_GRANT_TYPE_NOT_EXISTS);
        }
        if (CollUtil.isNotEmpty(scopes) && !CollUtil.containsAll(client.getScopes(), scopes)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_SCOPE_OVER);
        }
        if (StrUtil.isNotEmpty(redirectUri) && !StrUtils.startWithAny(redirectUri, client.getRedirectUris())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_REDIRECT_URI_NOT_MATCH, redirectUri);
        }
        return client;
    }

}
