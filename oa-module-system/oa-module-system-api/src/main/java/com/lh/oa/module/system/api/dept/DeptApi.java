package com.lh.oa.module.system.api.dept;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
import com.lh.oa.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = ApiConstants.NAME) //
@Tag(name = "RPC 服务 - 部门")
public interface DeptApi {

    String PREFIX = ApiConstants.PREFIX + "/dept";

    @GetMapping(PREFIX + "/get")
    //@Operation(summary = "获得部门信息")
    @Parameter(name = "id", description = "部门编号", example = "1024", required = true)
    CommonResult<DeptRespDTO> getDept(@RequestParam("id") Long id);

    @GetMapping(PREFIX + "/list")
    //@Operation(summary = "获得部门信息数组")
    @Parameter(name = "ids", description = "部门编号数组", example = "1,2", required = true)
    CommonResult<List<DeptRespDTO>> getDeptList(@RequestParam("ids") Collection<Long> ids);

    @GetMapping(PREFIX + "/valid")
    //@Operation(summary = "校验部门是否合法")
    @Parameter(name = "ids", description = "部门编号数组", example = "1,2", required = true)
    CommonResult<Boolean> validateDeptList(@RequestParam("ids") Collection<Long> ids);

    /**
     * 获得指定编号的部门 Map
     *
     * @param ids 部门编号数组
     * @return 部门 Map
     */
    default Map<Long, DeptRespDTO> getDeptMap(Set<Long> ids) {
        return CollectionUtils.convertMap(getDeptList(ids).getCheckedData(), DeptRespDTO::getId);
    }

    /**
     * 获得指定id的部门
     *
     * @param id 部门id
     * @return 部门
     */
    default DeptRespDTO getDeptById(Long id) {
        return getDept(id).getCheckedData();
    }

    @GetMapping(PREFIX + "/getAllDepts")
    //@Operation(summary = "获得所有部门")
    CommonResult<List<DeptRespDTO>> getAllDepts();

    @GetMapping(PREFIX + "/getOfficeDeptLeaderUserId")
    //@Operation(summary = "获得所有总经办部门")
    CommonResult<Long> getOfficeDeptLeaderUserIdByUserId(@RequestParam("userId") Long userId);

    @GetMapping(PREFIX + "/getOfficeDeptLeaderUserIds")
    //@Operation(summary = "获得所有总经办部门")
    CommonResult<Set<Long>> getOfficeDeptLeaderUserIdByUserIds(@RequestParam("userIds") Set<Long> userIds);
}
