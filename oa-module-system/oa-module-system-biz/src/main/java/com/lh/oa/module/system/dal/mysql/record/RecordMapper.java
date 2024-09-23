package com.lh.oa.module.system.dal.mysql.record;

import java.time.*;
import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.dal.dataobject.record.RecordDO;
import com.lh.oa.module.system.controller.admin.record.vo.RecordExportReqVO;
import com.lh.oa.module.system.controller.admin.record.vo.RecordPageReqVO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.system.controller.admin.record.vo.*;

/**
 * 打卡记录 Mapper
 *
 * @author
 */
@Mapper
public interface RecordMapper extends BaseMapperX<RecordDO> {

    default PageResult<RecordDO> selectPage(RecordPageReqVO reqVO) {
        if (reqVO.getPunch() != null){
            Instant instant = Instant.ofEpochSecond(reqVO.getPunch());

            // 将Instant对象转换为LocalDate
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            reqVO.setPunchDate(localDate);
        }
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
        return selectPage(reqVO, new LambdaQueryWrapperX<RecordDO>()
                .eqIfPresent(RecordDO::getUserId, reqVO.getUserId())
                .eqIfPresent(RecordDO::getDeptId, reqVO.getDepartmentId())
                .betweenIfPresent(RecordDO::getCheckInTime, reqVO.getCheckInTime())
                .betweenIfPresent(RecordDO::getCheckOutTime, reqVO.getCheckOutTime())
                .eqIfPresent(RecordDO::getCheckInStatus, reqVO.getCheckInStatus())
                .eqIfPresent(RecordDO::getCheckOutStatus, reqVO.getCheckOutStatus())
                .eqIfPresent(RecordDO::getPunchDate, reqVO.getPunchDate())
                .betweenIfPresent(RecordDO::getPunchDate, start,end)
                .eqIfPresent(RecordDO::getAttStatus, reqVO.getAttStatus())
                .orderByDesc(RecordDO::getId));
    }

    default List<RecordDO> selectList(RecordExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<RecordDO>()
                .eqIfPresent(RecordDO::getUserId, reqVO.getUserId())
                .eqIfPresent(RecordDO::getDeptId, reqVO.getDepartmentId())
                .betweenIfPresent(RecordDO::getCheckInTime, reqVO.getCheckInTime())
                .betweenIfPresent(RecordDO::getCheckOutTime, reqVO.getCheckOutTime())
                .eqIfPresent(RecordDO::getCheckInStatus, reqVO.getCheckInStatus())
                .eqIfPresent(RecordDO::getCheckOutStatus, reqVO.getCheckOutStatus())
                .betweenIfPresent(RecordDO::getPunchDate, reqVO.getPunchDate())
                .eqIfPresent(RecordDO::getAttStatus, reqVO.getAttStatus())
                .orderByDesc(RecordDO::getId));
    }

}
