package com.lh.oa.module.bpm.service.businessForm.performanceExamine.listener;

import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.bpm.service.businessForm.performanceExamine.PerformanceExamineFormService;
import com.lh.oa.module.bpm.service.businessForm.performanceExamine.PerformanceExamineFormServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tanghanlin
 * @since 2023/10/23
 */
@Component
public class PerformanceExamineFormListener extends BpmProcessInstanceResultEventListener {

    @Resource
    private PerformanceExamineFormService service;

    @Override
    protected String getProcessDefinitionKey() {
        return PerformanceExamineFormServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        if (event.getBusinessKey() == null) {
            return;
        }
        service.updateResult(Long.parseLong(event.getBusinessKey()), event.getResult());
    }
}
