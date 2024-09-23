package com.lh.oa.module.system.full.enums.attendance;

public enum AttendanceStatusEnum {

    NORMAL_ATTENDANCE("范围内打卡"),
    OFFSITE_ATTENDANCE("异地打卡"),
    OFFLINE_ATTENDANCE("离线打卡"),;

    private final String val;

    AttendanceStatusEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

}
