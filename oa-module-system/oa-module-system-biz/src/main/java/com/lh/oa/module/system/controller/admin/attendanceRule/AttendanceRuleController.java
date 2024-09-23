package com.lh.oa.module.system.controller.admin.attendanceRule;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.*;
import com.lh.oa.module.system.convert.attendanceRule.AttendanceRuleConvert;
import com.lh.oa.module.system.dal.dataobject.attendanceRule.AttendanceRuleDO;
import com.lh.oa.module.system.service.attendanceRule.AttendanceRuleService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 打卡规则（部门）")
@RestController
@RequestMapping("/system/attendance-rule")
@Validated
public class AttendanceRuleController {

    @Resource
    private AttendanceRuleService attendanceRuleService;

    @PostMapping("/create")
    //@Operation(summary = "创建打卡规则（部门）")
    @PreAuthorize("@ss.hasPermission('system:attendance-rule:create')")
    public CommonResult<Long> createAttendanceRule(@Valid @RequestBody DeptRuleListVO createReqVO) {
        return success(attendanceRuleService.createAttendanceRule(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新打卡规则（部门）")
    @PreAuthorize("@ss.hasPermission('system:attendance-rule:update')")
    public CommonResult<Boolean> updateAttendanceRule(@Valid @RequestBody DeptRuleListVO updateReqVO) {
        attendanceRuleService.updateAttendanceRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除打卡规则（部门）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:attendance-rule:delete')")
    public CommonResult<Boolean> deleteAttendanceRule(@RequestParam("id") Long id) {
        attendanceRuleService.deleteAttendanceRule(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得打卡规则（部门）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:attendance-rule:query')")
    public CommonResult<DeptRuleListVO> getAttendanceRule(@RequestParam("id") Long id) {
        return success(attendanceRuleService.getAttendanceRule(id));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得打卡规则（部门）列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:attendance-rule:query')")
    public CommonResult<List<AttendanceRuleRespVO>> getAttendanceRuleList(@RequestParam("ids") Collection<Long> ids) {
        List<AttendanceRuleDO> list = attendanceRuleService.getAttendanceRuleList(ids);
        return success(AttendanceRuleConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得打卡规则（部门）分页")
    @PreAuthorize("@ss.hasPermission('system:attendance-rule:query')")
    public CommonResult<PageResult<DeptRuleListVO>> getAttendanceRulePage(@Valid DeptRulePageBaseVO pageVO) {
        return success(attendanceRuleService.getAttendanceRulePage(pageVO));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出打卡规则（部门） Excel")
    @PreAuthorize("@ss.hasPermission('system:attendance-rule:export')")
    //@Operation(type = EXPORT)
    public void exportAttendanceRuleExcel(@Valid AttendanceRuleExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<AttendanceRuleDO> list = attendanceRuleService.getAttendanceRuleList(exportReqVO);
        // 导出 Excel
        List<AttendanceRuleExcelVO> datas = AttendanceRuleConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "打卡规则（部门）.xls", "数据", AttendanceRuleExcelVO.class, datas);
    }

}
