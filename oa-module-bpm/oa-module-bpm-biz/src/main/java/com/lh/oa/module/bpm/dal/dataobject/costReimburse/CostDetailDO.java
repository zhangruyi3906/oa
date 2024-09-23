package com.lh.oa.module.bpm.dal.dataobject.costReimburse;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@TableName("bpm_cost_detail")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostDetailDO extends BaseDO {
    @TableId
    private Long id;
    /**
     * 费用报销申请id
     */
    private Long costReimburseId;
    /**
     * 科目
     */
    private String subject;
    /**
     * 是否为X事业部/售后部
     */
    private Boolean isX;
    /**
     * 设备编号
     */
    private String number;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 规格型号
     */
    private String model;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 摘要
     */
    private String precis;
    /**
     * 报销金额
     */
    private BigDecimal reimburse;
}
