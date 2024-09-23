package com.lh.oa.module.system.controller.admin.schedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Schema(description = "管理后台 - 日程管理创建 Request VO")
@Data
@ToString(callSuper = true)
public class ScheduleMonthVO {
    private List<ScheduleRespVO> scheduleList;
    private Date scheduleDate;


}
