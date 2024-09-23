package com.lh.oa.module.system.full.enums.attendance;

public enum AttendanceClockStatusEnum {

    NORMAL_CLOCK("正常"),
//    LEAVE_CLOCK("请假"),
    LATE_CLOCK("迟到"),
    EARLY_CLOCK("早退"),
//    ABSENT_CLOCK("旷工"),
//    MISS_CLOCK("缺卡"),
    NOT_CLOCK("未打卡");

    private final String val;

    AttendanceClockStatusEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

}
