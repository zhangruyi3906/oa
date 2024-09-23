package com.lh.oa.module.bpm.framework.flowable.core.listener;

import com.lh.oa.module.bpm.framework.flowable.core.listener.enums.MultiInstanceCompleteTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("multiInstanceCompleteWithTypeTask")
@Slf4j
public class MultiInstanceCompleteWithTypeTask implements Serializable {
    public boolean completeTask(DelegateExecution execution, MultiInstanceCompleteTypeEnum type) {
        log.info("总会签数量:{}, 当前获取的会签任务数量:{}, 已经完成的会签任务数量:{}",
                execution.getVariable("nrOfInstances"), execution.getVariable("nrOfActiveInstances"), execution.getVariable("nrOfCompletedInstances"));
        boolean flag;
        switch (type) {
            case ALL: {
                flag = (int) execution.getVariable("nrOfInstances") == (int) execution.getVariable("nrOfCompletedInstances");
                break;
            }
            case ONE: {
                flag = ((int) execution.getVariable("nrOfCompletedInstances") >= 1);
                break;
            }
            default: {
                flag = false;
            }
        }
        log.info("当前意见:{}", flag);
        return flag;
    }
}
