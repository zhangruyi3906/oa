package com.lh.oa.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.tenant.core.util.TenantUtils;
import com.lh.oa.module.system.controller.admin.permission.vo.role.RoleCreateReqVO;
import com.lh.oa.module.system.controller.admin.permission.vo.role.RoleExportReqVO;
import com.lh.oa.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import com.lh.oa.module.system.controller.admin.permission.vo.role.RoleUpdateReqVO;
import com.lh.oa.module.system.convert.permission.RoleConvert;
import com.lh.oa.module.system.dal.dataobject.permission.RoleDO;
import com.lh.oa.module.system.dal.mysql.permission.RoleMapper;
import com.lh.oa.module.system.enums.permission.DataScopeEnum;
import com.lh.oa.module.system.enums.permission.RoleCodeEnum;
import com.lh.oa.module.system.enums.permission.RoleTypeEnum;
import com.google.common.annotations.VisibleForTesting;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 角色 Service 实现类
 *
 * @author
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    /**
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    private volatile Map<Long, RoleDO> roleCache;

    @Resource
    private PermissionService permissionService;



    @Resource
    private RoleMapper roleMapper;

    /**
     * 初始化 {@link #roleCache} 缓存
     */
    @Override
    @PostConstruct
    public void initLocalCache() {
        TenantUtils.executeIgnore(() -> {
            List<RoleDO> roleList = roleMapper.selectList();
            roleCache = convertMap(roleList, RoleDO::getId);
        });
    }

    @Override
    @Transactional
    public Long createRole(RoleCreateReqVO reqVO, Integer type) {
        validateRoleDuplicate(reqVO.getName(), reqVO.getCode(), null);
        RoleDO role = RoleConvert.INSTANCE.convert(reqVO);
        role.setType(ObjectUtil.defaultIfNull(type, RoleTypeEnum.CUSTOM.getType()));
        role.setStatus(CommonStatusEnum.ENABLE.getStatus());
        role.setDataScope(DataScopeEnum.ALL.getScope());
        roleMapper.insert(role);

        permissionService.assignRoleMenu(role.getId(), reqVO.getMenuIds());
        permissionService.assignRoleDataScope(role.getId(), reqVO.getDataScope(), reqVO.getDataScopeDeptIds());

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
//                roleProducer.sendRoleRefreshMessage();
                ThreadUtil.execAsync(() -> initLocalCache());
            }
        });
        return role.getId();
    }

    @Override
    public void updateRole(RoleUpdateReqVO reqVO) {
        validateRoleForUpdate(reqVO.getId());
        validateRoleDuplicate(reqVO.getName(), reqVO.getCode(), reqVO.getId());

        RoleDO updateObj = RoleConvert.INSTANCE.convert(reqVO);
        roleMapper.updateById(updateObj);
        permissionService.assignRoleMenu(updateObj.getId(), reqVO.getMenuIds());
        permissionService.assignRoleDataScope(updateObj.getId(), reqVO.getDataScope(), reqVO.getDataScopeDeptIds());
//        roleProducer.sendRoleRefreshMessage();
        ThreadUtil.execAsync(this::initLocalCache);
    }

    @Override
    public void updateRoleStatus(Long id, Integer status) {
        validateRoleForUpdate(id);
        RoleDO updateObj = new RoleDO().setId(id).setStatus(status);
        roleMapper.updateById(updateObj);
//        roleProducer.sendRoleRefreshMessage();
        ThreadUtil.execAsync(this::initLocalCache);
    }

    @Override
    public void updateRoleDataScope(Long id, Integer dataScope, Set<Long> dataScopeDeptIds) {
        validateRoleForUpdate(id);
        RoleDO updateObject = new RoleDO();
        updateObject.setId(id);
        updateObject.setDataScope(dataScope);
        updateObject.setDataScopeDeptIds(dataScopeDeptIds);
        roleMapper.updateById(updateObject);
//        roleProducer.sendRoleRefreshMessage();
        ThreadUtil.execAsync(this::initLocalCache);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        validateRoleForUpdate(id);
        roleMapper.deleteById(id);
        permissionService.processRoleDeleted(id);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                roleProducer.sendRoleRefreshMessage();
                ThreadUtil.execAsync(() -> initLocalCache());
            }

        });
    }

    @Override
    public RoleDO getRoleFromCache(Long id) {
        return roleCache.get(id);
    }

    @Override
    public List<RoleDO> getRoleListByStatus(@Nullable Collection<Integer> statuses) {
        if (CollUtil.isEmpty(statuses)) {
            return roleMapper.selectList();
        }
        return roleMapper.selectListByStatus(statuses);
    }

    @Override
    public List<RoleDO> getRoleListFromCache(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return roleCache.values().stream().filter(roleDO -> ids.contains(roleDO.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasAnySuperAdmin(Collection<RoleDO> roleList) {
        if (CollUtil.isEmpty(roleList)) {
            return false;
        }
        return roleList.stream().anyMatch(role -> RoleCodeEnum.isSuperAdmin(role.getCode()));
    }

    @Override
    public RoleDO getRole(Long id) {
        RoleDO roleDO = roleMapper.selectById(id);
        Set<Long> roleMenuIds = permissionService.getRoleMenuIds(roleDO.getId());
        roleDO.setMenusIds(roleMenuIds);
        return roleDO;
    }

    @Override
    public PageResult<RoleDO> getRolePage(RolePageReqVO reqVO) {

        PageResult<RoleDO> roleDOPageResult = roleMapper.selectPage(reqVO);
        List<RoleDO> list = roleDOPageResult.getList();
        for (RoleDO roleDO : list) {
            Set<Long> roleMenuIds = permissionService.getRoleMenuIds(roleDO.getId());
            roleDO.setMenusIds(roleMenuIds);
        }
        roleDOPageResult.setList(list);

        return roleDOPageResult;
    }

    @Override
    public List<RoleDO> getRoleList(RoleExportReqVO reqVO) {
        return roleMapper.selectList(reqVO);
    }

    /**
     * 校验角色的唯一字段是否重复
     *
     *
     * @param name 角色名字
     * @param code 角色额编码
     * @param id 角色编号
     */
    @VisibleForTesting
    void validateRoleDuplicate(String name, String code, Long id) {
        if (RoleCodeEnum.isSuperAdmin(code)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_ADMIN_CODE_ERROR, code);
        }
        RoleDO role = roleMapper.selectByName(name);
        if (role != null && !role.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_NAME_DUPLICATE, name);
        }
        if (!StringUtils.hasText(code)) {
            return;
        }
        role = roleMapper.selectByCode(code);
        if (role != null && !role.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_CODE_DUPLICATE, code);
        }
    }

    /**
     * 校验角色是否可以被更新
     *
     * @param id 角色编号
     */
    @VisibleForTesting
    void validateRoleForUpdate(Long id) {
        RoleDO roleDO = roleMapper.selectById(id);
        if (roleDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_NOT_EXISTS);
        }
        if (RoleTypeEnum.SYSTEM.getType().equals(roleDO.getType())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE);
        }
    }

    @Override
    public void validateRoleList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        List<RoleDO> roles = roleMapper.selectBatchIds(ids);
        Map<Long, RoleDO> roleMap = convertMap(roles, RoleDO::getId);
        ids.forEach(id -> {
            RoleDO role = roleMap.get(id);
            if (role == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_IS_DISABLE, role.getName());
            }
        });
    }

    @Override
    public List<RoleDO> selectByRoleNames(Set<String> roleNames) {
        if (org.springframework.util.CollectionUtils.isEmpty(roleNames)) {
            return Collections.emptyList();
        }
        return roleMapper.selectList(new LambdaQueryWrapperX<RoleDO>()
                .inIfPresent(RoleDO::getName, roleNames));
    }

    @Override
    public List<RoleDO> getRoleListByIds(Set<Long> ids) {
        if (org.springframework.util.CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return roleMapper.selectBatchIds(ids);
    }
}
