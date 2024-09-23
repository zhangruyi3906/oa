package com.lh.oa.module.bpm.framework.flowable.core.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author zhangfan
 * @since 2023/10/20 11:21
 */
@Component("multiInstanceCompleteTask")
@Slf4j
public class MultiInstanceCompleteTask implements Serializable {

    public boolean completeTask(DelegateExecution execution) {
        log.info("总会签数量:{}, 当前获取的会签任务数量:{}, 已经完成的会签任务数量:{}",
                execution.getVariable("nrOfInstances"), execution.getVariable("nrOfActiveInstances"), execution.getVariable("nrOfCompletedInstances"));
        boolean flag = (int) execution.getVariable("nrOfInstances") == (int) execution.getVariable("nrOfCompletedInstances");
        log.info("当前意见:{}", flag);
        return flag;
    }


}
