package com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@TableName("bpm_performance_examine_detail")
public class PerformanceExamineDetailVo implements Serializable {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "所在部门", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;

    @Schema(description = "适用文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private String applicableFile;

    @Schema(description = "文件编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileNo;

    @Schema(description = "条款内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String termDetail;

    @Schema(description = "是否是品质问题", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean qualityProblem;

    @Schema(description = "考核原因", requiredMode = Schema.RequiredMode.REQUIRED)
    private String examineReason;

    @Schema(description = "考核情况", requiredMode = Schema.RequiredMode.REQUIRED)
    private String examineSituation;

    @Schema(description = "考核分数", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal examineScore;

}
