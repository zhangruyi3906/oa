package com.lh.oa.module.bpm.wrapper;

import com.google.common.base.Joiner;
import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.system.api.dict.dto.DictDataRespDTO;
import com.lh.oa.module.system.api.project.ProjectApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2024/02/02
 */
@Slf4j
@Component
public class ProjectWrapper {

    @Resource
    private ProjectApi projectApi;

    public Map<String, String> getJntProjectMap(Set<Long> jntProjectIds) {
        log.info("根据ids获取JNT项目数据, postIds:{}", jntProjectIds);
        if (CollectionUtils.isEmpty(jntProjectIds)) {
            return Collections.emptyMap();
        }
        CommonResult<Map<String, String>> deptResult = projectApi.getJNTProjectByIds(Joiner.on(",").join(jntProjectIds));
        log.info("根据ids获取JNT项目数据-返回结果, postIds:{}, result:{}", jntProjectIds, JsonUtils.toJsonString(deptResult));
        ExceptionThrowUtils.throwIfTrue(
                !Objects.equals(HttpStatus.OK.value(), deptResult.getStatus()) || Objects.isNull(deptResult.getData()),
                "系统内部错误，请联系管理员");
        return deptResult.getData();
    }

}
