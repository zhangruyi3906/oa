package com.lh.oa.module.bpm.service.officesuppliesrequest.listener;

import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.bpm.service.officesuppliesrequest.OfficeSuppliesRequestService;
import com.lh.oa.module.bpm.service.officesuppliesrequest.OfficeSuppliesRequestServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OfficeSuppliesRequestListener extends BpmProcessInstanceResultEventListener {
    @Resource
    private OfficeSuppliesRequestService service;

    @Override
    protected String getProcessDefinitionKey() {
        return OfficeSuppliesRequestServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        if (event.getBusinessKey() == null) {
            return;
        }
        service.updateOfficeSuResult(Long.parseLong(event.getBusinessKey()), event.getResult());
    }
}
