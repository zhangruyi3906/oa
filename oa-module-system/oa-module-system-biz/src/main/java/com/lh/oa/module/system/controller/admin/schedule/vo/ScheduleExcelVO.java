package com.lh.oa.module.system.controller.admin.schedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 日程管理 Excel VO
 *
 * @author didida
 */
@Data
public class ScheduleExcelVO {

    @ExcelProperty("日程记录ID")
    private Long id;

    @ExcelProperty("日程标题")
    private String title;

    @ExcelProperty("日程描述")
    private String description;

    @ExcelProperty("日程时间")
    private LocalDateTime expireTime;


    @ExcelProperty("是否过期")
    private Boolean expired;

    @ExcelProperty("创建者id")
    private Long userId;

    @ExcelProperty("日程日期")
    private String expireDate;

}
