package com.lh.oa.module.bpm.framework.flowable.core.behavior.copyScript.impl;

import com.lh.oa.framework.common.util.collection.SetUtils;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.lh.oa.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import com.lh.oa.module.system.api.dept.DeptApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 分配给流程发起人所在部门对应的总经办的 Script 实现类
 *
 * @author
 */
@Component
@Slf4j
public class BpmTaskCopyGeneralManagerOfficeScript extends BpmTaskCopyLeaderAbstractScript {
    @Resource
    private BpmProcessInstanceExtMapper bpmProcessInstanceExtMapper;
    @Resource
    private DeptApi deptApi;

    @Override
    public Set<Long> calculateTaskCopyUsers(Task task) {
        String processInstanceId = task.getProcessInstanceId();
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(processInstanceId);
        Long startUserId = bpmProcessInstanceExtDO.getStartUserId();
        Long officeDeptLeaderUserId = deptApi.getOfficeDeptLeaderUserIdByUserId(startUserId).getCheckedData();
        return SetUtils.asSet(officeDeptLeaderUserId);
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.GENERAL_MANAGER_OFFICE;
    }
}
