package com.lh.oa.module.system.service.worklog.schedule;

import com.lh.oa.module.system.full.service.attendance.SysAttendanceRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Rick
 * @since 2024/2/20
 */

@Slf4j
@Component
public class SysAttendanceSchedule {

    @Resource
    private SysAttendanceRuleService sysAttendanceRuleService;

    /**
     * 每小时的20分和50分检查是否有即将打卡的固定打卡项目，有的话给对应的该项目关联的人员推送打卡提醒
     * 因为固定考勤的时间一般不会随意设置，基本都是整点或者半点，所以提示时间设置为20分和50分比较合适
     */
//    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 20,50 * * * ?")
    public void pushRegularAttendanceRemindMessage() {
        log.info("定时任务-执行固定打卡项目提醒");
        try {
            sysAttendanceRuleService.pushRegularAttendanceRemindMessage();
        } catch (Exception e) {
            log.info("定时任务-执行固定打卡项目提醒:执行异常, e:{}", e.getMessage());
        }
    }

}
