package com.lh.oa.module.system.full.enums.attendance;

public enum LegalHolidayStateEnum {

    NOT_SYNC_HOLIDAY("不同步法定节假日"),
    SYNC_HOLIDAY("同步法定节假日");


    private final String val;

    LegalHolidayStateEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

    @Override
    public String toString() {
        return this.val;
    }

}
