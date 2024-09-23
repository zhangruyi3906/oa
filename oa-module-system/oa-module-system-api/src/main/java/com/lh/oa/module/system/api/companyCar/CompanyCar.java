package com.lh.oa.module.system.api.companyCar;

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
@TableName("company_car")
public class CompanyCar extends Model<CompanyCar> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 车牌号
     */
    @TableField("plate_number")
    @NotNull(message = "车牌号不能为空")
    private String plateNumber;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 型号
     */
    private String model;
    /**
     * 颜色
     */
    private String color;
    /**
     * 车辆类别
     */
    private String type;
    /**
     * 购买日期
     */
    @TableField("purchase_date")
    private Date purchaseDate;
    /**
     * 保险单号码
     */
    @TableField("policy_number")
    private String policyNumber;
    /**
     * 车辆状况
     */
    @TableField("car_condition")
    private String carCondition;
    /**
     * 状态（0空闲，1借用）
     */
    private Integer status;
    /**
     * 是否删除
     */
    private Boolean deleted;
    /**
     * 租户编号
     */
    @TableField("tenant_id")
    private Long tenantId;
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 部门名
     */
    private String deptName;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 项目名
     */
    private String projectName;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getCarCondition() {
        return carCondition;
    }

    public void setCarCondition(String carCondition) {
        this.carCondition = carCondition;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CompanyCar{" +
        ", id=" + id +
        ", plateNumber=" + plateNumber +
        ", brand=" + brand +
        ", model=" + model +
        ", color=" + color +
        ", type=" + type +
        ", purchaseDate=" + purchaseDate +
        ", policyNumber=" + policyNumber +
        ", carCondition=" + carCondition +
        ", status=" + status +
        ", deleted=" + deleted +
        ", tenantId=" + tenantId +
        "}";
    }
}
