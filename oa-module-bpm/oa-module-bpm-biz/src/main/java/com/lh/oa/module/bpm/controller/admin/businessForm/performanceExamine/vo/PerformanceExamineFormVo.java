package com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class PerformanceExamineFormVo implements Serializable {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "被考评人所在部门", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;

    @Schema(description = "责任部门是否已核实", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean deptVerification;

    @Schema(description = "考评事项", requiredMode = Schema.RequiredMode.REQUIRED)
    private String examineDetail;

    @Schema(description = "附件url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String annexUrl;

    @Schema(description = "绩效考评详情列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PerformanceExamineDetailVo> detailVoList;

    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createTime;

    @Schema(description = "状态,参见 bpm_process_instance_result 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer result;

    @Schema(description = "流程id")
    private String processInstanceId;

}
