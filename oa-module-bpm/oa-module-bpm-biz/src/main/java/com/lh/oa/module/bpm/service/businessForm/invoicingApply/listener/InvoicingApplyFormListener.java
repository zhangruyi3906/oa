package com.lh.oa.module.bpm.service.businessForm.invoicingApply.listener;

import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.bpm.service.businessForm.invoicingApply.InvoicingApplyFormService;
import com.lh.oa.module.bpm.service.businessForm.invoicingApply.InvoicingApplyFormServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tanghanlin
 * @since 2023/10/23
 */
@Component
public class InvoicingApplyFormListener extends BpmProcessInstanceResultEventListener {

    @Resource
    private InvoicingApplyFormService service;

    @Override
    protected String getProcessDefinitionKey() {
        return InvoicingApplyFormServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        if (event.getBusinessKey() == null) {
            return;
        }
        service.updateResult(Long.parseLong(event.getBusinessKey()), event.getResult());
    }
}
