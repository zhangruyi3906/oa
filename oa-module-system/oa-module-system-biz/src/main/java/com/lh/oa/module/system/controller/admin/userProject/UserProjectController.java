package com.lh.oa.module.system.controller.admin.userProject;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.api.user.dto.UserProjectTo;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserSimpleRespVO;
import com.lh.oa.module.system.controller.admin.userProject.param.UserProjectRuleSameRelationParam;
import com.lh.oa.module.system.controller.admin.userProject.vo.*;
import com.lh.oa.module.system.convert.user.UserConvert;
import com.lh.oa.module.system.convert.userProject.UserProjectConvert;
import com.lh.oa.module.system.dal.dataobject.project.ProjectDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.full.service.attendance.UserProjectRuleSameRelationService;
import com.lh.oa.module.system.service.project.ProjectService;
import com.lh.oa.module.system.service.userProject.UserProjectService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Slf4j
@Tag(name = "管理后台 - 人员项目")
@RestController
@RequestMapping("/system/user-project")
@Validated
public class UserProjectController {

    @Resource
    private UserProjectService userProjectService;

    @Resource
    private AdminUserMapper userMapper;

    @Resource
    private UserProjectRuleSameRelationService userProjectRuleSameRelationService;

    @Resource
    private ProjectService projectService;

    @PostMapping("/create")
    //@Operation(summary = "创建人员项目")
    @PreAuthorize("@ss.hasPermission('system:project:create')")
    public CommonResult<Long> createUserProject(@Valid @RequestBody UserProjectCreateReqVO createReqVO) {
        return success(userProjectService.createUserProject(createReqVO));
    }

    @PostMapping("/sync/batch")
    public CommonResult<Boolean> syncBatchFromPMS(@RequestBody List<UserProjectCreateReqVO> list) {
        return success(userProjectService.syncBatchFromPMS(list));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新人员项目")
    @PreAuthorize("@ss.hasPermission('system:project:update')")
    public CommonResult<Boolean> updateUserProject(@Valid @RequestBody UserProjectUpdateReqVO updateReqVO) {
        userProjectService.updateUserProject(updateReqVO);
        return success(true);
    }

    @PutMapping("/update/from/pms")
    //@Operation(summary = "更新人员项目")
//    @PreAuthorize("@ss.hasPermission('system:project:update')")
    public CommonResult<Boolean> updateUserProjectFromPms(@Valid @RequestBody UserProjectUpdateReqVO baseVO) {
        userProjectService.updateUserProjectFromPMS(baseVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除人员项目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:project:delete')")
    public CommonResult<Boolean> deleteUserProject(@RequestParam("id") Long id) {
        userProjectService.deleteUserProject(id);
        return success(true);
    }

    @DeleteMapping("/delete/from/pms")
    //@Operation(summary = "删除人员项目")
//    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('system:project:delete')")
    public CommonResult<Boolean> deleteUserProjectFromPMS(@RequestParam("userId") Long userId, @RequestParam("projectId") Long projectId) {
        userProjectService.deleteUserProjectFromPMS(userId, projectId);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得人员项目")
    //TODO
//    @PermitAll
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:project:query')")
    public CommonResult<UserProjectRespVO> getUserProject(@RequestParam("id") Long id) {
        UserProjectDO userProject = userProjectService.getUserProject(id);
        return success(UserProjectConvert.INSTANCE.convert(userProject));
    }


    @GetMapping("/list")
    //@Operation(summary = "获得人员项目列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:project:query')")
    public CommonResult<List<UserProjectRespVO>> getUserProjectList(@RequestParam("ids") Collection<Long> ids) {
        List<UserProjectDO> list = userProjectService.getUserProjectListByUserIds(ids);
        return success(UserProjectConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('system:userproject:query')")
    public CommonResult<PageResult<UserProjectDO>> getUserProjectPage(@Valid UserProjectPageReqVO pageVO) {
        PageResult<UserProjectDO> pageResult = userProjectService.getUserProjectPage(pageVO);
        return success(pageResult);
    }

    @GetMapping("/getProjectUser")
    public CommonResult<List<UserProjectUserVO>> getProjectUser(@RequestParam("projectId") Long projectId) {
        List<UserProjectUserVO> Users = userProjectService.getProjectUser(projectId);
        return success(Users);
    }

    @PostMapping("/filter-page")
    //@Operation(summary = "获得人员项目分页")
//    @PreAuthorize("@ss.hasPermission('system:userproject:query')")
    //TODO
    @PermitAll
    public CommonResult<PageResult<UserProjectDO>> getFilterUserProjectPage(@Valid @RequestBody UserProjectPageReqVO pageVO) {
        PageResult<UserProjectDO> pageResult = userProjectService.getUserProjectPage(pageVO);
        return success(pageResult);

    }


    @GetMapping("/export-excel")
    //@Operation(summary = "导出人员项目 Excel")
    @PreAuthorize("@ss.hasPermission('system:project:export')")
    //@Operation(type = EXPORT)
    public void exportUserProjectExcel(@Valid UserProjectExportReqVO exportReqVO,
                                       HttpServletResponse response) throws IOException {
        List<UserProjectDO> list = userProjectService.getUserProjectListByUserIds(exportReqVO);
        // 导出 Excel
        List<UserProjectExcelVO> datas = UserProjectConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "人员项目.xls", "数据", UserProjectExcelVO.class, datas);
    }

    @GetMapping("/list-user")
    //@Operation(summary = "获得人员项目列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
//    @PreAuthorize("@ss.hasPermission('system:project:query')")
//    @PermitAll
    public CommonResult<List<UserProjectRespVO>> getList(@Valid UserProjectExportReqVO ids) {
        List<UserProjectDO> list = userProjectService.getUserProjectListByUserIds(ids);
        List<UserProjectDO> distinctList = new ArrayList<>(list.stream()
                .collect(Collectors.toMap(UserProjectDO::getUserId,
                        userProject -> userProject,
                        (existing, replacement) -> replacement))
                .values());
        return success(UserProjectConvert.INSTANCE.convertList(distinctList));
    }

    @GetMapping("/list-distinct-user")
    //@Operation(summary = "获得人员项目列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:project:query')")
//    @PermitAll
    public CommonResult<List<UserSimpleRespVO>> getDistinctList(@Valid UserProjectExportReqVO ids) {
        List<UserProjectDO> list = userProjectService.getUserProjectListByUserIds(ids);
        List<UserProjectDO> distinctList = new ArrayList<>(list.stream()
                .collect(Collectors.toMap(UserProjectDO::getUserId,
                        userProject -> userProject,
                        (existing, replacement) -> replacement))
                .values());
        List<Long> collect = distinctList.stream().map(UserProjectDO::getUserId).collect(Collectors.toList());
        List<AdminUserDO> adminUserDOS = userMapper.selectList();
        List<AdminUserDO> filteredAdminUserDOS = adminUserDOS.stream()
                .filter(adminUser -> !collect.contains(adminUser.getId()))
                .collect(Collectors.toList());
        return success(UserConvert.INSTANCE.convertList04(filteredAdminUserDOS));
    }

    @GetMapping("/getProject")
    //@Operation(summary = "是否使用项目打卡")
    public CommonResult<Boolean> getProject() {
        return success(userProjectService.getProject(getLoginUserId()));
    }

    @GetMapping("/list-project-user")
    //@Operation(summary = "获得人员项目列表")
    @Parameter(name = "ids")
    @PermitAll
    public CommonResult<List<UserProjectRespVO>> getUsers(@RequestParam("ids") Collection<Long> ids) {
        List<UserProjectDO> list = userProjectService.getUserProjectListByUserIds(ids);
        return success(UserProjectConvert.INSTANCE.convertList(list));
    }


    @GetMapping("/list-user-att")
    //@Operation(summary = "获表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:project:query')")
//    @PermitAll
    public CommonResult<List<UserProjectRespVO>> getAttList(@Valid UserProjectExportReqVO ids) {
        List<UserProjectDO> list = userProjectService.getUserProjectListByUserIds(ids);
        return success(UserProjectConvert.INSTANCE.convertList(list));
    }

    @PostMapping("/resetRuleSameRelation")
    public CommonResult<String> resetRuleSameRelation(@RequestBody UserProjectRuleSameRelationParam param) {
        log.info("开始重置用户打卡考勤相似规则关系数据, param:{}", JsonUtils.toJsonString(param));
        userProjectRuleSameRelationService.resetRuleSameRelation(param);
        return success();
    }

    @GetMapping("/getByUserIds")
    public CommonResult<List<UserProjectTo>> getByUserIds(@RequestParam("userIds") Set<Long> userIds) {
        log.info("通过用户ids查询用户项目关联关系，userIds:{}", userIds);
        List<UserProjectDO> userList = userProjectService.getUserProjectListByUserIds(userIds);
        Map<Long, ProjectDO> projectMap = projectService.getProjectMap(userList.stream().map(UserProjectDO::getProjectId).collect(Collectors.toSet()));
        List<UserProjectTo> result = JsonUtils.covertList(userList, UserProjectTo.class);
        result.forEach(userProject -> {
            ProjectDO projectDO = projectMap.get(userProject.getProjectId());
            if (Objects.nonNull(projectDO)) {
                userProject.setProjectName(projectDO.getName());
            }
        });
        return success(result);
    }

    @PostMapping("/all")
    public CommonResult<List<UserProjectTo>> getAllUserProjectSimpleList(@RequestBody UserProjectPageReqVO userProjectPageReqVO) {
        log.info("查询所有项目人员");
        List<UserProjectDO> userList = userProjectService.getAllUserProjectSimpleList(userProjectPageReqVO);
        List<UserProjectTo> result = JsonUtils.covertList(userList, UserProjectTo.class);
        return success(result);
    }

}
