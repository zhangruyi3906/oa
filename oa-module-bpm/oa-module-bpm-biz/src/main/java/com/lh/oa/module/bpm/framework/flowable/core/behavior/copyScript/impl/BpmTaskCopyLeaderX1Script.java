package com.lh.oa.module.bpm.framework.flowable.core.behavior.copyScript.impl;

import com.lh.oa.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 分配给发起人的一级 Leader 审批的 Script 实现类
 *
 * @author
 */
@Component
public class BpmTaskCopyLeaderX1Script extends BpmTaskCopyLeaderAbstractScript {


    @Override
    public Set<Long> calculateTaskCopyUsers(Task task) {
        return calculateTaskCopyUsers(task, 1);
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.LEADER_X1;
    }

}
