package com.lh.oa.module.system.controller.admin.user;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.datapermission.core.annotation.DataPermission;
import com.lh.oa.module.system.controller.admin.user.vo.profile.UserProfileRespVO;
import com.lh.oa.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import com.lh.oa.module.system.convert.user.UserConvert;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.dept.PostDO;
import com.lh.oa.module.system.dal.dataobject.permission.RoleDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.service.dept.DeptService;
import com.lh.oa.module.system.service.dept.PostService;
import com.lh.oa.module.system.service.permission.PermissionService;
import com.lh.oa.module.system.service.permission.RoleService;
import com.lh.oa.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.lh.oa.module.infra.enums.ErrorCodeConstants.FILE_IS_EMPTY;

@Tag(name =  "管理后台 - 用户个人中心")
@RestController
@RequestMapping("/system/user/profile")
@Validated
@Slf4j
public class UserProfileController {

    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;
    @Resource
    private PostService postService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;

    @GetMapping("/get")
    //@Operation(summary = "获得登录用户信息")
    @DataPermission(enable = false)
    public CommonResult<UserProfileRespVO> profile() {
        AdminUserDO user = userService.getUser(getLoginUserId());
        UserProfileRespVO resp = UserConvert.INSTANCE.convert03(user);
        List<RoleDO> userRoles = roleService.getRoleListFromCache(permissionService.getUserRoleIdListByUserId(user.getId()));
        resp.setRoles(UserConvert.INSTANCE.convertList(userRoles));
        if (user.getDeptId() != null) {
            DeptDO dept = deptService.getDept(user.getDeptId());
            resp.setDept(UserConvert.INSTANCE.convert02(dept));
        }
        if (CollUtil.isNotEmpty(user.getPostIds())) {
            List<PostDO> posts = postService.getPostList(user.getPostIds());
            resp.setPosts(UserConvert.INSTANCE.convertList02(posts));
        }
        return success(resp);
    }

    @PutMapping("/update")
    //@Operation(summary = "修改用户个人信息")
    public CommonResult<Boolean> updateUserProfile(@Valid @RequestBody UserProfileUpdateReqVO reqVO) {
        userService.updateUserProfile(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/update-password")
    //@Operation(summary = "修改用户个人密码")
    public CommonResult<Boolean> updateUserProfilePassword(@Valid @RequestBody UserProfileUpdatePasswordReqVO reqVO) {
        userService.updateUserPassword(getLoginUserId(), reqVO);
        return success(true);
    }

    @RequestMapping(value = "/update-avatar", method = {RequestMethod.POST, RequestMethod.PUT}) // 解决 uni-app 不支持 Put 上传文件的问题
    //@Operation(summary = "上传用户个人头像")
    public CommonResult<String> updateUserAvatar(@RequestParam("avatarFile") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw exception(FILE_IS_EMPTY);
        }
        String avatar = userService.updateUserAvatar(getLoginUserId(), file);
        return success(avatar);
    }

}
