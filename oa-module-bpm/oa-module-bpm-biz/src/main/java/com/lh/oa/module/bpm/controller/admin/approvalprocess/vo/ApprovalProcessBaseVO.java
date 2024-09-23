package com.lh.oa.module.bpm.controller.admin.approvalprocess.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.constraints.*;

/**
 * 项目立项 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ApprovalProcessBaseVO {

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目名称不能为空")
    private String projectName;

    @Schema(description = "项目开始日期")
    @NotNull(message = "项目开始日期不能为空")
    private LocalDateTime startDate;

    @Schema(description = "项目结束日期")
    @NotNull(message = "项目结束日期不能为空")
    private LocalDateTime endDate;

    @Schema(description = "项目经理ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目经理ID不能为空")
    private Long projectManagerId;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer approvalStatus;

    @Schema(description = "流程实例的编号")
    private String processInstanceId;

    private Long userId;

}
