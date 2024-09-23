package com.lh.oa.module.system.dal.dataobject.customers;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 客户基础信息 DO
 *
 * @author 狗蛋
 */
@TableName("pro_customers")
//("pro_customers_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomersDO extends BaseDO {

    /**
     * 客户id
     */
    @TableId
    private Long id;

    /**
     * 电子邮件地址
     */
    private String email;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 地址
     */
    private String address;


    private String area;


    private Long areaId;

    private Long cityId;

    private Long stateId;
    /**
     * 城市
     */
    private String city;
    /**
     * 省/州
     */
    private String state;
    /**
     * 邮政编码
     */
    private String postalCode;
    /**
     * 国家
     */
    private String country;
    /**
     * 客户等级
     */
    private Integer cusLevel;


    private String url;


    private Long fileId;

}
