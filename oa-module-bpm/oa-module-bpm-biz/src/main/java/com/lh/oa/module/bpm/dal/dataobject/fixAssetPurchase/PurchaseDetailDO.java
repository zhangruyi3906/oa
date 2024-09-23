package com.lh.oa.module.bpm.dal.dataobject.fixAssetPurchase;

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
import java.util.Date;

@TableName("bpm_purchase_detail")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDetailDO extends BaseDO {
    @TableId
    private Long id;
    /**
     * 固定资产申购id
     */
    private Long fixAssetPurchaseId;
    /**
     * 物品名称
     */
    private String goodsName;
    /**
     * 型号
     */
    private String model;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 单位
     */
    private String unit;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 总价
     */
    private BigDecimal totalPrice;
    /**
     * 购回日期
     */
    private Date buyDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 附件
     */
    private String annex;
}
