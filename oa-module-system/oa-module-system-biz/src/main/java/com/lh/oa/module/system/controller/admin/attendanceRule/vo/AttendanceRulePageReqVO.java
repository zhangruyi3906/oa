package com.lh.oa.module.system.controller.admin.attendanceRule.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Time;

@Schema(description = "管理后台 - 打卡规则（部门）分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AttendanceRulePageReqVO extends PageParam {

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "打卡半径（米）")
    private Object punchRadius;

    @Schema(description = "迟到限制时间")
    private Time checkInLimit;

    @Schema(description = "上班时间")
    private Time flexibleCheckInStart;

    @Schema(description = "下班时间")
    private Time flexibleCheckInEnd;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "打卡经度纬度")
    private String latiLong;

    @Schema(description = "打卡纬度")
    private Object longitude;
    private String punchTypeName;
    private String workDays;
    private Integer syncHolidays;

}
