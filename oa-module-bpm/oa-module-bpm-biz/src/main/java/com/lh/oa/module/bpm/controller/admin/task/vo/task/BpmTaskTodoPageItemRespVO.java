package com.lh.oa.module.bpm.controller.admin.task.vo.task;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.lh.oa.framework.mybatis.core.type.StringListTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 流程任务的 Running 进行中的分页项 Response VO")
@Data
public class BpmTaskTodoPageItemRespVO {

    @Schema(description = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String id;

    @Schema(description = "任务名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    private String name;

    @Schema(description = "接收时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime claimTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "激活状态,参见 SuspensionState 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer suspensionState;

    /**
     * 任务是否已读 0-未读 1-已读
     */
    @Schema(description = "已读状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer readState;

    @Schema(description = "任务表单", requiredMode = Schema.RequiredMode.REQUIRED)
    private String formKey;

    @Schema(description = "任务表单配置", requiredMode = Schema.RequiredMode.REQUIRED)
    private String formConf;

    @Schema(description = "表单项数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> formFields;

    @Schema(description = "表单参数", requiredMode = Schema.RequiredMode.REQUIRED)
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> formVariables;

    @Schema(description = "流程名字", requiredMode = Schema.RequiredMode.REQUIRED)
    private String instanceName;

    @Schema(description = "表单是否可读，0不可读", requiredMode = Schema.RequiredMode.REQUIRED)
    private int readAble;

    /**
     * 节点可编辑表单字段
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> editFields;
    /**
     * 节点可编辑表单字段
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
    /**
     * 默认抄送人姓名s
     */
    private String copyUsernames;
    /**
     * 所属流程实例
     */
    private ProcessInstance processInstance;
    /**
     * 是否可取消
     */
    private Boolean cancelAble;

    @Data
    @Schema(description = "流程实例")
    public static class ProcessInstance {

        @Schema(description = "流程实例编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private String id;

        @Schema(description = "流程实例名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
        private String name;

        @Schema(description = "发起人的用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private Long startUserId;

        @Schema(description = "发起人的用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
        private String startUserNickname;

        @Schema(description = "流程定义的编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
        private String processDefinitionId;

    }

}