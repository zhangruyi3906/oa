package com.lh.oa.module.system.full.enums.jnt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum ProjectAttendanceStateEnum {

    NONE("否"),
    YEAH("是");

    private final String val;

    ProjectAttendanceStateEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

    public static ProjectAttendanceStateEnum of(String val) {
        return Arrays.stream(ProjectAttendanceStateEnum.values()).filter(f -> StrUtil.equals(f.val, val)).findFirst().orElse(null);
    }

    public static List<Map<String, String>> arr() {
        List<Map<String, String>> list = CollUtil.newArrayList();
        ProjectAttendanceStateEnum[] arr = ProjectAttendanceStateEnum.values();

        for (ProjectAttendanceStateEnum item : arr) {
            Map<String, String> map = MapUtil.newHashMap();
            map.put("key", item.name());
            map.put("val", item.val);
            list.add(map);
        }
        return list;
    }

}
