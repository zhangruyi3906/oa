package com.lh.oa.module.bpm.dal.dataobject.giftGivingSupply;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@TableName("bpm_gift_detail")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftDetailDO extends BaseDO {
    @TableId
    private Long id;
    /**
     * 礼品赠送申请id
     */
    private Long giftGivingSupplyId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 总价
     */
    private BigDecimal totalPrice;
    /**
     * 联系业务
     */
    private String businessType;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 客户地址
     */
    private String customerAddress;
    /**
     * 送礼内容
     */
    private String giftContent;
}
