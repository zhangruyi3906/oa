package com.lh.oa.module.system.dal.mysql.attendanceRule;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.AttendanceRuleExportReqVO;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.AttendanceRulePageReqVO;
import com.lh.oa.module.system.dal.dataobject.attendanceRule.AttendanceRuleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 打卡规则（部门） Mapper
 *
 * @author
 */
@Mapper
public interface AttendanceRuleMapper extends BaseMapperX<AttendanceRuleDO> {

    default PageResult<AttendanceRuleDO> selectPage(AttendanceRulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AttendanceRuleDO>()
                .eqIfPresent(AttendanceRuleDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(AttendanceRuleDO::getSyncHolidays, reqVO.getSyncHolidays())
                .eqIfPresent(AttendanceRuleDO::getPunchRadius, reqVO.getPunchRadius())
                .eqIfPresent(AttendanceRuleDO::getFlexibleCheckInStart, reqVO.getFlexibleCheckInStart())
                .eqIfPresent(AttendanceRuleDO::getFlexibleCheckInEnd, reqVO.getFlexibleCheckInEnd())
                .eqIfPresent(AttendanceRuleDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttendanceRuleDO::getLatiLong, reqVO.getLatiLong())
                .orderByDesc(AttendanceRuleDO::getId));
    }

    default List<AttendanceRuleDO> selectList(AttendanceRuleExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<AttendanceRuleDO>()
                .eqIfPresent(AttendanceRuleDO::getDeptId, reqVO.getDepartmentId())
                .eqIfPresent(AttendanceRuleDO::getPunchRadius, reqVO.getPunchRadius())
                .eqIfPresent(AttendanceRuleDO::getFlexibleCheckInStart, reqVO.getFlexibleCheckInStart())
                .eqIfPresent(AttendanceRuleDO::getFlexibleCheckInEnd, reqVO.getFlexibleCheckInEnd())
                .eqIfPresent(AttendanceRuleDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttendanceRuleDO::getLatiLong, reqVO.getLatiLong())
                .orderByDesc(AttendanceRuleDO::getId));
    }

}
