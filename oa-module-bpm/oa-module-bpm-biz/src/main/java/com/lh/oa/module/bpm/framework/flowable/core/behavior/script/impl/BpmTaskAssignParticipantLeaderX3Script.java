package com.lh.oa.module.bpm.framework.flowable.core.behavior.script.impl;

import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

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
public class BpmTaskAssignParticipantLeaderX3Script extends BpmTaskAssignLeaderAbstractScript {

    @Override
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        Map<String, Object> variables = execution.getVariables();
        Object party = variables.get("party");
        if (Objects.isNull(party)) {
            throw new BusinessException("当前流程无当事人，分配审批人失败");
        }

        return getPartyAssigneeUser(party, 3);
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.PARTICIPANT_LEADER_X3;
    }

}
