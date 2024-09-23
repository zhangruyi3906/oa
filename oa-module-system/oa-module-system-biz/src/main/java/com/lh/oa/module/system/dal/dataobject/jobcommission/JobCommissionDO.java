package com.lh.oa.module.system.dal.dataobject.jobcommission;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 项目工种提成 DO
 *
 * @author
 */
@TableName("project_job_commission")
//("project_job_commission_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCommissionDO extends BaseDO {

    /**
     * 记录编号
     */
    @TableId
    private Long id;
    /**
     * 项目编号
     */
    private Long projectId;
    /**
     * 工种id
     */
    private Long jobId;
    /**
     * 基础提成
     */
    private BigDecimal baseCommission;
    /**
     * 奖励提成
     */
    private BigDecimal bonusCommission;

}
