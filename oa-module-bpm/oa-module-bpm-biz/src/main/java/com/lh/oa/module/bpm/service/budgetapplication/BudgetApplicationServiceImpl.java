package com.lh.oa.module.bpm.service.budgetapplication;

import com.lh.oa.module.bpm.api.task.BpmProcessInstanceApi;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.BudgetApplicationCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.BudgetApplicationPageReqVO;
import com.lh.oa.module.bpm.convert.budgetapplication.BudgetApplicationConvert;
import com.lh.oa.module.bpm.dal.dataobject.budgetapplication.BudgetApplicationDO;
import com.lh.oa.module.bpm.dal.mysql.budgetapplication.BudgetApplicationMapper;
import com.lh.oa.module.bpm.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.*;
import com.lh.oa.framework.common.pojo.PageResult;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 资金预算申请 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class BudgetApplicationServiceImpl implements BudgetApplicationService {

    public static final String PROCESS_KEY = "budget";

    @Resource
    private BudgetApplicationMapper budgetApplicationMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    public Long createBudgetApplication(Long userId, BudgetApplicationCreateReqVO createReqVO) {
        // 插入
        BudgetApplicationDO budgetApplication = BudgetApplicationConvert.INSTANCE.convert(createReqVO).setApprovalStatus(BpmProcessInstanceResultEnum.PROCESS.getResult()).setUserId(userId);
        budgetApplicationMapper.insert(budgetApplication);
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("budget", createReqVO.getBudgetType());
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(budgetApplication.getId())));
        budgetApplicationMapper.updateById(new BudgetApplicationDO().setId(budgetApplication.getId()).setProcessInstanceId(processInstanceId));

        // 返回
        return budgetApplication.getId();
    }

    @Override
    public void updateBudgetApplication(Long id, Integer result) {
        // 校验存在
        validatebudgetApplicationExists(id);
        // 更新
        budgetApplicationMapper.updateById(new BudgetApplicationDO().setApprovalStatus(result).setId(id));
    }

    @Override
    public void deleteBudgetApplication(Long id) {
        // 校验存在
        validatebudgetApplicationExists(id);
        // 删除
        budgetApplicationMapper.deleteById(id);
    }

    private void validatebudgetApplicationExists(Long id) {
        if (budgetApplicationMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.BUDGET_APPLICATION_NOT_EXISTS);
        }
    }

    @Override
    public BudgetApplicationDO getBudgetApplication(Long id) {
        return budgetApplicationMapper.selectById(id);
    }

    @Override
    public List<BudgetApplicationDO> getBudgetApplicationList(Collection<Long> ids) {
        return budgetApplicationMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BudgetApplicationDO> getBudgetApplicationPage(BudgetApplicationPageReqVO pageReqVO) {
        return budgetApplicationMapper.selectPage(pageReqVO);
    }

//    @Override
//    public List<budgetApplicationDO> getbudgetApplicationList(budgetApplicationExportReqVO exportReqVO) {
//        return budgetApplicationMapper.selectList(exportReqVO);
//    }

}
