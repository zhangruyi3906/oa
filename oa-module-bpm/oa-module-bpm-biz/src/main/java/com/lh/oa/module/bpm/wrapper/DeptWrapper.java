package com.lh.oa.module.bpm.wrapper;

import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.system.api.dept.DeptApi;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
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
public class DeptWrapper {

    @Resource
    private DeptApi deptApi;

    public List<DeptRespDTO> getDeptList(Set<Long> deptIds) {
        log.info("根据ids获取部门数据, deptIds:{}", deptIds);
        if (CollectionUtils.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        CommonResult<List<DeptRespDTO>> deptResult = deptApi.getDeptList(deptIds);
        log.info("根据ids获取部门数据-返回结果, deptIds:{}, result:{}", deptIds, JsonUtils.toJsonString(deptResult));
        ExceptionThrowUtils.throwIfTrue(
                !Objects.equals(HttpStatus.OK.value(), deptResult.getStatus()) || Objects.isNull(deptResult.getData()),
                "系统内部错误，请联系管理员");
        return deptResult.getData();
    }

}
