package com.lh.oa.module.system.controller.admin.dept;

import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.StringUtils;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptPressVO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptRespVO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptSimpleRespVO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptUpdateReqVO;
import com.lh.oa.module.system.convert.dept.DeptConvert;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.service.dept.DeptService;
import com.lh.oa.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
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
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 部门")
@RestController
@RequestMapping("/system/dept")
@Validated
public class DeptController {

    @Resource
    private DeptService deptService;

    @Resource
    private AdminUserService adminUserService;

    @PostMapping("create")
    ////@Operation(summary = "创建部门")
    @PreAuthorize("@ss.hasPermission('system:dept:create')")
    public CommonResult<Long> createDept(@Valid @RequestBody DeptCreateReqVO reqVO) {
        Long deptId = deptService.createDept(reqVO);
        return success(deptId);
    }

    @GetMapping("sync")
    @PreAuthorize("@ss.hasPermission('system:dept:sync')")
    public CommonResult<Boolean> createDept(@RequestParam("id") Long id) {
        deptService.handSyncDept(id);
        return success(true);
    }

    @PutMapping("update")
    ////@Operation(summary = "更新部门")
    @PreAuthorize("@ss.hasPermission('system:dept:update')")
    public CommonResult<Boolean> updateDept(@Valid @RequestBody DeptUpdateReqVO reqVO) {
        deptService.updateDept(reqVO);
        return success(true);
    }

    @DeleteMapping("delete")
    ////@Operation(summary = "删除部门")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:dept:delete')")
    public CommonResult<Boolean> deleteDept(@RequestParam("id") Long id,@RequestParam(value = "notSyncJnt",required = false,defaultValue = "false")Boolean notSyncJnt) {
        deptService.deleteDept(id,notSyncJnt);
        return success(true);
    }

    @GetMapping("/list")
    ////@Operation(summary = "获取部门列表")
//    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    @PermitAll
    @SneakyThrows
    public CommonResult<List<DeptRespVO>> listDepts(DeptListReqVO reqVO) {
        List<DeptDO> list = deptService.getDeptList(reqVO);
        if (list.isEmpty()) {
            return CommonResult.success(Collections.emptyList());
        }
        list.sort(Comparator.comparing(DeptDO::getSort));
        List<DeptRespVO> deptRespVOS = DeptConvert.INSTANCE.convertList(list);
        if(list.size()==1){
            return success(deptRespVOS);
        }
        Map<Long, AdminUserDO> leaderUserMap = adminUserService.getUserMap(list.stream().map(DeptDO::getLeaderUserId).collect(Collectors.toSet()));
        deptRespVOS.forEach(dept -> {
            AdminUserDO leaderUser = leaderUserMap.get(dept.getLeaderUserId());
            if (Objects.nonNull(leaderUser)) {
                dept.setLeaderUserName(leaderUser.getNickname());
            }
        });
        return success(buildMenuTree(deptRespVOS));
    }

    @GetMapping("/list-all-simple")
    ////@Operation(summary = "获取部门精简信息列表")
    @PermitAll
    public CommonResult<List<DeptSimpleRespVO>> getSimpleDepts(@RequestParam(value = "name", required = false) String name) {
        DeptListReqVO reqVO = new DeptListReqVO();
        reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        if (StringUtils.isNotEmpty(name)) {
            reqVO.setName(name);
        }
        List<DeptDO> list = deptService.getDeptList(reqVO);
        list.sort(Comparator.comparing(DeptDO::getSort));
        return success(DeptConvert.INSTANCE.convertList02(list));
    }

    @GetMapping("/get")
    ////@Operation(summary = "获得部门信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    public CommonResult<DeptRespVO> getDept(@RequestParam("id") Long id) {
        return success(DeptConvert.INSTANCE.convert(deptService.getDept(id)));
    }




    public static List<DeptRespVO> buildMenuTree(List<DeptRespVO> menuList)  {
        Map<Long, DeptRespVO> menuMap = new HashMap<>();
        for (DeptRespVO menu : menuList) {
            DeptRespVO menuRespVO = new DeptRespVO();
            BeanUtils.copyProperties(menu, menuRespVO);
            menuMap.put(menu.getId(), menuRespVO);
        }

        List<DeptRespVO> menuTree = new ArrayList<>();
        for (DeptRespVO menu : menuList) {
            if (menu.getParentId() == 0) {
                menuTree.add(menuMap.get(menu.getId()));
            } else {
                DeptRespVO parentMenu = menuMap.get(menu.getParentId());
                if (parentMenu != null) {
                    parentMenu.getChildren().add(menuMap.get(menu.getId()));
                }
            }
        }

        return menuTree;
    }

    @GetMapping("/list-dept-simple")
    ////@Operation(summary = "列表")
    @PermitAll
    public CommonResult<List<DeptPressVO>> getPressDepts() {
        ArrayList<DeptPressVO> list1 = new ArrayList<>();
        // 获得部门列表，只要开启状态和部门类型的
        DeptListReqVO reqVO = new DeptListReqVO();
        reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        List<DeptDO> list = deptService.getDeptList(reqVO);
        // 排序后，返回给前端
        list.sort(Comparator.comparing(DeptDO::getSort));
        List<DeptSimpleRespVO> deptSimpleRespVOS = DeptConvert.INSTANCE.convertList02(list);
        deptSimpleRespVOS.forEach(s->{
            DeptPressVO deptPressVO = new DeptPressVO();
            deptPressVO.setLabel(s.getName());
            deptPressVO.setValue(s.getId());
            deptPressVO.setParentId(s.getParentId());
            list1.add(deptPressVO);
        });

        return success(list1);
    }

    @GetMapping("/roleList")
    ////@Operation(summary = "获取登录人权限内部门列表")
//    @PermitAll
    @SneakyThrows
    public CommonResult<List<DeptRespVO>> roleListDepts() {
        List<DeptDO> deptDOList = deptService.roleListDepts();
        List<DeptRespVO> deptRespVOS = DeptConvert.INSTANCE.convertList(deptDOList);
        return success(buildRoleMenuTree(deptRespVOS));
    }

    public static List<DeptRespVO> buildRoleMenuTree(List<DeptRespVO> menuList)  {
        Map<Long, DeptRespVO> menuMap = new HashMap<>();
        for (DeptRespVO menu : menuList) {
            DeptRespVO menuRespVO = new DeptRespVO();
            BeanUtils.copyProperties(menu, menuRespVO);
            menuMap.put(menu.getId(), menuRespVO);
        }

        List<DeptRespVO> menuTree = new ArrayList<>();
        for (DeptRespVO menu : menuList) {
            if (menu.getParentId() == 0) {
                menuTree.add(menuMap.get(menu.getId()));
            } else {
                DeptRespVO parentMenu = menuMap.get(menu.getParentId());
                if (parentMenu != null) {
                    parentMenu.getChildren().add(menuMap.get(menu.getId()));
                } else {
                    menuTree.add(menuMap.get(menu.getId()));
                }
            }
        }
        return menuTree;
    }
}