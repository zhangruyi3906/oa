package com.lh.oa.module.bpm.api.task.dto;

import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2023/11/3
 */
@Getter
@Setter
@ToString
public class BpmProcessInstanceTo implements Serializable {

    private Long id;
    /**
     * 发起流程的用户编号
     * <p>
     * 冗余 HistoricProcessInstance 的 startUserId 属性
     */
    private Long startUserId;
    /**
     * 流程实例的名字
     * <p>
     * 冗余 ProcessInstance 的 name 属性，用于筛选
     */
    private String name;
    /**
     * 流程实例的编号
     * <p>
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
    /**
     * 流程定义的编号
     * <p>
     * 关联 ProcessDefinition 的 id 属性
     */
    private String processDefinitionId;
    /**
     * 流程分类
     * <p>
     * 冗余 ProcessDefinition 的 category 属性
     * 数据字典 bpm_model_category
     */
    private String category;
    /**
     * 流程实例的状态
     * <p>
     * 枚举 {@link BpmProcessInstanceStatusEnum}
     */
    private Integer status;
    /**
     * 流程实例的结果
     * <p>
     * 枚举 {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;
    /**
     * 结束时间
     * <p>
     * 冗余 HistoricProcessInstance 的 endTime 属性
     */
    private LocalDateTime endTime;

    /**
     * 提交的表单值
     */
    private Map<String, Object> formVariables;

    /**
     * 表单开始时间
     *
     */
    private LocalDateTime formStartTime;

    /**
     * 表单结束时间
     *
     */
    private LocalDateTime formEndTime;

    /**
     *流程发起人部门id
     *
     */
    private Long deptId;

    /**
     *流程名字
     *
     */
    private String instanceName;

    /**
     *表单是否可见
     */
    private int readAble;

    /**
     * 流程对应的审核人id列表
     */
    private Set<Long> assigneeUserIds;

}
