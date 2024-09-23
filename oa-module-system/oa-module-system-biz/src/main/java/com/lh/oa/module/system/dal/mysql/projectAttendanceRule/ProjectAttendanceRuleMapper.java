package com.lh.oa.module.system.dal.mysql.projectAttendanceRule;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.ProjectAttendanceRuleExportReqVO;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.ProjectAttendanceRulePageReqVO;
import com.lh.oa.module.system.dal.dataobject.projectAttendanceRule.ProjectAttendanceRuleDO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.*;

import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

/**
 * 打卡规则（项目） Mapper
 *
 * @author
 */
@Mapper
public interface ProjectAttendanceRuleMapper extends BaseMapperX<ProjectAttendanceRuleDO> {

    default PageResult<ProjectAttendanceRuleDO> selectPage(ProjectAttendanceRulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProjectAttendanceRuleDO>()
                .likeIfPresent(ProjectAttendanceRuleDO::getPunchName, reqVO.getPunchName())
                .likeIfPresent(ProjectAttendanceRuleDO::getProjectName, reqVO.getProjectName())
                .eqIfPresent(ProjectAttendanceRuleDO::getProjectId, reqVO.getProjectId())
                .likeIfPresent(ProjectAttendanceRuleDO::getPunchRadius, reqVO.getPunchRadius())
                .likeIfPresent(ProjectAttendanceRuleDO::getLatiLong, reqVO.getLatiLong())
                .or(projectDO -> projectDO.eq(ProjectAttendanceRuleDO::getCreator, getLoginUserId().toString()))
                .orderByDesc(ProjectAttendanceRuleDO::getId));
    }

    default List<ProjectAttendanceRuleDO> selectList(List<Long> collect, ProjectAttendanceRuleExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProjectAttendanceRuleDO>()
                .inIfPresent(ProjectAttendanceRuleDO::getProjectId,collect)
                .likeIfPresent(ProjectAttendanceRuleDO::getPunchName, reqVO.getPunchName())
                .likeIfPresent(ProjectAttendanceRuleDO::getProjectName, reqVO.getProjectName())
                .eqIfPresent(ProjectAttendanceRuleDO::getProjectId, reqVO.getProjectId())
                .likeIfPresent(ProjectAttendanceRuleDO::getPunchRadius, reqVO.getPunchRadius())
                .likeIfPresent(ProjectAttendanceRuleDO::getLatiLong, reqVO.getLatiLong())
                .or(projectDO -> projectDO.eq(ProjectAttendanceRuleDO::getCreator, getLoginUserId().toString()))
                .orderByDesc(ProjectAttendanceRuleDO::getId));
    }

    default List<ProjectAttendanceRuleDO> selectByProjectList(Long reqVOs) {
        return selectList(new LambdaQueryWrapperX<ProjectAttendanceRuleDO>()
                .eq(ProjectAttendanceRuleDO::getProjectId, reqVOs)
                .orderByDesc(ProjectAttendanceRuleDO::getId));

    }

}
