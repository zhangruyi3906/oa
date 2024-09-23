package com.lh.oa.module.system.api.user;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import com.lh.oa.module.system.api.user.dto.UserAndInformationDTO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UpdateUserAndInformation;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserAndInformation;
import com.lh.oa.module.system.convert.user.UserConvert;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.full.service.jnt.JntBaseDataSyncService;
import com.lh.oa.module.system.service.user.AdminUserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class AdminUserApiImpl implements AdminUserApi {

    @Resource
    private AdminUserService userService;
    @Resource
    private JntBaseDataSyncService jntBaseDataSyncService;

    @Override
    public CommonResult<AdminUserRespDTO> getUser(Long id) {
        AdminUserDO user = userService.getUser(id);
        return success(UserConvert.INSTANCE.convert4(user));
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getUsers(Collection<Long> ids) {
        List<AdminUserDO> users = userService.getUserList(ids);
        return success(UserConvert.INSTANCE.convertList4(users));
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getUserListByDeptIds(Collection<Long> deptIds) {
        List<AdminUserDO> users = userService.getUserListByDeptIds(deptIds);
        return success(UserConvert.INSTANCE.convertList4(users));
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getUserListByPostIds(Collection<Long> postIds) {
        List<AdminUserDO> users = userService.getUserListByPostIds(postIds);
        return success(UserConvert.INSTANCE.convertList4(users));
    }

    @Override
    public CommonResult<Boolean> validUserList(Set<Long> ids) {
        userService.validateUserList(ids);
        return success(true);
    }


    @Override
    public CommonResult<UserAndInformationDTO> getUserAndInformation(Long id) {
        UserAndInformationDTO userAndInformationDTO = new UserAndInformationDTO();
        UserAndInformation userAndInformation = userService.getUserAndInformation(id);
        BeanUtils.copyProperties(userAndInformation,userAndInformationDTO);
        return success(userAndInformationDTO);
    }

    @Override
    public CommonResult<Boolean> updateUserAndInformation(UserAndInformationDTO userAndInformationDTO) {
        UpdateUserAndInformation updateUserAndInformation = new UpdateUserAndInformation();
        BeanUtils.copyProperties(userAndInformationDTO, updateUserAndInformation);
        userService.updateUserAndInformation(updateUserAndInformation);
        return success(true);
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getListByNickname(String nickname) {
        List<AdminUserDO> users = userService.getUserListByNickname(nickname);
        return success(UserConvert.INSTANCE.convertList4(users));
    }

    @Override
    public CommonResult<Set<Long>> getContainUserList(Long projectId, Long deptId, Long userId) {
        Set<Long> containUserIds = userService.getContainUserIds(projectId, deptId, userId);
        return success(containUserIds);
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getAllUsers() {
        List<AdminUserDO> users = userService.getAllUsers();
        return success(UserConvert.INSTANCE.convertList4(users));
    }

    @Override
    public CommonResult<String> getSysUserAccount(String id) {
        return success(jntBaseDataSyncService.getSysUserByOaUserId(Integer.parseInt(id)));
    }

}
