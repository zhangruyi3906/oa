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

@TableName("bpm_fix_asset_purchase")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixAssetPurchaseDO extends BaseDO {
    @TableId
    private Long id;
    /**
     * 申请人姓名
     */
    private String userName;
    /**
     * 申请人id
     */
    private Long userId;
    /**
     * 申请人部门id
     */
    private Long deptId;
    /**
     * 申请人部门
     */
    private String deptName;
    /**
     * 办公地点
     */
    private String place;
    /**
     * 申请类型
     */
    private Integer type;
    /**
     * 申请事件
     */
    private Date supportedDate;
    /**
     * 申购原因
     */
    private String reason;
    /**
     * 编号
     */
    private String number;
    /**
     * 合同总价
     */
    private BigDecimal contractValue;
    /**
     * 备注
     */
    private String notes;

    private Integer result;
    private String processInstanceId;
}
