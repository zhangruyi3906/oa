package com.lh.oa.module.system.service.worklog.schedule;

import com.lh.oa.module.system.dal.mysql.worklog.WorkLogMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@EnableScheduling
public class WorkLogSchedule {

    @Resource
    private WorkLogMapper workLogMapper;

    /**
     * 每日为每人新增一条空白日志，并修改8天前的状态
     */
    @Scheduled(cron = "2 0 0 * * ?")
    public void stopEditLog(){
        workLogMapper.insertForAllUsers();
    }
}
