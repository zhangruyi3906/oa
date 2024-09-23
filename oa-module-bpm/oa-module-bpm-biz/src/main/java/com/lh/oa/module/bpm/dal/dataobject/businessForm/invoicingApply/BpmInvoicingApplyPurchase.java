package com.lh.oa.module.bpm.dal.dataobject.businessForm.invoicingApply;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* 开票申请单采购信息
*
* @author tanghanlin
* @since 2023-10-21
*/
@Getter
@Setter
@Accessors(chain = true)
@TableName("bpm_invoicing_apply_purchase")
public class BpmInvoicingApplyPurchase extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 开票申请单id
     */
    private Long bpmInvoicingApplyFormId;

    /**
     * 是否老金蝶系统
     */
    private Boolean oldJindieSys;

    /**
     * 销售订单号
     */
    private String purchaseOrderNo;

    /**
     * 销售金额
     */
    private BigDecimal purchaseAmount;

}
