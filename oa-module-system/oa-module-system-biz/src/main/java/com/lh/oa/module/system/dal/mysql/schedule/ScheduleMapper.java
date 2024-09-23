package com.lh.oa.module.system.dal.mysql.schedule;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.schedule.vo.ScheduleExportReqVO;
import com.lh.oa.module.system.controller.admin.schedule.vo.SchedulePageReqVO;
import com.lh.oa.module.system.controller.admin.schedule.vo.ScheduleRespVO;
import com.lh.oa.module.system.dal.dataobject.schedule.ScheduleDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.*;
import java.util.Date;
import java.util.List;

/**
 * 日程管理 Mapper
 *
 * @author
 */
@Mapper
public interface ScheduleMapper extends BaseMapperX<ScheduleDO> {

    default PageResult<ScheduleDO> selectPage(SchedulePageReqVO reqVO) {
        Date start = null;
        Date end = null;
        if (reqVO.getStartTime() != null) {
            Long startTime = reqVO.getStartTime();
            start = Date.from(Instant.ofEpochSecond(startTime));
        }
        if (reqVO.getEndTime() != null) {
            Long endTime = reqVO.getEndTime();
            end = Date.from(Instant.ofEpochSecond(endTime));
        }
        return selectPage(reqVO, new LambdaQueryWrapperX<ScheduleDO>()
                .eqIfPresent(ScheduleDO::getTitle, reqVO.getTitle())
                .eqIfPresent(ScheduleDO::getDescription, reqVO.getDescription())
                .eqIfPresent(ScheduleDO::getExpired, reqVO.getExpired())
                .eqIfPresent(ScheduleDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ScheduleDO::getExpireDateDay, reqVO.getExpireDateDay())
                .betweenIfPresent(ScheduleDO::getExpireDate, start, end)
                .orderByDesc(ScheduleDO::getId));
    }

    default List<ScheduleDO> selectList(ScheduleExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ScheduleDO>()
                .eqIfPresent(ScheduleDO::getTitle, reqVO.getTitle())
                .eqIfPresent(ScheduleDO::getDescription, reqVO.getDescription())
                .betweenIfPresent(ScheduleDO::getExpireTime, reqVO.getExpireTime())
                .eqIfPresent(ScheduleDO::getExpired, reqVO.getExpired())
                .eqIfPresent(ScheduleDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(ScheduleDO::getExpireDate, reqVO.getExpireDate())
                .orderByDesc(ScheduleDO::getId));
    }

    default List<ScheduleDO> selectListByMonth(ScheduleRespVO reqVO) {
        Date start = null;
        Date end = null;
        if (reqVO.getStartTime() != null) {
            Long startTime = reqVO.getStartTime();
            start = Date.from(Instant.ofEpochSecond(startTime));
        }
        if (reqVO.getEndTime() != null) {
            Long endTime = reqVO.getEndTime();
            end = Date.from(Instant.ofEpochSecond(endTime));
        }
        return selectList(new LambdaQueryWrapperX<ScheduleDO>()
                .betweenIfPresent(ScheduleDO::getExpireDateDay, start,end)
                .eqIfPresent(ScheduleDO::getUserId, reqVO.getUserId())
                .orderByDesc(ScheduleDO::getId));
    }



    default List<ScheduleDO> selectByTime(Date start, Date end, Long userId){

        return selectList(new LambdaQueryWrapperX<ScheduleDO>()
                .neIfPresent(ScheduleDO::getStatus, 3)
                .and(wrapper -> wrapper.between(ScheduleDO::getScheStartTime, start, end))
                .notIn(ScheduleDO::getScheStartTime, start, end)
                        .or().between(ScheduleDO::getScheEndTime, start, end)
                .notIn(ScheduleDO::getScheEndTime, start, end)
                .eq(ScheduleDO::getUserId, userId));
    }

}
