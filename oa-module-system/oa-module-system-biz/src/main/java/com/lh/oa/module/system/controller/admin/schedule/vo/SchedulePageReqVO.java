package com.lh.oa.module.system.controller.admin.schedule.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 日程管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SchedulePageReqVO extends PageParam {

    @Schema(description = "日程标题")
    private String title;

    @Schema(description = "日程描述")
    private String description;

    @Schema(description = "日程时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Time[] expireTime;

    private Long startTime;

    private Long endTime;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "是否过期")
    private Boolean expired;

    @Schema(description = "创建者id")
    private Long userId;

    @Schema(description = "日程日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] expireDate;

    @Schema(description = "开始时间")
    private Date scheStartTime;

    @Schema(description = "结束时间")
    private Date scheEndTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "日程状态")
    private Integer status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expireDateDay;

}
