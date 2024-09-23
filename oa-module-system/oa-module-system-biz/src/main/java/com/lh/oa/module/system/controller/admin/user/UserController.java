package com.lh.oa.module.system.controller.admin.user;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UpdateUserAndInformation;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserAndInformation;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserCreateReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserExcelVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserExportReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserImportExcelVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserPageItemRespVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserPageReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserPressVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserSimpleRespVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserUpdatePasswordReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserUpdateReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserUpdateStatusReqVO;
import com.lh.oa.module.system.convert.user.UserConvert;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.dept.PostDO;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.mysql.dept.DeptMapper;
import com.lh.oa.module.system.dal.mysql.dept.PostMapper;
import com.lh.oa.module.system.dal.mysql.information.InformationMapper;
import com.lh.oa.module.system.service.dept.DeptService;
import com.lh.oa.module.system.service.permission.PermissionService;
import com.lh.oa.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertList;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Slf4j
@Tag(name = "管理后台 - 用户")
@RestController
@RequestMapping("/system/user")
@Validated
public class UserController {

    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;

    @Resource
    private PermissionService permissionService;



    @PostMapping("/create")
    //@Operation(summary = "新增用户")
    //TODO
    @PermitAll
    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Long> createUser(@Valid @RequestBody UserCreateReqVO reqVO) {
        Long id = userService.createUser(reqVO);
        return success(id);
    }

    @PostMapping("/create-all")
    //@Operation(summary = "新增用户和详情")
    public CommonResult<Long> createUserAndInformation(@Valid @RequestBody UserAndInformation userAndInformation) {
        Long id = userService.createUserAndInformation(userAndInformation);
        return success(id);
    }

    @GetMapping("/sync")
    @PreAuthorize("@ss.hasPermission('system:user:sync')")
    public CommonResult<Boolean> handSyncUser(@RequestParam("id") Long id) {
        userService.handSyncUser(id);
        return success(true);
    }

    @PutMapping("update-all")
    //@Operation(summary = "修改用户及详情")
    public CommonResult<Boolean> updateUserAndInformation(@Valid @RequestBody UpdateUserAndInformation updateUserAndInformation) {
        userService.updateUserAndInformation(updateUserAndInformation);
        return success(true);
    }

    @PutMapping("update")
    //@Operation(summary = "修改用户")
    //TODO
//    @PermitAll
    @PreAuthorize("@ss.hasPermission('system:user:update')")
    public CommonResult<Boolean> updateUser(@Valid @RequestBody UserUpdateReqVO reqVO) {
        userService.updateUser(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:user:delete')")
    public CommonResult<Boolean> deleteUser(@RequestParam("id") Long id,@RequestParam(value = "notSyncJnt",required = false,defaultValue = "false")Boolean notSyncJnt) {
        userService.deleteUser(id,notSyncJnt);
        return success(true);
    }

    @PutMapping("/update-password")
    //@Operation(summary = "重置用户密码")
    @PreAuthorize("@ss.hasPermission('system:user:update-password')")
    public CommonResult<Boolean> updateUserPassword(@Valid @RequestBody UserUpdatePasswordReqVO reqVO) {
        userService.updateUserPassword(reqVO.getId(), reqVO.getPassword());
        return success(true);
    }

    @PutMapping("/update-status")
    //@Operation(summary = "修改用户状态")
    @PreAuthorize("@ss.hasPermission('system:user:update')")
    public CommonResult<Boolean> updateUserStatus(@Valid @RequestBody UserUpdateStatusReqVO reqVO) {
        userService.updateUserStatus(reqVO.getId(), reqVO.getStatus(),reqVO.getSource());
        return success(true);
    }

    @GetMapping("/page")
    //@Operation(summary = "获得用户分页列表")
    @PreAuthorize("@ss.hasPermission('system:user:list')")
//    @PermitAll
    public CommonResult<PageResult<UserPageItemRespVO>> getUserPage(@Valid UserPageReqVO reqVO) {
        // 获得用户分页列表
        PageResult<AdminUserDO> pageResult = userService.getUserPage(reqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal())); // 返回空
        }

        // 获得拼接需要的数据
        Collection<Long> deptIds = convertList(pageResult.getList(), AdminUserDO::getDeptId);
        Map<Long, DeptDO> deptMap = deptService.getDeptMap(deptIds);
        // 拼接结果返回
        List<UserPageItemRespVO> userList = new ArrayList<>(pageResult.getList().size());
        pageResult.getList().forEach(user -> {
            UserPageItemRespVO respVO = UserConvert.INSTANCE.convert(user);
            respVO.setDept(UserConvert.INSTANCE.convert(deptMap.get(user.getDeptId())));
            userList.add(respVO);
        });
        return success(new PageResult<>(userList, pageResult.getTotal()));
    }

    @GetMapping("/list-all-simple")
    @PermitAll
    //@Operation(summary = "获取用户精简信息列表", description = "只包含被开启的用户，主要用于前端的下拉选项")
    public CommonResult<List<UserSimpleRespVO>> getSimpleUsers(UserSimpleRespVO userSimpleRespVO) {
        // 获用户门列表，只要开启状态的
        List<AdminUserDO> list = userService.getUserListByUserSimpleRespVO(userSimpleRespVO);
        List<UserSimpleRespVO> userSimpleRespVOS = UserConvert.INSTANCE.convertList04(list);
        Set<Long> deptIds = list.stream().map(AdminUserDO::getDeptId).collect(Collectors.toSet());
        Map<Long, DeptDO> deptMap = deptService.getDeptMap(deptIds);
        userSimpleRespVOS.forEach(s -> {
            DeptDO deptDO = deptMap.get(s.getDeptId());
            if (Objects.nonNull(deptDO)) {
                s.setDeptName(deptDO.getName());
            }
            s.setLabel(s.getNickname());
            s.setValue(s.getId());
        });
        // 排序后，返回给前端
        return success(userSimpleRespVOS);
    }

//    @GetMapping("/get")
//    //@Operation(summary = "获得用户详情")
//    //TODO
//    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('system:user:query')")
////    @PermitAll
//    public CommonResult<UserRespVO> getInfo(@RequestParam("id") Long id) {
//        AdminUserDO user = userService.getUser(id);
//        Set<Long> userRoleIdList = permissionService.getUserRoleIdListByUserId(id);
//        // 获得部门数据
//        DeptDO dept = deptService.getDept(user.getDeptId());
//        return success(UserConvert.INSTANCE.convert(user).setDept(UserConvert.INSTANCE.convert(dept)).setUserRoleIdList(userRoleIdList));
//    }


    @GetMapping("/get")
    //@Operation(summary = "获得用户详情")
    //TODO
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:user:query')")
//    @PermitAll
    public CommonResult<UserAndInformation> getUserAndInformation(@RequestParam("id") Long id) {
        AdminUserDO user = userService.getUser(id);
        Set<Long> userRoleIdList = permissionService.getUserRoleIdListByUserId(id);
        // 获得部门数据
        DeptDO dept = deptService.getDept(user.getDeptId());
        UserAndInformation userAndInformation = userService.getUserAndInformation(id);
        userAndInformation.setUserRoleIdList(userRoleIdList);
        return success(userAndInformation);
    }


    @GetMapping("/export")
    //@Operation(summary = "导出用户")
    @PreAuthorize("@ss.hasPermission('system:user:export')")
    //@Operation(type = EXPORT)
    public void exportUsers(@Validated UserExportReqVO reqVO,
                            HttpServletResponse response) throws IOException {
        List<UserExcelVO> excelUsers = userService.exportUserAndInformation(reqVO);
        // 输出
        ExcelUtils.write(response, "用户数据.xls", "用户列表", UserExcelVO.class, excelUsers);
    }

    @GetMapping("/get-import-template")
    //@Operation(summary = "获得导入用户模板")
    public void importTemplate(HttpServletResponse response) throws IOException {
        // 示例数据
        List<UserImportExcelVO> list = userService.getImportExample();
        Map<Integer, List<String>> importTemplateDropList = userService.getImportTemplateDropListMap();
        Map<Integer, Map<String, List<String>>> parentChildMap = userService.getImportTemplateParentChildMap();
        ExcelUtils.write(response, "用户导入模板.xls", "用户列表", UserImportExcelVO.class, list, importTemplateDropList, parentChildMap);
    }

    @PostMapping("/import")
    @Parameters({
            @Parameter(name = "file", description = "Excel 文件", required = true),
            @Parameter(name = "updateSupport", description = "是否支持更新，默认为 false", example = "true")
    })
    @PreAuthorize("@ss.hasPermission('system:user:import')")
    @SneakyThrows
    public CommonResult<String> importExcel(@RequestParam("file") MultipartFile file,
                                            @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport,
                                            HttpServletResponse response) {
        List<UserImportExcelVO> list = ExcelUtils.read(file, UserImportExcelVO.class);
        List<UserImportExcelVO> result = userService.importUserList(list, updateSupport);
        if (!result.isEmpty()) {
            ExcelUtils.write(response, "错误数据.xls", "用户列表", UserImportExcelVO.class, result);
            return CommonResult.halfSuccess("部分数据存在问题");
        } else {
            return success();
        }
    }

    /**
     * 直接以列表形式导入用户数据，后台操作使用
     */
    @PostMapping("/importList")
    public CommonResult<String> importExcelV2(@RequestBody List<UserImportExcelVO> list, HttpServletResponse response) throws IOException {
        List<UserImportExcelVO> result = userService.importUserList(list, false);
        if (!result.isEmpty()) {
            ExcelUtils.write(response, "错误数据.xls", "用户列表", UserImportExcelVO.class, result);
            return CommonResult.halfSuccess("部分数据存在问题");
        } else {
            return success();
        }
    }


    @GetMapping("/list-user-simple")
//    @Operation(summary = "获取用户精简信息列表", description = "只包含被开启的用户，主要用于前端的下拉选项")
    @PermitAll
    public CommonResult<List<UserPressVO>> getPressUsers() {
        return success(userService.getPressUsers());
    }


    @GetMapping("/list-user")
    //@Operation(summary = "获取用户信息列表", description = "只包含被开启的用户")
    @PermitAll
    public CommonResult<List<AdminUserDO>> getListUsers() {
        // 获用户门列表，只要开启状态的
//        List<AdminUserDO> list = userService.getUserListByStatus(CommonStatusEnum.ENABLE.getStatus());
        // 排序后，返回给前端
        return success(userService.getUserListByStatus(CommonStatusEnum.ENABLE.getStatus()));
    }

    /**
     * 模糊查询用户的简易信息，如果没有传名字，则查询当前登录用户的信息
     */
    @GetMapping("/getUserSimpleInfo")
    public CommonResult<List<UserSimpleRespVO>> getUserSimpleInfo(@RequestParam(value = "nickName", required = false) String nickName) {
        List<UserSimpleRespVO> result = userService.getUserSimpleInfo(getLoginUserId(), nickName);
        return success(result);
    }

    @GetMapping("/getContainUserList")
    public CommonResult<Set<Long>> getContainUserList(@RequestParam(value = "projectId", required = false) Long projectId,
                                                      @RequestParam(value = "deptId", required = false) Long deptId,
                                                      @RequestParam(value = "userId", required = false) Long userId) {
        log.info("通过参数获取用户ids的交集，projectId:{}, deptId:{}, userId:{}", projectId, deptId, userId);
        Set<Long> userIds = userService.getContainUserIds(projectId, deptId, userId);
        return success(userIds);
    }

}
