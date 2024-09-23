package com.lh.oa.module.system.api.user;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import com.lh.oa.module.system.api.user.dto.UserAndInformationDTO;
import com.lh.oa.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = ApiConstants.NAME) //
@Tag(name = "RPC 服务 - 管理员用户")
public interface AdminUserApi {

    String PREFIX = ApiConstants.PREFIX + "/user";

    @GetMapping(PREFIX + "/get")
    //@Operation(summary = "通过用户 ID 查询用户")
    @Parameter(name = "id", description = "用户编号", example = "1", required = true)
    CommonResult<AdminUserRespDTO> getUser(@RequestParam("id") Long id);

    @GetMapping(PREFIX + "/list")
    //@Operation(summary = "通过用户 ID 查询用户们")
    @Parameter(name = "ids", description = "部门编号数组", example = "1,2", required = true)
    CommonResult<List<AdminUserRespDTO>> getUsers(@RequestParam("ids") Collection<Long> ids);

    @GetMapping(PREFIX + "/list-by-dept-id")
    //@Operation(summary = "获得指定部门的用户数组")
    @Parameter(name = "deptIds", description = "部门编号数组", example = "1,2", required = true)
    CommonResult<List<AdminUserRespDTO>> getUserListByDeptIds(@RequestParam("deptIds") Collection<Long> deptIds);

    @GetMapping(PREFIX + "/list-by-post-id")
    //@Operation(summary = "获得指定岗位的用户数组")
    @Parameter(name = "postIds", description = "岗位编号数组", example = "2,3", required = true)
    CommonResult<List<AdminUserRespDTO>> getUserListByPostIds(@RequestParam("postIds") Collection<Long> postIds);

    /**
     * 获得用户 Map
     *
     * @param ids 用户编号数组
     * @return 用户 Map
     */
    default Map<Long, AdminUserRespDTO> getUserMap(Collection<Long> ids) {
        return CollectionUtils.convertMap(getUsers(ids).getCheckedData(), AdminUserRespDTO::getId);
    }

    default Map<Long, AdminUserRespDTO> getAllUserMap() {
        return CollectionUtils.convertMap(getAllUsers().getCheckedData(), AdminUserRespDTO::getId);
    }

    @GetMapping(PREFIX + "/valid")
    //@Operation(summary = "校验用户们是否有效")
    @Parameter(name = "ids", description = "用户编号数组", example = "3,5", required = true)
    CommonResult<Boolean> validUserList(@RequestParam("ids") Set<Long> ids);

    @GetMapping(PREFIX + "/getByOaUserId")
    //@Operation(summary = "校验用户们是否有效")
    @Parameter(name = "id", description = "用户id", example = "3,5", required = true)
    CommonResult<String> getSysUserAccount(@RequestParam("id") String id);

    @GetMapping(PREFIX + "/getUserAndInformation")
    CommonResult<UserAndInformationDTO> getUserAndInformation(@RequestParam("id") Long id);

    @PutMapping(PREFIX + "/updateUserAndInformation")
    CommonResult<Boolean> updateUserAndInformation(@RequestBody UserAndInformationDTO userAndInformationDTO);

    @GetMapping(PREFIX + "/getListByNickname")
    //@Operation(summary = "模糊查询用户")
    @Parameter(name = "nickname", required = true)
    CommonResult<List<AdminUserRespDTO>> getListByNickname(@RequestParam("nickname") String nickname);

    @PutMapping(PREFIX + "/getContainUserList")
    CommonResult<Set<Long>> getContainUserList(@RequestParam(value = "projectId", required = false) Long projectId,
                                               @RequestParam(value = "deptId", required = false) Long deptId,
                                               @RequestParam(value = "userId", required = false) Long userId);


    @GetMapping(PREFIX + "/getAllUsers")
    //@Operation(summary = "查询所有用户")
    CommonResult<List<AdminUserRespDTO>> getAllUsers();
}
