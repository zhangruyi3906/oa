package com.lh.oa.module.bpm.enums.task;

import com.lh.oa.framework.common.util.object.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程实例的结果
 *
 * @author jason
 */
@Getter
@AllArgsConstructor
public enum BpmProcessInstanceResultEnum {

    PROCESS(1, "处理中"),
    APPROVE(2, "通过"),
    REJECT(3, "退回"),
    CANCEL(4, "已取消"),

    // ========== 流程任务独有的状态 ==========

    BACK(5, "回退"),
    RECALL(6, "撤回");;

    /**
     * 结果
     *
     */
    private final Integer result;
    /**
     * 描述
     */
    private final String desc;

    /**
     *
     * @param result 结果
     * @return 是否
     */
    public static boolean isEndResult(Integer result) {
        return ObjectUtils.equalsAny(result, APPROVE.getResult(), REJECT.getResult(), CANCEL.getResult(), BACK.getResult());
    }

}
