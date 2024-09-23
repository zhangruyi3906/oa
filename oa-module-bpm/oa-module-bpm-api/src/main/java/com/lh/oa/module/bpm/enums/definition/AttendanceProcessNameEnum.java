package com.lh.oa.module.bpm.enums.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 考勤相关流程的类型枚举
 *
 * @author tanghanlin
 * @since 2023/12/19
 */
@Getter
@AllArgsConstructor
public enum AttendanceProcessNameEnum {

    LATE("请假流程", "qingjia"),
    EARLY("加班流程", "jiaban"),
    MISS("出差流程", "chuchai"),
    ABSENT("考勤补充说明流程", "kaoqin");

    /**
     * 类型
     */
    private final String label;

    /**
     * 描述
     */
    private final String value;

    public static String getValueByLabel(String label) {
        for (AttendanceProcessNameEnum nameEnum : values()) {
            if (Objects.equals(label, nameEnum.getLabel())) {
                return nameEnum.getValue();
            }
        }
        return "";
    }

}
