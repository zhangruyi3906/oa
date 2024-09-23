package com.lh.oa.module.system.dal.dataobject.companyCar;


import com.baomidou.mybatisplus.annotations.TableField;
import com.lh.oa.framework.common.pojo.PageParam;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author director
 * @since 2023-09-04
 */
@Data
public class CompanyCarQuery extends PageParam {

    /**
     * 车牌号
     */
    @TableField("plate_number")

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

}