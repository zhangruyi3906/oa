package com.lh.oa.module.system.util.roleScope;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.permission.RoleDO;
import com.lh.oa.module.system.dal.dataobject.permission.UserRoleDO;
import com.lh.oa.module.system.dal.mysql.dept.DeptMapper;
import com.lh.oa.module.system.dal.mysql.permission.RoleMapper;
import com.lh.oa.module.system.dal.mysql.permission.UserRoleMapper;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.service.dept.DeptService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleScopeUtils {
    @Resource
    @Lazy
    private UserRoleMapper mapper;
    @Resource
    @Lazy
    private RoleMapper roleMapper;
    @Resource
    @Lazy
    private AdminUserMapper userMapper;
    @Resource
    @Lazy
    private DeptMapper deptMapper;
    @Resource
    @Lazy
    private DeptService deptService;

    public Set<Long> isPage(Long userId) {
        Set<Long> deptIds = new HashSet<>();
        List<Long> roles = mapper.selectList(new LambdaQueryWrapperX<UserRoleDO>().eq(UserRoleDO::getUserId, userId)).stream().map(UserRoleDO::getRoleId).collect(Collectors.toList());
        List<RoleDO> roleDOS = roleMapper.selectList(new LambdaQueryWrapperX<RoleDO>().inIfPresent(RoleDO::getId, roles));
        Integer minDataScope = roleDOS.stream()
                .map(RoleDO::getDataScope).min(Comparator.naturalOrder())
                .orElse(null);

        if (minDataScope == 5) {
            return deptIds;
        } else if (minDataScope == 4) {
            Long deptId1 = userMapper.selectById(userId).getDeptId();
            List<Long> list = new ArrayList<>();
            findAllSubtreeIdsRecursive(deptId1, list);
            list.add(deptId1);
            deptIds.addAll(list);
            return deptIds;
        } else if (minDataScope == 3) {
            deptIds.add(userMapper.selectById(userId).getDeptId());
            return deptIds;
        } else if (minDataScope == 2) {
            if (CollUtil.isEmpty(roleDOS)) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.WORK_LOG_is_ROLE);
            }
            return roleDOS.stream()
                    .map(RoleDO::getDataScopeDeptIds)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        } else if (minDataScope == 1) {
            deptIds.addAll(deptMapper.selectList().stream().map(DeptDO::getId).collect(Collectors.toSet())); // 使用addAll避免重复添加元素
            return deptIds;
        } else if (minDataScope == 6) {
            Long deptId = userMapper.selectById(userId).getDeptId();
            Set<Long> organizationAndChildDeptIdSet = deptService.getOrganizationAndChildDeptList(deptId);
            deptIds.addAll(organizationAndChildDeptIdSet);
            return deptIds;
        }
        return deptIds;
    }

    private void findAllSubtreeIdsRecursive(Long parentId, List<Long> result) {
        List<Long> childIds = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().eqIfPresent(DeptDO::getParentId, parentId))
                .stream().map(DeptDO::getId).collect(Collectors.toList());
        result.addAll(childIds);
        for (Long childId : childIds) {
            findAllSubtreeIdsRecursive(childId, result);
        }
    }
}
