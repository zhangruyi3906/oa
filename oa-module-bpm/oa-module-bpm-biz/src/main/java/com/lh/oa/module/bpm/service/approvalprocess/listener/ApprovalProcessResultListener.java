package com.lh.oa.module.bpm.service.approvalprocess.listener;

import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.bpm.service.approvalprocess.ApprovalProcessService;
import com.lh.oa.module.bpm.service.approvalprocess.ApprovalProcessServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class ApprovalProcessResultListener extends BpmProcessInstanceResultEventListener {

    @Resource
    private ApprovalProcessService approvalProcessService;
    @Override
    protected String getProcessDefinitionKey() {
        return ApprovalProcessServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        approvalProcessService.updateApprovalProcess(Long.parseLong(event.getBusinessKey()), event.getResult());
    }
}
