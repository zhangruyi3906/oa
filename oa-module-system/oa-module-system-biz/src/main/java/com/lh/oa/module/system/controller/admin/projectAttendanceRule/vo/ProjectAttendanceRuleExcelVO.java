package com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 打卡规则（项目） Excel VO
 *
 * @author
 */
@Data
public class ProjectAttendanceRuleExcelVO {

    @ExcelProperty("项目规则id")
    private String punchName;

    @ExcelProperty("用户id")
    private Long userId;

    @ExcelProperty("项目id")
    private Long projectId;

    @ExcelProperty("打卡半径")
    private String punchRadius;

    @ExcelProperty("打卡经纬度")
    private String latiLong;

    @ExcelProperty("项目")
    private String projectName;

}
