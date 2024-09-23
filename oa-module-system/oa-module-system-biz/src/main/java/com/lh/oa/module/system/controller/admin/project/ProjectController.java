package com.lh.oa.module.system.controller.admin.project;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.api.dict.dto.DictDataRespDTO;
import com.lh.oa.module.system.controller.admin.project.vo.*;
import com.lh.oa.module.system.convert.project.ProjectConvert;
import com.lh.oa.module.system.dal.dataobject.project.ProjectDO;
import com.lh.oa.module.system.full.service.jnt.JntBaseDataSyncService;
import com.lh.oa.module.system.service.project.ProjectService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Slf4j
@Tag(name = "管理后台 - 项目")
@RestController
@RequestMapping("/system/project")
@Validated
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @Resource
    private JntBaseDataSyncService jntBaseDataSyncService;

    @GetMapping("/getProjectsByUsrId")
    public Map<Long, String> getProjectsByUsrId(Long userId) {
        return projectService.getProjectsByUsrId(userId);
    }


    @PostMapping("/create")
    //@Operation(summary = "创建项目")
    @PreAuthorize("@ss.hasPermission('system:project:create')")
    public CommonResult<Integer> createProject(@Valid @RequestBody ProjectCreateReqVO createReqVO) {
        return success(projectService.createProject(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新项目")
    @PreAuthorize("@ss.hasPermission('system:project:update')")
    public CommonResult<Boolean> updateProject(@Valid @RequestBody ProjectUpdateReqVO updateReqVO) {
        projectService.updateProject(updateReqVO);
        return success(true);
    }


    @GetMapping("/disabled")
    //@Operation(summary = "禁用/解禁项目")

    //Todo
//    @PermitAll
    @PreAuthorize("@ss.hasPermission('system:project:update')")
    public CommonResult<Boolean> disabledProject(@Valid @RequestParam("id") Integer id) {
        projectService.disabledProject(id);
        return success(true);
    }


    @PutMapping("/isTop")
    //@Operation(summary = "置顶项目")
    //Todo
//    @PermitAll
    public CommonResult<Boolean> isTopProject(@Valid @RequestParam("id") Integer id) {
        projectService.isTopProject(id);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除项目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:project:delete')")
    public CommonResult<Boolean> deleteProject(@RequestParam("id") Integer id,@RequestParam(value = "notSyncJnt",required = false,defaultValue = "false")Boolean notSyncJnt) {
        projectService.deleteProject(id,notSyncJnt);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得项目")
    //Todo
//    @PermitAll
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:project:query')")
    public CommonResult<ProjectRespVO> getProject(@RequestParam("id") Integer id) {
        ProjectDO project = projectService.getProject(id);
        return success(ProjectConvert.INSTANCE.convert(project));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得项目列表")
    //Todo
//    @PermitAll
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:project:query')")
    public CommonResult<List<ProjectRespVO>> getProjectList(@RequestParam("ids") Collection<Integer> ids) {
        List<ProjectDO> list = projectService.getProjectList(ids);
        return success(ProjectConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得项目分页")
    //Todo
    @PreAuthorize("@ss.hasPermission('system:project:query')")
//    @PermitAll
    public CommonResult<PageResult<ProjectRespVO>> getProjectPage(@Valid ProjectPageReqVO pageVO) {
        PageResult<ProjectDO> pageResult = projectService.getProjectPage(pageVO);
        return success(ProjectConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/all")
//    @PermitAll
    public CommonResult<List<ProjectDO>> getProjectAll() {
        return success(projectService.getProjectAll());
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出项目 Excel")
    @PreAuthorize("@ss.hasPermission('system:project:export')")
    //@Operation(type = EXPORT)
    public void exportProjectExcel(@Valid ProjectExportReqVO exportReqVO,
                                   HttpServletResponse response) throws IOException {
        List<ProjectDO> list = projectService.getProjectList(exportReqVO);
        List<ProjectExcelVO> datas = ProjectConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "项目.xls", "数据", ProjectExcelVO.class, datas);
    }

    /**
     * 注意：该接口只会返回当前用户关联的项目，而不是返回全部的项目
     */
    @GetMapping("/list-all-simple")
    //@Operation(summary = "获取项目精简信息列表")
    public CommonResult<List<ProjectSimpleRespVO>> getSimpleRoleList() {
        List<ProjectDO> list = projectService.getProjectList();
        return success(ProjectConvert.INSTANCE.convertList03(list));
    }

    @GetMapping("/getProjectByOrgId")
//@Operation(summary = "获得客户的相关项目")
    public CommonResult<List<ProjectRespVO>> getProjectByOrgId(@RequestParam("orgId") Long orgId) {
        List<ProjectDO> projectByOrgId = projectService.getProjectByOrgId(orgId);
        return success(ProjectConvert.INSTANCE.convertList(projectByOrgId));
    }

    /**
     * 获取全部项目的精简信息
     */
    @GetMapping("/getAllSimpleProjectList")
    public CommonResult<List<ProjectSimpleRespVO>> getAllSimpleProjectList() {
        List<ProjectDO> list = projectService.getProjectAll();
        return success(JsonUtils.covertList(list, ProjectSimpleRespVO.class));
    }

    /**
     * 获取JNT项目的精简信息
     */
    @GetMapping("/getWarehouseProject")
    @PermitAll
    public CommonResult<List<JntWarehouseProjectVO>> getWarehouseProject() {
        List<JntWarehouseProjectVO> list = projectService.getWarehouseProject();
        return success(list);
    }

    /**
     * 获取JNT项目的精简信息
     */
    @GetMapping("/getJNTProjectByIds")
    @PermitAll
    public CommonResult<Map<String, String>> getJNTProjectByIds(@RequestParam("projectIds") String projectIds) {
        log.info("获取JNT项目的映射关系, projectIds:{}", projectIds);
        Map<String, String> map = jntBaseDataSyncService.getJNTProjectByIds(projectIds);
        return success(map);
    }

    @GetMapping("/getWarehouseMaterialByProject")
    @PermitAll
    public CommonResult<List<JntMaterialVO>> getWarehouseMaterialByProject(@RequestParam("projectId") Long projectId) {
        List<JntMaterialVO> list = projectService.getWarehouseMaterialByProject(projectId);
        return success(list);
    }

    @GetMapping("/getWarehouseByProjectId")
    @PermitAll
    public CommonResult<List<JntWarehouseVO>> getWarehouseByProjectId(@RequestParam(value = "projectId", required = false) Long projectId) {
        List<JntWarehouseVO> list = projectService.getWarehouseByProjectId(projectId);
        return success(list);
    }

}
