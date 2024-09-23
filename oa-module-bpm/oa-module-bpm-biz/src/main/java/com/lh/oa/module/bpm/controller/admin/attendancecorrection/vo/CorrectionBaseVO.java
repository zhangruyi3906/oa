package com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

/**
 * 补卡流程 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class CorrectionBaseVO {

    @Schema(description = "申请人ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "申请人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "申请人姓名不能为空")
    private String userName;

    @Schema(description = "补卡原因", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "补卡原因不能为空")
    private String reason;

    @Schema(description = "补卡时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "补卡时间不能为空")
    private Date correctionTime;

    @Schema(description = "月份", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "月份不能为空")
    private String month;

    @Schema(description = "补卡类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "补卡类型不能为空")
    private String type;

    @Schema(description = "流程实例编号")
    private String processInstanceId;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "审批状态不能为空")
    private Integer approvalStatus;

}
