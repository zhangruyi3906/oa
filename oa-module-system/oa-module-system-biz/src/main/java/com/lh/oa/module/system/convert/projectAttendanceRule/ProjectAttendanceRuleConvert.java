package com.lh.oa.module.system.convert.projectAttendanceRule;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.ProjectAttendanceRuleCreateReqVO;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.ProjectAttendanceRuleExcelVO;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.ProjectAttendanceRuleRespVO;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.ProjectAttendanceRuleUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.projectAttendanceRule.ProjectAttendanceRuleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.*;

/**
 * 打卡规则（项目） Convert
 *
 * @author
 */
@Mapper
public interface ProjectAttendanceRuleConvert {

    ProjectAttendanceRuleConvert INSTANCE = Mappers.getMapper(ProjectAttendanceRuleConvert.class);

    ProjectAttendanceRuleDO convert(ProjectAttendanceRuleCreateReqVO bean);

    ProjectAttendanceRuleDO convert(ProjectAttendanceRuleUpdateReqVO bean);

    ProjectAttendanceRuleRespVO convert(ProjectAttendanceRuleDO bean);

    List<ProjectAttendanceRuleRespVO> convertList(List<ProjectAttendanceRuleDO> list);

    PageResult<ProjectAttendanceRuleRespVO> convertPage(PageResult<ProjectAttendanceRuleDO> page);

    List<ProjectAttendanceRuleExcelVO> convertList02(List<ProjectAttendanceRuleDO> list);
    List<ProjectAttendanceRuleDO> convertList03(List<ProjectAttendanceRuleCreateReqVO> list);

}
