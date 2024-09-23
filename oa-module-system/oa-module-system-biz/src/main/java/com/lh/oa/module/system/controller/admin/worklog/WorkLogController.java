package com.lh.oa.module.system.controller.admin.worklog;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogCreateReqVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogExcelVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogExportReqVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogPageReqVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogRespVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogUpdateReqVO;
import com.lh.oa.module.system.convert.worklog.WorkLogConvert;
import com.lh.oa.module.system.dal.dataobject.worklog.WorkLogDO;
import com.lh.oa.module.system.service.worklog.WorkLogService;
import com.lh.oa.module.system.util.MapExcelUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 员工工作日志")
@RestController
@RequestMapping("/system/work-log")
@Validated
public class WorkLogController {

    @Resource
    private WorkLogService workLogService;

    @PostMapping("/create")
    //@Operation(summary = "创建员工工作日志")
    //todo
    @PermitAll
//    @PreAuthorize("@ss.hasPermission('system:work-log:create')")
    public CommonResult<Long> createWorkLog(@Valid @RequestBody WorkLogCreateReqVO createReqVO) {
        return success(workLogService.createWorkLog(getLoginUserId(),createReqVO));

    }

    @PostMapping("/repair/create")
    //@Operation(summary = "补交员工工作日志")
    //todo
    @PermitAll
//    @PreAuthorize("@ss.hasPermission('system:work-log:create')")
    public CommonResult<Long> repairCreateWorkLog(@Valid @RequestBody WorkLogCreateReqVO createReqVO) {
        return success(workLogService.repairCreateWorkLog(getLoginUserId(),createReqVO));


    }


    @PutMapping("/update")
    //@Operation(summary = "更新员工工作日志")
    @PermitAll
//    @PreAuthorize("@ss.hasPermission('system:work-log:update')")
    public CommonResult<Boolean> updateWorkLog(@Valid @RequestBody WorkLogUpdateReqVO updateReqVO) {
        workLogService.updateWorkLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除员工工作日志")
//    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('system:work-log:delete')")
    @PermitAll
    public CommonResult<Boolean> deleteWorkLog(@RequestParam("id") Long id) {
        workLogService.deleteWorkLog(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得员工工作日志")
//    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('system:work-log:query')")
    @PermitAll
    public CommonResult<WorkLogRespVO> getWorkLog(@RequestParam("id") Long id) {
        WorkLogDO workLog = workLogService.getWorkLog(id);
        return success(WorkLogConvert.INSTANCE.convert(workLog));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得员工工作日志列表")
//    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
//    @PreAuthorize("@ss.hasPermission('system:work-log:query')")
    @PermitAll
    public CommonResult<List<WorkLogRespVO>> getWorkLogList(@RequestParam("ids") Collection<Long> ids) {
        List<WorkLogDO> list = workLogService.getWorkLogList(ids);
        return success(WorkLogConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得员工工作日志分页")
//    @PreAuthorize("@ss.hasPermission('system:work-log:query')")
    @PermitAll
    public CommonResult<PageResult<WorkLogRespVO>> getWorkLogPage(@Valid WorkLogPageReqVO pageVO) {
        PageResult<WorkLogDO> pageResult = workLogService.getWorkLogPage(pageVO);
        PageResult<WorkLogRespVO> workLogRespVOPageResult = WorkLogConvert.INSTANCE.convertPage(pageResult);
        return success(workLogRespVOPageResult);
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出员工工作日志 Excel")
//    @PreAuthorize("@ss.hasPermission('system:work-log:export')")
    //@Operation(type = EXPORT)
    @PermitAll
    public void exportWorkLogExcel(@Valid WorkLogExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<WorkLogDO> list = workLogService.getWorkLogList(exportReqVO);
        // 导出 Excel
        List<WorkLogExcelVO> datas = WorkLogConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "员工工作日志.xls", "数据", WorkLogExcelVO.class, datas);
    }


    @GetMapping("/export")
    @PermitAll
    public CommonResult<List<Map<String, Object>>> getWorkLogExportList(@RequestParam("month") String month,
                                                                        @RequestParam(required = false) Long deptId,
                                                                        @RequestParam(required = false) Long userId) throws ParseException {
        return success(workLogService.getWorkLogMonthTotalList(month, deptId, userId));
    }


    @GetMapping("/export-total-excel")
    //@Operation(summary = "导出员工工作日志 Excel")
//    @PreAuthorize("@ss.hasPermission('system:work-log:export')")
    //@Operation(type = EXPORT)
    public void exportTotalWorkLogExcel(@RequestParam("month") String month,
                                        @RequestParam(required = false) Long deptId,
                                        @RequestParam(required = false) Long userId,
                                        HttpServletResponse response) throws ParseException {
        List<Map<String, Object>> workLogMonthTotalList = workLogService.getWorkLogMonthTotalList(month, deptId, userId);
        MapExcelUtil.getInstance().createExcel(workLogMonthTotalList, "员工工作微博填写详情.xls", month + "微博统计", response);
    }
}
