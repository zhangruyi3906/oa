package com.lh.oa.module.system.controller.admin.joblevelsalary.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 员工工种等级基础工资 Excel VO
 *
 * @author
 */
@Data
public class JobLevelSalaryExcelVO {

    @ExcelProperty("记录编号")
    private Long id;

    @ExcelProperty("工种类型")
    private String jobCode;

    @ExcelProperty("技术等级")
    private Integer jobLevel;

    @ExcelProperty("员工id")
    private Long userId;

    @ExcelProperty("基础工资")
    private BigDecimal baseSalary;

}
