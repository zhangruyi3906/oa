package com.lh.oa.module.system.controller.admin.customers.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 客户基础信息 Excel 导出 Request VO，参数和 CustomersPageReqVO 是一致的")
@Data
public class CustomersExportReqVO {

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "电子邮件地址")
    private String email;

    @Schema(description = "电话号码")
    private String phone;


    @Schema(description = "地址")
    private String address;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "省/州")
    private String state;

    @Schema(description = "区、县")
    private String area;

    @Schema(description = "区、县id")
    private Long areaId;

    @Schema(description = "市id")
    private Long cityId;

    @Schema(description = "省id")
    private Long stateId;

    @Schema(description = "邮政编码")
    private String postalCode;

    @Schema(description = "国家")
    private String country;

    @Schema(description = "客户等级")
    private Integer cusLevel;

}
