package com.lh.oa.module.system.full.enums.attendance;

public enum OffsiteClockStateEnum {

    FORBID_OFFSITE("不允许异地打卡"),
    ALLOW_OFFSITE("允许异地打卡");

    private final String val;

    OffsiteClockStateEnum(String val) {
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
