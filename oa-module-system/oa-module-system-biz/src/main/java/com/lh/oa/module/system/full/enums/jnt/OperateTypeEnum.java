package com.lh.oa.module.system.full.enums.jnt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

import java.util.List;
import java.util.Map;

public enum OperateTypeEnum {

    ADD("新增"),
    UPDATE("修改"),
    DELETE("删除"),
    GET("查询");

    private final String val;

    OperateTypeEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

    public static List<Map<String, String>> arr() {
        List<Map<String, String>> list = CollUtil.newArrayList();
        OperateTypeEnum[] arr = OperateTypeEnum.values();

        for (OperateTypeEnum item : arr) {
            Map<String, String> map = MapUtil.newHashMap();
            map.put("key", item.name());
            map.put("val", item.val);
            list.add(map);
        }
        return list;
    }

}
