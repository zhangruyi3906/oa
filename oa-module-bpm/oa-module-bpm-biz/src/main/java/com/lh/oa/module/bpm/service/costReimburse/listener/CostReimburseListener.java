package com.lh.oa.module.bpm.service.costReimburse.listener;

import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.bpm.service.costReimburse.CostReimburseService;
import com.lh.oa.module.bpm.service.costReimburse.CostReimburseServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yangsheng
 * @since 2023/10/23
 */
@Component
public class CostReimburseListener extends BpmProcessInstanceResultEventListener {

    @Resource
    private CostReimburseService service;

    @Override
    protected String getProcessDefinitionKey() {
        return CostReimburseServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        if (event.getBusinessKey() == null) {
            return;
        }
        service.updateResult(Long.parseLong(event.getBusinessKey()), event.getResult());
    }
}
