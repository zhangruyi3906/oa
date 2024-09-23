package com.lh.oa.module.system.controller.admin.projectworktype;

import com.lh.oa.module.system.controller.admin.projectworktype.vo.*;
import com.lh.oa.module.system.dal.dataobject.projectworktype.ProjectWorkTypeDO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.pojo.CommonResult;
import static com.lh.oa.framework.common.pojo.CommonResult.success;

import com.lh.oa.framework.excel.core.util.ExcelUtils;

import com.lh.oa.module.system.controller.admin.projectworktype.vo.*;
import com.lh.oa.module.system.convert.projectworktype.ProjectWorkTypeConvert;
import com.lh.oa.module.system.service.projectworktype.ProjectWorkTypeService;

@Tag(name = "管理后台 - 项目工种")
@RestController
@RequestMapping("/system/project-work-type")
@Validated
public class ProjectWorkTypeController {

    @Resource
    private ProjectWorkTypeService projectWorkTypeService;

    @PostMapping("/create")
    //@Operation(summary = "创建项目工种")
    @PreAuthorize("@ss.hasPermission('system:project-work-type:create')")
    public CommonResult<Integer> createProjectWorkType(@Valid @RequestBody ProjectWorkTypeCreateReqVO createReqVO) {
        return success(projectWorkTypeService.createProjectWorkType(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新项目工种")
    @PreAuthorize("@ss.hasPermission('system:project-work-type:update')")
    public CommonResult<Boolean> updateProjectWorkType(@Valid @RequestBody ProjectWorkTypeUpdateReqVO updateReqVO) {
        projectWorkTypeService.updateProjectWorkType(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除项目工种")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:project-work-type:delete')")
    public CommonResult<Boolean> deleteProjectWorkType(@RequestParam("id") Integer id) {
        projectWorkTypeService.deleteProjectWorkType(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得项目工种")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:project-work-type:query')")
    public CommonResult<ProjectWorkTypeRespVO> getProjectWorkType(@RequestParam("id") Integer id) {
        ProjectWorkTypeDO projectWorkType = projectWorkTypeService.getProjectWorkType(id);
        return success(ProjectWorkTypeConvert.INSTANCE.convert(projectWorkType));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得项目工种列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:project-work-type:query')")
    public CommonResult<List<ProjectWorkTypeRespVO>> getProjectWorkTypeList(@RequestParam("ids") Collection<Integer> ids) {
        List<ProjectWorkTypeDO> list = projectWorkTypeService.getProjectWorkTypeList(ids);
        return success(ProjectWorkTypeConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得项目工种分页")
    @PreAuthorize("@ss.hasPermission('system:project-work-type:query')")
    public CommonResult<PageResult<ProjectWorkTypeRespVO>> getProjectWorkTypePage(@Valid ProjectWorkTypePageReqVO pageVO) {
        PageResult<ProjectWorkTypeDO> pageResult = projectWorkTypeService.getProjectWorkTypePage(pageVO);
        return success(ProjectWorkTypeConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出项目工种 Excel")
    @PreAuthorize("@ss.hasPermission('system:project-work-type:export')")
    //@Operation(type = EXPORT)
    public void exportProjectWorkTypeExcel(@Valid ProjectWorkTypeExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ProjectWorkTypeDO> list = projectWorkTypeService.getProjectWorkTypeList(exportReqVO);
        // 导出 Excel
        List<ProjectWorkTypeExcelVO> datas = ProjectWorkTypeConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "项目工种.xls", "数据", ProjectWorkTypeExcelVO.class, datas);
    }

}
