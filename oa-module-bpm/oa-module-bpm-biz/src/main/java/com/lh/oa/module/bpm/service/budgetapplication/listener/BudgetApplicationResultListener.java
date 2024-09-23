package com.lh.oa.module.bpm.service.budgetapplication.listener;

import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.bpm.service.budgetapplication.BudgetApplicationService;
import com.lh.oa.module.bpm.service.budgetapplication.BudgetApplicationServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class BudgetApplicationResultListener extends BpmProcessInstanceResultEventListener {

    @Resource
    private BudgetApplicationService budgetApplicationService;

    @Override
    protected String getProcessDefinitionKey() {
        return BudgetApplicationServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        budgetApplicationService.updateBudgetApplication(Long.parseLong(event.getBusinessKey()),event.getResult());
    }
}
