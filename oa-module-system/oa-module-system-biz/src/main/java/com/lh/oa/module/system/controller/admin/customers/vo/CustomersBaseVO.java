package com.lh.oa.module.system.controller.admin.customers.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 客户基础信息 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class CustomersBaseVO {

    @Schema(description = "公司名称")
    @NotNull(message = "公司名称不能为空")
    private String companyName;

    @Schema(description = "电子邮件地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "电子邮件地址不能为空")
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

    @Schema(description = "文件url")
//    @NotNull(message = "文件URL不能为空")
    private String url;

    @Schema(description = "文件id")
//    @NotNull(message = "文件id不能为空")
    private Long fileId;

}
