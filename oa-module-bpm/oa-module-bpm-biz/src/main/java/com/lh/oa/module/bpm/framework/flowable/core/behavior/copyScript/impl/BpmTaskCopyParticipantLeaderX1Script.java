package com.lh.oa.module.bpm.framework.flowable.core.behavior.copyScript.impl;

import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 分配给发起人的一级 Leader 审批的 Script 实现类
 *
 * @author
 */
@Component
@Slf4j
public class BpmTaskCopyParticipantLeaderX1Script extends BpmTaskCopyLeaderAbstractScript {
    @Resource
    private RuntimeService runtimeService;

    @Override
    public Set<Long> calculateTaskCopyUsers(Task task) {
        Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
        Object party = variables.get("party");
        if (Objects.isNull(party)) {
            throw new BusinessException("当前流程无当事人，分配抄送人失败");
        }

        return getPartyCopyeeUser(party, 1);
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.PARTICIPANT_LEADER_X1;
    }

}
