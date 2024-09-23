package com.lh.oa.module.system.controller.admin.projectAttendanceRule;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.*;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.*;
import com.lh.oa.module.system.convert.projectAttendanceRule.ProjectAttendanceRuleConvert;
import com.lh.oa.module.system.dal.dataobject.projectAttendanceRule.ProjectAttendanceRuleDO;
import com.lh.oa.module.system.service.projectAttendanceRule.ProjectAttendanceRuleService;
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

@Tag(name = "管理后台 - 打卡规则（项目）")
@RestController
@RequestMapping("/system/project-attendance-rule")
@Validated
public class ProjectAttendanceRuleController {

    @Resource
    private ProjectAttendanceRuleService projectAttendanceRuleService;

    @PostMapping("/create")
    //@Operation(summary = "创建打卡规则（项目）")
    @PreAuthorize("@ss.hasPermission('system:project-attendance-rule:create')")
    public CommonResult<Long> createProjectAttendanceRule(@Valid @RequestBody ProjectAttendanceRuleListBaseVO createReqVO) {
        return success(projectAttendanceRuleService.createProjectAttendanceRule(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新打卡规则（项目）")
    @PreAuthorize("@ss.hasPermission('system:project-attendance-rule:update')")
    public CommonResult<Boolean> updateProjectAttendanceRule(@Valid @RequestBody ProjectAttendanceRuleListBaseVO updateReqVO) {
        projectAttendanceRuleService.updateProjectAttendanceRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除打卡规则（项目）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:project-attendance-rule:delete')")
    public CommonResult<Boolean> deleteProjectAttendanceRule(@RequestParam("id") Long id) {
        projectAttendanceRuleService.deleteProjectAttendanceRule(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得打卡规则（项目）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:project-attendance-rule:query')")
    public CommonResult<ProjectAttendanceRuleListBaseVO> getProjectAttendanceRule(@RequestParam("id") Long id) {
        ProjectAttendanceRuleListBaseVO projectAttendanceRule = projectAttendanceRuleService.getProjectAttendanceRule(id);
        return success(projectAttendanceRule);
    }

    @GetMapping("/list")
    //@Operation(summary = "获得打卡规则（项目）列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:project-attendance-rule:query')")
    public CommonResult<List<ProjectAttendanceRuleRespVO>> getProjectAttendanceRuleList(@RequestParam("ids") Collection<Long> ids) {
        List<ProjectAttendanceRuleDO> list = projectAttendanceRuleService.getProjectAttendanceRuleList(ids);
        return success(ProjectAttendanceRuleConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得打卡规则（项目）分页")
    @PreAuthorize("@ss.hasPermission('system:project-attendance-rule:query')")
    public CommonResult<PageResult<ProjectAttendanceRuleListBaseVO>> getProjectAttendanceRulePage(@Valid ProjectAttendanceRuleListPageBaseVO pageVO) {
        PageResult<ProjectAttendanceRuleListBaseVO> pageResult = projectAttendanceRuleService.getProjectAttendanceRulePage(pageVO);
        return success(pageResult);
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出打卡规则（项目） Excel")
    @PreAuthorize("@ss.hasPermission('system:project-attendance-rule:export')")
    //@Operation(type = EXPORT)
    public void exportProjectAttendanceRuleExcel(@Valid ProjectAttendanceRuleExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ProjectAttendanceRuleDO> list = projectAttendanceRuleService.getProjectAttendanceRuleList(exportReqVO);
        // 导出 Excel
        List<ProjectAttendanceRuleExcelVO> datas = ProjectAttendanceRuleConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "打卡规则（项目）.xls", "数据", ProjectAttendanceRuleExcelVO.class, datas);
    }

}
