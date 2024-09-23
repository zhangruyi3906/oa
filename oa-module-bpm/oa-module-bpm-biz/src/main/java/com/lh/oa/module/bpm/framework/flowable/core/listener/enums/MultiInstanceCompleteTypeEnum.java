package com.lh.oa.module.bpm.framework.flowable.core.listener.enums;

/**
 * 多实例节点审批通过条件类型
 */
public enum MultiInstanceCompleteTypeEnum {
    ALL("全部通过"),
    ONE("一人通过");

    private final String val;

    MultiInstanceCompleteTypeEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

}
