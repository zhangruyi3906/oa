package com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
@Schema(description = "管理后台 - 请假申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OfficeSuppliesRequestPageReqVo extends PageParam {
    @Schema(description = "状态,参见 bpm_process_instance_result 枚举", example = "1")
    private Integer result;

    @Schema(description = "请假类型,参见 bpm_oa_type", example = "1")
    private Integer type;

    @Schema(description = "原因,模糊匹配", example = "")
    private String reason;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "申请时间")
    private Date[] createTime;

    private Long startTime;
    private Long endTime;
}
