package com.lh.oa.module.system.controller.admin.projectrecord;


import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.*;
import com.lh.oa.module.system.dal.dataobject.projectrecord.ProjectRecordDO;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRecordService;
import com.lh.oa.module.system.service.projectrecord.ProjectRecordService;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordCreateReqVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordPageReqVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordUpdateReqVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Slf4j
@Tag(name = "管理后台 - 打卡记录")
@RestController
@RequestMapping("/system/project-record")
@Validated
public class ProjectRecordController {

    @Resource
    private ProjectRecordService service;

    @Resource
    private SysAttendanceRecordService sysAttendanceRecordService;

    /**
     * 以下接口已不在OA内使用，但是建能通仍有调用，所以这里先改掉实现方式
     */
    @PostMapping("/create")
//    @PreAuthorize("@ss.hasPermission('system:project-record:create')")
    public CommonResult<Boolean> createRecordProject(@Valid @RequestBody ProjectRecordCreateReqVO createReqVO) {
        Boolean result = sysAttendanceRecordService.savePmsAttendanceRecord(createReqVO);
        return success(result);
    }

    @Deprecated
    @PutMapping("/update")
//    @PreAuthorize("@ss.hasPermission('system:record:update')")
    public CommonResult<Boolean> updateRecordProject(@Valid @RequestBody ProjectRecordUpdateReqVO updateReqVO) {
        service.updateRecordProject(updateReqVO);
        return success(true);
    }

    @GetMapping("/page")
//    @PreAuthorize("@ss.hasPermission('system:record:query')")
    public CommonResult<PageResult<ProjectRecordDO>> getRecordProjectPage(@Valid ProjectRecordPageReqVO pageVO) {
        PageResult<ProjectRecordDO> result = sysAttendanceRecordService.queryPmsAttendanceRecordPage(pageVO);
        return success(result);
    }

}
