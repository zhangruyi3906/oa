package com.lh.oa.module.system.controller.admin.information.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 员工信息 Excel VO
 *
 * @author
 */
@Data
public class InformationExcelVO {

    @ExcelProperty("员工唯一标识符")
    private Long id;

    @ExcelProperty("员工ID")
    private Long userId;

    @ExcelProperty("员工姓名")
    private String name;

    @ExcelProperty("入职时间")
    private LocalDateTime hireDate;

    @ExcelProperty("是否有试用期（true表示有试用期，false表示没有试用期）")
    private Boolean hasProbation;

    @ExcelProperty("试用期工资比例")
    private BigDecimal probationSalaryRatio;

    @ExcelProperty("工资发放方式")
    private String salaryPaymentMethod;

    @ExcelProperty("银行卡信息")
    private String bankAccount;



    @ExcelProperty("员工基础工资")
    private BigDecimal baseSalary;

    @ExcelProperty("记录创建时间")
    private LocalDateTime createdAt;

    @ExcelProperty("记录最后更新时间")
    private LocalDateTime updatedAt;

    @ExcelProperty("身份证号码")
    private String identityCard;



    @ExcelProperty("离职时间")
    private Date resignTime;

    @ExcelProperty("是否离职")
    private Boolean isResigned;


}
