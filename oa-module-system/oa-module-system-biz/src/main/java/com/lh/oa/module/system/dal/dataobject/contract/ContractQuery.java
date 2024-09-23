package com.lh.oa.module.system.dal.dataobject.contract;


import com.baomidou.mybatisplus.annotations.TableField;
import com.lh.oa.framework.common.pojo.PageParam;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author ${author}
 * @since 2023-09-08
 */
@Data
public class ContractQuery extends PageParam {

    private Long id;
    /**
     * 合同类型(0采购合同，1劳务分包合同)
     */
    private Integer type;
    /**
     * 合同类别（0初始合同，2补充合同）
     */
    private Integer category;
    /**
     * 单位名
     */
    @TableField("customer_name")
    private String customerName;

    @TableField("customer_id")
    private Long customerId;
    /**
     * 合同名
     */
    @TableField("contract_name")
    private String contractName;
    /**
     * 合同文件地址
     */
    private String url;
    /**
     * 文件id
     */
    @TableField("file_id")
    private Long fileId;
    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 付款计划（进度款+尾款，预付款+进度款+尾款）
     */
    @TableField("pay_schedule")
    private String paySchedule;
    /**
     * 付款方式（一般转账，电汇）
     */
    @TableField("pay_way")
    private String payWay;
    @TableField("parent_id")
    private Long parentId;
}