package com.lh.oa.module.system.controller.admin.customers.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 客户基础信息 Excel VO
 *
 * @author 狗蛋
 */
@Data
public class CustomersExcelVO {

    @ExcelProperty("客户id")
    private Long id;

    @ExcelProperty("公司名称")
    private String companyName;

    @ExcelProperty("电子邮件地址")
    private String email;

    @ExcelProperty("电话号码")
    private String phone;


    @ExcelProperty("地址")
    private String address;

    @ExcelProperty("区、县")
    private String area;

    @ExcelProperty("市")
    private String city;


    @ExcelProperty("区、县id")
    private Long areaId;

    @ExcelProperty("市id")
    private Long cityId;

    @ExcelProperty("省id")
    private Long stateId;



    @ExcelProperty("省/州")
    private String state;


    @ExcelProperty("邮政编码")
    private String postalCode;

    @ExcelProperty("国家")
    private String country;

    @ExcelProperty("客户等级")
    private Integer cusLevel;

}
