package com.lh.oa.module.system.full.enums.jnt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

import java.util.List;
import java.util.Map;

public enum ProjectCategoryEnum {

    SYSTEM_PROJECT("系统项目"),
    POST_SALE_MANAGE_PROJECT("售后管理项目"),
    POST_SALE_CONSTRUCTION_PROJECT("售后施工项目"),
    ;

    private final String val;

    ProjectCategoryEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

    public static List<Map<String, String>> arr() {
        List<Map<String, String>> list = CollUtil.newArrayList();
        ProjectCategoryEnum[] arr = ProjectCategoryEnum.values();

        for (ProjectCategoryEnum item : arr) {
            Map<String, String> map = MapUtil.newHashMap();
            map.put("key", item.name());
            map.put("val", item.val);
            list.add(map);
        }
        return list;
    }

}
