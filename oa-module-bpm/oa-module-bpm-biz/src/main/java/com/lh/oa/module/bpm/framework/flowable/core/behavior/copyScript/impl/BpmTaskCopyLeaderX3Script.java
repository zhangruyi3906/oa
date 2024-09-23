package com.lh.oa.module.bpm.framework.flowable.core.behavior.copyScript.impl;

import com.lh.oa.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BpmTaskCopyLeaderX3Script extends BpmTaskCopyLeaderAbstractScript {


    @Override
    public Set<Long> calculateTaskCopyUsers(Task task) {
        return calculateTaskCopyUsers(task, 3);
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.LEADER_X3;
    }
}
