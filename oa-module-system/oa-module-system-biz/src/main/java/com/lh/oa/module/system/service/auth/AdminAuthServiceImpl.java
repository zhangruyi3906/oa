package com.lh.oa.module.system.service.auth;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.annotations.VisibleForTesting;
import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.enums.UserTypeEnum;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.util.monitor.TracerUtils;
import com.lh.oa.framework.common.util.servlet.ServletUtils;
import com.lh.oa.framework.common.util.validation.ValidationUtils;
import com.lh.oa.module.system.api.logger.dto.LoginLogCreateReqDTO;
import com.lh.oa.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import com.lh.oa.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.lh.oa.module.system.convert.auth.AuthConvert;
import com.lh.oa.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.mysql.oauth2.OAuth2AccessTokenMapper;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.dal.redis.oauth2.OAuth2AccessTokenRedisDAO;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.enums.logger.LoginLogTypeEnum;
import com.lh.oa.module.system.enums.logger.LoginResultEnum;
import com.lh.oa.module.system.enums.oauth2.OAuth2ClientConstants;
import com.lh.oa.module.system.service.logger.LoginLogService;
import com.lh.oa.module.system.service.member.MemberService;
import com.lh.oa.module.system.service.oauth2.OAuth2TokenService;
import com.lh.oa.module.system.service.user.AdminUserService;
import com.xingyuv.captcha.model.common.ResponseModel;
import com.xingyuv.captcha.model.vo.CaptchaVO;
import com.xingyuv.captcha.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.Objects;

/**
 * Auth Service 实现类
 *
 * @author
 */
@Service
@Slf4j
public class AdminAuthServiceImpl implements AdminAuthService {

    @Resource
    private AdminUserService userService;
    @Resource
    private LoginLogService loginLogService;
    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Resource
    private MemberService memberService;
    @Resource
    private Validator validator;
    @Resource
    private CaptchaService captchaService;
    @Resource
    private AdminUserMapper mapper;
    @Resource
    private OAuth2AccessTokenMapper oAuth2AccessTokenMapper;
    @Resource
    private OAuth2AccessTokenRedisDAO oAuth2AccessTokenRedisDAO;

    /**
     *
     */
    @Value("${oa.captcha.enable}")
    private Boolean captchaEnable;

    @Override
    public AdminUserDO authenticate(String username, String password) {
        final LoginLogTypeEnum logTypeEnum = LoginLogTypeEnum.LOGIN_USERNAME;
        AdminUserDO user = userService.getUserByUsername(username);
        if (user == null) {
            createLoginLog(null, username, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (!userService.isPasswordMatch(password, user.getPassword())) {
            createLoginLog(user.getId(), username, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (ObjectUtil.notEqual(user.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            createLoginLog(user.getId(), username, logTypeEnum, LoginResultEnum.USER_DISABLED);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED);
        }
        return user;
    }

    @Override
    public AuthLoginRespVO login(AuthLoginReqVO reqVO) {
        validateCaptcha(reqVO);
        AdminUserDO user = authenticate(reqVO.getUsername(), reqVO.getPassword());
//        if (user.getId() != 1) {
//            List<OAuth2AccessTokenDO> oAuth2AccessTokenDO = oAuth2AccessTokenMapper.selectList(new LambdaQueryWrapperX<OAuth2AccessTokenDO>()
//                    .eqIfPresent(OAuth2AccessTokenDO::getUserId, user.getId())
//                    .ge(OAuth2AccessTokenDO::getExpiresTime, LocalDateTime.now()));
//            if (!oAuth2AccessTokenDO.isEmpty()) {
//                oAuth2AccessTokenDO.forEach(s -> logout(s.getAccessToken(), LoginLogTypeEnum.LOGOUT_DELETE.getType()));
//            }
//        }
        AuthLoginRespVO authLoginRespVO = createTokenAfterLoginSuccess(user.getId(), reqVO.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME);
        authLoginRespVO.setUsername(user.getNickname());
        return authLoginRespVO;
    }

    @Override
    public AuthLoginRespVO loginOthers(Long id) {
        AdminUserDO user = mapper.selectById(id);
//        if (user.getId() != 1) {
//            List<OAuth2AccessTokenDO> oAuth2AccessTokenDO = oAuth2AccessTokenMapper.selectList(new LambdaQueryWrapperX<OAuth2AccessTokenDO>()
//                    .eqIfPresent(OAuth2AccessTokenDO::getUserId, user.getId())
//                    .ge(OAuth2AccessTokenDO::getExpiresTime, LocalDateTime.now()));
//            if (!oAuth2AccessTokenDO.isEmpty()) {
//                oAuth2AccessTokenDO.forEach(s -> logout(s.getAccessToken(), LoginLogTypeEnum.LOGOUT_DELETE.getType()));
//            }
//        }
        return createTokenAfterLoginSuccess(user.getId(), user.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME);
    }

    private void createLoginLog(Long userId, String username,
                                LoginLogTypeEnum logTypeEnum, LoginResultEnum loginResult) {
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logTypeEnum.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(getUserType().getValue());
        reqDTO.setUsername(username);
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setResult(loginResult.getResult());
        loginLogService.createLoginLog(reqDTO);
        if (userId != null && Objects.equals(LoginResultEnum.SUCCESS.getResult(), loginResult.getResult())) {
            userService.updateUserLogin(userId, ServletUtils.getClientIP());
        }
    }

    @VisibleForTesting
    void validateCaptcha(AuthLoginReqVO reqVO) {
        if (!captchaEnable) {
            return;
        }
        ValidationUtils.validate(validator, reqVO, AuthLoginReqVO.CodeEnableGroup.class);
        CaptchaVO captchaVO = new CaptchaVO();
//        captchaVO.setCaptchaVerification(reqVO.getCaptchaVerification());
        ResponseModel response = captchaService.verification(captchaVO);
        if (!response.isSuccess()) {
            createLoginLog(null, reqVO.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME, LoginResultEnum.CAPTCHA_CODE_ERROR);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_CAPTCHA_CODE_ERROR, response.getRepMsg());
        }
    }

    private AuthLoginRespVO createTokenAfterLoginSuccess(Long userId, String username, LoginLogTypeEnum logType) {
        createLoginLog(userId, username, logType, LoginResultEnum.SUCCESS);
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(userId, getUserType().getValue(),
                OAuth2ClientConstants.CLIENT_ID_DEFAULT, null);
        return AuthConvert.INSTANCE.convert(accessTokenDO);
    }

    @Override
    public AuthLoginRespVO refreshToken(String refreshToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken, OAuth2ClientConstants.CLIENT_ID_DEFAULT);
        return AuthConvert.INSTANCE.convert(accessTokenDO);
    }

    @Override
    public void logout(String token, Integer logType) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.removeAccessToken(token);
        oAuth2AccessTokenRedisDAO.delete(token);
        if (accessTokenDO == null) {
            return;
        }
        createLogoutLog(accessTokenDO.getUserId(), accessTokenDO.getUserType(), logType);
    }

    private void createLogoutLog(Long userId, Integer userType, Integer logType) {
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logType);
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(userType);
        if (ObjectUtil.equal(getUserType().getValue(), userType)) {
            reqDTO.setUsername(getUsername(userId));
        } else {
            reqDTO.setUsername(memberService.getMemberUserMobile(userId));
        }
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setResult(LoginResultEnum.SUCCESS.getResult());
        loginLogService.createLoginLog(reqDTO);
    }

    private String getUsername(Long userId) {
        if (userId == null) {
            return null;
        }
        AdminUserDO user = userService.getUser(userId);
        return user != null ? user.getUsername() : null;
    }

    private UserTypeEnum getUserType() {
        return UserTypeEnum.ADMIN;
    }

}
