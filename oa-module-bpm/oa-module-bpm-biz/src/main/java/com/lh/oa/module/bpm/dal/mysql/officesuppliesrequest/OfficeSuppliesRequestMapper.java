package com.lh.oa.module.bpm.dal.mysql.officesuppliesrequest;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestPageReqVo;
import com.lh.oa.module.bpm.dal.dataobject.officesuppliesrequest.OfficeSuppliesRequestDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Mapper
public interface OfficeSuppliesRequestMapper extends BaseMapperX<OfficeSuppliesRequestDO> {
    default PageResult<OfficeSuppliesRequestDO> selectPage(Long userId, OfficeSuppliesRequestPageReqVo pageVO) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (pageVO.getStartTime() != null) {
            Long startTime = pageVO.getStartTime();
            start = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime), ZoneId.systemDefault()).with(LocalTime.MIN);;
        }
        if (pageVO.getEndTime() != null) {
            Long endTime = pageVO.getEndTime();
            end = LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime), ZoneId.systemDefault()).with(LocalTime.MIN);;
        }
        return selectPage(pageVO, new LambdaQueryWrapperX<OfficeSuppliesRequestDO>()
//                .eqIfPresent(OfficeSuppliesRequestDO::getUserId, userId)
                .eqIfPresent(OfficeSuppliesRequestDO::getResult, pageVO.getResult())
                .likeIfPresent(OfficeSuppliesRequestDO::getReason, pageVO.getReason())
                .betweenIfPresent(OfficeSuppliesRequestDO::getCreateTime, start,end)
                .orderByDesc(OfficeSuppliesRequestDO::getId));

    }
}
