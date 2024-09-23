package com.lh.oa.module.system.api.sysAttendanceRule.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author tanghanlin
 * @since 2023/10/31
 */
@Getter
@Setter
@ToString
public class SysAttendanceRuleTO implements Serializable {

    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 考勤类型，FIXED_ATTENDANCE固定考勤，FREEDOM_ATTENDANCE自由考勤
     */
    private String attendanceType;

    /**
     * 上班时间，只有固定考勤会有这个配置
     */
    private String clockInTime;

    /**
     * 下班时间，只有固定考勤会有这个配置
     */
    private String clockOffTime;

    /**
     * 工作日，自由考勤一般是七天全上，就是1,2,3,4,5,6,7
     */
    private String weekday;

    /**
     * 截止时间，记为第二天，只有自由考勤会有这个配置
     */
    private String cutOffTime;

    /**
     * 同步法定节假日状态，NOT_SYNC_HOLIDAY不同步，SYNC_HOLIDAY同步
     */
    private String legalHolidayState;

    /**
     * 异地打卡状态，FORBID_OFFSITE不允许异地打卡，ALLOW_OFFSITE允许异地打卡
     */
    private String offsiteClockState;

    /**
     * 是否支持异地打卡，目前没啥用，前端默认允许异地打卡
     */
    private String offlineClockState;

    /**
     * 当前规则是否是自由考勤
     *
     * @return 是否是自由考勤
     */
    public Boolean isFreedomRule() {
        return Objects.equals("FREEDOM_ATTENDANCE", this.attendanceType);
    }

}
