package com.lh.oa.module.bpm.enums.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BpmSaveProcessTypeEnum {

    SAVE(1, "保存"),
    RETRACT(2, "撤回");

    /**
     * 状态
     */
    private final Integer status;
    /**
     * 描述
     */
    private final String desc;

    public String getStatus() {
        return String.valueOf(status);
    }

}
