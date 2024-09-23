package com.lh.oa.module.system.service.project;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.project.vo.*;
import com.lh.oa.module.system.convert.project.ProjectConvert;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.project.ProjectDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;
import com.lh.oa.module.system.dal.mysql.project.ProjectMapper;
import com.lh.oa.module.system.dal.mysql.userProject.UserProjectMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.full.entity.jnt.JntProject;
import com.lh.oa.module.system.full.entity.jnt.JntProjectUser;
import com.lh.oa.module.system.full.enums.jnt.*;
import com.lh.oa.module.system.full.service.jnt.JntBaseDataSyncService;
import com.lh.oa.module.system.service.information.InformationService;
import com.lh.oa.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

/**
 * 项目 Service 实现类
 *
 * @author 狗蛋
 */
@Slf4j
@Service
@Validated
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private BaseMapper<ProjectDO> baseMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private UserProjectMapper userProjectMapper;

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private JntBaseDataSyncService jntBaseDataSyncService;

    @Resource
    private InformationService informationService;

    @Override
    public Integer createProject(ProjectCreateReqVO createReqVO) {
        log.info("创建项目，创建人id：{}，项目信息:{}", getLoginUserId(), JsonUtils.toJsonString(createReqVO));
        // 插入
        ProjectDO project = ProjectConvert.INSTANCE.convert(createReqVO);
        UserProjectDO userProjectDO = new UserProjectDO();
        Long loginUserId = getLoginUserId();
        if (loginUserId == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_LOGIN);
        }
        List<ProjectDO> projectDOS = projectMapper.selectList();
        boolean exist = projectDOS.stream().anyMatch(t -> t.getName().equals(project.getName()));
        if (exist) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PROJECT_NAME_IS_EXISTS);
        }

        // 报存项目和项目人员
        transactionTemplate.executeWithoutResult((status) -> {
            projectMapper.insert(project);
            AdminUserDO user = adminUserService.getUser(loginUserId);

            buildProjectInfo(createReqVO, project, userProjectDO, loginUserId, user);
            userProjectMapper.insert(userProjectDO);
        });

        if (createReqVO.getSource() != null && createReqVO.getSource() == ProjectSourceEnum.PMS) {//项目管理平台
            return project.getId();
        }
        this.syncCreateProjectAndUser(project, userProjectDO);
        // 返回
        return project.getId();
    }

    private void buildProjectInfo(ProjectCreateReqVO createReqVO, ProjectDO project, UserProjectDO userProjectDO,
                                  Long loginUserId, AdminUserDO user) {
        userProjectDO.setUserId(loginUserId);
        userProjectDO.setProjectId(project.getId().longValue());
        userProjectDO.setStatus((byte) 1);
        userProjectDO.setProjectName(project.getName());
        userProjectDO.setUserName(user.getNickname());
        userProjectDO.setType(0);
        userProjectDO.setIsRecord(1);
        userProjectDO.setInTime(new Date());
    }

    public void syncCreateProjectAndUser(ProjectDO project, UserProjectDO userProjectDO) {
        //同步建能通(项目)
        try {
            JntProject jntProject = new JntProject();
            BeanUtil.copyProperties(project, jntProject, "id", "effectGraphFileId", "planarGraphFileId");
            jntProject.setOperateType(OperateTypeEnum.ADD);
            jntProject.setSource(SourceEnum.OA);
            jntProject.setOaProjectId(project.getId());

            jntBaseDataSyncService.syncProject(jntProject);
        } catch (Exception e) {
            throw new BusinessException("[JNT]同步失败，请到建能通手动同步，" + e.getLocalizedMessage());
        }

        //同步项目人员
        try {
            JntProjectUser jntProjectUser = new JntProjectUser();
            jntProjectUser.setSource(SourceEnum.OA);
            jntProjectUser.setOperateType(OperateTypeEnum.ADD);
            jntProjectUser.setOaProjectId(userProjectDO.getProjectId() == null || userProjectDO.getProjectId() == 0 ? 0 : Math.toIntExact(userProjectDO.getProjectId()));
            jntProjectUser.setOaUserId(userProjectDO.getUserId() == null || userProjectDO.getUserId() == 0 ? 0 : Math.toIntExact(userProjectDO.getUserId()));
            if (userProjectDO.getType() != null) {
                jntProjectUser.setStaffType(userProjectDO.getType() == 0 ? ProjectUserTypeEnum.MANAGE_STAFF_TYPE : ProjectUserTypeEnum.CONSTRUCT_STAFF_TYPE);
            } else {
                jntProjectUser.setStaffType(ProjectUserTypeEnum.CONSTRUCT_STAFF_TYPE);
            }
            if (userProjectDO.getIsRecord() != null) {
                jntProjectUser.setAttendState(userProjectDO.getIsRecord() == 0 ? ProjectAttendanceStateEnum.YEAH : ProjectAttendanceStateEnum.NONE);
            } else {
                jntProjectUser.setAttendState(ProjectAttendanceStateEnum.NONE);
            }
            InformationDO informationDO = informationService.getInformationByUserId(userProjectDO.getUserId());
            if (informationDO != null && informationDO.getHireDate() != null) {
                jntProjectUser.setEntrantTime((int) DateUtil.date(informationDO.getHireDate()).getTime() / 1000);
            } else {
                jntProjectUser.setEntrantTime((int) DateUtil.date().getTime() / 1000);
            }
            jntBaseDataSyncService.syncUserProject(jntProjectUser);
        } catch (Exception e) {
            throw new BusinessException("[JNT]同步失败，请到建能通手动同步，" + e.getLocalizedMessage());
        }
    }


    @Transactional
    @Override
    public void updateProject(ProjectUpdateReqVO updateReqVO) {
        // 校验存在
        validateProjectExists(updateReqVO.getId());
        // 更新
        ProjectDO updateObj = ProjectConvert.INSTANCE.convert(updateReqVO);
        projectMapper.updateById(updateObj);
        if (updateReqVO.getSource() != null && updateReqVO.getSource() == ProjectSourceEnum.PMS) {//项目管理平台
            return;
        }
//        CompletableFuture.runAsync(() ->
        this.syncUpdateProject(updateReqVO);
//        );
    }

    @Transactional
    public void syncUpdateProject(ProjectUpdateReqVO updateReqVO) {
        //同步建能通
        try {
            JntProject jntProject = new JntProject();
            BeanUtil.copyProperties(updateReqVO, jntProject, "effectGraphFileId", "planarGraphFileId");
            jntProject.setOperateType(OperateTypeEnum.UPDATE);
            jntProject.setSource(SourceEnum.OA);
            jntProject.setOaProjectId(updateReqVO.getId());

            // TODO [Rz Liu]: 2023-09-11 效果图、平面图暂不同步，因为没有


            jntBaseDataSyncService.syncProject(jntProject);
        } catch (Exception e) {
            throw new BusinessException("[JNT]同步失败，请到建能通手动同步，" + e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public void deleteProject(Integer id, Boolean notSyncJnt) {
        // 校验存在
        validateProjectExists(id);
        // 删除
        projectMapper.deleteById(id);
        // 删除项目用户
        userProjectMapper.delete(new LambdaQueryWrapperX<UserProjectDO>().eq(UserProjectDO::getProjectId, id));
//        CompletableFuture.runAsync(() ->  this.syncDeleteProject(id));
        if (notSyncJnt) {
            return;
        }
        this.syncDeleteProject(id);
    }

    @Transactional
    public void syncDeleteProject(Integer id) {
        //同步建能通
        try {
            JntProject jntProject = new JntProject();
            jntProject.setIds(id + "");
            jntProject.setOperateType(OperateTypeEnum.DELETE);
            jntBaseDataSyncService.syncProject(jntProject);
        } catch (Exception e) {
            throw new BusinessException("[JNT]同步失败，请到建能通手动同步，" + e.getLocalizedMessage());
        }
    }

    private void validateProjectExists(Integer id) {
        if (projectMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PROJECT_NOT_EXISTS);
        }
    }

    private void validateProjectExistsByOrgId(Long orgId) {
        List<ProjectDO> ProjectDOS = projectMapper.selectList("org_id", orgId);
        if (ProjectDOS.size() == 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CUSTOMERS_PROJECT_NOT_EXISTS);
        }
    }

    @Override
    public ProjectDO getProject(Integer id) {
        ProjectDO projectDO = projectMapper.selectById(id);
        Long loginUserId = getLoginUserId();
        if ("".equals(loginUserId) || loginUserId == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_LOGIN);
        }
        String isTop = projectDO.getIsTop();
        if (!"".equals(isTop) && isTop != null) {
            String[] top = isTop.split(",");

            // 判断是否存在 登录人ID
            boolean isExist = Arrays.stream(top)
                    .map(Long::parseLong)
                    .anyMatch(element -> element.equals(loginUserId));
            projectDO.setTopped(isExist);
        } else {
            projectDO.setTopped(false);
        }
        return projectDO;
    }

    @Override
    public List<ProjectDO> getProjectList(Collection<Integer> ids) {
        return projectMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ProjectDO> getProjectPage(ProjectPageReqVO pageReqVO) {
        if (ObjectUtils.isNotEmpty(pageReqVO.getUserId())) {
            List<UserProjectDO> userProjectDOS = userProjectMapper.selectList(new LambdaQueryWrapperX<UserProjectDO>().eqIfPresent(UserProjectDO::getUserId, pageReqVO.getUserId()));
            if (ObjectUtils.isNotEmpty(userProjectDOS)) {
                Set<Long> projectIds = userProjectDOS.stream().map(UserProjectDO::getProjectId).collect(Collectors.toSet());
                pageReqVO.setProjectIds(projectIds);
            }
        }
        PageResult<ProjectDO> projectDOPageResult = projectMapper.selectPage(pageReqVO);
        Long loginUserId = getLoginUserId();
        if ("".equals(loginUserId) || loginUserId == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_LOGIN);
        }
        List<ProjectDO> projectDOList = projectDOPageResult.getList();
        List<ProjectDO> sortedList = projectDOList.stream()
                .peek(projectDO -> {
                    String isTop = projectDO.getIsTop();
                    if (!"".equals(isTop) && isTop != null) {
                        String[] top = isTop.split(",");

                        // 判断是否存在 登录人ID
                        boolean isExist = Arrays.stream(top)
                                .map(Long::parseLong)
                                .anyMatch(element -> element.equals(loginUserId));
                        projectDO.setTopped(isExist);
                    } else {
                        projectDO.setTopped(false);
                    }
                })
                .sorted((p1, p2) -> Boolean.compare(p2.getTopped(), p1.getTopped())) // 排序，将 topped=true 的项目移到前面
                .collect(Collectors.toList());
        projectDOPageResult.setList(sortedList);
        return projectDOPageResult;
    }

    @Override
    public List<ProjectDO> getProjectList(ProjectExportReqVO exportReqVO) {
        return projectMapper.selectList(exportReqVO);
    }

    @Override
    public List<ProjectDO> getProjectList() {
        List<UserProjectDO> list = userProjectMapper.selectList(new LambdaQueryWrapperX<UserProjectDO>().eq(UserProjectDO::getUserId, getLoginUserId()).eq(UserProjectDO::getStatus, 1));
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        List<Long> collect = list.stream().map(UserProjectDO::getProjectId).collect(Collectors.toList());
        return projectMapper.selectList(new LambdaQueryWrapperX<ProjectDO>().in(ProjectDO::getId, collect));
    }

    @Override
    public List<ProjectDO> getProjectAll() {
        return projectMapper.selectList();
    }

    @Override
    public void isTopProject(Integer id) {
        // 校验存在
        validateProjectExists(id);
        //获取登录信息
        Long loginUserId = getLoginUserId();
        ProjectDO projectDO = projectMapper.selectById(id);
        String isTop = projectDO.getIsTop();
        if (!"".equals(isTop) && isTop != null) {
            String[] top = isTop.split(",");
            // 判断是否存在 登录人ID
            boolean isExist = Arrays.stream(top)
                    .map(Long::parseLong)
                    .anyMatch(element -> element.equals(loginUserId));
            String[] updatedTop;
            if (isExist) {
                updatedTop = Arrays.stream(top)
                        .map(Long::parseLong)
                        .filter(element -> !element.equals(loginUserId))
                        .map(String::valueOf)
                        .toArray(String[]::new);
            } else {
                updatedTop = Arrays.copyOf(top, top.length + 1);
                updatedTop[top.length] = String.valueOf(loginUserId); // 结尾添加loginId
            }
            if (!isExist) {
                updatedTop[top.length] = loginUserId.toString(); // 手动添加
            }
            String endTop = String.join(",", updatedTop);
            projectDO.setIsTop(endTop);
        } else {
            projectDO.setIsTop(loginUserId.toString());
        }
        projectMapper.updateById(projectDO);
    }

    @Transactional
    @Override
    public void disabledProject(Integer id) {
        validateProjectExists(id);
        ProjectDO projectDO = projectMapper.selectById(id);

        Boolean isDisabled = projectDO.getIsDisabled();
        if ("".equals(isDisabled) && isDisabled == null) {
            throw new RuntimeException("系统异常,禁用有问题");
        }
        if (isDisabled) {
            projectDO.setIsDisabled(false);
        } else {
            projectDO.setIsDisabled(true);
        }
        projectMapper.updateById(projectDO);
    }

    @Override
    public List<ProjectDO> getProjectByOrgId(Long orgId) {
        List<ProjectDO> ProjectDOS = projectMapper.selectList("org_id", orgId);
        if (ProjectDOS.size() == 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CUSTOMERS_PROJECT_NOT_EXISTS);
        }
        return ProjectDOS;
    }

    @Override
    public Map<Long, String> getProjectsByUsrId(Long userId) {
        List<UserProjectDO> userProjectDOS = userProjectMapper.selectList(UserProjectDO::getUserId, userId);
        if (userProjectDOS == null) {
            return null;
        }
        Map<Long, String> map = new HashMap<>();
        userProjectDOS.stream().forEach(userProjectDO -> {
            ProjectDO projectDO = baseMapper.selectById(userProjectDO.getProjectId());
            if (projectDO != null) {
                map.put(projectDO.getId().longValue(), projectDO.getName());
            }
        });
        return map;
    }

    @Override
    public Map<Long, ProjectDO> getProjectMap(Set<Long> projectIds) {
        if (CollectionUtils.isEmpty(projectIds)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectBatchIds(projectIds)
                .stream()
                .collect(Collectors.toMap(p -> (long) p.getId(), Function.identity()));
    }

    @Override
    public List<JntWarehouseProjectVO> getWarehouseProject() {
        return jntBaseDataSyncService.getWarehouseProject();
    }

    @Override
    public List<JntMaterialVO> getWarehouseMaterialByProject(Long projectId) {
        return jntBaseDataSyncService.getWarehouseMaterialByProject(projectId);
    }

    @Override
    public List<JntWarehouseVO> getWarehouseByProjectId(Long projectId) {
        return jntBaseDataSyncService.getWarehouseByProjectId(projectId);
    }
}
