package com.lh.oa.module.system.controller.admin.oauth2;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.oauth2.vo.token.OAuth2AccessTokenPageReqVO;
import com.lh.oa.module.system.controller.admin.oauth2.vo.token.OAuth2AccessTokenRespVO;
import com.lh.oa.module.system.convert.auth.OAuth2TokenConvert;
import com.lh.oa.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.lh.oa.module.system.enums.logger.LoginLogTypeEnum;
import com.lh.oa.module.system.service.auth.AdminAuthService;
import com.lh.oa.module.system.service.oauth2.OAuth2TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name =  "管理后台 - OAuth2.0 令牌")
@RestController
@RequestMapping("/system/oauth2-token")
public class OAuth2TokenController {

    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Resource
    private AdminAuthService authService;

    @GetMapping("/page")
    //@Operation(summary = "获得访问令牌分页", description = "只返回有效期内的")
    @PreAuthorize("@ss.hasPermission('system:oauth2-token:page')")
    public CommonResult<PageResult<OAuth2AccessTokenRespVO>> getAccessTokenPage(@Valid OAuth2AccessTokenPageReqVO reqVO) {
        PageResult<OAuth2AccessTokenDO> pageResult = oauth2TokenService.getAccessTokenPage(reqVO);
        return success(OAuth2TokenConvert.INSTANCE.convert(pageResult));
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除访问令牌")
    @Parameter(name = "accessToken", description = "访问令牌", required = true, example = "tudou")
    @PreAuthorize("@ss.hasPermission('system:oauth2-token:delete')")
    public CommonResult<Boolean> deleteAccessToken(@RequestParam("accessToken") String accessToken) {
        authService.logout(accessToken, LoginLogTypeEnum.LOGOUT_DELETE.getType());
        return success(true);
    }

}