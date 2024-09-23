package com.lh.oa.module.bpm.controller.admin.budgetapplication.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.lh.oa.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 资金预算申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BudgetApplicationPageReqVO extends PageParam {

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "预算周期")
    private String budgetPeriod;

    @Schema(description = "预算类型")
    private String budgetType;

    @Schema(description = "预算金额")
    private BigDecimal budgetAmount;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] createTime;

    @Schema(description = "审批状态")
    private Integer approvalStatus;

    @Schema(description = "流程实例的编号")
    private String processInstanceId;

}
