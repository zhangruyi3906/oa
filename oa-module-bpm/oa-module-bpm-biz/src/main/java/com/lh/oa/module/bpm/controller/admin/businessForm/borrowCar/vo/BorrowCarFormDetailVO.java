package com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用车申请表单（新）
 *
 * @author tanghanlin
 * @since 2023-10-24
 */
@Getter
@Setter
@ToString
public class BorrowCarFormDetailVO implements Serializable {

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
