package com.lh.oa.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.tenant.core.context.TenantContextHolder;
import com.lh.oa.framework.tenant.core.util.TenantUtils;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptUpdateReqVO;
import com.lh.oa.module.system.convert.dept.DeptConvert;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.dept.PostDO;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.mysql.dept.DeptMapper;
import com.lh.oa.module.system.dal.mysql.dept.PostMapper;
import com.lh.oa.module.system.dal.mysql.information.InformationMapper;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.enums.DeptTypeEnum;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.enums.dept.DeptIdEnum;
import com.lh.oa.module.system.full.entity.jnt.JntDept;
import com.lh.oa.module.system.full.entity.jnt.JntOrg;
import com.lh.oa.module.system.full.entity.jnt.JntUser;
import com.lh.oa.module.system.full.enums.jnt.EmployTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.GenderEnum;
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.ProjectSourceEnum;
import com.lh.oa.module.system.full.enums.jnt.SourceEnum;
import com.lh.oa.module.system.full.service.jnt.JntBaseDataSyncService;
import com.lh.oa.module.system.util.roleScope.RoleScopeUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 部门 Service 实现类
 *
 * @author
 */
@Service
@Validated
@Slf4j
public class DeptServiceImpl implements DeptService {

    /**
     *
     */
    @Getter
    private volatile Map<Long, DeptDO> deptCache;
    /**
     *
     */
    @Getter
    private volatile Multimap<Long, DeptDO> parentDeptCache;

    @Resource
    private DeptMapper deptMapper;

    @Resource
    private AdminUserMapper userMapper;

    @Resource
    private InformationMapper informationMapper;


    @Resource
    private JntBaseDataSyncService jntBaseDataSyncService;
    @Resource
    private RoleScopeUtils roleScopeUtils;
    @Resource
    private PostMapper postMapper;

    /**
     *
     */
    @Override
    @PostConstruct
    public synchronized void initLocalCache() {
        TenantUtils.executeIgnore(() -> {
            List<DeptDO> depts = deptMapper.selectList();
            log.info("[initLocalCache][缓存部门，数量为:{}]", depts.size());
            ImmutableMap.Builder<Long, DeptDO> builder = ImmutableMap.builder();
            ImmutableMultimap.Builder<Long, DeptDO> parentBuilder = ImmutableMultimap.builder();
            depts.forEach(sysRoleDO -> {
                builder.put(sysRoleDO.getId(), sysRoleDO);
                parentBuilder.put(sysRoleDO.getParentId(), sysRoleDO);
            });
            deptCache = builder.build();
            parentDeptCache = parentBuilder.build();
        });
    }

    @Override
    public DeptDO getParentOrgOfCompany(Long id) {
        DeptDO entity = this.getDept(id);
        if (entity == null)
            throw new BusinessException("所选组织部门不存在");
        if (entity.getType() == DeptTypeEnum.ORGANIZATION)
            return entity;
        if ((entity.getParentId() == null || entity.getParentId() == 0) && entity.getType() == DeptTypeEnum.DEPARTMENT)
            throw new BusinessException("上级组织部门数据有误");
        return getParentOrgOfCompany(entity.getParentId());
    }

    @Override
    public DeptDO getParentDeptOfDept(Long id) {
        DeptDO entity = this.getDept(id);
        if (entity == null)
            throw new BusinessException("所选组织部门不存在");
        if (entity.getType() == DeptTypeEnum.DEPARTMENT)
            return entity;
        if ((entity.getParentId() == null || entity.getParentId() == 0) && entity.getType() == DeptTypeEnum.ORGANIZATION)
            throw new BusinessException("上级组织部门数据有误");
        return getParentDeptOfDept(entity.getParentId());
    }

    @Override
    public List<DeptDO> getAllParentDeptList() {
        return deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>()
                .eq(DeptDO::getParentId, 0));
    }

    @Override
    public List<DeptRespDTO> getAllDepts() {
        List<DeptDO> deptDOS = deptMapper.selectList();
        return DeptConvert.INSTANCE.convertList03(deptDOS);
    }

    @Override
    public List<Long> getDeptAndAllChildDeptList(Long deptId) {
        if (deptId == null) {
            return new ArrayList<>();
        }
        DeptDO deptDO = deptMapper.selectOne(DeptDO::getId, deptId);
        if (ObjectUtils.isEmpty(deptDO)) {
            return new ArrayList<>();
        }
        Set<Long> deptIdSet = new HashSet<>();
        deptIdSet.add(deptId);
        deptIdSet = this.queryDeptByParentDept(deptId, deptIdSet);
        List<Long> deptIdList = deptIdSet.stream().collect(Collectors.toList());
        return deptIdList;
    }

    @Override
    public Set<Long> getOrganizationAndChildDeptList(Long deptId) {
        DeptDO organization = this.getParentDeptOfDept(deptId);
        Set<Long> deptIdSet = new HashSet<>();
        Set<Long> organizationAndChildDeptIdSet = this.queryDeptByParentDept(organization.getId(), deptIdSet);
        organizationAndChildDeptIdSet.add(deptId);
        return organizationAndChildDeptIdSet;
    }

    @Override
    public Long getOfficeDeptLeaderUserIdByUserId(Long userId) {
        log.info("getOfficeDeptLeaderUserIdByUserId  用户id:{}", userId);
        List<DeptDO> deptDOS = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().likeIfPresent(DeptDO::getName, "总经办"));
        if (CollUtil.isEmpty(deptDOS)) {
            return userId;
        }
        Map<Long, DeptDO> officeDeptIdToDeptDO = deptDOS.stream().collect(Collectors.toMap(DeptDO::getId, item -> item));
        Set<Long> officeDeptIdSet = deptDOS.stream().map(DeptDO::getId).collect(Collectors.toSet());
        AdminUserDO adminUserDO = userMapper.selectOne(AdminUserDO::getId, userId);
        if (ObjectUtils.isEmpty(adminUserDO)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
        }
        Long deptId = adminUserDO.getDeptId();
        while (!officeDeptIdSet.contains(deptId)) {
            DeptDO deptDO = deptMapper.selectOne(DeptDO::getId, deptId);
            if (ObjectUtils.isEmpty(deptDO)) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_FOUND);
            }
            if (deptDO.getParentId() == 0) {
                //上级部门是组织的话返回组织的负责人
                return deptDO.getLeaderUserId();
            }
            deptId = deptDO.getParentId();
        }
        DeptDO officeDept = officeDeptIdToDeptDO.get(deptId);
        log.info("getOfficeDeptLeaderUserIdByUserId  经办部门id:{}, 部门：{}", deptId, JsonUtils.toJsonString(officeDept));
        Long leaderUserId = officeDept.getLeaderUserId();
        return leaderUserId;
    }

    @Override
    public Set<Long> getOfficeDeptLeaderUserIdByUserIds(Set<Long> userIds) {
        log.info("查找当事人所在总经办  用户userIds:{}", userIds);
        List<DeptDO> deptDOS = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().likeIfPresent(DeptDO::getName, "总经办"));
        if (CollUtil.isEmpty(deptDOS)) {
            return userIds;
        }
        Map<Long, DeptDO> DeptIdToDeptDO = deptMapper.selectList().stream().collect(Collectors.toMap(DeptDO::getId, item -> item));
        Set<Long> officeDeptIdSet = deptDOS.stream().map(DeptDO::getId).collect(Collectors.toSet());
        List<AdminUserDO> adminUserDOS = userMapper.selectList(new LambdaQueryWrapperX<AdminUserDO>().in(AdminUserDO::getId, userIds));
        if (CollUtil.isEmpty(adminUserDOS)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
        }
        Set<Long> deptIdSet = adminUserDOS.stream().map(AdminUserDO::getDeptId).collect(Collectors.toSet());
        Set<Long> realOfficeDeptIdSet = new HashSet<>();
        deptIdSet.forEach(deptId -> {
            while (!officeDeptIdSet.contains(deptId)) {
                DeptDO deptDO = DeptIdToDeptDO.get(deptId);
                if (ObjectUtils.isEmpty(deptDO)) {
                    throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_FOUND);
                }
                if (deptDO.getParentId() == 0) {
                    break;
                }
                deptId = deptDO.getParentId();
            }
            realOfficeDeptIdSet.add(deptId);
        });
        Set<Long> leaderUserIdSet = DeptIdToDeptDO.entrySet().stream()
                .filter(entry -> realOfficeDeptIdSet.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(DeptDO::getLeaderUserId)
                .collect(Collectors.toSet());
        return leaderUserIdSet;
    }

    @Override
    public List<DeptDO> roleListDepts() {
        Long loginUserId = getLoginUserId();
        Set<Long> deptIdSet = roleScopeUtils.isPage(loginUserId);
        List<DeptDO> deptDOList = new ArrayList<>();
        if (deptIdSet == null || deptIdSet.isEmpty()) {
            AdminUserDO adminUserDO = userMapper.selectById(loginUserId);
            Long deptId = adminUserDO.getDeptId();
            DeptDO deptDO = deptMapper.selectById(deptId);
            deptDOList.add(deptDO);
            return deptDOList;
        }
        deptDOList = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().in(DeptDO::getId, deptIdSet));
        return deptDOList;
    }


    public Set<Long> queryDeptByParentDept(Long deptId, Set<Long> deptIdSet) {
        //所选择的部门id--parentId
        List<DeptDO> childrenDeptDOS = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().eq(DeptDO::getParentId, deptId));

        //如果该部门有子部门，获取子部门，查询该子部门中的所有用户，
        //并传入userSet，将查询结果，存入到userSet中，将其返回
        if (childrenDeptDOS.size() > 0) {
            Set<Long> childrenDeptIds = childrenDeptDOS.stream().map(DeptDO::getId).collect(Collectors.toSet());
            if (childrenDeptIds != null && !childrenDeptIds.isEmpty()) {
                deptIdSet.addAll(childrenDeptIds);
                childrenDeptIds.forEach(item -> {
                    queryDeptByParentDept(item, deptIdSet);
                });
            }
        }
        return deptIdSet;
    }

    @Override
    public List<DeptDO> getDeptAndChildList(Long deptId) {
        return deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>()
                .eqIfPresent(DeptDO::getId, deptId).or().eq(DeptDO::getParentId, deptId));
    }

    @Transactional
    @Override
    public Long createDept(DeptCreateReqVO reqVO) {
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        if (reqVO.getType() == DeptTypeEnum.DEPARTMENT) {
            Long parentId = reqVO.getParentId();
            DeptDO parentDept = getDept(parentId);
            if (parentDept == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.ORGANIZATION_NOT_EXITS);
            }
        }
        validateForCreateOrUpdate(null, reqVO.getParentId(), reqVO.getName(), null);
        DeptDO dept = DeptConvert.INSTANCE.convert(reqVO);
        deptMapper.insert(dept);
        ThreadUtil.execAsync(this::initLocalCache);
//        CompletableFuture.runAsync(() -> this.syncCreateDept(reqVO, dept));
        if (reqVO.getSource() != null && reqVO.getSource() == ProjectSourceEnum.PMS) {//项目管理平台
            return dept.getId();
        }
        this.syncCreateDept(dept);
        return dept.getId();
    }

    public void handSyncDept(Long deptId) {
        log.info("手动同步部门, deptId:{}", deptId);

        DeptDO dept = deptMapper.selectById(deptId);
        if (dept == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_FOUND);
        }

        this.syncCreateDept(dept);
    }


    @Transactional
    public void syncCreateDept(DeptDO dept) {
        //同步建能通
        // TODO [Rz Liu]: 2023-09-11 后面加类型，公司/部门，暂默认部门，因公司已经加好了
        try {
            if (Objects.equals(DeptTypeEnum.ORGANIZATION, dept.getType())) {
                JntOrg jntOrg = new JntOrg();
                jntOrg.setOperateType(OperateTypeEnum.ADD);
                jntOrg.setOaOrgId(Math.toIntExact(dept.getId()));
                if (dept.getParentId() == null || dept.getParentId() == 0) {
                    jntOrg.setOaParentOrgId(0);
                } else {
                    DeptDO parentOrgOfCompany = this.getParentOrgOfCompany(dept.getParentId());
                    jntOrg.setOaParentOrgId(parentOrgOfCompany == null ? 0 : Math.toIntExact(parentOrgOfCompany.getId()));
                }
                jntOrg.setName(dept.getName());
                jntOrg.setSource(SourceEnum.OA);
                jntOrg.setOrderNumber(dept.getSort());
                jntBaseDataSyncService.syncOrg(jntOrg);
            } else {
                JntDept jntDept = new JntDept();
                jntDept.setOperateType(OperateTypeEnum.ADD);
                jntDept.setOaDeptId(Math.toIntExact(dept.getId()));
                if (dept.getParentId() == null || dept.getParentId() == 0) {
                    jntDept.setOaParentDeptId(0);
                } else {
                    DeptDO entity = this.getDept(dept.getParentId());
                    if (entity == null)
                        throw new BusinessException("所选组织部门不存在");
                    if (entity.getType() == DeptTypeEnum.DEPARTMENT) {
                        jntDept.setOaParentDeptId(Math.toIntExact(dept.getParentId()));
                        DeptDO parentOrgOfCompany = this.getParentOrgOfCompany(dept.getParentId());
                        jntDept.setOaOrgId(Math.toIntExact(parentOrgOfCompany.getId()));
                    } else {
                        jntDept.setOaOrgId(Math.toIntExact(entity.getId()));
                    }
                }
                jntDept.setName(dept.getName());
                jntDept.setSource(SourceEnum.OA);
                jntDept.setOrderNumber(dept.getSort());
                jntBaseDataSyncService.syncDept(jntDept);
            }
        } catch (Exception e) {
            throw new BusinessException("[JNT]操作失败，" + e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public void updateDept(DeptUpdateReqVO reqVO) {
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        validateForCreateOrUpdate(reqVO.getId(), reqVO.getParentId(), reqVO.getName(), reqVO.getStatus());
        if (reqVO.getType() == DeptTypeEnum.DEPARTMENT) {
            Long parentId = reqVO.getParentId();
            DeptDO parentDept = getDept(parentId);
            if (parentDept == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.ORGANIZATION_NOT_EXITS);
            }
        }
        DeptDO updateObj = DeptConvert.INSTANCE.convert(reqVO);
        DeptDO deptDO = deptMapper.selectById(reqVO.getId());
        deptMapper.updateById(updateObj);
        ThreadUtil.execAsync(this::initLocalCache);
//        CompletableFuture.runAsync(() -> this.syncUpdateDept(reqVO, updateObj, deptDO));
        if (reqVO.getSource() != null && reqVO.getSource() == ProjectSourceEnum.PMS) {//项目管理平台
            return;
        }
        this.syncUpdateDept(reqVO, updateObj, deptDO);
    }

    @Transactional
    public void syncUpdateDept(DeptUpdateReqVO reqVO, DeptDO updateObj, DeptDO deptDO) {
        //同步建能通
        // TODO [Rz Liu]: 2023-09-11 后面加类型，公司/部门，暂默认部门，因公司已经加好了
        try {
            List<AdminUserDO> adminUserDOS = userMapper.selectByDeptId(reqVO.getId());
            //部门转组织，组织转部门
            if (deptDO.getType() == DeptTypeEnum.ORGANIZATION && updateObj.getType() == DeptTypeEnum.DEPARTMENT) {//组织转部门
                if (!adminUserDOS.isEmpty()) {
                    //同步人员
                    for (AdminUserDO adminUserDO : adminUserDOS) {
                        InformationDO information = informationMapper.getByUserId(adminUserDO.getId());
                        Integer infoType = information.getInfoType();
                        JntUser jntUser = new JntUser();
                        jntUser.setOperateType(OperateTypeEnum.UPDATE);
                        jntUser.setOaUserId(Math.toIntExact(adminUserDO.getId()));
                        jntUser.setOaDeptId(Math.toIntExact(reqVO.getId()));

                        if (updateObj.getParentId() != null) {
                            DeptDO parentOrgOfCompany = this.getParentOrgOfCompany(deptDO.getParentId());
                            jntUser.setOaOrgId(Math.toIntExact(parentOrgOfCompany.getId()));
                        } else {
                            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ORGANIZATION_NOT_EXITS);
                        }
                        if (adminUserDO.getPostIds() != null && adminUserDO.getPostIds().size() > 0) {
                            jntUser.setOaPostId(Math.toIntExact(adminUserDO.getPostIds().iterator().next()));
                        }
                        jntUser.setAccount(adminUserDO.getUsername());
                        jntUser.setName(adminUserDO.getNickname());
                        jntUser.setMobile(adminUserDO.getMobile());
                        jntUser.setGender(adminUserDO.getSex() == null ? GenderEnum.UNKNOWN : (adminUserDO.getSex() == 0 ? GenderEnum.MAN : GenderEnum.WOMAN));

                        EmployTypeEnum employTypeEnum = null;
                        if (infoType != 2) {
                            employTypeEnum = EmployTypeEnum.INNER_EMPLOY;
                        } else {
                            employTypeEnum = EmployTypeEnum.OUTER_EMPLOY;
                        }
                        jntUser.setEmployType(employTypeEnum);//设置内外聘
                        jntUser.setSource(SourceEnum.OA);
                        jntBaseDataSyncService.syncUser(jntUser);
                    }
                }
                //删除原组织
                JntOrg jntOrg = new JntOrg();
                jntOrg.setIds(reqVO.getId() + "");
                jntOrg.setOperateType(OperateTypeEnum.DELETE);
                jntBaseDataSyncService.syncOrg(jntOrg);
                //添加部门
                JntDept jntDept = new JntDept();
                jntDept.setOperateType(OperateTypeEnum.ADD);
                jntDept.setOaDeptId(Math.toIntExact(updateObj.getId()));
                if (updateObj.getParentId() == null || updateObj.getParentId() == 0) {
                    jntDept.setOaParentDeptId(0);
                } else {
                    DeptDO entity = this.getDept(updateObj.getParentId());
                    if (entity == null)
                        throw new BusinessException("所选组织部门不存在");
                    if (entity.getType() == DeptTypeEnum.DEPARTMENT) {
                        jntDept.setOaParentDeptId(Math.toIntExact(updateObj.getParentId()));
                        Long parentOrgId = this.getParentOrgId(updateObj.getParentId());
                        jntDept.setOaOrgId(Math.toIntExact(parentOrgId));
                    } else {
                        jntDept.setOaOrgId(Math.toIntExact(entity.getId()));
                    }
                }
                jntDept.setName(updateObj.getName());
                jntDept.setSource(SourceEnum.OA);
                jntDept.setOrderNumber(updateObj.getSort());
                jntBaseDataSyncService.syncDept(jntDept);

            } else if (deptDO.getType() == DeptTypeEnum.DEPARTMENT && updateObj.getType() == DeptTypeEnum.ORGANIZATION) {//部门转组织
                //同步人员
                for (AdminUserDO adminUserDO : adminUserDOS) {
                    InformationDO information = informationMapper.getByUserId(adminUserDO.getId());
                    Integer infoType = information.getInfoType();
                    JntUser jntUser = new JntUser();
                    jntUser.setOperateType(OperateTypeEnum.UPDATE);
                    jntUser.setOaUserId(Math.toIntExact(adminUserDO.getId()));
                    jntUser.setOaOrgId(Math.toIntExact(updateObj.getId()));
                    jntUser.setOaDeptId(null);

                    if (adminUserDO.getPostIds() != null && adminUserDO.getPostIds().size() > 0) {
                        jntUser.setOaPostId(Math.toIntExact(adminUserDO.getPostIds().iterator().next()));
                    }
                    jntUser.setAccount(adminUserDO.getUsername());
                    jntUser.setName(adminUserDO.getNickname());
                    jntUser.setMobile(adminUserDO.getMobile());
                    jntUser.setGender(adminUserDO.getSex() == null ? GenderEnum.UNKNOWN : (adminUserDO.getSex() == 0 ? GenderEnum.MAN : GenderEnum.WOMAN));

                    EmployTypeEnum employTypeEnum = null;
                    if (infoType != 2) {
                        employTypeEnum = EmployTypeEnum.INNER_EMPLOY;
                    } else {
                        employTypeEnum = EmployTypeEnum.OUTER_EMPLOY;
                    }
                    jntUser.setEmployType(employTypeEnum);//设置内外聘
                    jntUser.setSource(SourceEnum.OA);
                    jntBaseDataSyncService.syncUser(jntUser);
                }
                //删除原部门
                JntDept jntDept = new JntDept();
                jntDept.setIds(reqVO.getId() + "");
                jntDept.setOperateType(OperateTypeEnum.DELETE);
                jntBaseDataSyncService.syncDept(jntDept);
                //增加组织
                JntOrg jntOrg = new JntOrg();
                jntOrg.setOperateType(OperateTypeEnum.ADD);
                jntOrg.setOaOrgId(Math.toIntExact(updateObj.getId()));
                if (updateObj.getParentId() == null || updateObj.getParentId() == 0) {
                    jntOrg.setOaParentOrgId(0);
                } else {
                    DeptDO parentOrgOfCompany = this.getParentOrgOfCompany(updateObj.getParentId());
                    jntOrg.setOaParentOrgId(parentOrgOfCompany == null ? 0 : Math.toIntExact(parentOrgOfCompany.getId()));
                }
                jntOrg.setName(updateObj.getName());
                jntOrg.setSource(SourceEnum.OA);
                jntOrg.setOrderNumber(updateObj.getSort());
                jntBaseDataSyncService.syncOrg(jntOrg);
            } else {
                if (reqVO.getType() == DeptTypeEnum.ORGANIZATION) {
                    JntOrg jntOrg = new JntOrg();
                    jntOrg.setOperateType(OperateTypeEnum.UPDATE);
                    jntOrg.setOaOrgId(Math.toIntExact(updateObj.getId()));
                    if (updateObj.getParentId() == null || updateObj.getParentId() == 0) {
                        jntOrg.setOaParentOrgId(0);
                    } else {
                        DeptDO parentOrgOfCompany = this.getParentOrgOfCompany(updateObj.getParentId());
                        jntOrg.setOaParentOrgId(parentOrgOfCompany == null ? 0 : Math.toIntExact(parentOrgOfCompany.getId()));
                    }
                    jntOrg.setName(updateObj.getName());
                    jntOrg.setSource(SourceEnum.OA);
                    jntOrg.setOrderNumber(updateObj.getSort());
                    jntBaseDataSyncService.syncOrg(jntOrg);
                } else {
                    JntDept jntDept = new JntDept();
                    jntDept.setOperateType(OperateTypeEnum.UPDATE);
                    jntDept.setOaDeptId(Math.toIntExact(updateObj.getId()));
                    if (updateObj.getParentId() == null || updateObj.getParentId() == 0) {
                        jntDept.setOaParentDeptId(0);
                    } else {
                        DeptDO entity = this.getDept(updateObj.getParentId());
                        if (entity == null) {
                            throw new BusinessException("所选组织部门不存在");
                        }
                        if (entity.getType() == DeptTypeEnum.DEPARTMENT) {
                            jntDept.setOaParentDeptId(Math.toIntExact(updateObj.getParentId()));
                            Long parentOrgId = this.getParentOrgId(updateObj.getParentId());
                            jntDept.setOaOrgId(Math.toIntExact(parentOrgId));
                        } else {
                            jntDept.setOaOrgId(Math.toIntExact(entity.getId()));
                        }
                    }
                    jntDept.setName(updateObj.getName());
                    jntDept.setSource(SourceEnum.OA);
                    jntDept.setOrderNumber(updateObj.getSort());
                    jntBaseDataSyncService.syncDept(jntDept);
                }
            }
        } catch (Exception e) {
            throw new BusinessException("[JNT]操作失败，" + e.getLocalizedMessage());
        }
    }

    public Long getParentOrgId(Long parentId) {
        if (parentId == 0) {
            return parentId;
        }
        DeptDO dept = this.getDept(parentId);
        if (dept.getType() == DeptTypeEnum.DEPARTMENT) {
            return this.getParentOrgId(dept.getParentId());
        }
        return dept.getId();
    }

    @Transactional
    @Override
    public void deleteDept(Long id, Boolean notSyncJnt) {
        validateDeptExists(id);
        validateDeptChildren(id);
        if (deptMapper.selectCountByParentId(id) > 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_EXITS_CHILDREN);
        }
        DeptDO entity = this.getDept(id);
        List<AdminUserDO> adminUserDOS = userMapper.selectByDeptId(id);
        if (adminUserDOS != null && adminUserDOS.size() > 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_EXISTS_USER);
        } else {
            List<PostDO> postDOS = postMapper.selectList(new LambdaQueryWrapperX<PostDO>().eqIfPresent(PostDO::getOrgId, id));
            if (CollUtil.isNotEmpty(postDOS)) {
                Set<Long> postIdSet = postDOS.stream().map(PostDO::getId).collect(Collectors.toSet());
                postMapper.deleteBatchIds(postIdSet);
            }
            deptMapper.deleteById(id);
            ThreadUtil.execAsync(this::initLocalCache);
//            CompletableFuture.runAsync(() -> this.syncDeleteDept(id, entity));
            if (!notSyncJnt) {
                this.syncDeleteDept(id, entity);
            }
        }

    }

    @Transactional
    public void syncDeleteDept(Long id, DeptDO entity) {
        //同步建能通
        try {
            if (entity.getType() == DeptTypeEnum.ORGANIZATION) {
                JntOrg jntOrg = new JntOrg();
                jntOrg.setIds(id + "");
                jntOrg.setOperateType(OperateTypeEnum.DELETE);
                jntBaseDataSyncService.syncOrg(jntOrg);
            } else {
                JntDept jntDept = new JntDept();
                jntDept.setIds(id + "");
                jntDept.setOperateType(OperateTypeEnum.DELETE);
                jntBaseDataSyncService.syncDept(jntDept);
            }
        } catch (Exception e) {
            throw new BusinessException("[JNT]操作失败，" + e.getLocalizedMessage());
        }
    }

    @Override
    public List<DeptDO> getDeptList(DeptListReqVO reqVO) {
        return deptMapper.selectList(reqVO);
    }

    @Override
    public List<DeptDO> getDeptByDeptName(String deptName) {
        List<DeptDO> deptDOS = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().eqIfPresent(DeptDO::getName, deptName));
        return deptDOS;
    }

    @Override
    public List<DeptDO> getDeptByDeptNames(Set<String> deptNames) {
        if (org.springframework.util.CollectionUtils.isEmpty(deptNames)) {
            return Collections.emptyList();
        }
        return deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>()
                .inIfPresent(DeptDO::getName, deptNames));
    }

    @Override
    public List<DeptDO> getDeptListByParentIdFromCache(Long parentId, boolean recursive) {
        if (parentId == null) {
            return Collections.emptyList();
        }
        List<DeptDO> result = new ArrayList<>();
        getDeptsByParentIdFromCache(result, parentId,
                recursive ? Integer.MAX_VALUE : 1,
                parentDeptCache);
        return result;
    }

    /**
     * 递归获取所有的子部门，添加到 result 结果
     *
     * @param result         结果
     * @param parentId       父编号
     * @param recursiveCount 递归次数
     * @param parentDeptMap  父部门 Map，使用缓存，避免变化
     */
    private void getDeptsByParentIdFromCache(List<DeptDO> result, Long parentId, int recursiveCount,
                                             Multimap<Long, DeptDO> parentDeptMap) {
        if (recursiveCount == 0) {
            return;
        }

        Collection<DeptDO> depts = parentDeptMap.get(parentId);
        if (CollUtil.isEmpty(depts)) {
            return;
        }
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId != null) {
            depts = CollUtil.filterNew(depts, dept -> tenantId.equals(dept.getTenantId()));
        }
        result.addAll(depts);

        depts.forEach(dept -> getDeptsByParentIdFromCache(result, dept.getId(),
                recursiveCount - 1, parentDeptMap));
    }

    private void validateForCreateOrUpdate(Long id, Long parentId, String name, Integer status) {
        validateDeptExists(id);
        validateParentDeptEnable(id, parentId);
        validateDeptNameUnique(id, parentId, name);
        if (status != null && deptMapper.selectById(id).getStatus().equals(0) && status.equals(1)) {
            validateDeptChildren(id);
        }
    }

    private void validateParentDeptEnable(Long id, Long parentId) {
        if (parentId == null || DeptIdEnum.ROOT.getId().equals(parentId)) {
            return;
        }
        if (parentId.equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_PARENT_ERROR);
        }
        DeptDO dept = deptMapper.selectById(parentId);
        if (dept == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_PARENT_NOT_EXITS);
        }
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_ENABLE);
        }
        List<DeptDO> children = getDeptListByParentIdFromCache(id, true);
        if (children.stream().anyMatch(dept1 -> dept1.getId().equals(parentId))) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_PARENT_IS_CHILD);
        }
    }

    private void validateDeptExists(Long id) {
        if (id == null) {
            return;
        }
        DeptDO dept = deptMapper.selectById(id);
        if (dept == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_FOUND);
        }
    }

    private void validateDeptNameUnique(Long id, Long parentId, String name) {
        DeptDO menu = deptMapper.selectByParentIdAndName(parentId, name);
        if (menu == null) {
            return;
        }
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NAME_DUPLICATE);
        }
        if (!menu.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NAME_DUPLICATE);
        }
    }

    private void validateDeptChildren(Long id) {
        List<DeptDO> deptDOS = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().eq(DeptDO::getParentId, id));
        if (!CollUtil.isEmpty(deptDOS)) {
            List<Long> collect = deptDOS.stream().map(DeptDO::getId).collect(Collectors.toList());
            List<DeptDO> deptDOS1 = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().in(DeptDO::getParentId, collect));
            if (!CollUtil.isEmpty(deptDOS1)) {
                deptDOS.addAll(deptDOS1);
                List<Long> collect1 = deptDOS1.stream().map(DeptDO::getId).collect(Collectors.toList());
                boolean hasUser = userMapper.selectObjs(new LambdaQueryWrapperX<AdminUserDO>().in(AdminUserDO::getDeptId, collect1).eq(AdminUserDO::getStatus, 0)).stream().anyMatch(o -> true);
                if (hasUser) {
                    throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_EXISTS_USER_CHILD);
                }
            }
            long count = deptDOS.stream().filter(e -> e.getStatus().equals(0)).count();
            if (count != 0) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_HAS_CHILD);
            }
        }

    }

    @Override
    public List<DeptDO> getDeptList(Collection<Long> ids) {
        return deptMapper.selectBatchIds(ids);
    }

    @Override
    public DeptDO getDept(Long id) {
        return deptMapper.selectById(id);
    }

    @Override
    public void validateDeptList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        Map<Long, DeptDO> deptMap = getDeptMap(ids);
        ids.forEach(id -> {
            DeptDO dept = deptMap.get(id);
            if (dept == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_ENABLE, dept.getName());
            }
        });
    }

    @Override
    public void validateDeptListByDeptName(String deptName) {
        if (deptName == null) {
            return;
        }
        List<DeptDO> deptDOS = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().inIfPresent(DeptDO::getName, deptName));
        Map<String, DeptDO> stringDeptDOMap = CollectionUtils.convertMap(deptDOS, DeptDO::getName);
        DeptDO dept = stringDeptDOMap.get(deptName);
        if (dept == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_FOUND);
        }
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_ENABLE, dept.getName());
        }
        ;
    }
}
