package com.lh.oa.module.bpm.framework.flowable.core.behavior.script.impl;

import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import com.lh.oa.module.system.api.dept.DeptApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.util.collection.SetUtils.asSet;
/**
 * 分配给流程当事人所在部门对应的总经办的 Script 实现类
 *
 * @author
 */
@Component
@Slf4j
public class BpmTaskAssignParticipantGeneralManagerOfficeScript extends BpmTaskAssignLeaderAbstractScript{
    @Resource
    private DeptApi deptApi;
    @Override
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        Map<String, Object> variables = execution.getVariables();
        Object party = variables.get("party");
        if (Objects.isNull(party)) {
            throw new BusinessException("当前流程无当事人，分配审批人失败");
        }
        Set<Long> partyIds = new HashSet<>();
        if (party instanceof Integer) {
            Integer partyUserId = (Integer) party;
            partyIds = asSet(partyUserId.longValue());
        } else if (party instanceof List) {
            List<Integer> partyUserIds = (List<Integer>) party;
            partyIds = partyUserIds.stream().map(Integer::longValue).collect(Collectors.toSet());
        } else if (party instanceof String) {
            String partyStr = (String) party;
            partyIds = Arrays.stream(partyStr.split(",")).map(Long::parseLong).collect(Collectors.toSet());
        } else {
            throw new BusinessException("当事人数据格式错误，分配审批人失败");
        }
        Set<Long> partyUserIdSet = deptApi.getOfficeDeptLeaderUserIdByUserIds(partyIds).getCheckedData();
        return partyUserIdSet;
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.PARTICIPANT_GENERAL_MANAGER_OFFICE;
    }
}
