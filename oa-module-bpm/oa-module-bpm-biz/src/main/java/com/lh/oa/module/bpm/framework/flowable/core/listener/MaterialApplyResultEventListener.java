package com.lh.oa.module.bpm.framework.flowable.core.listener;

import cn.hutool.core.thread.ThreadUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.bpm.wrapper.ImWrapper;
import com.lh.oa.module.system.api.materialApply.MaterialApplyApi;
import com.lh.oa.module.system.api.user.AdminUserApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 物料申请流程完成监听器
 */
@Component
@Slf4j
public class MaterialApplyResultEventListener extends BpmProcessInstanceResultEventListener {
    @Value("${apply.auto-storage}")
    private Boolean autoStorage;
    @Resource
    private MaterialApplyApi materialApplyApi;
    @Resource
    private BpmProcessInstanceExtMapper bpmProcessInstanceExtMapper;
    @Resource
    private ImWrapper imWrapper;
    @Resource
    private AdminUserApi adminUserApi;

    private static final String PROCESS_KEY = "wuliaoshenqing";

    @Override
    protected String getProcessDefinitionKey() {
        return PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        log.info("收到流程实例结果事件，流程实例编号：{}，流程实例结果：{}, 是否自动入库：{}", event.getId(), event.getResult(), autoStorage);
        if (!autoStorage) {
            return;
        }
        if (ObjectUtils.equals(BpmProcessInstanceResultEnum.APPROVE.getResult(), event.getResult())) {
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(event.getId());
            if (ObjectUtils.isNotEmpty(bpmProcessInstanceExtDO) && bpmProcessInstanceExtDO.getFormVariables() != null) {
                String message = materialApplyApi.materialApplyStorage(bpmProcessInstanceExtDO.getFormVariables());
                //发送消息提醒
                Long startUserId = bpmProcessInstanceExtDO.getStartUserId();
                pushMaterialApplyResultMessage(startUserId, message);
            }
        }
    }

    private void pushMaterialApplyResultMessage(Long startUserId, String message) {
        ThreadUtil.safeSleep(1000);
        CommonResult<String> sysUserAccount = adminUserApi.getSysUserAccount(String.valueOf(startUserId));
        log.info("发送消息参数，message:{},account:{}", message, sysUserAccount.getData());
        imWrapper.sendAccountMessageFromAdmin(message, sysUserAccount.getData());
    }
}
