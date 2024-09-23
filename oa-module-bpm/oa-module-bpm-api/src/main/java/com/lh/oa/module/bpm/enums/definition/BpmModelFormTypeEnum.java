package com.lh.oa.module.bpm.enums.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BPM 模型的表单类型的枚举
 *
 * @author
 */
@Getter
@AllArgsConstructor
public enum BpmModelFormTypeEnum {

    NORMAL(10, "流程表单"), //
    CUSTOM(20, "业务表单") //
    ;

    private final Integer type;
    private final String desc;
}
