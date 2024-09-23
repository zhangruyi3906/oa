package com.lh.oa.module.system.controller.admin.erp.vo;

import lombok.Data;

/**
 * erp仓库
 */
@Data
public class ErpWarehouseVO {
    private Integer id;
    private String label;
    private String value;
    /**
     * erp仓库编码
     */
    private String code;
    /**
     * erp仓库名称
     */
    private String name;
    /**
     * 仓库负责人名称
     */
    private String headerName;

    /**
     * 仓库负责人编码
     */
    private String erpHeaderNumber;
    /**
     * 所属erp组织编号
     */
    private String orgNumber;
    /**
     * 组织名称
     */
    private String orgName;
}
