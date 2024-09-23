package com.lh.oa.module.system.full.enums.attendance;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

import java.util.List;
import java.util.Map;

public enum AttendanceClockTypeEnum {

    CLOCK_IN("上班"),
    CLOCK_OFF("下班");

    private final String val;

    AttendanceClockTypeEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

}
