package com.lh.oa.module.system.dal.mysql.fundtransfer;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferExportReqVO;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferPageReqVO;
import com.lh.oa.module.system.dal.dataobject.fundtransfer.FundTransferDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

/**
 * 资金划拨 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface FundTransferMapper extends BaseMapperX<FundTransferDO> {

    default PageResult<FundTransferDO> selectPage(FundTransferPageReqVO reqVO) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (reqVO.getStartTime() != null) {
            Long startTime = reqVO.getStartTime();
            start = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime), ZoneId.systemDefault()).with(LocalTime.MIN);
        }
        if (reqVO.getEndTime() != null) {
            Long endTime = reqVO.getEndTime();
            end = LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime), ZoneId.systemDefault()).with(LocalTime.MIN);
        }
        return selectPage(reqVO, new LambdaQueryWrapperX<FundTransferDO>()
                .eqIfPresent(FundTransferDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(FundTransferDO::getAmount, reqVO.getAmount())
                .betweenIfPresent(FundTransferDO::getCreateTime, start,end)
                .orderByDesc(FundTransferDO::getId));
    }

    default List<FundTransferDO> selectList(FundTransferExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<FundTransferDO>()
                .eqIfPresent(FundTransferDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(FundTransferDO::getAmount, reqVO.getAmount())
                .betweenIfPresent(FundTransferDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FundTransferDO::getId));
    }

}
