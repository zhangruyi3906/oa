package com.lh.oa.module.system.enums;

import java.util.Arrays;

public enum DeptTypeEnum {
    ORGANIZATION("组织"),
    DEPARTMENT("部门");

    private final String val;

    DeptTypeEnum(String val) {
        this.val = val;
    }

    public static DeptTypeEnum get(int ordinal) {
        return Arrays.stream(DeptTypeEnum.values()).filter(f -> f.ordinal() == ordinal).findFirst().orElse(null);
    }

    public String getVal() {
        return this.val;
    }

}
