package com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 绩效考评申请单详情
 *
 * @author tanghanlin
 * @since 2023-10-24
 */
@Getter
@Setter
@ToString
public class PerformanceExamineDetailParam implements Serializable {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "姓名不能为空")
    private Long userId;

    @Schema(description = "所在部门", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所在部门不能为空")
    private Long deptId;

    @Schema(description = "适用文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer applicableFile;

    @Schema(description = "文件编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileNo;

    @Schema(description = "条款内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String termDetail;

    @Schema(description = "是否是品质问题", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean qualityProblem;

    @Schema(description = "考核原因", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "考核原因不能为空")
    private String examineReason;

    @Schema(description = "考核情况", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "考核情况不能为空")
    private Integer examineSituation;

    @Schema(description = "考核分数", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal examineScore;

}
