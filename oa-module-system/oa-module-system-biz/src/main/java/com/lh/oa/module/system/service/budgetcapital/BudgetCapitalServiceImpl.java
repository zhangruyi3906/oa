package com.lh.oa.module.system.service.budgetcapital;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalCreateReqVO;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalExportReqVO;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalPageReqVO;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalUpdateReqVO;
import com.lh.oa.module.system.convert.budgetcapital.BudgetCapitalConvert;
import com.lh.oa.module.system.dal.dataobject.budgetcapital.BudgetCapitalDO;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.*;
import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.dal.mysql.budgetcapital.BudgetCapitalMapper;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 资金预算 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class BudgetCapitalServiceImpl implements BudgetCapitalService {

    @Resource
    private BudgetCapitalMapper budgetCapitalMapper;

    @Override
    public Long createBudgetCapital(BudgetCapitalCreateReqVO createReqVO) {
        // 插入
        BudgetCapitalDO budgetCapital = BudgetCapitalConvert.INSTANCE.convert(createReqVO);
        budgetCapitalMapper.insert(budgetCapital);
        // 返回
        return budgetCapital.getId();
    }

    @Override
    public void updateBudgetCapital(BudgetCapitalUpdateReqVO updateReqVO) {
        // 校验存在
        validateBudgetCapitalExists(updateReqVO.getId());
        // 更新
        BudgetCapitalDO updateObj = BudgetCapitalConvert.INSTANCE.convert(updateReqVO);
        budgetCapitalMapper.updateById(updateObj);
    }

    @Override
    public void deleteBudgetCapital(Long id) {
        // 校验存在
        validateBudgetCapitalExists(id);
        // 删除
        budgetCapitalMapper.deleteById(id);
    }

    private void validateBudgetCapitalExists(Long id) {
        if (budgetCapitalMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.BUDGET_CAPITAL_NOT_EXISTS);
        }
    }

    @Override
    public BudgetCapitalDO getBudgetCapital(Long id) {
        return budgetCapitalMapper.selectById(id);
    }

    @Override
    public List<BudgetCapitalDO> getBudgetCapitalList(Collection<Long> ids) {
        return budgetCapitalMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BudgetCapitalDO> getBudgetCapitalPage(BudgetCapitalPageReqVO pageReqVO) {
        return budgetCapitalMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BudgetCapitalDO> getBudgetCapitalList(BudgetCapitalExportReqVO exportReqVO) {
        return budgetCapitalMapper.selectList(exportReqVO);
    }

}
