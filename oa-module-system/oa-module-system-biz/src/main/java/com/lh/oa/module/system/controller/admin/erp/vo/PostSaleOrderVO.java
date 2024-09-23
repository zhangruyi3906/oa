package com.lh.oa.module.system.controller.admin.erp.vo;

import lombok.Data;

@Data
public class PostSaleOrderVO {
    /**
     * 工单编号
     */
    private String number;
    /**
     * 工单状态
     */
    private String state;
    private String stateName;
    /**
     * 工单类型
     */
    private String type;
    private String typeName;
    /**
     * 创建时间
     */
    private Integer createdTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 受理时间
     */
    private Integer actualAcceptanceTime;
    /**
     * 受理工程师
     */
    private String engineerName;

    private String label;

    public String getLabel() {
        return number + "-" + engineerName + "-" + typeName;
    }
}
