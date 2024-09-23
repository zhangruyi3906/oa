package com.lh.oa.module.bpm.enums.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BpmTaskCopyTypeEnum {
    ROLE(10, "角色"),
    DEPT_MEMBER(20, "部门的成员"), // 包括负责人
    DEPT_LEADER(30, "部门的负责人"),
    POST(40, "岗位"),
    USER(50, "用户"),
    SCRIPT(60, "自定义脚本"), // 例如说，发起人所在部门的领导、发起人所在部门的领导的领导
    ;

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String desc;
}
