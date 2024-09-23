package com.lh.oa.module.system.dal.dataobject.salarysettlement;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 员工工资结算 DO
 *
 * @author
 */
@TableName("user_salary_settlement")
//("user_salary_settlement_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalarySettlementDO extends BaseDO {

    /**
     * 记录编号
     */
    @TableId
    private Long id;
    /**
     * 员工名字
     * */
    private String userName;
    /**
     * 员工编号
     */
    private Long userId;
    /**
     * 项目编号
     */
    private Long projectId;
    /**
     * 部门编号
     */
    private Long deptId;
    /**
     * 基础工资
     */
    private BigDecimal baseSalary;
    /**
     * 技术提成
     */
    private BigDecimal technicalBonus;
    /**
     * 奖金提成
     */
    private BigDecimal rewardBonus;
    /**
     * 法定扣款
     */
    private BigDecimal statutoryDeduction;
    /**
     * 考勤扣款
     */
    private BigDecimal attendanceDeduction;
    /**
     * 加班工资
     */
    private BigDecimal overtimeSalary;
    /**
     * 实发工资
     */
    private BigDecimal realSalary;
    /**
     * 扣款明细
     */
    private String deductionDetails;

    /**
     * 考勤天数
     */
    private BigDecimal attendanceDays;
    /**
     * 结算日期
     */
    private String  settlementDate;

    private String deptName;

    private String projectName;

}
