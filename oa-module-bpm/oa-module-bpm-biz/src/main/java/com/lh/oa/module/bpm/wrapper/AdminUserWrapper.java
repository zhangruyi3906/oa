package com.lh.oa.module.bpm.wrapper;

import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2023/12/15
 */
@Slf4j
@Component
public class AdminUserWrapper {

    @Resource
    private AdminUserApi adminUserApi;

    /**
     * 根据条件获取用户交集数据
     *
     * @param userId    用户id
     * @param deptId    部门id
     * @param projectId 项目id
     * @return 用户交集列表
     */
    public Set<Long> getContainUserList(Long userId, Long deptId, Long projectId) {
        log.info("根据条件获取用户交集数据, userId:{}, deptId:{}, projectId:{}", userId, deptId, projectId);
        if (Objects.isNull(userId) && Objects.isNull(deptId) && Objects.isNull(projectId)) {
            return Collections.emptySet();
        }
        CommonResult<Set<Long>> userResult = adminUserApi.getContainUserList(projectId, deptId, userId);
        log.info("根据条件获取用户交集数据-返回结果, userId:{}, deptId:{}, projectId:{}, result:{}", userId, deptId, projectId, JsonUtils.toJsonString(userResult));
        ExceptionThrowUtils.throwIfTrue(
                !Objects.equals(HttpStatus.OK.value(), userResult.getStatus()) || Objects.isNull(userResult.getData()),
                "系统内部错误，请联系管理员");
        return userResult.getData();
    }

    /**
     * 根据ids获取用户数据
     *
     * @param userIds 用户ids
     * @return 用户列表
     */
    public List<AdminUserRespDTO> getByIds(Set<Long> userIds) {
        log.info("根据ids获取用户数据, userIds:{}", userIds);
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        CommonResult<List<AdminUserRespDTO>> userResult = adminUserApi.getUsers(userIds);
        log.info("根据ids获取用户数据-返回结果, userIds:{}, result:{}", userIds, JsonUtils.toJsonString(userResult));
        ExceptionThrowUtils.throwIfTrue(
                !Objects.equals(HttpStatus.OK.value(), userResult.getStatus()) || Objects.isNull(userResult.getData()),
                "系统内部错误，请联系管理员");
        return userResult.getData();
    }

}
