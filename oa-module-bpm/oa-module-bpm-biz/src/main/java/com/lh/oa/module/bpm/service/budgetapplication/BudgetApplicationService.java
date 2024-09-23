package com.lh.oa.module.bpm.service.budgetapplication;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.*;
import com.lh.oa.module.bpm.dal.dataobject.budgetapplication.BudgetApplicationDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.BudgetApplicationCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.BudgetApplicationPageReqVO;

/**
 * 资金预算申请 Service 接口
 *
 * @author 管理员
 */
public interface BudgetApplicationService {

    /**
     * 创建资金预算申请
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBudgetApplication(Long userId,@Valid BudgetApplicationCreateReqVO createReqVO);

    /**
     * 更新资金预算申请
     *
     * @param updateReqVO 更新信息
     */
    void updateBudgetApplication(Long id, Integer result);

    /**
     * 删除资金预算申请
     *
     * @param id 编号
     */
    void deleteBudgetApplication(Long id);

    /**
     * 获得资金预算申请
     *
     * @param id 编号
     * @return 资金预算申请
     */
    BudgetApplicationDO getBudgetApplication(Long id);

    /**
     * 获得资金预算申请列表
     *
     * @param ids 编号
     * @return 资金预算申请列表
     */
    List<BudgetApplicationDO> getBudgetApplicationList(Collection<Long> ids);

    /**
     * 获得资金预算申请分页
     *
     * @param pageReqVO 分页查询
     * @return 资金预算申请分页
     */
    PageResult<BudgetApplicationDO> getBudgetApplicationPage(BudgetApplicationPageReqVO pageReqVO);

    /**
     * 获得资金预算申请列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 资金预算申请列表
     */
//    List<budgetApplicationDO> getbudgetApplicationList(budgetApplicationExportReqVO exportReqVO);

}
