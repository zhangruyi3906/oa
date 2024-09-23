package com.lh.oa.module.bpm.framework.flowable.core.behavior.copyScript;

import com.lh.oa.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import org.flowable.task.api.Task;

import java.util.Set;

public interface BpmTaskCopyScript {

    /**
     * 基于执行任务，获得任务的候选用户们
     *
     * @return 候选人用户的编号数组
     */
    Set<Long> calculateTaskCopyUsers(Task task);

    /**
     * 获得枚举值
     *
     * @return 枚举值
     */
    BpmTaskRuleScriptEnum getEnum();
}