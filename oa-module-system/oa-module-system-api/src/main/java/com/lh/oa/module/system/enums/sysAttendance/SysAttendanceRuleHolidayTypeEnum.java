package com.lh.oa.module.system.enums.sysAttendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author tanghanlin
 * @since 2023-11-10
 */
@Getter
@AllArgsConstructor
public enum SysAttendanceRuleHolidayTypeEnum {

    REST(1, "休息日"),
    WORK(2, "工作日");

    /**
     * 类型
     */
    private final Integer code;

    /**
     * 名称
     */
    private final String name;

    private static String getNameByCode(Integer code) {
        for (SysAttendanceRuleHolidayTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getName();
            }
        }
        return "";
    }

}
