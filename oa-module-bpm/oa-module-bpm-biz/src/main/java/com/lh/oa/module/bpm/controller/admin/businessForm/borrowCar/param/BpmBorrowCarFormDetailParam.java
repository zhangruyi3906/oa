package com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用车申请表单明细（新）
 *
 * @author yangpeng
 * @since 2023-11-2
 */
@Getter
@Setter
@ToString
public class BpmBorrowCarFormDetailParam  {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用车申请表单ID
     */
    private Long borrowCarId;

    /**
     * 用车日期
     */
    private String borrowCarDate;

    /**
     * 起点
     */
    private String startPoint;

    /**
     * 终点
     */
    private String endPoint;

    /**
     * 起始里程
     */
    private Double startMileage;

    /**
     * 结束里程
     */
    private Double endMileage;

    /**
     * 行驶里程
     */
    private Double travelMileage;

    /**
     * 是否工地补贴,0否，1是
     */
    private int constructionSiteSubsidy;

    /**
     * 是否高原地区,0否，1是
     */
    private int plateauAreas;

    /**
     * 用车事由
     */
    private String borrowCarReasons;

    /**
     * 油费报销金额
     */
    private BigDecimal oilFeeAmount;

    /**
     * 差旅报销金额
     */
    private BigDecimal travelBusinessAmount;

    /**
     * 合计报销金额
     */
    private BigDecimal amountTotal;


}
