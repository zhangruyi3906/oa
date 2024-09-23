package com.lh.oa.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
import com.lh.oa.framework.common.util.collection.MapUtils;
import com.lh.oa.framework.datapermission.core.annotation.DataPermission;
import com.lh.oa.framework.tenant.core.aop.TenantIgnore;
import com.lh.oa.framework.tenant.core.util.TenantUtils;
import com.lh.oa.module.system.api.permission.dto.DeptDataPermissionRespDTO;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.permission.MenuDO;
import com.lh.oa.module.system.dal.dataobject.permission.RoleDO;
import com.lh.oa.module.system.dal.dataobject.permission.RoleMenuDO;
import com.lh.oa.module.system.dal.dataobject.permission.UserRoleDO;
import com.lh.oa.module.system.dal.mysql.permission.RoleMenuMapper;
import com.lh.oa.module.system.dal.mysql.permission.UserRoleMapper;
import com.lh.oa.module.system.enums.permission.DataScopeEnum;
import com.lh.oa.module.system.service.dept.DeptService;
import com.lh.oa.module.system.service.user.AdminUserService;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.function.Supplier;

import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertSet;
import static java.util.Collections.singleton;

/**
 * 权限 Service 实现类
 *
 * @author
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    /**
     * 角色编号与菜单编号的缓存映射
     * key：角色编号
     * value：菜单编号的数组
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter
    private volatile Multimap<Long, Long> roleMenuCache;
    /**
     * 菜单编号与角色编号的缓存映射
     * key：菜单编号
     * value：角色编号的数组
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter
    private volatile Multimap<Long, Long> menuRoleCache;

    /**
     * 用户编号与角色编号的缓存映射
     * key：用户编号
     * value：角色编号的数组
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter
    private volatile Map<Long, Set<Long>> userRoleCache;

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;
    @Resource
    private DeptService deptService;
    @Resource
    private AdminUserService userService;

    @Override
    @PostConstruct
    public void initLocalCache() {
        initLocalCacheForRoleMenu();
        initLocalCacheForUserRole();
    }

    /**
     * 刷新 RoleMenu 本地缓存
     */
    @VisibleForTesting
    void initLocalCacheForRoleMenu() {
        TenantUtils.executeIgnore(() -> {
            List<RoleMenuDO> roleMenus = roleMenuMapper.selectList();
            ImmutableMultimap.Builder<Long, Long> roleMenuCacheBuilder = ImmutableMultimap.builder();
            ImmutableMultimap.Builder<Long, Long> menuRoleCacheBuilder = ImmutableMultimap.builder();
            roleMenus.forEach(roleMenuDO -> {
                roleMenuCacheBuilder.put(roleMenuDO.getRoleId(), roleMenuDO.getMenuId());
                menuRoleCacheBuilder.put(roleMenuDO.getMenuId(), roleMenuDO.getRoleId());
            });
            roleMenuCache = roleMenuCacheBuilder.build();
            menuRoleCache = menuRoleCacheBuilder.build();
        });
    }

    /**
     * 刷新 UserRole 本地缓存
     */
    @VisibleForTesting
    void initLocalCacheForUserRole() {
        TenantUtils.executeIgnore(() -> {
            List<UserRoleDO> userRoles = userRoleMapper.selectList();
            ImmutableMultimap.Builder<Long, Long> userRoleCacheBuilder = ImmutableMultimap.builder();
            userRoles.forEach(userRoleDO -> userRoleCacheBuilder.put(userRoleDO.getUserId(), userRoleDO.getRoleId()));
            userRoleCache = CollectionUtils.convertMultiMap2(userRoles, UserRoleDO::getUserId, UserRoleDO::getRoleId);
        });
    }

    @Override
    public List<MenuDO> getRoleMenuListFromCache(Collection<Long> roleIds, Collection<Integer> menuTypes,
                                                 Collection<Integer> menusStatuses) {
        if (CollectionUtils.isAnyEmpty(roleIds, menuTypes, menusStatuses)) {
            return Collections.emptyList();
        }

        List<RoleDO> roleList = roleService.getRoleListFromCache(roleIds);
        if (roleService.hasAnySuperAdmin(roleList)) {
            return menuService.getMenuListFromCache(menuTypes, menusStatuses);
        }

        List<Long> menuIds = MapUtils.getList(roleMenuCache, roleIds);
        return menuService.getMenuListFromCache(menuIds, menuTypes, menusStatuses);
    }

    @Override
    public Set<Long> getUserRoleIdsFromCache(Long userId, Collection<Integer> roleStatuses) {
        Set<Long> cacheRoleIds = userRoleCache.get(userId);
        if (CollUtil.isEmpty(cacheRoleIds)) {
            return Collections.emptySet();
        }
        Set<Long> roleIds = new HashSet<>(cacheRoleIds);
        if (CollUtil.isNotEmpty(roleStatuses)) {
            roleIds.removeIf(roleId -> {
                RoleDO role = roleService.getRoleFromCache(roleId);
                return role == null || !roleStatuses.contains(role.getStatus());
            });
        }
        return roleIds;
    }

    @Override
    public Set<Long> getRoleMenuIds(Long roleId) {
        if (roleService.hasAnySuperAdmin(Collections.singleton(roleId))) {
            return convertSet(menuService.getMenuList(), MenuDO::getId);
        }
        return convertSet(roleMenuMapper.selectListByRoleId(roleId), RoleMenuDO::getMenuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleMenu(Long roleId, Set<Long> menuIds) {
        Set<Long> dbMenuIds = convertSet(roleMenuMapper.selectListByRoleId(roleId),
                RoleMenuDO::getMenuId);
        Collection<Long> createMenuIds = CollUtil.subtract(menuIds, dbMenuIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(dbMenuIds, menuIds);
        if (!CollUtil.isEmpty(createMenuIds)) {
            roleMenuMapper.insertBatch(CollectionUtils.convertList(createMenuIds, menuId -> {
                RoleMenuDO entity = new RoleMenuDO();
                entity.setRoleId(roleId);
                entity.setMenuId(menuId);
                return entity;
            }));
        }
        if (!CollUtil.isEmpty(deleteMenuIds)) {
            roleMenuMapper.deleteListByRoleIdAndMenuIds(roleId, deleteMenuIds);
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                permissionProducer.sendRoleMenuRefreshMessage();
                ThreadUtil.execAsync(() -> initLocalCache());
            }

        });
    }

    @Override
    public Set<Long> getUserRoleIdListByUserId(Long userId) {
        return convertSet(userRoleMapper.selectListByUserId(userId),
                UserRoleDO::getRoleId);
    }

    @Override
    public Set<Long> getUserRoleIdListByRoleIds(Collection<Long> roleIds) {
        return convertSet(userRoleMapper.selectListByRoleIds(roleIds),
                UserRoleDO::getUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserRole(Long userId, Set<Long> roleIds) {
        log.info("assignUserRole: userId={}, roleIds={}", userId, roleIds);
        if (CollUtil.isEmpty(roleIds))
            return;
        Set<Long> dbRoleIds = convertSet(userRoleMapper.selectListByUserId(userId),
                UserRoleDO::getRoleId);
        Collection<Long> createRoleIds = CollUtil.subtract(roleIds, dbRoleIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(dbRoleIds, roleIds);
        if (!CollUtil.isEmpty(createRoleIds)) {
            userRoleMapper.insertBatch(CollectionUtils.convertList(createRoleIds, roleId -> {
                UserRoleDO entity = new UserRoleDO();
                entity.setUserId(userId);
                entity.setRoleId(roleId);
                return entity;
            }));
        }
        if (!CollUtil.isEmpty(deleteMenuIds)) {
            userRoleMapper.deleteListByUserIdAndRoleIdIds(userId, deleteMenuIds);
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                permissionProducer.sendUserRoleRefreshMessage();
                ThreadUtil.execAsync(() -> initLocalCache());
            }

        });
    }

    @Override
    public void assignRoleDataScope(Long roleId, Integer dataScope, Set<Long> dataScopeDeptIds) {
        roleService.updateRoleDataScope(roleId, dataScope, dataScopeDeptIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processRoleDeleted(Long roleId) {
        userRoleMapper.deleteListByRoleId(roleId);
        roleMenuMapper.deleteListByRoleId(roleId);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                permissionProducer.sendRoleMenuRefreshMessage();
//                permissionProducer.sendUserRoleRefreshMessage();
                ThreadUtil.execAsync(() -> initLocalCache());
            }

        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processMenuDeleted(Long menuId) {
        roleMenuMapper.deleteListByMenuId(menuId);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                permissionProducer.sendRoleMenuRefreshMessage();
                ThreadUtil.execAsync(() -> initLocalCache());
            }

        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processUserDeleted(Long userId) {
        userRoleMapper.deleteListByUserId(userId);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                permissionProducer.sendUserRoleRefreshMessage();
                ThreadUtil.execAsync(() -> initLocalCache());
            }

        });
    }

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        if (ArrayUtil.isEmpty(permissions)) {
            return true;
        }
        Set<Long> roleIds = getUserRoleIdsFromCache(userId, singleton(CommonStatusEnum.ENABLE.getStatus()));
        if (CollUtil.isEmpty(roleIds)) {
            return false;
        }
        if (roleService.hasAnySuperAdmin(roleIds)) {
            return true;
        }

        return Arrays.stream(permissions).anyMatch(permission -> {
            List<MenuDO> menuList = menuService.getMenuListByPermissionFromCache(permission);
            if (CollUtil.isEmpty(menuList)) {
                return false;
            }
            return menuList.stream().anyMatch(menu -> CollUtil.containsAny(roleIds,
                    menuRoleCache.get(menu.getId())));
        });
    }

    @Override
    public boolean hasAnyRoles(Long userId, String... roles) {
        if (ArrayUtil.isEmpty(roles)) {
            return true;
        }

        Set<Long> roleIds = getUserRoleIdsFromCache(userId, singleton(CommonStatusEnum.ENABLE.getStatus()));
        if (CollUtil.isEmpty(roleIds)) {
            return false;
        }
        if (roleService.hasAnySuperAdmin(roleIds)) {
            return true;
        }
        Set<String> userRoles = convertSet(roleService.getRoleListFromCache(roleIds),
                RoleDO::getCode);
        return CollUtil.containsAny(userRoles, Sets.newHashSet(roles));
    }

    @Override
    @DataPermission(enable = false)
    @TenantIgnore
    public DeptDataPermissionRespDTO getDeptDataPermission(Long userId) {
        Set<Long> roleIds = getUserRoleIdsFromCache(userId, singleton(CommonStatusEnum.ENABLE.getStatus()));
        DeptDataPermissionRespDTO result = new DeptDataPermissionRespDTO();
        if (CollUtil.isEmpty(roleIds)) {
            result.setSelf(true);
            return result;
        }
        List<RoleDO> roles = roleService.getRoleListFromCache(roleIds);

        Supplier<Long> userDeptIdCache = Suppliers.memoize(() -> userService.getUser(userId).getDeptId());
        for (RoleDO role : roles) {
            if (role.getDataScope() == null) {
                continue;
            }
            if (Objects.equals(role.getDataScope(), DataScopeEnum.ALL.getScope())) {
                result.setAll(true);
                continue;
            }
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_CUSTOM.getScope())) {
                CollUtil.addAll(result.getDeptIds(), role.getDataScopeDeptIds());
                CollUtil.addAll(result.getDeptIds(), userDeptIdCache.get());
                continue;
            }
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_ONLY.getScope())) {
                CollectionUtils.addIfNotNull(result.getDeptIds(), userDeptIdCache.get());
                continue;
            }
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_AND_CHILD.getScope())) {
                List<DeptDO> depts = deptService.getDeptListByParentIdFromCache(userDeptIdCache.get(), true);
                CollUtil.addAll(result.getDeptIds(), CollectionUtils.convertList(depts, DeptDO::getId));
                CollUtil.addAll(result.getDeptIds(), userDeptIdCache.get());
                continue;
            }
            if (Objects.equals(role.getDataScope(), DataScopeEnum.ORGANIZATION_AND_CHILD.getScope())) {
                Set<Long> organizationAndChildDeptIdSet = deptService.getOrganizationAndChildDeptList(userDeptIdCache.get());
                CollUtil.addAll(result.getDeptIds(), organizationAndChildDeptIdSet);
                continue;
            }
            if (Objects.equals(role.getDataScope(), DataScopeEnum.SELF.getScope())) {
                result.setSelf(true);
                continue;
            }
        }
        return result;
    }

}
