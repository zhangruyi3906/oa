package com.lh.oa.module.system.dal.dataobject.systemDeptPunish;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.lh.oa.framework.common.pojo.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *
 * @author ${author}
 * @since 2023-09-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemDeptPunishQuery extends PageParam {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("dept_id")
    private Long deptId;
    @TableField("dept_name")
    private String deptName;
    /**
     * 允许迟到次数
     */
    @TableField("allow_late_count")
    private Integer allowLateCount;
    /**
     * 迟到罚款
     */
    @TableField("late_penalty")
    private BigDecimal latePenalty;
    /**
     * 允许早退次数
     */
    @TableField("allow_leave_early_count")
    private Integer allowLeaveEarlyCount;
    /**
     * 早退罚款
     */
    @TableField("leave_early_penalty")
    private BigDecimal leaveEarlyPenalty;
    /**
     * 允许漏签次数
     */
    @TableField("allow_miss_signature_count")
    private Integer allowMissSignatureCount;
    /**
     * 漏签罚款
     */
    @TableField("miss_signature_penalty")
    private BigDecimal missSignaturePenalty;
    /**
     * 允许请假次数
     */
    @TableField("allow_excused_count")
    private Integer allowExcusedCount;

    private BigDecimal excusedPenalty;

    private BigDecimal retirementScale;
    private BigDecimal treatScale;
    private BigDecimal birthScale;
    private BigDecimal hurtScale;
    private BigDecimal unemploymentScale;
    private BigDecimal fundsScale;
}