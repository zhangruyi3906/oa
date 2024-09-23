package com.lh.oa.module.system.service.userProject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.redis.template.RedisLockTemplate;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserPageItemRespVO;
import com.lh.oa.module.system.controller.admin.userProject.param.UserProjectRuleSameRelationParam;
import com.lh.oa.module.system.controller.admin.userProject.vo.*;
import com.lh.oa.module.system.convert.userProject.UserProjectConvert;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;
import com.lh.oa.module.system.dal.mysql.userProject.UserProjectMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.full.entity.jnt.JntProjectUser;
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.ProjectAttendanceStateEnum;
import com.lh.oa.module.system.full.enums.jnt.ProjectUserTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.SourceEnum;
import com.lh.oa.module.system.full.service.attendance.UserProjectRuleSameRelationService;
import com.lh.oa.module.system.full.service.jnt.JntBaseDataSyncService;
import com.lh.oa.module.system.service.dept.DeptService;
import com.lh.oa.module.system.service.information.InformationService;
import com.lh.oa.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lh.oa.module.system.full.enums.jnt.ProjectSourceEnum.PMS;

/**
 * 人员项目 Service 实现类
 *
 * @author
 */
@Service
@Validated
@Slf4j
public class UserProjectServiceImpl implements UserProjectService {

    @Resource
    private UserProjectMapper userProjectMapper;

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private InformationService informationService;

    @Resource
    private JntBaseDataSyncService jntBaseDataSyncService;

    @Resource
    private DeptService deptService;

    @Resource
    private UserProjectRuleSameRelationService userProjectRuleSameRelationService;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private RedisLockTemplate redisLockTemplate;

    @Transactional
    @Override
    public Long createUserProject(UserProjectCreateReqVO createReqVO) {
        String key = "USER_PROJECT_CREATE" + ":" + createReqVO.getProjectId() + "-" + createReqVO.getUserId();
        List<UserProjectDO> list = new ArrayList<>();
        redisLockTemplate.tryLock(key, () -> {
            // 插入
            List<Long> collect = userProjectMapper.selectList(new LambdaQueryWrapperX<UserProjectDO>().eqIfPresent(UserProjectDO::getProjectId, createReqVO.getProjectId()).eqIfPresent(UserProjectDO::getStatus, 1)).stream().map(UserProjectDO::getUserId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)) {
                createReqVO.getList().removeIf(user -> collect.contains(user.getUserId()));
            }
            StringJoiner errorMsg = new StringJoiner(",");
            createReqVO.getList().forEach(s -> {
                UserProjectDO.UserProjectDOBuilder userProjectDO = UserProjectDO.builder();
                userProjectDO.userId(s.getUserId());
                userProjectDO.projectId(createReqVO.getProjectId());
                userProjectDO.status((byte) 1);
                userProjectDO.projectName(createReqVO.getProjectName());
                userProjectDO.userName(s.getUserName());
                userProjectDO.isRecord(createReqVO.getIsRecord());
                userProjectDO.type(createReqVO.getType());
                InformationDO information = informationService.getInformationByUserId(s.getUserId());
                if (information == null) {
                    return;
                }
                if (ObjectUtils.isEmpty(information.getHireDate())) {
                    throw ServiceExceptionUtil.exception(ErrorCodeConstants.HIRE_DATE_NOT_EXISTS);
                }
                Date hireDate = Date.from(information.getHireDate().atZone(ZoneId.systemDefault()).toInstant());
                userProjectDO.inTime(createReqVO.getInTime());
                if (hireDate.after(createReqVO.getInTime())) {
                    createReqVO.setInTime(hireDate);
//                errorMsg.add(s.getUserName());
                }
                list.add(userProjectDO.build());
            });
            if (errorMsg.length() > 0) {
                throw new BusinessException(errorMsg + "的入职时间大于进场时间，未同步添加进该项目，请先处理账户数据");
            }
            userProjectMapper.insertBatch(list);
            userProjectRuleSameRelationService.resetRuleSameRelation(new UserProjectRuleSameRelationParam(list.stream().map(up -> up.getUserId().intValue()).collect(Collectors.toSet()), null));
        });

//        CompletableFuture.runAsync(() -> this.syncCreateProjectUser(list));

        if (PMS == createReqVO.getSource()) {
            return (long) list.size();
        }
        this.syncCreateProjectUser(list);
        // 返回
        return (long) list.size();
    }

    @Transactional
    public void syncCreateProjectUser(List<UserProjectDO> list) {
        //同步建能通
        try {
            for (UserProjectDO up : list) {
                JntProjectUser jntProjectUser = new JntProjectUser();
                jntProjectUser.setSource(SourceEnum.OA);
                jntProjectUser.setOperateType(OperateTypeEnum.ADD);
                jntProjectUser.setOaProjectId(up.getProjectId() == null || up.getProjectId() == 0 ? 0 : Math.toIntExact(up.getProjectId()));
                jntProjectUser.setOaUserId(up.getUserId() == null || up.getUserId() == 0 ? 0 : Math.toIntExact(up.getUserId()));
                if (up.getType() != null) {
                    jntProjectUser.setStaffType(up.getType() == 0 ? ProjectUserTypeEnum.MANAGE_STAFF_TYPE : ProjectUserTypeEnum.CONSTRUCT_STAFF_TYPE);
                } else {
                    jntProjectUser.setStaffType(ProjectUserTypeEnum.CONSTRUCT_STAFF_TYPE);
                }
                if (up.getIsRecord() != null) {
                    jntProjectUser.setAttendState(up.getIsRecord() == 0 ? ProjectAttendanceStateEnum.YEAH : ProjectAttendanceStateEnum.NONE);
                } else {
                    jntProjectUser.setAttendState(ProjectAttendanceStateEnum.NONE);
                }
                Date inTime = up.getInTime();
                if (inTime != null) {
                    jntProjectUser.setEntrantTime((int) (DateUtil.date(inTime).getTime() / 1000));
                } else {
                    jntProjectUser.setEntrantTime((int) (DateUtil.date(new Date()).getTime() / 1000));//TODO 线上用户和员工是分别添加的，可能用户添加但是员工未添加，防止未添加员工时同步用户
                }
                jntBaseDataSyncService.syncUserProject(jntProjectUser);
            }
        } catch (Exception e) {
            throw new BusinessException("[JNT]同步失败，请到建能通手动同步，" + e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public void updateUserProject(UserProjectUpdateReqVO updateReqVO) {
        validateUserProjectExists(updateReqVO.getId());
        Date leaveTime = updateReqVO.getLeaveTime();

        UserProjectDO userProject = this.getUserProject(updateReqVO.getId());
        updateReqVO.setProjectId(userProject.getProjectId());
        updateReqVO.setUserId(userProject.getUserId());
        Date inTime = updateReqVO.getInTime();
        InformationDO information = informationService.getInformationByUserId(updateReqVO.getUserId());
        if (ObjectUtils.isEmpty(information.getHireDate())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.HIRE_DATE_NOT_EXISTS);
        }
        Date hireDate = Date.from(information.getHireDate().atZone(ZoneId.systemDefault()).toInstant());
        if (hireDate.after(inTime)) {
            // 入职时间在进场时间之后 则将进场时间设置为入职时间
            updateReqVO.setInTime(hireDate);
//            throw ServiceExceptionUtil.exception(ErrorCodeConstants.IN_TIME_BEFORE_HIRE_DATE);
        }
        if (ObjectUtil.isNotEmpty(leaveTime)) {
            updateReqVO.setStatus((byte) 0);
            if (inTime.after(leaveTime)) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.LEAVE_TIME_BEFORE_IN_TIME);
            }
        } else {
            updateReqVO.setStatus((byte) 1);
            updateReqVO.setLeaveTime(null);
        }
        UserProjectDO updateObj = UserProjectConvert.INSTANCE.convert(updateReqVO);
        userProjectMapper.updateById(updateObj);
        // OA的项目人员更新接口实际上不会更新项目id，所以这里不需要同步修改项目考勤规则相似关系表
//        CompletableFuture.runAsync(() -> this.syncUpdateUserProject(updateObj));
        if (PMS == updateReqVO.getSource()) {
            return;
        }
        this.syncUpdateUserProject(updateObj);
    }

    @Transactional
    public void syncUpdateUserProject(UserProjectDO updateObj) {
        //同步建能通
        try {
            JntProjectUser jntProjectUser = new JntProjectUser();
            jntProjectUser.setSource(SourceEnum.OA);
            jntProjectUser.setOperateType(OperateTypeEnum.UPDATE);
            jntProjectUser.setOaProjectId(updateObj.getProjectId() == null || updateObj.getProjectId() == 0 ? 0 : Math.toIntExact(updateObj.getProjectId()));
            jntProjectUser.setOaUserId(updateObj.getUserId() == null || updateObj.getUserId() == 0 ? 0 : Math.toIntExact(updateObj.getUserId()));
            if (updateObj.getType() != null) {
                jntProjectUser.setStaffType(updateObj.getType() == 0 ? ProjectUserTypeEnum.MANAGE_STAFF_TYPE : ProjectUserTypeEnum.CONSTRUCT_STAFF_TYPE);
            } else {
                jntProjectUser.setStaffType(ProjectUserTypeEnum.CONSTRUCT_STAFF_TYPE);
            }
            if (updateObj.getIsRecord() != null) {
                jntProjectUser.setAttendState(updateObj.getIsRecord() == 0 ? ProjectAttendanceStateEnum.YEAH : ProjectAttendanceStateEnum.NONE);
            } else {
                jntProjectUser.setAttendState(ProjectAttendanceStateEnum.NONE);
            }
            Date inTime = updateObj.getInTime();
            if (inTime != null) {
                jntProjectUser.setEntrantTime((int) (DateUtil.date(inTime).getTime() / 1000));
            } else {
                jntProjectUser.setEntrantTime((int) (DateUtil.date(new Date()).getTime() / 1000));//TODO 线上用户和员工是分别添加的，可能用户添加但是员工未添加，防止未添加员工时同步用户
            }
            jntBaseDataSyncService.syncUserProject(jntProjectUser);
        } catch (Exception e) {
            throw new BusinessException("[JNT]同步失败，请到建能通手动同步，" + e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public void deleteUserProject(Long id, boolean sync) {
        // 校验存在
        validateUserProjectExists(id);
        UserProjectDO userProjectDO = userProjectMapper.selectById(id);
        // 删除
        userProjectMapper.deleteById(id);
//        CompletableFuture.runAsync(() -> this.syncDeleteUserProject(userProjectDO));
        userProjectRuleSameRelationService.resetRuleSameRelation(new UserProjectRuleSameRelationParam(Collections.singleton(userProjectDO.getUserId().intValue()), null));
        if (sync) {
            this.syncDeleteUserProject(userProjectDO);
        }
    }

    @Transactional
    @Override
    public void deleteUserProject(Long id) {
        // 校验存在
        validateUserProjectExists(id);
        UserProjectDO userProjectDO = userProjectMapper.selectById(id);
        // 删除
        userProjectMapper.deleteById(id);
//        CompletableFuture.runAsync(() -> this.syncDeleteUserProject(userProjectDO));
        userProjectRuleSameRelationService.resetRuleSameRelation(new UserProjectRuleSameRelationParam(Collections.singleton(userProjectDO.getUserId().intValue()), null));
        this.syncDeleteUserProject(userProjectDO);
    }

    @Transactional
    public void syncDeleteUserProject(UserProjectDO userProjectDO) {
        //同步建能通
        try {
            JntProjectUser jntProjectUser = new JntProjectUser();
            jntProjectUser.setSource(SourceEnum.OA);
            jntProjectUser.setOperateType(OperateTypeEnum.DELETE);
            jntProjectUser.setOaProjectId(userProjectDO.getProjectId() == null || userProjectDO.getProjectId() == 0 ? 0 : Math.toIntExact(userProjectDO.getProjectId()));
            jntProjectUser.setOaUserId(userProjectDO.getUserId() == null || userProjectDO.getUserId() == 0 ? 0 : Math.toIntExact(userProjectDO.getUserId()));
            jntBaseDataSyncService.syncUserProject(jntProjectUser);
        } catch (Exception e) {
            throw new BusinessException("[JNT]同步失败，请到建能通手动同步，" + e.getLocalizedMessage());
        }
    }

    private void validateUserProjectExists(Long id) {
        if (userProjectMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_PROJECT_NOT_EXISTS);
        }
    }

    @Override
    public UserProjectDO getUserProject(Long id) {
        UserProjectDO userProjectDO = userProjectMapper.selectById(id);
        AdminUserDO adminUserDO = adminUserService.getUser(userProjectDO.getUserId());
        InformationDO informationDO = informationService.getInformationByUserId(userProjectDO.getUserId());
        DeptDO dept = deptService.getDept(adminUserDO.getDeptId());
        if (ObjectUtils.isNotEmpty(dept))
            informationDO.setDept(new UserPageItemRespVO.Dept(dept.getId(), dept.getName()));
        userProjectDO.setAdminUserDO(adminUserDO);
        userProjectDO.setInformationDO(informationDO);
        userProjectDO.setUserName(adminUserDO.getNickname());
        return userProjectDO;
    }

    @Override
    public List<UserProjectDO> getUserProjectListByUserIds(Collection<Long> ids) {
        return userProjectMapper.selectList(new LambdaQueryWrapperX<UserProjectDO>().inIfPresent(UserProjectDO::getUserId, ids));
    }

    @Override
    public PageResult<UserProjectDO> getUserProjectPage(UserProjectPageReqVO pageReqVO) {
        PageResult<UserProjectDO> page = userProjectMapper.selectPage(pageReqVO);
        if (page == null) {
            return null;
        }
        List<UserProjectDO> list = page.getList();
        if (list == null) {
            return null;
        }
        for (UserProjectDO userProjectDO : list) {
            AdminUserDO adminUserDO = adminUserService.getUser(userProjectDO.getUserId());
            Long deptId = adminUserDO.getDeptId();
            InformationDO informationDO = informationService.getInformationByUserId(userProjectDO.getUserId());
            if (deptId != null) {
                DeptDO dept = deptService.getDept(deptId);
                if (ObjectUtils.isNotEmpty(dept)) {
                    informationDO.setDept(new UserPageItemRespVO.Dept(dept.getId(), dept.getName()));
                }
            }
            userProjectDO.setAdminUserDO(adminUserDO);
            userProjectDO.setInformationDO(informationDO);
            userProjectDO.setUserName(adminUserDO.getNickname());
        }
        page.setList(list);
        return page;
    }

    @Override
    public List<UserProjectDO> getUserProjectListByUserIds(UserProjectExportReqVO exportReqVO) {
        return userProjectMapper.selectList(exportReqVO);
    }

    @Override
    public Boolean getProject(Long id) {
        long count = userProjectMapper.selectCount(new LambdaQueryWrapperX<UserProjectDO>().eqIfPresent(UserProjectDO::getUserId, id));
        return count > 0;
    }

    @Override
    public List<UserProjectUserVO> getProjectUser(Long projectId) {
        List<UserProjectDO> userProjectDOList = userProjectMapper
                .selectList(new LambdaQueryWrapperX<UserProjectDO>().eqIfPresent(UserProjectDO::getProjectId, projectId));
        List<UserProjectUserVO> userProjectUserVOList = new ArrayList<>();
        userProjectDOList.forEach(t -> {
            UserProjectUserVO userProjectUserVO = new UserProjectUserVO();
            userProjectUserVO.setUserId(t.getUserId());
            userProjectUserVO.setUserName(t.getUserName());
            userProjectUserVOList.add(userProjectUserVO);
        });

        return userProjectUserVOList;
    }

    @Override
    public void updateUserProjectFromPMS(UserProjectBaseVO baseVO) {
        UserProjectUpdateReqVO userProjectBaseVO = new UserProjectUpdateReqVO();
        Long userProjectIdByUserAndProject = this.getUserProjectIdByUserAndProject(baseVO.getUserId(), baseVO.getProjectId());
        if (userProjectIdByUserAndProject == null || userProjectIdByUserAndProject == 0) {
            UserProjectCreateReqVO userProjectCreateReqVO = new UserProjectCreateReqVO();
            BeanUtils.copyProperties(baseVO, userProjectCreateReqVO);
            this.createUserProject(userProjectCreateReqVO);
            return;
        }
        BeanUtils.copyProperties(baseVO, userProjectBaseVO);
        userProjectBaseVO.setId(userProjectIdByUserAndProject);
        this.updateUserProject(userProjectBaseVO);
    }

    @Override
    public void deleteUserProjectFromPMS(Long userId, Long projectId) {
        Long id = this.getUserProjectIdByUserAndProject(userId, projectId);
        this.deleteUserProject(id, false);
    }

    @Override
    public Boolean syncBatchFromPMS(List<UserProjectCreateReqVO> userProjectCreateReqVOList) {
        List<UserProjectDO> list = new ArrayList<>();
        // 插入
        StringJoiner errorMsg = new StringJoiner(",");
        Set<Long> userIdSet = new HashSet<>();
        userProjectCreateReqVOList.forEach(userProjectCreateReqVO -> {
            List<UserProjectCreateReqVO.User> userList = userProjectCreateReqVO.getList();
            if (CollectionUtils.isEmpty(userList)) {
                return;
            }
            userList.forEach(user -> {
                userIdSet.add(user.getUserId());
            });
        });
        Map<Long, InformationDO> userInfoemationMap = informationService.selectListByUserIds(userIdSet).stream().collect(Collectors.toMap(InformationDO::getUserId, Function.identity()));
        for (UserProjectCreateReqVO createReqVO : userProjectCreateReqVOList) {
            createReqVO.getList().forEach(s -> {
                UserProjectDO.UserProjectDOBuilder userProjectDO = UserProjectDO.builder();
                userProjectDO.userId(s.getUserId());
                userProjectDO.projectId(createReqVO.getProjectId());
                userProjectDO.status((byte) 1);
                userProjectDO.projectName(createReqVO.getProjectName());
                userProjectDO.userName(s.getUserName());
                userProjectDO.isRecord(createReqVO.getIsRecord());
                userProjectDO.type(createReqVO.getType());
                InformationDO information = userInfoemationMap.get(s.getUserId());
                ;
                if (information == null) {
                    return;
                }
                if (ObjectUtils.isEmpty(information.getHireDate())) {
                    throw ServiceExceptionUtil.exception(ErrorCodeConstants.HIRE_DATE_NOT_EXISTS);
                }
                Date hireDate = Date.from(information.getHireDate().atZone(ZoneId.systemDefault()).toInstant());
                userProjectDO.inTime(createReqVO.getInTime());
                if (hireDate.after(createReqVO.getInTime())) {
                    createReqVO.setInTime(hireDate);
                }
                list.add(userProjectDO.build());
            });
        }
        if (errorMsg.length() > 0) {
            throw new BusinessException(errorMsg + "的入职时间大于进场时间，未同步添加进该项目，请先处理账户数据");
        }
        transactionTemplate.executeWithoutResult(status -> {
            userProjectMapper.insertBatch(list);
            userProjectRuleSameRelationService.resetRuleSameRelation(new UserProjectRuleSameRelationParam(list.stream().map(up -> up.getUserId().intValue()).collect(Collectors.toSet()), null));

        });
        return true;

    }


    /**
     * 根据人员和项目查询关联id
     */
    private Long getUserProjectIdByUserAndProject(Long userId, Long projectId) {
        UserProjectDO userProjectDO = userProjectMapper.selectOne(new LambdaQueryWrapperX<UserProjectDO>().eq(UserProjectDO::getProjectId, projectId).eq(UserProjectDO::getUserId, userId));
        return userProjectDO == null ? 0L : userProjectDO.getId();
    }


    @Override
    public List<UserProjectDO> getAllUserProjectSimpleList(UserProjectPageReqVO userProjectPageReqVO) {
        LambdaQueryWrapperX<UserProjectDO> eq = new LambdaQueryWrapperX<UserProjectDO>()
                .eqIfPresent(UserProjectDO::getUserId, userProjectPageReqVO.getUserId())
                .eqIfPresent(UserProjectDO::getProjectId, userProjectPageReqVO.getProjectId());
        return userProjectMapper.selectPage(userProjectPageReqVO, eq).getList();
    }

}
