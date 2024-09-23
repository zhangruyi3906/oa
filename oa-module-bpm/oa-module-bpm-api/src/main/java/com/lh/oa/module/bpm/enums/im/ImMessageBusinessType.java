package com.lh.oa.module.bpm.enums.im;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Bpm 消息的枚举
 *
 * @author
 */
@AllArgsConstructor
@Getter
public enum ImMessageBusinessType {

    BPM_TASK_TODO("bpm_task_todo"), // 流程待办任务详情
    BPM_TASK_COPY("bpm_task_copy"); // 流程抄送、流程详情 （都是跳转历史详情）

    /**
     * IM消息业务类型
     */
    private final String businessType;

}
