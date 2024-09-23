package com.lh.oa.module.bpm.dal.mysql.budgetapplication;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.BudgetApplicationPageReqVO;
import com.lh.oa.module.bpm.dal.dataobject.budgetapplication.BudgetApplicationDO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.*;

/**
 * 资金预算申请 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface BudgetApplicationMapper extends BaseMapperX<BudgetApplicationDO> {

    default PageResult<BudgetApplicationDO> selectPage(BudgetApplicationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BudgetApplicationDO>()
                .eqIfPresent(BudgetApplicationDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(BudgetApplicationDO::getBudgetPeriod, reqVO.getBudgetPeriod())
                .eqIfPresent(BudgetApplicationDO::getBudgetType, reqVO.getBudgetType())
                .eqIfPresent(BudgetApplicationDO::getBudgetAmount, reqVO.getBudgetAmount())
                .betweenIfPresent(BudgetApplicationDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(BudgetApplicationDO::getApprovalStatus, reqVO.getApprovalStatus())
                .eqIfPresent(BudgetApplicationDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .orderByDesc(BudgetApplicationDO::getId));
    }
//
//    default List<budgetApplicationDO> selectList(budgetApplicationExportReqVO reqVO) {
//        return selectList(new LambdaQueryWrapperX<budgetApplicationDO>()
//                .eqIfPresent(budgetApplicationDO::getProjectId, reqVO.getProjectId())
//                .eqIfPresent(budgetApplicationDO::getBudgetPeriod, reqVO.getBudgetPeriod())
//                .eqIfPresent(budgetApplicationDO::getBudgetType, reqVO.getBudgetType())
//                .eqIfPresent(budgetApplicationDO::getBudgetAmount, reqVO.getBudgetAmount())
//                .betweenIfPresent(budgetApplicationDO::getCreateTime, reqVO.getCreateTime())
//                .eqIfPresent(budgetApplicationDO::getApprovalStatus, reqVO.getApprovalStatus())
//                .eqIfPresent(budgetApplicationDO::getProcessInstanceId, reqVO.getProcessInstanceId())
//                .orderByDesc(budgetApplicationDO::getId));
//    }

}
