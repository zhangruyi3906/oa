package com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 绩效考评申请单
 *
 * @author tanghanlin
 * @since 2023-10-24
 */
@Getter
@Setter
@ToString
public class PerformanceExamineFormParam implements Serializable {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "被考评人所在部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "被考评人所在部门不能为空")
    private Long deptId;

    @Schema(description = "责任部门是否已核实", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean deptVerification;

    @Schema(description = "考评事项", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "考评事项不能为空")
    private String examineDetail;

    @Schema(description = "附件url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String annexUrl;

    @Schema(description = "绩效考评详情列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PerformanceExamineDetailParam> detailList;

}
