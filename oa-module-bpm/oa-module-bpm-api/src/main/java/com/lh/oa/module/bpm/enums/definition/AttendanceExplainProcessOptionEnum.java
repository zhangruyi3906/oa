package com.lh.oa.module.bpm.enums.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 考勤补充说明流程的类型枚举
 *
 * @author tanghanlin
 * @since 2023/12/19
 */
@Getter
@AllArgsConstructor
public enum AttendanceExplainProcessOptionEnum {

    LATE("1", "迟到"),
    EARLY("2", "早退"),
    MISS("3", "漏签"),
    ABSENT("4", "旷工");

    /**
     * 类型
     */
    private final String value;

    /**
     * 描述
     */
    private final String label;

}
