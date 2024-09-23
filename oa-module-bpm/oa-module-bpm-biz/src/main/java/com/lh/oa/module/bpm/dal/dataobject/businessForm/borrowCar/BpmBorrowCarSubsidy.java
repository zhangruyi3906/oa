package com.lh.oa.module.bpm.dal.dataobject.businessForm.borrowCar;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 用车申补贴表（新）
 *
 * @author yangpeng
 * @since 2023-10-31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bpm_borrow_car_subsidy")
public class BpmBorrowCarSubsidy extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 排量类型（存数据字典中car_displacement_type值）
     */
    private int displacementType;

    /**
     * 油费补贴
     */
    private BigDecimal oilSubsidy;

    /**
     * 车损补贴
     */
    private BigDecimal vehicleDamageSubsidy;

    /**
     * 保险补贴
     */
    private BigDecimal insureSubsidy;

    /**
     * 停车/洗车补贴
     */
    private BigDecimal stopWashCarSubsidy;

    /**
     * 工地补贴
     */
    private BigDecimal constructionSiteSubsidy;

    /**
     * 高原补贴
     */
    private BigDecimal plateauSubsidy;


}
