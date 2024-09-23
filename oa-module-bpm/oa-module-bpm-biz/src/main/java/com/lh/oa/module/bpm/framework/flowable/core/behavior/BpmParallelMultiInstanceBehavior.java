package com.lh.oa.module.bpm.framework.flowable.core.behavior;

import com.lh.oa.framework.flowable.core.util.FlowableUtils;
import com.lh.oa.module.bpm.service.definition.BpmTaskAssignRuleService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Activity;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;

import java.util.Set;

/**
 * @author kemengkai
 * @date 2022-04-21 16:57
 */
@Slf4j
public class BpmParallelMultiInstanceBehavior extends ParallelMultiInstanceBehavior {

    @Setter
    private BpmTaskAssignRuleService bpmTaskRuleService;

    public BpmParallelMultiInstanceBehavior(Activity activity,
                                            AbstractBpmnActivityBehavior innerActivityBehavior) {
        super(activity, innerActivityBehavior);
    }

    /**

     *
     * @param execution 执行任务
     * @return 数量
     */
    @Override
    protected int resolveNrOfInstances(DelegateExecution execution) {
        super.collectionExpression = null;
        super.collectionVariable = FlowableUtils.formatCollectionVariable(execution.getCurrentActivityId());

        super.collectionElementVariable = FlowableUtils.formatCollectionElementVariable(execution.getCurrentActivityId());


        Set<Long> assigneeUserIds = bpmTaskRuleService.calculateTaskCandidateUsers(execution);
        execution.setVariable(collectionVariable, assigneeUserIds);
        return assigneeUserIds.size();
    }

}
