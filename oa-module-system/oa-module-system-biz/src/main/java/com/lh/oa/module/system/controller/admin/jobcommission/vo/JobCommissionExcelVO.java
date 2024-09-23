package com.lh.oa.module.system.controller.admin.jobcommission.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 项目工种提成 Excel VO
 *
 * @author
 */
@Data
public class JobCommissionExcelVO {

    @ExcelProperty("记录编号")
    private Long id;

    @ExcelProperty("项目编号")
    private Long projectId;

    @ExcelProperty("工种id")
    private Long jobId;

    @ExcelProperty("基础提成")
    private BigDecimal baseCommission;

    @ExcelProperty("奖励提成")
    private BigDecimal bonusCommission;

}
