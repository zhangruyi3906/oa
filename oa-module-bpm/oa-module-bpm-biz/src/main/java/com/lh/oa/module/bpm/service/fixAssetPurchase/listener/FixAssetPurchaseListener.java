package com.lh.oa.module.bpm.service.fixAssetPurchase.listener;

import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.bpm.service.fixAssetPurchase.FixAssetPurchaseService;
import com.lh.oa.module.bpm.service.fixAssetPurchase.FixAssetPurchaseServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yangsheng
 * @since 2023/10/24
 */
@Component
public class FixAssetPurchaseListener extends BpmProcessInstanceResultEventListener {

    @Resource
    private FixAssetPurchaseService service;

    @Override
    protected String getProcessDefinitionKey() {
        return FixAssetPurchaseServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        if (event.getBusinessKey() == null) {
            return;
        }
        service.updateResult(Long.parseLong(event.getBusinessKey()), event.getResult());
    }
}
