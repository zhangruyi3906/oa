package com.lh.oa.module.system.dal.mysql.salarysettlement;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.SalarySettlementExportReqVO;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.SalarySettlementPageReqVO;
import com.lh.oa.module.system.dal.dataobject.salarysettlement.SalarySettlementDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 员工工资结算 Mapper
 *
 * @author
 */
@Mapper
public interface SalarySettlementMapper extends BaseMapperX<SalarySettlementDO> {

    default PageResult<SalarySettlementDO> selectPage(SalarySettlementPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SalarySettlementDO>()
                .eqIfPresent(SalarySettlementDO::getUserId, reqVO.getUserId())
                .eqIfPresent(SalarySettlementDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(SalarySettlementDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(SalarySettlementDO::getBaseSalary, reqVO.getBaseSalary())
                .eqIfPresent(SalarySettlementDO::getTechnicalBonus, reqVO.getTechnicalBonus())
                .eqIfPresent(SalarySettlementDO::getRewardBonus, reqVO.getRewardBonus())
                .eqIfPresent(SalarySettlementDO::getStatutoryDeduction, reqVO.getStatutoryDeduction())
                .eqIfPresent(SalarySettlementDO::getOvertimeSalary, reqVO.getOvertimeSalary())
                .eqIfPresent(SalarySettlementDO::getDeductionDetails, reqVO.getDeductionDetails())
                .eqIfPresent(SalarySettlementDO::getAttendanceDays, reqVO.getAttendanceDays())
                .eqIfPresent(SalarySettlementDO::getSettlementDate, reqVO.getSettlementDate())
                .orderByDesc(SalarySettlementDO::getId));
    }

    default List<SalarySettlementDO> selectList(SalarySettlementExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<SalarySettlementDO>()
                .eqIfPresent(SalarySettlementDO::getUserId, reqVO.getUserId())
                .eqIfPresent(SalarySettlementDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(SalarySettlementDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(SalarySettlementDO::getBaseSalary, reqVO.getBaseSalary())
                .eqIfPresent(SalarySettlementDO::getTechnicalBonus, reqVO.getTechnicalBonus())
                .eqIfPresent(SalarySettlementDO::getRewardBonus, reqVO.getRewardBonus())
                .eqIfPresent(SalarySettlementDO::getStatutoryDeduction, reqVO.getStatutoryDeduction())
                .eqIfPresent(SalarySettlementDO::getOvertimeSalary, reqVO.getOvertimeSalary())
                .eqIfPresent(SalarySettlementDO::getDeductionDetails, reqVO.getDeductionDetails())
                .eqIfPresent(SalarySettlementDO::getAttendanceDays, reqVO.getAttendanceDays())
                .eqIfPresent(SalarySettlementDO::getSettlementDate, reqVO.getSettlementDate())
                .orderByDesc(SalarySettlementDO::getId));
    }

}
