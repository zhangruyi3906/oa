package com.lh.oa.module.system.controller.admin.customerservice.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 客户服务 Excel VO
 *
 * @author 管理员
 */
@Data
public class CustomerServiceExcelVO {

    @ExcelProperty("服务id")
    private Long id;

    @ExcelProperty("客户id")
    private Long customerId;

    @ExcelProperty("服务内容")
    private String serviceContent;

    @ExcelProperty("反馈")
    private String feedback;

    @ExcelProperty("状态")
    private String status;

    @ExcelProperty("创建时间")
    private Date createTime;

}
