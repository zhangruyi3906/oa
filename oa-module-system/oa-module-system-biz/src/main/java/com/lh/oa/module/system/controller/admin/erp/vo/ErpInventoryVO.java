package com.lh.oa.module.system.controller.admin.erp.vo;

import lombok.Data;

/**
 * erp库存
 */
@Data
public class ErpInventoryVO {
    private String id;
    private String label;
    private String value;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料型号
     */
    private String materialModel;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * erp仓库编码
     */
    private String warehouseId;
    /**
     * 物料单位
     */
    private String unit;
    /**
     * 库存数量
     */
    private String inventoryCount;
    /**
     * 库存组织
     */
    private String inventoryOrgName;
    /**
     * 库存组织编码
     */
    private String inventoryOrgId;
    /**
     * 仓库负责人名称
     */
    private String headerName;
    /**
     * 库存 仓库名+数量
     */
    private String inventory;
}
