package com.lh.oa.module.bpm.framework.flowable.core.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceCreateReqVO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmProcessDefinitionExtMapper;
import com.lh.oa.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.bpm.service.definition.BpmProcessDefinitionService;
import com.lh.oa.module.bpm.service.task.BpmProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CustomerEndListener extends BpmProcessInstanceResultEventListener {

    public static final String PROCESS_KEY = "kehu"; //客户新增流程的流程标识
    public static final String PROCESS_KEY2 = "we"; //业绩划分流程的流程标识

    @Value(value = "${process.start-user-id}")
    public Long startUserId;
    @Resource
    private BpmProcessInstanceExtMapper bpmProcessInstanceExtMapper;
    @Resource
    private BpmProcessDefinitionExtMapper bpmProcessDefinitionExtMapper;
    @Resource
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private BpmProcessInstanceService processInstanceService;

    @Override
    protected String getProcessDefinitionKey() {
        return PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        if (ObjectUtils.notEqual(event.getResult(), BpmProcessInstanceResultEnum.APPROVE.getResult()))
            return;
        log.info("客户新增流程，流程ID：{}", event.getId());
        BpmProcessInstanceExtDO processInstance = bpmProcessInstanceExtMapper.selectOne("process_instance_id", event.getId());
        ProcessDefinition activeProcessDefinition2 = processDefinitionService.getActiveProcessDefinition(PROCESS_KEY2);
        BpmProcessDefinitionExtDO bpmProcessDefinitionExtDO2 = bpmProcessDefinitionExtMapper.selectByProcessDefinitionId(activeProcessDefinition2.getId());
        log.info("客户新增流程，流程ID：{}，内容：{}", processInstance.getProcessInstanceId(), processInstance.getFormVariables());
        Map<String, Object> formVariables = processInstance.getFormVariables();
        Object customerName = formVariables.get("customerName");
        Object contact = formVariables.get("contact");
        Object mobile = formVariables.get("mobile");
        String formConf = bpmProcessDefinitionExtDO2.getFormConf();
        log.info("formConf为{}",formConf);
        // 将JSON字符串转换为JSONObject
        JSONObject jsonObject = JSONObject.parseObject(formConf);
        // 获取页面主体内容
        JSONArray bodyArray = jsonObject.getJSONArray("body");
        log.info("bodyArray为{}",bodyArray);
        JSONArray body = (JSONArray) ((JSONObject) bodyArray.get(0)).get("body");
        List<String> formFields = new ArrayList<>();

        for (int i = 0; i < body.size(); i++) {
            JSONObject jsonObject1 = body.getJSONObject(i);
            String name = jsonObject1.getString("name");
            formFields.add(name);
        }

        Map<String, Object> map = new HashMap<>();
        for (String field : formFields) {
            if ("customerName".equals(field))
                map.put(field, customerName);
            if ("contact".equals(field))
                map.put(field, contact);
            if ("mobile".equals(field))
                map.put(field, mobile);
        }
        map.put(PROCESS_KEY2, PROCESS_KEY2);
        log.info("业绩划分流程，流程ID：{}，内容：{}", processInstance.getProcessInstanceId(), processInstance.getFormVariables());
        processInstanceService.createProcessInstance(startUserId,
                new BpmProcessInstanceCreateReqVO().setProcessDefinitionId(activeProcessDefinition2.getId()).setVariables(map));
    }
}
