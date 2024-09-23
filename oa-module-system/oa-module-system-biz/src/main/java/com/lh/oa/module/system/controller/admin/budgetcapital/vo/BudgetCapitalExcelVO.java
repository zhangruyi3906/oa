package com.lh.oa.module.system.controller.admin.budgetcapital.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金预算 Excel VO
 *
 * @author 管理员
 */
@Data
public class BudgetCapitalExcelVO {

    @ExcelProperty("预算ID")
    private Long id;

    @ExcelProperty("项目ID")
    private Long projectId;

    @ExcelProperty("预算周期")
    private String budgetPeriod;

    @ExcelProperty("预算类型")
    private String budgetType;

    @ExcelProperty("预算金额")
    private BigDecimal budgetAmount;

    @ExcelProperty("创建时间")
    private Date createTime;

}
