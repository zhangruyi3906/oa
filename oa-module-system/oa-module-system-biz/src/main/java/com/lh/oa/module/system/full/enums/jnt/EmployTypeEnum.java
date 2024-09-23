package com.lh.oa.module.system.full.enums.jnt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum EmployTypeEnum {

    INNER_EMPLOY("内聘"),
    OUTER_EMPLOY("外聘");

    private final String val;

    EmployTypeEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

    public static EmployTypeEnum of(String val) {
        return Arrays.stream(EmployTypeEnum.values()).filter(f -> StrUtil.equals(f.val, val)).findFirst().orElse(null);
    }

    public static List<Map<String, String>> arr() {
        List<Map<String, String>> list = CollUtil.newArrayList();
        EmployTypeEnum[] arr = EmployTypeEnum.values();

        for (EmployTypeEnum item : arr) {
            Map<String, String> map = MapUtil.newHashMap();
            map.put("key", item.name());
            map.put("val", item.val);
            list.add(map);
        }
        return list;
    }

}
