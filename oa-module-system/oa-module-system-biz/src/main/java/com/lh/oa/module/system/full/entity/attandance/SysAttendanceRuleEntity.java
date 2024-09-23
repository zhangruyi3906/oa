package com.lh.oa.module.system.full.entity.attandance;

import com.lh.oa.module.system.full.entity.attandance.vo.SysAttendanceRuleHolidayVo;
import com.lh.oa.module.system.full.entity.base.BaseEntity;
import com.lh.oa.module.system.full.enums.attendance.AttendanceTypeEnum;
import com.lh.oa.module.system.full.enums.attendance.LegalHolidayStateEnum;
import com.lh.oa.module.system.full.enums.attendance.OfflineClockStateEnum;
import com.lh.oa.module.system.full.enums.attendance.OffsiteClockStateEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysAttendanceRuleEntity extends BaseEntity {
    private Integer deptId;
    private Integer projectId;
    private AttendanceTypeEnum attendanceType;
    private String clockInTime;
    private String clockOffTime;
    private String weekday;
    private String cutOffTime;
    private LegalHolidayStateEnum legalHolidayState;
    private OffsiteClockStateEnum offsiteClockState;
    private OfflineClockStateEnum offlineClockState;

    /**
     * 午休开始时间
     */
    private String noonRestStartTime;

    /**
     * 午休结束时间
     */
    private String noonRestEndTime;

    /**
     * 是否启用自定义节假日，0否1是，默认不启用
     */
    private Boolean enableCustomHoliday;

    private List<SysAttendanceRulePositionEntity> attendRulePositionList;
    private String weekdayVal;

    private String deptName;

    private String projectName;
    private SysAttendanceRecordEntity attendanceRecord;

    /**
     * 自定义节假日列表
     */
    private List<SysAttendanceRuleHolidayVo> customHolidayList;

    public String getAttendanceTypeVal() {
        return attendanceType == null ? null : attendanceType.getVal();
    }

    public String getLegalHolidayStateVal() {
        return legalHolidayState == null ? null : legalHolidayState.getVal();
    }

    public String getOffsiteClockStateVal() {
        return offsiteClockState == null ? null : offsiteClockState.getVal();
    }

    /**
     * 当前规则是否是自由考勤
     *
     * @return 是否是自由考勤
     */
    public Boolean isFreedomRule() {
        return Objects.equals(AttendanceTypeEnum.FREEDOM_ATTENDANCE, this.attendanceType);
    }

    /**
     * 获取规则的业务表达字符串，用来判断两个规则在业务上是否是一样的
     *
     * @return 规则表达字符串
     */
    public String getRuleBusinessStr() {
        // 没有启用自定义节假日，才能视作是同一个规则，如果启用，不管配置的是否相同，暂时全部视作不同的规则
        String customFlag = Objects.equals(true, this.getEnableCustomHoliday()) ? this.getProjectId().toString() : "0";
        return this.getAttendanceType() + "-" + ifNull(this.getClockInTime()) + "-" + ifNull(this.getClockOffTime()) + "-"
                + ifNull(this.getWeekday()) + "-" + ifNull(this.getCutOffTime()) + "-" + this.getLegalHolidayState() + "-"
                + this.getOffsiteClockState() + "-" + this.getOfflineClockState() + "-" + ifNull(this.getNoonRestStartTime()) + "-"
                + ifNull(this.getNoonRestEndTime()) + "-" + customFlag;
    }

    private String ifNull(Object obj) {
        return Objects.isNull(obj) ? "" : obj.toString();
    }

}