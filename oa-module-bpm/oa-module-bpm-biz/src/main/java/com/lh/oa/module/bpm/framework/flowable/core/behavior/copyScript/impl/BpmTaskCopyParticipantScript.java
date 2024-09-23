package com.lh.oa.module.bpm.framework.flowable.core.behavior.copyScript.impl;

import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.util.collection.SetUtils.asSet;

/**
 * 分配给发起人的一级 Leader 审批的 Script 实现类
 *
 * @author
 */
@Component
@Slf4j
public class BpmTaskCopyParticipantScript extends BpmTaskCopyLeaderAbstractScript {

    @Resource
    private RuntimeService runtimeService;

    @Override
    public Set<Long> calculateTaskCopyUsers(Task task) {
        Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
        Object party = variables.get("party");
        if (Objects.isNull(party)) {
            throw new BusinessException("当前流程无当事人，分配抄送人失败");
        }

        if (party instanceof Integer) {
            Integer partyUserId = (Integer) party;
            return asSet(partyUserId.longValue());
        } else if (party instanceof List) {
            List<Integer> partyUserIds = (List<Integer>) party;
            return partyUserIds.stream().map(Integer::longValue).collect(Collectors.toSet());
        } else if (party instanceof String) {
            String partyStr = (String) party;
            return Arrays.stream(partyStr.split(",")).map(Long::parseLong).collect(Collectors.toSet());
        } else {
            throw new BusinessException("当事人数据格式错误，分配抄送人失败");
        }
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.PARTICIPANT;
    }

}
