package com.lh.oa.module.bpm.controller.admin.definition.vo.rule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 流程任务分配规则 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class BpmTaskAssignRuleBaseVO {

    @Schema(description = "规则类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "bpm_task_assign_rule_type")
    @NotNull(message = "规则类型不能为空")
    private Integer type;

    @Schema(description = "规则值数组", requiredMode = Schema.RequiredMode.REQUIRED, example = "1,2,3")
    @NotNull(message = "规则值数组不能为空")
    private Set<Long> options;
    /**
     * 退回的节点任务标识
     */
    private String rejectToTaskDefinitionKey;

    /**
     * 节点可编辑表单字段
     */
    private List<String> editFields;
    /**
     * 节点文件下载字段
     */
    private List<String> downloadFields;
    /**
     * 节点可编辑表单字段所在表单
     */
    private Long formId;
    /**
     * 默认抄送人Ids
     */
    private String copyUserIds;
    /**
     * 抄送用户数组
     */
    private Set<Long> copyUserOptions;
    /**
     * 抄送岗位数组
     */
    private Set<Long> copyPostOptions;
    /**
     * 抄送脚本数组
     */
    private Set<Long> copyScriptOptions;
}