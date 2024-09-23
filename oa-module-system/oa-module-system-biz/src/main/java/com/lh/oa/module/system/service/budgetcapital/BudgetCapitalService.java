package com.lh.oa.module.system.service.budgetcapital;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.*;
import com.lh.oa.module.system.dal.dataobject.budgetcapital.BudgetCapitalDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalCreateReqVO;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalExportReqVO;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalPageReqVO;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalUpdateReqVO;

/**
 * 资金预算 Service 接口
 *
 * @author 管理员
 */
public interface BudgetCapitalService {

    /**
     * 创建资金预算
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBudgetCapital(@Valid BudgetCapitalCreateReqVO createReqVO);

    /**
     * 更新资金预算
     *
     * @param updateReqVO 更新信息
     */
    void updateBudgetCapital(@Valid BudgetCapitalUpdateReqVO updateReqVO);

    /**
     * 删除资金预算
     *
     * @param id 编号
     */
    void deleteBudgetCapital(Long id);

    /**
     * 获得资金预算
     *
     * @param id 编号
     * @return 资金预算
     */
    BudgetCapitalDO getBudgetCapital(Long id);

    /**
     * 获得资金预算列表
     *
     * @param ids 编号
     * @return 资金预算列表
     */
    List<BudgetCapitalDO> getBudgetCapitalList(Collection<Long> ids);

    /**
     * 获得资金预算分页
     *
     * @param pageReqVO 分页查询
     * @return 资金预算分页
     */
    PageResult<BudgetCapitalDO> getBudgetCapitalPage(BudgetCapitalPageReqVO pageReqVO);

    /**
     * 获得资金预算列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 资金预算列表
     */
    List<BudgetCapitalDO> getBudgetCapitalList(BudgetCapitalExportReqVO exportReqVO);

}
