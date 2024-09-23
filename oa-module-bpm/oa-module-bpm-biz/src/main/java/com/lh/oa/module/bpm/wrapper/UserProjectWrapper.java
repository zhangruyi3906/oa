package com.lh.oa.module.bpm.wrapper;

import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.system.api.user.UserProjectApi;
import com.lh.oa.module.system.api.user.dto.UserProjectTo;
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
public class UserProjectWrapper {

    @Resource
    private UserProjectApi userProjectApi;

    /**
     * 请求根据用户ids获取用户项目关联数据
     *
     * @param userIds 用户ids
     * @return 用户项目关联数据
     */
    public List<UserProjectTo> getUserProjectList(Set<Long> userIds) {
        log.info("根据用户ids获取用户项目关联数据, userIds:{}", userIds);
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        CommonResult<List<UserProjectTo>> userProjectResult = userProjectApi.getByUserIds(userIds);
        log.info("根据用户ids获取用户项目关联数据-返回结果, userIds:{}, result:{}", userIds, JsonUtils.toJsonString(userProjectResult));
        ExceptionThrowUtils.throwIfTrue(
                !Objects.equals(HttpStatus.OK.value(), userProjectResult.getStatus()) || Objects.isNull(userProjectResult.getData()),
                "系统内部错误，请联系管理员");
        return userProjectResult.getData();
    }

}
