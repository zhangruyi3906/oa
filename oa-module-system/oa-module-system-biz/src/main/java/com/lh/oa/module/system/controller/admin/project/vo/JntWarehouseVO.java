package com.lh.oa.module.system.controller.admin.project.vo;

import lombok.Data;

@Data
public class JntWarehouseVO {
    /**
     * 仓库名称
     */
    private String name;
    /**
     * 仓库编码
     */
    private String  code;
    /**
     * 仓库的建能通id
     */
    private String id;
    /**
     * 下拉框选项显示值
     */
    private String label;
    /**
     * 下拉框选中值
     */
    private String value;

    public String getLabel() {
        return this.getName();
    }

    public String getValue() {
        return this.getId();
    }
}
