package com.lh.oa.module.system.dal.dataobject.contract;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2023-09-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("contract")
public class Contract extends Model<Contract> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 合同类型(0采购合同，1劳务分包合同)
     */
    @NotNull(message = "合同类型不能为空")
    private Integer type;
    /**
     * 合同类别（0初始合同，1补充合同）
     */
    private Integer category;
    /**
     * 单位名
     */
    @TableField("customer_name")
    private String customerName;

    @TableField("customer_id")
    private Long customerId;
    /**
     * 合同名
     */
    @TableField("contract_name")
    private String contractName;

    /**
     * 合同编号
     */
    @NotBlank(message = "合同编号不能为空")
    @TableField("contract_code")
    private String contractCode;


    /**
     * 合同文件地址
     */
    private String url;
    /**
     * 文件id
     */
    @TableField("file_id")
    private Long fileId;
    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 付款计划（进度款+尾款，预付款+进度款+尾款）
     */
    @TableField("pay_schedule")
    private String paySchedule;
    /**
     * 付款方式（一般转账，电汇）
     */
    @TableField("pay_way")
    private String payWay;
    @TableField("parent_id")
    private Long parentId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 是否删除
     */
    private Boolean deleted;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 更新人
     */
    private String updater;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", type=" + type +
                ", category=" + category +
                ", customerName='" + customerName + '\'' +
                ", customerId=" + customerId +
                ", contractName='" + contractName + '\'' +
                ", url='" + url + '\'' +
                ", fileId=" + fileId +
                ", projectName='" + projectName + '\'' +
                ", amount=" + amount +
                ", paySchedule='" + paySchedule + '\'' +
                ", payWay='" + payWay + '\'' +
                ", parentId=" + parentId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                ", tenantId=" + tenantId +
                ", creator='" + creator + '\'' +
                ", updater='" + updater + '\'' +
                '}';
    }
}
