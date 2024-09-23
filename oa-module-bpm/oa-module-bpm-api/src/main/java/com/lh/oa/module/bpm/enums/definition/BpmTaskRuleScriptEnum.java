package com.lh.oa.module.bpm.enums.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BPM 任务规则的脚本枚举
 *
 *
 * @author
 */
@Getter
@AllArgsConstructor
public enum BpmTaskRuleScriptEnum {

    START_USER(10L, ""),

    LEADER_X1(20L, "流程发起人的一级领导"),
    LEADER_X2(21L, "流程发起人的二级领导"),
    LEADER_X3(22L, "流程发起人的三级领导"),
    PARTICIPANT(23L, "流程当事人"),
    PARTICIPANT_LEADER_X1(24L, "流程当事人的一级领导"),
    PARTICIPANT_LEADER_X2(25L, "流程当事人的二级领导"),
    PARTICIPANT_LEADER_X3(26L, "流程当事人的三级领导"),
    TRANSFER_DEPARTMENT_LEADER_X1(27L, "调动部门的一级领导"),
    TRANSFER_DEPARTMENT_LEADER_X2(28L, "调动部门的二级领导"),
    TRANSFER_DEPARTMENT_LEADER_X3(29L, "调动部门的三级领导"),
    GENERAL_MANAGER_OFFICE(30L, "总经办审批"),
    PARTICIPANT_GENERAL_MANAGER_OFFICE(31L, "当事人总经办");

    /**
     * 脚本编号
     */
    private final Long id;
    /**
     * 脚本描述
     */
    private final String desc;

}
