package com.lh.oa.module.system.controller.admin.record.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 打卡记录 Excel VO
 *
 * @author
 */
@Data
public class RecordExcelVO {

    @ExcelProperty("打卡记录ID")
    private Long id;

    @ExcelProperty("员工ID")
    private Long userId;

    @ExcelProperty("部门ID")
    private Long departmentId;

    @ExcelProperty("签到时间")
    private LocalDateTime checkInTime;

    @ExcelProperty("签退时间")
    private LocalDateTime checkOutTime;

    @ExcelProperty("签到状态")
    private Byte checkInStatus;

    @ExcelProperty("签退状态")
    private Byte checkOutStatus;

    @ExcelProperty("打卡年月日")
    private LocalDate punchDate;

    @ExcelProperty("签到类型（部门 0，项目 1）")
    private Byte attStatus;

    @ExcelProperty("项目id")
    private Long projectId;

    @ExcelProperty("地点备注")
    private String remark;

}
