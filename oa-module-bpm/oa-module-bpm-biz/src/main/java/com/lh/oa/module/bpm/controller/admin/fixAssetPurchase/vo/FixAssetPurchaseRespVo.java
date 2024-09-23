package com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 固定资产申购 ResponseVo")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FixAssetPurchaseRespVo extends FixAssetPurchaseBaseVo {
    @Schema(description = "请假表单主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;
    @Schema(description = "状态,参见 bpm_process_instance_result 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer result;
    @Schema(description = "申请时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTime;
    @Schema(description = "流程id")
    private String processInstanceId;
}