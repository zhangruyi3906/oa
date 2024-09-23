package com.lh.oa.module.bpm.dal.dataobject.costReimburse;

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

@TableName("bpm_cost_reimburse")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostReimburseDO extends BaseDO {
    @TableId
    private Long id;
    /**
     * 报销人姓名
     */
    private String userName;
    /**
     * 报销人id
     */
    private Long userId;
    /**
     * 报销人部门id
     */
    private Long deptId;
    /**
     * 报销人部门
     */
    private String deptName;
    /**
     * 办公地点
     */
    private String place;
    /**
     * 费用所属公司
     */
    private String companyName;
    /**
     * 付款项目
     */
    private String projectName;
    /**
     * 报销日期
     */
    private Date reimburseDate;
    /**
     * 付款方式
     */
    private String payWay;
    /**
     * 收款人
     */
    private String payee;
    /**
     * 收款账号
     */
    private String account;
    /**
     * 收款银行
     */
    private String bank;
    /**
     * 费用详细说明
     */
    private String illustrate;
    /**
     * 附件
     */
    private String annex;
    /**
     * 报销总额
     */
    private BigDecimal totalPrice;

    private Integer result;
    private String processInstanceId;

}
