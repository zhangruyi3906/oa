package com.lh.oa.module.system.controller.admin.fundtransfer.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金划拨 Excel VO
 *
 * @author 管理员
 */
@Data
public class FundTransferExcelVO {

    @ExcelProperty("划拨ID")
    private Long id;

    @ExcelProperty("项目ID")
    private Long projectId;

    @ExcelProperty("划拨金额")
    private BigDecimal amount;

    @ExcelProperty("创建时间")
    private Date createTime;

}
