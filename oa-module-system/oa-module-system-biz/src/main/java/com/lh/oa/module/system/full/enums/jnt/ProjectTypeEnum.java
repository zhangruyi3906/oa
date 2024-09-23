package com.lh.oa.module.system.full.enums.jnt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

import java.util.List;
import java.util.Map;

public enum ProjectTypeEnum {

    ROAD_BRIDGE("路桥"),
    TUNNEL("隧道"),
    IRRIGATION("水利");

    private final String val;

    ProjectTypeEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

    public static List<Map<String, String>> arr() {
        List<Map<String, String>> list = CollUtil.newArrayList();
        ProjectTypeEnum[] arr = ProjectTypeEnum.values();

        for (ProjectTypeEnum item : arr) {
            Map<String, String> map = MapUtil.newHashMap();
            map.put("key", item.name());
            map.put("val", item.val);
            list.add(map);
        }
        return list;
    }

}
