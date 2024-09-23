package com.lh.oa.module.bpm.framework.flowable.core.behavior.copyScript.impl;

import com.lh.oa.framework.common.util.collection.SetUtils;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.lh.oa.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import com.lh.oa.module.bpm.framework.flowable.core.behavior.copyScript.BpmTaskCopyScript;
import org.flowable.task.api.Task;
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
public class BpmTaskCopyStartUserScript implements BpmTaskCopyScript {

    @Resource
    @Lazy
    private BpmProcessInstanceExtMapper bpmProcessInstanceExtMapper;

    @Override
    public Set<Long> calculateTaskCopyUsers(Task task) {
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(task.getProcessInstanceId());
        return SetUtils.asSet(bpmProcessInstanceExtDO.getStartUserId());
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.START_USER;
    }

}
