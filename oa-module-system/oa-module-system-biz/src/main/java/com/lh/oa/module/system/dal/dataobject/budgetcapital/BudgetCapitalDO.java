package com.lh.oa.module.system.dal.dataobject.budgetcapital;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 资金预算 DO
 *
 * @author 管理员
 */
@TableName("budget_capital")
//("budget_capital_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetCapitalDO extends BaseDO {

    /**
     * 预算ID
     */
    @TableId
    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 预算周期
     */
    private String budgetPeriod;
    /**
     * 预算类型
     */
    private String budgetType;
    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    private String projectName;

}
