package com.lh.oa.module.system.dal.dataobject.joblevelsalary;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 员工工种等级基础工资 DO
 *
 * @author
 */
@TableName("user_job_level_salary")
//("user_job_level_salary_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobLevelSalaryDO extends BaseDO {

    /**
     * 记录编号
     */
    @TableId
    private Long id;
    /**
     * 工种类型
     */
    private String jobCode;
    /**
     * 技术等级
     */
    private Integer jobLevel;
    /**
     * 员工id
     */
    private Long userId;
    /**
     * 基础工资
     */
    private BigDecimal baseSalary;

}
