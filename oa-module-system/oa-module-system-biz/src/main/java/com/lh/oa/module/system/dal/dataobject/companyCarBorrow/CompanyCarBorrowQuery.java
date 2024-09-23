package com.lh.oa.module.system.dal.dataobject.companyCarBorrow;


import com.baomidou.mybatisplus.annotations.TableField;
import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.module.system.api.companyCar.CompanyCar;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author ${author}
 * @since 2023-09-05
 */
@Data
public class CompanyCarBorrowQuery extends PageParam {

    private Long carId;
    /**
     * 借用人id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 借用人姓名
     */
    private String username;
    /**
     * 借用时间
     */
    @TableField("borrowed_time")
    private Date borrowedTime;
    /**
     * 借用时间
     */
    @TableField("expect_time")
    private Date expectTime;
    /**
     * 归还时间
     */
    @TableField("return_time")
    private Date returnTime;
    /**
     * 借用理由
     */
    @TableField("borrow_reason")
    private String borrowReason;
    /**
     * 审批人id
     */
    @TableField("approver_id")
    private Long approverId;
    /**
     * 审批人姓名
     */
    @TableField("approver_name")
    private String approverName;
    /**
     * 审批意见
     */
    private Long opinion;
    /**
     * 是否删除
     */
    private Boolean deleted;
    /**
     * 租户编号
     */
    @TableField("tenant_id")
    private Long tenantId;

    private String processInstanceId;


    private CompanyCar companyCar;
}