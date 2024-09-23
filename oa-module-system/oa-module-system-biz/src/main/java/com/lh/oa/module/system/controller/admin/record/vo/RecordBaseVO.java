package com.lh.oa.module.system.controller.admin.record.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 打卡记录 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class RecordBaseVO {

    @Schema(description = "员工ID")
    private Long userId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "签到时间")
    private LocalDateTime checkInTime;

    @Schema(description = "签退时间")
    private LocalDateTime checkOutTime;

    @Schema(description = "签到状态")
    private Byte checkInStatus;

    @Schema(description = "签退状态")
    private Byte checkOutStatus;

    @Schema(description = "打卡年月日")
    private LocalDate punchDate;

    @Schema(description = "签到类型（部门 0，项目 1）")
    private Byte attStatus;


    @Schema(description = "地点备注")
    private String remarkIn;

    private String userName;

    private String deptName;

    private Integer isBusiness;

}
