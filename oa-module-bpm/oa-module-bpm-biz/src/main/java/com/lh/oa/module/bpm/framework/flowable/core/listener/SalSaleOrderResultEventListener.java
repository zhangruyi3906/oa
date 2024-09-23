package com.lh.oa.module.bpm.framework.flowable.core.listener;

import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.system.api.sal.SalSaleApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class SalSaleOrderResultEventListener extends BpmProcessInstanceResultEventListener {
    @Resource
    private SalSaleApi salSaleApi;
    @Resource
    private BpmProcessInstanceExtMapper bpmProcessInstanceExtMapper;

    public static final String PROCESS_KEY = "post_sale_accessory";
    @Override
    protected String getProcessDefinitionKey() {
        return PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        log.info("收到流程实例结果事件，流程实例编号：{}，流程实例结果：{}", event.getId(), event.getResult());
        if (ObjectUtils.equals(BpmProcessInstanceResultEnum.APPROVE.getResult(), event.getResult())) {
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(event.getId());
            if (ObjectUtils.isNotEmpty(bpmProcessInstanceExtDO)) {
                salSaleApi.getSalSaleOrderParams(bpmProcessInstanceExtDO.getFormVariables());
            }
        }
    }
}
