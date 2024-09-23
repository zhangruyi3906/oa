package com.lh.oa.module.system.controller.admin.record.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;


@Schema(description = "管理后台 - 打卡记录 Excel 导出 Request VO，参数和 RecordPageReqVO 是一致的")
@Data
public class RecordExportReqVO {

    @Schema(description = "员工ID")
    private Long userId;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "签到时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] checkInTime;

    @Schema(description = "签退时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] checkOutTime;

    @Schema(description = "签到状态")
    private Byte checkInStatus;

    @Schema(description = "签退状态")
    private Byte checkOutStatus;

    @Schema(description = "打卡年月日")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] punchDate;

    @Schema(description = "签到类型（正常 0，旷工 1）")
    private Byte attStatus;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "地点备注")
    private String remark;

}
