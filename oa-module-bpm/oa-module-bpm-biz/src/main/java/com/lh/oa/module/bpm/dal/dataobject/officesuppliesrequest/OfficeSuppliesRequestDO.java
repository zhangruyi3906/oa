package com.lh.oa.module.bpm.dal.dataobject.officesuppliesrequest;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

@TableName("bpm_office_supplies")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficeSuppliesRequestDO extends BaseDO {
    @TableId
    private Long id;
    private String number;
    private String userName;
    private Long userId;
    private Long deptId;
    private String deptName;
    private String reason;
    private String place;
    private BigDecimal allPrice;
    private Integer result;

    private String processInstanceId;
}
