package com.lh.oa.module.system.dal.mysql.projectrecord;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordCreateReqVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordPageReqVO;
import com.lh.oa.module.system.dal.dataobject.projectrecord.ProjectRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.*;
import java.util.List;

@Mapper
public interface ProjectRecordMapper extends BaseMapperX<ProjectRecordDO> {
    default PageResult<ProjectRecordDO> selectPage(List<LocalDate> list, ProjectRecordPageReqVO reqVO) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (reqVO.getStartTime() != null) {
            Long startTime = reqVO.getStartTime();
            start = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime), ZoneId.systemDefault()).with(LocalTime.MIN);;
        }
        if (reqVO.getEndTime() != null) {
            Long endTime = reqVO.getEndTime();
            end = LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime), ZoneId.systemDefault()).with(LocalTime.MIN);;
        }
        return selectPage(reqVO, new LambdaQueryWrapperX<ProjectRecordDO>()
                .eqIfPresent(ProjectRecordDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ProjectRecordDO::getProjectId,reqVO.getProjectId())
                .betweenIfPresent(ProjectRecordDO::getCheckInTime, reqVO.getCheckInTime())
                .betweenIfPresent(ProjectRecordDO::getCheckOutTime, reqVO.getCheckOutTime())
                .eqIfPresent(ProjectRecordDO::getCheckInStatus, reqVO.getCheckInStatus())
                .eqIfPresent(ProjectRecordDO::getCheckOutStatus, reqVO.getCheckOutStatus())
                .inIfPresent(ProjectRecordDO::getPunchDate, list)
                .betweenIfPresent(ProjectRecordDO::getPunchDate, start,end)
                .eqIfPresent(ProjectRecordDO::getAttStatus, reqVO.getAttStatus())
                .eqIfPresent(ProjectRecordDO::getProjectId, reqVO.getProjectId())
                .orderByDesc(ProjectRecordDO::getId));
    }

    default List<ProjectRecordDO> selectList(ProjectRecordCreateReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProjectRecordDO>()
                .eqIfPresent(ProjectRecordDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ProjectRecordDO::getCheckInStatus, reqVO.getCheckInStatus())
                .eqIfPresent(ProjectRecordDO::getCheckOutStatus, reqVO.getCheckOutStatus())
                .eqIfPresent(ProjectRecordDO::getAttStatus, reqVO.getAttStatus())
                .eqIfPresent(ProjectRecordDO::getProjectId, reqVO.getProjectId())
                .orderByDesc(ProjectRecordDO::getId));
    }
}
