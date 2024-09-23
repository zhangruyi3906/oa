package com.lh.oa.module.system.dal.mysql.monthstatistics;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.MonthStatisticsExportReqVO;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.MonthStatisticsPageReqVO;
import com.lh.oa.module.system.dal.dataobject.monthstatistics.MonthStatisticsDO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 考勤月统计 Mapper
 *
 * @author
 */
@Mapper
public interface MonthStatisticsMapper extends BaseMapperX<MonthStatisticsDO> {

    default PageResult<MonthStatisticsDO> selectPage(Set<Long> list, MonthStatisticsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MonthStatisticsDO>()
                .eqIfPresent(MonthStatisticsDO::getUserId, reqVO.getUserId())
                .eqIfPresent(MonthStatisticsDO::getProjectId, reqVO.getProjectId())
                .inIfPresent(MonthStatisticsDO::getDeptId, list)
                .eq(MonthStatisticsDO::getAttStatus, reqVO.getAttStatus())
                .eqIfPresent(MonthStatisticsDO::getAttendanceMonth, reqVO.getAttendanceMonth())
                .eqIfPresent(MonthStatisticsDO::getTotalWorkingDays, reqVO.getTotalWorkingDays())
                .eqIfPresent(MonthStatisticsDO::getActualWorkingDays, reqVO.getActualWorkingDays())
                .eqIfPresent(MonthStatisticsDO::getTotalOvertimeHours, reqVO.getTotalOvertimeHours())
                .eqIfPresent(MonthStatisticsDO::getTotalLeaveDays, reqVO.getTotalLeaveDays())
                .eqIfPresent(MonthStatisticsDO::getTotalAbsenceDays, reqVO.getTotalAbsenceDays())
                .orderByDesc(MonthStatisticsDO::getId));
    }
    default PageResult<MonthStatisticsDO> selectPage1(MonthStatisticsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MonthStatisticsDO>()
                .eqIfPresent(MonthStatisticsDO::getUserId, reqVO.getUserId())
                .eqIfPresent(MonthStatisticsDO::getProjectId, reqVO.getProjectId())
                .eq(MonthStatisticsDO::getAttStatus, reqVO.getAttStatus())
                .eqIfPresent(MonthStatisticsDO::getAttendanceMonth, reqVO.getAttendanceMonth())
                .eqIfPresent(MonthStatisticsDO::getTotalWorkingDays, reqVO.getTotalWorkingDays())
                .eqIfPresent(MonthStatisticsDO::getActualWorkingDays, reqVO.getActualWorkingDays())
                .eqIfPresent(MonthStatisticsDO::getTotalOvertimeHours, reqVO.getTotalOvertimeHours())
                .eqIfPresent(MonthStatisticsDO::getTotalLeaveDays, reqVO.getTotalLeaveDays())
                .eqIfPresent(MonthStatisticsDO::getTotalAbsenceDays, reqVO.getTotalAbsenceDays())
                .isNull(MonthStatisticsDO::getDeptId)
                .orderByDesc(MonthStatisticsDO::getId));
    }

    default List<MonthStatisticsDO> selectList(MonthStatisticsExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MonthStatisticsDO>()
                .eqIfPresent(MonthStatisticsDO::getUserId, reqVO.getUserId())
                .eqIfPresent(MonthStatisticsDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(MonthStatisticsDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(MonthStatisticsDO::getAttendanceMonth, reqVO.getAttendanceMonth())
                .eqIfPresent(MonthStatisticsDO::getTotalWorkingDays, reqVO.getTotalWorkingDays())
                .eqIfPresent(MonthStatisticsDO::getActualWorkingDays, reqVO.getActualWorkingDays())
                .eqIfPresent(MonthStatisticsDO::getTotalOvertimeHours, reqVO.getTotalOvertimeHours())
                .eqIfPresent(MonthStatisticsDO::getTotalLeaveDays, reqVO.getTotalLeaveDays())
                .eqIfPresent(MonthStatisticsDO::getTotalAbsenceDays, reqVO.getTotalAbsenceDays())
                .orderByDesc(MonthStatisticsDO::getId));
    }
    @Update("UPDATE attendance_month_statistics SET deleted = 1 WHERE id = #{id}")
    void updateToDelete(@Param("id") Long id);
}