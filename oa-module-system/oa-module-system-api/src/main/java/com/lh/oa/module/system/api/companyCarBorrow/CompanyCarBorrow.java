package com.lh.oa.module.system.api.companyCarBorrow;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2023-09-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("company_car_borrow")
public class CompanyCarBorrow extends Model<CompanyCarBorrow> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 车辆id
     */
    @TableField("car_id")
    @NotNull(message = "车辆不能为空")
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
    @NotNull(message = "借用理由不能为空")
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
