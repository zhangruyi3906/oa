package com.lh.oa.module.bpm.service.schedule;

import com.lh.oa.module.bpm.dal.dataobject.task.BpmTaskExtDO;
import com.lh.oa.module.bpm.dal.mysql.task.BpmTaskExtMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class TaskTimeOutSchedule {

    @Autowired
    private TaskService taskService;
    @Resource
    private BpmTaskExtMapper taskExtMapper;

    /**
     * 每天清理处于关闭状态超过20天的账户
     */
    @Scheduled(cron = "0 * * * * ?")
    public void completeOvertimeTask(){
        List<Task> taskList = taskService.createTaskQuery().taskDueBefore(new Date()).list();

        taskList.forEach(task -> {
            taskService.complete(task.getId());

            taskExtMapper.updateByTaskId(
                    new BpmTaskExtDO().setTaskId(task.getId()).setResult(BpmProcessInstanceResultEnum.APPROVE.getResult())
                            .setReason("超时自动办理"));
        });
    }
}
