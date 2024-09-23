package com.lh.oa.module.system.dal.dataobject.customerservice;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 客户服务 DO
 *
 * @author 管理员
 */
@TableName("pro_customer_service")
//("pro_customer_service_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerServiceDO extends BaseDO {

    /**
     * 服务id
     */
    @TableId
    private Long id;
    /**
     * 客户id
     */
    private Long customerId;
    /**
     * 服务内容
     */
    private String serviceContent;
    /**
     * 反馈
     */
    private String feedback;
    /**
     * 状态
     */
    private String status;

}
