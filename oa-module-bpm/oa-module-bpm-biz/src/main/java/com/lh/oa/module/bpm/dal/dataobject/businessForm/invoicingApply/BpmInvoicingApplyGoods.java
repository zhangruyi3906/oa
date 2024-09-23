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
* 开票申请单货物信息
*
* @author tanghanlin
* @since 2023-10-21
*/
@Getter
@Setter
@Accessors(chain = true)
@TableName("bpm_invoicing_apply_goods")
public class BpmInvoicingApplyGoods extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 开票内容
     */
    private Long bpmInvoicingApplyFormId;

    /**
     * 货物名称
     */
    private String name;

    /**
     * 规格
     */
    private String spec;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 含税单价
     */
    private BigDecimal taxPrice;

    /**
     * 含税金额
     */
    private BigDecimal taxAmount;

}
