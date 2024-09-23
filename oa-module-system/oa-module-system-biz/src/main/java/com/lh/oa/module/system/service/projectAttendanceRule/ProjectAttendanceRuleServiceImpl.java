package com.lh.oa.module.system.service.projectAttendanceRule;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.*;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.*;
import com.lh.oa.module.system.convert.projectAttendanceRule.ProjectAttendanceRuleConvert;
import com.lh.oa.module.system.dal.dataobject.projectAttendanceRule.ProjectAttendanceRuleDO;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;
import com.lh.oa.module.system.dal.mysql.projectAttendanceRule.ProjectAttendanceRuleMapper;
import com.lh.oa.module.system.dal.mysql.userProject.UserProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.lh.oa.module.system.enums.ErrorCodeConstants.PROJECT_ATTENDANCE_RULE_IS_EXISTS;
import static com.lh.oa.module.system.enums.ErrorCodeConstants.PROJECT_ATTENDANCE_RULE_NOT_EXISTS;

/**
 * 打卡规则（项目） Service 实现类
 *
 * @author
 */
@Service
@Validated
public class ProjectAttendanceRuleServiceImpl implements ProjectAttendanceRuleService {

    @Resource
    private ProjectAttendanceRuleMapper projectAttendanceRuleMapper;
    @Resource
    private UserProjectMapper mapper;
    @Override
    public Long createProjectAttendanceRule(ProjectAttendanceRuleListBaseVO createReqVO) {
        List<ProjectAttendanceRuleDO> exchange = exchange(createReqVO);
        List<ProjectAttendanceRuleDO> ruleDOS = projectAttendanceRuleMapper.selectList(new LambdaQueryWrapperX<ProjectAttendanceRuleDO>().eq(ProjectAttendanceRuleDO::getProjectId, createReqVO.getProjectId()));
        if (CollUtil.isNotEmpty(ruleDOS)) {
            throw exception(PROJECT_ATTENDANCE_RULE_IS_EXISTS);
        }
        projectAttendanceRuleMapper.insertBatch(exchange);
        return (long) exchange.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProjectAttendanceRule(ProjectAttendanceRuleListBaseVO updateReqVO) {
        validateProjectAttendanceRuleExists(updateReqVO.getProjectId());
        List<ProjectAttendanceRuleDO> exchange = exchange(updateReqVO);
        projectAttendanceRuleMapper.delete(new LambdaQueryWrapperX<ProjectAttendanceRuleDO>().eq(ProjectAttendanceRuleDO::getProjectId, updateReqVO.getProjectId()));
        projectAttendanceRuleMapper.insertBatch(exchange);
    }

    @Override
    public void deleteProjectAttendanceRule(Long id) {
        validateProjectAttendanceRuleExists(id);
        projectAttendanceRuleMapper.delete(new LambdaQueryWrapperX<ProjectAttendanceRuleDO>().eq(ProjectAttendanceRuleDO::getProjectId, id));
    }


    private void validateProjectAttendanceRuleExists(Long id) {
        List<ProjectAttendanceRuleDO> ruleDOS = projectAttendanceRuleMapper.selectList(new LambdaQueryWrapperX<ProjectAttendanceRuleDO>().eq(ProjectAttendanceRuleDO::getProjectId, id));
        if (CollUtil.isEmpty(ruleDOS)) {
            throw exception(PROJECT_ATTENDANCE_RULE_NOT_EXISTS);
        }
    }

    @Override
    public ProjectAttendanceRuleListBaseVO getProjectAttendanceRule(Long id) {
        List<RuleVO> ruleVOS = new ArrayList<>();
        List<ProjectAttendanceRuleDO> ruleDOS = projectAttendanceRuleMapper.selectList(new LambdaQueryWrapperX<ProjectAttendanceRuleDO>().eq(ProjectAttendanceRuleDO::getProjectId, id));
        List<ProjectAttendanceRuleRespVO> list = ProjectAttendanceRuleConvert.INSTANCE.convertList(ruleDOS);
        list.forEach(n -> {
            RuleVO ruleVO = new RuleVO();
            ruleVO.setPunchRadius(n.getPunchRadius());
            ruleVO.setPunchName(n.getPunchName());
            ruleVO.setLatiLong(n.getLatiLong());
            ruleVOS.add(ruleVO);
        });
        ProjectAttendanceRuleListBaseVO dos = new ProjectAttendanceRuleListBaseVO();
        dos.setList(ruleVOS);
        dos.setProjectId(id);
        dos.setProjectName(list.get(0).getProjectName());
        return dos;
    }

    @Override
    public List<ProjectAttendanceRuleDO> getProjectAttendanceRuleList(Collection<Long> ids) {
        return projectAttendanceRuleMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ProjectAttendanceRuleListBaseVO> getProjectAttendanceRulePage(ProjectAttendanceRuleListPageBaseVO pageReqVO) {
        ProjectAttendanceRuleExportReqVO reqVO = new ProjectAttendanceRuleExportReqVO();
        List<UserProjectDO> list1 = mapper.selectList(new LambdaQueryWrapperX<UserProjectDO>().eq(UserProjectDO::getUserId, getLoginUserId()).eq(UserProjectDO::getStatus, 1));
        if (CollUtil.isEmpty(list1)){
            return null;
        }
        List<Long> collect1 = list1.stream().map(UserProjectDO::getProjectId).collect(Collectors.toList());
        List<ProjectAttendanceRuleDO> ruleDOS = projectAttendanceRuleMapper.selectList(collect1,reqVO);
        List<ProjectAttendanceRuleRespVO> list = ProjectAttendanceRuleConvert.INSTANCE.convertList(ruleDOS);
        Map<Long, List<ProjectAttendanceRuleRespVO>> collect = list.stream()
                .collect(Collectors.groupingBy(ProjectAttendanceRuleRespVO::getProjectId));

        List<ProjectAttendanceRuleListBaseVO> baseVOS = new ArrayList<>();
        for (Map.Entry<Long, List<ProjectAttendanceRuleRespVO>> entry : collect.entrySet()) {
            Long key = entry.getKey();
            List<ProjectAttendanceRuleRespVO> value = entry.getValue();
            ProjectAttendanceRuleListBaseVO projectAttendanceRuleListBaseVO = new ProjectAttendanceRuleListBaseVO();
            projectAttendanceRuleListBaseVO.setProjectId(key);
            projectAttendanceRuleListBaseVO.setProjectName(value.get(0).getProjectName());
            projectAttendanceRuleListBaseVO.setPunchTypeName(value.get(0).getPunchTypeName());
            projectAttendanceRuleListBaseVO.setPunchType(value.get(0).getPunchType());
            projectAttendanceRuleListBaseVO.setAllopatricStatus(value.get(0).getAllopatricStatus());
            projectAttendanceRuleListBaseVO.setSyncHolidays(value.get(0).getSyncHolidays());
            projectAttendanceRuleListBaseVO.setWorkDays(value.get(0).getWorkDays());
            Optional<LocalTime> endNextDayOptional = Optional.ofNullable(value.get(0).getEndNextDay());
            Optional<LocalTime> flexibleCheckInEndOptional = Optional.ofNullable(value.get(0).getFlexibleCheckInEnd());
            Optional<LocalTime> flexibleCheckInStartOptional = Optional.ofNullable(value.get(0).getFlexibleCheckInStart());
            LocalTime endNextDay = endNextDayOptional.orElse(LocalTime.of(0, 0));
            LocalTime flexibleCheckInEnd = flexibleCheckInEndOptional.orElse(LocalTime.of(0, 0));
            LocalTime flexibleCheckInStart = flexibleCheckInStartOptional.orElse(LocalTime.of(0, 0));
            projectAttendanceRuleListBaseVO.setEndNextDay(endNextDay);
            projectAttendanceRuleListBaseVO.setFlexibleCheckInEnd(flexibleCheckInEnd);
            projectAttendanceRuleListBaseVO.setFlexibleCheckInStart(flexibleCheckInStart);
            List<RuleVO> ruleVOS = new ArrayList<>();
            for (ProjectAttendanceRuleRespVO n : value) {
                RuleVO ruleVO = new RuleVO();
                ruleVO.setPunchRadius(n.getPunchRadius());
                ruleVO.setPunchName(n.getPunchName());
                ruleVO.setLatiLong(n.getLatiLong());
                ruleVOS.add(ruleVO);
            }
            projectAttendanceRuleListBaseVO.setList(ruleVOS);
            baseVOS.add(projectAttendanceRuleListBaseVO);
        }
        int fromIndex = (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize();
        int toIndex = Math.min(fromIndex + pageReqVO.getPageSize(), baseVOS.size());
        List<ProjectAttendanceRuleListBaseVO> vos = baseVOS.subList(fromIndex, toIndex);
        return new PageResult<>(vos, (long) baseVOS.size());
    }

    @Override
    public List<ProjectAttendanceRuleDO> getProjectAttendanceRuleList(ProjectAttendanceRuleExportReqVO exportReqVO) {
        return projectAttendanceRuleMapper.selectList(new ArrayList<>(),exportReqVO);
    }

    private List<ProjectAttendanceRuleDO> exchange(ProjectAttendanceRuleListBaseVO createReqVO) {
        List<ProjectAttendanceRuleCreateReqVO> list = new ArrayList<>();
        createReqVO.getList().forEach(s -> {
            ProjectAttendanceRuleCreateReqVO projectAttendanceRuleBaseVO = new ProjectAttendanceRuleCreateReqVO();
            projectAttendanceRuleBaseVO.setProjectId(createReqVO.getProjectId());
            projectAttendanceRuleBaseVO.setProjectName(createReqVO.getProjectName());
            projectAttendanceRuleBaseVO.setPunchName(s.getPunchName());
            projectAttendanceRuleBaseVO.setLatiLong(s.getLatiLong());
            projectAttendanceRuleBaseVO.setPunchRadius(s.getPunchRadius());
            projectAttendanceRuleBaseVO.setPunchType(createReqVO.getPunchType());
            projectAttendanceRuleBaseVO.setPunchTypeName(createReqVO.getPunchTypeName());
            projectAttendanceRuleBaseVO.setAllopatricStatus(createReqVO.getAllopatricStatus());
            projectAttendanceRuleBaseVO.setSyncHolidays(createReqVO.getSyncHolidays());
            projectAttendanceRuleBaseVO.setWorkDays(createReqVO.getWorkDays());
            projectAttendanceRuleBaseVO.setEndNextDay(createReqVO.getEndNextDay());
            projectAttendanceRuleBaseVO.setFlexibleCheckInStart(createReqVO.getFlexibleCheckInStart());
            projectAttendanceRuleBaseVO.setFlexibleCheckInEnd(createReqVO.getFlexibleCheckInEnd());
            list.add(projectAttendanceRuleBaseVO);
        });
        return ProjectAttendanceRuleConvert.INSTANCE.convertList03(list);
    }

}
