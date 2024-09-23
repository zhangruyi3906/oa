package com.lh.oa.module.system.service.salarysettlement;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.*;
import com.lh.oa.module.system.dal.dataobject.salarysettlement.SalarySettlementDO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 员工工资结算 Service 接口
 *
 * @author
 */
public interface SalarySettlementService {

    /**
     * 创建员工工资结算
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSalarySettlement(@Valid MonthAttendance createReqVO);

    /**
     * 更新员工工资结算
     *
     * @param updateReqVO 更新信息
     */
    void updateSalarySettlement(@Valid SalarySettlementUpdateReqVO updateReqVO);

    /**
     * 删除员工工资结算
     *
     * @param id 编号
     */
    void deleteSalarySettlement(Long id);

    /**
     * 获得员工工资结算
     *
     * @param id 编号
     * @return 员工工资结算
     */
    SalarySettlementDO getSalarySettlement(Long id);

    /**
     * 获得员工工资结算列表
     *
     * @param ids 编号
     * @return 员工工资结算列表
     */
    List<SalarySettlementDO> getSalarySettlementList(Collection<Long> ids);

    /**
     * 获得员工工资结算分页
     *
     * @param pageReqVO 分页查询
     * @return 员工工资结算分页
     */
    PageResult<SalarySettlementDO> getSalarySettlementPage(SalarySettlementPageReqVO pageReqVO);

    /**
     * 获得员工工资结算列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 员工工资结算列表
     */
    List<SalarySettlementDO> getSalarySettlementList(SalarySettlementExportReqVO exportReqVO);


    void export(List<SalarySettlementExcelVO> list, HttpServletRequest req, HttpServletResponse resp);

}
