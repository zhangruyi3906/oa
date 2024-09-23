package com.lh.oa.module.system.full.enums.attendance;

public enum OfflineClockStateEnum {

    FORBID_OFFLINE("不允许离线打卡"),
    ALLOW_OFFLINE("允许离线打卡");

    private final String val;

    OfflineClockStateEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

    @Override
    public String toString() {
        return this.val;
    }

}
