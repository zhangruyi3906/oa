package com.lh.oa.module.system.dal.dataobject.fundtransfer;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 资金划拨 DO
 *
 * @author 管理员
 */
@TableName("fund_transfer")
//("fund_transfer_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundTransferDO extends BaseDO {

    /**
     * 划拨ID
     */
    @TableId
    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 划拨金额
     */
    private BigDecimal amount;
    private String projectName;

}
