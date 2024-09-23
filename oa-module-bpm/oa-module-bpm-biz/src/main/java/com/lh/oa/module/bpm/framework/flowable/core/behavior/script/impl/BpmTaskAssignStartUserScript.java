package com.lh.oa.module.bpm.framework.flowable.core.behavior.script.impl;

import com.lh.oa.framework.common.util.collection.SetUtils;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.lh.oa.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import com.lh.oa.module.bpm.framework.flowable.core.behavior.script.BpmTaskAssignScript;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 分配给发起人审批的 Script 实现类
 *
 * @author
 */
@Component
public class BpmTaskAssignStartUserScript implements BpmTaskAssignScript {

    @Resource
    @Lazy
    private BpmProcessInstanceExtMapper bpmProcessInstanceExtMapper;
    @Override
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(execution.getProcessInstanceId());
        return SetUtils.asSet(bpmProcessInstanceExtDO.getStartUserId());
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.START_USER;
    }

}
