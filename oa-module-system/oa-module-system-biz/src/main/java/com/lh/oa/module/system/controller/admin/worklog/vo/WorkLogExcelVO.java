package com.lh.oa.module.system.controller.admin.worklog.vo;

import lombok.*;
import java.util.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 员工工作日志 Excel VO
 *
 * @author 管理员
 */
@Data
public class WorkLogExcelVO {

    @ExcelProperty("日志ID")
    private Long id;

    @ExcelProperty("员工ID")
    private Long userId;

    @ExcelProperty("日志日期")
    private Date logDate;

    @ExcelProperty("日志内容")
    private String logContent;

    @ExcelProperty("创建时间")
    private Date createdAt;

    @ExcelProperty("字段描述")
    private String description;

    @ExcelProperty("是否可修改")
    private Boolean isEditable;

    @ExcelProperty("部门id")
    private Long deptId;

}
