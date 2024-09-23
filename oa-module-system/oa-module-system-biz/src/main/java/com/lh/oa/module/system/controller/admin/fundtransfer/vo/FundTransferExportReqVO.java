package com.lh.oa.module.system.controller.admin.fundtransfer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 资金划拨 Excel 导出 Request VO，参数和 FundTransferPageReqVO 是一致的")
@Data
public class FundTransferExportReqVO {

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "划拨金额")
    private BigDecimal amount;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] createTime;

}
