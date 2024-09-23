package com.lh.oa.module.system.convert.attendanceRule;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.attendanceRule.vo.AttendanceRuleCreateReqVO;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.AttendanceRuleExcelVO;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.AttendanceRuleRespVO;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.AttendanceRuleUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.attendanceRule.AttendanceRuleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.*;

/**
 * 打卡规则（部门） Convert
 *
 * @author
 */
@Mapper
public interface AttendanceRuleConvert {

    AttendanceRuleConvert INSTANCE = Mappers.getMapper(AttendanceRuleConvert.class);

    AttendanceRuleDO convert(AttendanceRuleCreateReqVO bean);

    AttendanceRuleDO convert(AttendanceRuleUpdateReqVO bean);

    AttendanceRuleRespVO convert(AttendanceRuleDO bean);

    List<AttendanceRuleRespVO> convertList(List<AttendanceRuleDO> list);

    PageResult<AttendanceRuleRespVO> convertPage(PageResult<AttendanceRuleDO> page);

    List<AttendanceRuleExcelVO> convertList02(List<AttendanceRuleDO> list);

}
