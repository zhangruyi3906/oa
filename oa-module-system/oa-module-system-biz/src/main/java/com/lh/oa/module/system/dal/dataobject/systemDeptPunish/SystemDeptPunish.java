package com.lh.oa.module.system.dal.dataobject.systemDeptPunish;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 部门考勤处罚表
 * </p>
 *
 * @author ${author}
 * @since 2023-09-14
 */
@Data
@TableName("system_dept_punish")
public class SystemDeptPunish extends Model<SystemDeptPunish> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("dept_id")
    private Long deptId;
    @TableField("dept_name")
    private String deptName;

    /**
     * 迟到罚款
     */
    @TableField("late_penalty")
    private BigDecimal latePenalty;
    /**
     * 早退罚款
     */
    @TableField("leave_early_penalty")
    private BigDecimal leaveEarlyPenalty;
    /**
     * 漏签罚款
     */
    @TableField("miss_signature_penalty")
    private BigDecimal missSignaturePenalty;


    private BigDecimal excusedPenalty;

    private BigDecimal retirementScale;
    private BigDecimal treatScale;
    private BigDecimal birthScale;
    private BigDecimal hurtScale;
    private BigDecimal unemploymentScale;
    private BigDecimal fundsScale;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }



    public BigDecimal getLatePenalty() {
        return latePenalty;
    }

    public void setLatePenalty(BigDecimal latePenalty) {
        this.latePenalty = latePenalty;
    }


    public BigDecimal getLeaveEarlyPenalty() {
        return leaveEarlyPenalty;
    }

    public void setLeaveEarlyPenalty(BigDecimal leaveEarlyPenalty) {
        this.leaveEarlyPenalty = leaveEarlyPenalty;
    }


    public BigDecimal getMissSignaturePenalty() {
        return missSignaturePenalty;
    }

    public void setMissSignaturePenalty(BigDecimal missSignaturePenalty) {
        this.missSignaturePenalty = missSignaturePenalty;
    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SystemDeptPunish{" +
                "id=" + id +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", latePenalty=" + latePenalty +
                ", leaveEarlyPenalty=" + leaveEarlyPenalty +
                ", missSignaturePenalty=" + missSignaturePenalty +
                ", excusedPenalty=" + excusedPenalty +
                ", retirementScale=" + retirementScale +
                ", treatScale=" + treatScale +
                ", birthScale=" + birthScale +
                ", hurtScale=" + hurtScale +
                ", unemploymentScale=" + unemploymentScale +
                ", fundsScale=" + fundsScale +
                '}';
    }
}
