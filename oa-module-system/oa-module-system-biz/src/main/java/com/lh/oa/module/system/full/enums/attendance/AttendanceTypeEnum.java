package com.lh.oa.module.system.full.enums.attendance;

import java.util.Arrays;

public enum AttendanceTypeEnum {
    FIXED_ATTENDANCE("固定考勤"),
    FREEDOM_ATTENDANCE("自由考勤");

    private final String val;

    AttendanceTypeEnum(String val) {
        this.val = val;
    }

    public static AttendanceTypeEnum get(int ordinal) {
        return Arrays.stream(AttendanceTypeEnum.values()).filter(f -> f.ordinal() == ordinal).findFirst().orElse(null);
    }

    public String getVal() {
        return this.val;
    }

    @Override
    public String toString() {
        return this.val;
    }
}
