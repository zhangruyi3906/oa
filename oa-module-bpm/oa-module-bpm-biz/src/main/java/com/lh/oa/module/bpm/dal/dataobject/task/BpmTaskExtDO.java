package com.lh.oa.module.bpm.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.lh.oa.framework.mybatis.core.type.StringListTypeHandler;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Bpm 流程任务的拓展表
 * 主要解决 Flowable Task 和 HistoricTaskInstance 不支持拓展字段，所以新建拓展表
 *
 * @author
 */
@TableName(value = "bpm_task_ext", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskExtDO extends BaseDO {

    /**
     * 编号，自增
     */
    @TableId
    private Long id;

    /**
     * 任务的审批人
     *
     * 冗余 Task 的 assignee 属性
     */
    private Long assigneeUserId;
    /**
     * 任务的名字
     *
     * 冗余 Task 的 name 属性，为了筛选
     */
    private String name;
    /**
     * 任务的编号
     *
     * 关联 Task 的 id 属性
     */
    private String taskId;
//    /**
//     * 任务的标识
//     *
//     * 关联 {@link Task#getTaskDefinitionKey()}
//     */
//    private String definitionKey;
    /**
     * 任务的结果
     *
     * 枚举 {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;
    /**
     * 审批建议
     */
    private String reason;
    /**
     * 任务的结束时间
     *
     * 冗余 HistoricTaskInstance 的 endTime  属性
     */
    private LocalDateTime endTime;

    /**
     * 流程实例的编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
    /**
     * 流程定义的编号
     *
     * 关联 ProcessDefinition 的 id 属性
     */
    private String processDefinitionId;

    /**
     * 任务是否已读 0-未读 1-已读
     */
    private Integer readState;

    /**
     * 任务关联表单
     */
    private String formKey;

    /**
     * 提交的表单值
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> formVariables;

    /**
     * 表单的配置,JSON 字符串
     */
    private String formConf;

    /**
     * 表单项的数组,JSON 字符串的数组
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> formFields;
    /**
     *表单是否可见
     */
    private int readAble;

    /**
     * 节点可编辑表单字段
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> editFields;

    /**
     * 节点文件下载字段
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> downloadFields;
    /**
     * 节点可编辑表单字段所在表单
     */
    private Long formId;
    /**
     * 默认抄送人Ids
     */
    private String copyUserIds;

}
