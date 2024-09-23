package com.lh.oa.module.system.controller.admin.projectrecord.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 打卡记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectRecordPageReqVO extends PageParam {

    @Schema(description = "员工ID")
    private Long userId;

    @Schema(description = "签到时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] checkInTime;

    @Schema(description = "签退时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] checkOutTime;

    @Schema(description = "签到状态")
    private Byte checkInStatus;

    @Schema(description = "签退状态")
    private Byte checkOutStatus;

    @Schema(description = "打卡年月日")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate punchDate;

    @Schema(description = "签到类型（0/1矿工/非矿工）")
    private Byte attStatus;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "地点备注")
    private String remark;

    private Long startTime;

    private Long endTime;

    private Long punch;

    private String remarkOut;

    private String remarkIn;

    private String photoUrl;

}