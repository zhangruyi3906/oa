package com.lh.oa.module.bpm.dal.dataobject.officesuppliesrequest;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@TableName("bpm_office_list")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficeDO extends BaseDO {
    @TableId
    private Long id;
    private Long offSuId;
    private String officeListName;
    private Long listNumber;
    private String unit;
    private BigDecimal price;
    private BigDecimal allPrice;
    private Date purchaseDate;
    private String reason;
    private String url;
}
