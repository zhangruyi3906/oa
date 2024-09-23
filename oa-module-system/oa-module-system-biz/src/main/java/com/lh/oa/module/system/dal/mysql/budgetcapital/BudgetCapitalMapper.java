package com.lh.oa.module.system.dal.mysql.budgetcapital;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.dal.dataobject.budgetcapital.BudgetCapitalDO;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalExportReqVO;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalPageReqVO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.*;

/**
 * 资金预算 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface BudgetCapitalMapper extends BaseMapperX<BudgetCapitalDO> {

    default PageResult<BudgetCapitalDO> selectPage(BudgetCapitalPageReqVO reqVO) {
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
        return selectPage(reqVO, new LambdaQueryWrapperX<BudgetCapitalDO>()
                .eqIfPresent(BudgetCapitalDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(BudgetCapitalDO::getBudgetPeriod, reqVO.getBudgetPeriod())
                .eqIfPresent(BudgetCapitalDO::getBudgetType, reqVO.getBudgetType())
                .eqIfPresent(BudgetCapitalDO::getBudgetAmount, reqVO.getBudgetAmount())
                .betweenIfPresent(BudgetCapitalDO::getCreateTime,start,end)
                .orderByDesc(BudgetCapitalDO::getId));
    }

    default List<BudgetCapitalDO> selectList(BudgetCapitalExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<BudgetCapitalDO>()
                .eqIfPresent(BudgetCapitalDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(BudgetCapitalDO::getBudgetPeriod, reqVO.getBudgetPeriod())
                .eqIfPresent(BudgetCapitalDO::getBudgetType, reqVO.getBudgetType())
                .eqIfPresent(BudgetCapitalDO::getBudgetAmount, reqVO.getBudgetAmount())
                .betweenIfPresent(BudgetCapitalDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BudgetCapitalDO::getId));
    }

}
