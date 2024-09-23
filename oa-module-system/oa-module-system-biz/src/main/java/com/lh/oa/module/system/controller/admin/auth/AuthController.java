package com.lh.oa.module.system.controller.admin.auth;

import cn.hutool.core.text.CharSequenceUtil;
import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.collection.SetUtils;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.security.config.SecurityProperties;
import com.lh.oa.framework.security.core.util.SecurityFrameworkUtils;
import com.lh.oa.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import com.lh.oa.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.lh.oa.module.system.controller.admin.auth.vo.AuthMenuRespVO;
import com.lh.oa.module.system.controller.admin.auth.vo.AuthPermissionInfoRespVO;
import com.lh.oa.module.system.convert.auth.AuthConvert;
import com.lh.oa.module.system.dal.dataobject.permission.MenuDO;
import com.lh.oa.module.system.dal.dataobject.permission.RoleDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.enums.logger.LoginLogTypeEnum;
import com.lh.oa.module.system.enums.permission.MenuTypeEnum;
import com.lh.oa.module.system.service.auth.AdminAuthService;
import com.lh.oa.module.system.service.information.InformationService;
import com.lh.oa.module.system.service.permission.PermissionService;
import com.lh.oa.module.system.service.permission.RoleService;
import com.lh.oa.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.obtainAuthorization;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.lh.oa.module.system.enums.ErrorCodeConstants.USER_NOT_EXISTS;
import static java.util.Collections.singleton;

@Tag(name = "管理后台 - 认证")
@RestController
@RequestMapping("/system/auth")
@Validated
@Slf4j
public class AuthController {

    @Resource
    private AdminAuthService authService;
    @Resource
    private AdminUserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;

    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private InformationService informationService;

    @PostMapping("/login")
    @PermitAll
    ////@Operation(summary = "使用账号密码登录")
    //@Operation(enable = false)
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {
        return success(authService.login(reqVO));
    }

    @PostMapping("/logout")
    @PermitAll
    ////@Operation(summary = "登出系统")
    //@Operation(enable = false)
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = obtainAuthorization(request, securityProperties.getTokenHeader());
        if (CharSequenceUtil.isNotBlank(token)) {
            authService.logout(token, LoginLogTypeEnum.LOGOUT_SELF.getType());
        }
        return success(true);
    }

    @PostMapping("/refresh-token")
    @PermitAll
    ////@Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    //@Operation(enable = false)
    public CommonResult<AuthLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return success(authService.refreshToken(refreshToken));
    }

    @GetMapping("/get-permission-info")
    ////@Operation(summary = "获取登录用户的权限信息")
    public CommonResult<AuthPermissionInfoRespVO> getPermissionInfo() {
        // 获得用户信息
        Long loginUserId = getLoginUserId();
//        Long loginUserId = 1l;

        AdminUserDO user = userService.getUser(loginUserId);
        if (user == null) {
            return null;
        }
        // 获得角色列表
        Set<Long> roleIds = permissionService.getUserRoleIdsFromCache(loginUserId, singleton(CommonStatusEnum.ENABLE.getStatus()));
        List<RoleDO> roleList = roleService.getRoleListFromCache(roleIds);
        // 获得菜单列表
        List<MenuDO> menuList = permissionService.getRoleMenuListFromCache(roleIds,
                SetUtils.asSet(MenuTypeEnum.DIR.getType(), MenuTypeEnum.MENU.getType(), MenuTypeEnum.BUTTON.getType()),
                singleton(CommonStatusEnum.ENABLE.getStatus())); // 只要开启的
        // 拼接结果返回
        return success(AuthConvert.INSTANCE.convert(user, roleList, menuList));
    }


    @GetMapping("/list-menus")
    ////@Operation(summary = "获得登录用户的菜单列表")
    public CommonResult<List<AuthMenuRespVO>> getMenus() {
        Set<Long> roleIds = permissionService.getUserRoleIdsFromCache(getLoginUserId(), singleton(CommonStatusEnum.ENABLE.getStatus()));
        List<MenuDO> menuList = permissionService.getRoleMenuListFromCache(roleIds,
                SetUtils.asSet(MenuTypeEnum.DIR.getType(), MenuTypeEnum.MENU.getType()), // 只要目录和菜单类型
                singleton(CommonStatusEnum.ENABLE.getStatus())); // 只要开启的
        return success(AuthConvert.INSTANCE.buildMenuTree(menuList));
    }

    @GetMapping("/login-others")
    @PermitAll
    ////@Operation(summary = "使用账号密码登录")
    //@Operation(enable = false)
    public CommonResult<AuthLoginRespVO> loginOthers(@Valid Long id) {
        AuthLoginRespVO authLoginRespVO = authService.loginOthers(id);
        AdminUserDO adminUserDO = userService.getUser(id);
        if (adminUserDO == null) {
            throw exception(USER_NOT_EXISTS);
        }
        if (adminUserDO.getDeptId() != null) {
            authLoginRespVO.setDeptId(adminUserDO.getDeptId());
            authLoginRespVO.setUsername(adminUserDO.getNickname());
            authLoginRespVO.setBankAccount(adminUserDO.getBankAccount());
            authLoginRespVO.setBankAccountNumber(adminUserDO.getBankAccountNumber());
            if (Objects.nonNull(SecurityFrameworkUtils.getLoginUser())) {
                SecurityFrameworkUtils.setLoginUserDeptId(SecurityFrameworkUtils.getLoginUser(), adminUserDO.getDeptId());
            }
        }
        if (ObjectUtils.isNotEmpty(adminUserDO.getPostIds())) {
            authLoginRespVO.setPostId((Long) adminUserDO.getPostIds().toArray()[0]);
        }

        log.info("登录信息:{}", JsonUtils.toJsonString(SecurityFrameworkUtils.getLoginUser()));
        return success(authLoginRespVO);
    }
}