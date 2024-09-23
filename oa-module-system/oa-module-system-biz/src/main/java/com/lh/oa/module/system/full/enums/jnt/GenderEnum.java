package com.lh.oa.module.system.full.enums.jnt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum GenderEnum {

    WOMAN("WOMAN", "女"),
    MAN("MAN", "男"),
    UNKNOWN("UNKNOWN", "未知");

    private final String key;
    private final String val;

    GenderEnum(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return this.key;
    }

    public String getVal() {
        return this.val;
    }

    public static GenderEnum of(int ordinal) {
        return Arrays.stream(GenderEnum.values()).filter(f -> f.ordinal() == ordinal).findFirst().orElse(UNKNOWN);
    }

    public static GenderEnum of(String val) {
        return Arrays.stream(GenderEnum.values()).filter(f -> StrUtil.equals(f.val, val)).findFirst().orElse(UNKNOWN);
    }

    public static List<Map<String, String>> arr() {
        List<Map<String, String>> list = CollUtil.newArrayList();
        GenderEnum[] arr = GenderEnum.values();

        for (GenderEnum item : arr) {
            Map<String, String> map = MapUtil.newHashMap();
            map.put("key", item.name());
            map.put("val", item.val);
            list.add(map);
        }
        return list;
    }

}
