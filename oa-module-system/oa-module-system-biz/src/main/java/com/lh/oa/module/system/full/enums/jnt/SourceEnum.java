package com.lh.oa.module.system.full.enums.jnt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

import java.util.List;
import java.util.Map;

public enum SourceEnum {

    DEFAULT("系统默认"),
    OA("OA系统"),
    CUSTOM("自定义");

    private final String val;

    SourceEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

    public static List<Map<String, String>> arr() {
        List<Map<String, String>> list = CollUtil.newArrayList();
        SourceEnum[] arr = SourceEnum.values();

        for (SourceEnum item : arr) {
            Map<String, String> map = MapUtil.newHashMap();
            map.put("key", item.name());
            map.put("val", item.val);
            list.add(map);
        }
        return list;
    }

}
