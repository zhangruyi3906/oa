package com.lh.oa.module.system.api.dept;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
import com.lh.oa.module.system.convert.dept.DeptConvert;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.service.dept.DeptService;
import org.apache.dubbo.config.annotation.DubboService;
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
public class DeptApiImpl implements DeptApi {

    @Resource
    private DeptService deptService;

    @Override
    public CommonResult<DeptRespDTO> getDept(Long id) {
        DeptDO dept = deptService.getDept(id);
        return success(DeptConvert.INSTANCE.convert03(dept));
    }

    @Override
    public CommonResult<List<DeptRespDTO>> getDeptList(Collection<Long> ids) {
        List<DeptDO> depts = deptService.getDeptList(ids);
        return success(DeptConvert.INSTANCE.convertList03(depts));
    }

    @Override
    public CommonResult<Boolean> validateDeptList(Collection<Long> ids) {
        deptService.validateDeptList(ids);
        return success(true);
    }

    @Override
    public CommonResult<List<DeptRespDTO>> getAllDepts() {
        List<DeptRespDTO> deptRespDTOS = deptService.getAllDepts();
        return success(deptRespDTOS);
    }

    @Override
    public CommonResult<Long> getOfficeDeptLeaderUserIdByUserId(Long userId) {
        Long officeDeptLeaderUserId = deptService.getOfficeDeptLeaderUserIdByUserId(userId);
        return success(officeDeptLeaderUserId);
    }

    @Override
    public CommonResult<Set<Long>> getOfficeDeptLeaderUserIdByUserIds(Set<Long> userIds) {
        Set<Long> officeDeptLeaderUserIdSet = deptService.getOfficeDeptLeaderUserIdByUserIds(userIds);
        return success(officeDeptLeaderUserIdSet);
    }

}
