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

@TableName("bpm_gift_giving_supply")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftGivingSupplyDO extends BaseDO {
    @TableId
    private Long id;
    /**
     * 赠送人姓名
     */
    private String userName;
    /**
     * 赠送人id
     */
    private Long userId;
    /**
     * 赠送人部门id
     */
    private Long deptId;
    /**
     * 赠送人部门
     */
    private String deptName;
    /**
     * 办公地点
     */
    private String place;

    private Integer result;
    private String processInstanceId;
}
