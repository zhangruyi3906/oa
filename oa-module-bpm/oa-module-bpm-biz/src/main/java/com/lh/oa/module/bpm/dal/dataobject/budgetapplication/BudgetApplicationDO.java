package com.lh.oa.module.bpm.dal.dataobject.budgetapplication;

import lombok.*;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;

/**
 * 资金预算申请 DO
 *
 * @author 管理员
 */
@TableName("budget_application")
//("budget_application_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetApplicationDO extends BaseDO {

    /**
     * 预算申请ID
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
    /**
     * 审批状态
     */
    private Integer approvalStatus;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    private Long userId;

}
