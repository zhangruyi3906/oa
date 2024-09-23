package com.lh.oa.module.system.full.enums.jnt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

import java.util.List;
import java.util.Map;

/**
 * 项目来源
 */
public enum ProjectSourceEnum {
    OA("OA添加"),
    PMS("项目管理平台添加");
    private final String val;

    ProjectSourceEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

    public static List<Map<String, String>> arr() {
        List<Map<String, String>> list = CollUtil.newArrayList();
        ProjectSourceEnum[] arr = ProjectSourceEnum.values();

        for (ProjectSourceEnum item : arr) {
            Map<String, String> map = MapUtil.newHashMap();
            map.put("key", item.name());
            map.put("val", item.val);
            list.add(map);
        }
        return list;
    }
}
