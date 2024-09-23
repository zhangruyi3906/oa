package com.lh.oa.module.bpm.dal.dataobject.businessForm.borrowCar;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用车申请表单（新）
 *
 * @author tanghanlin
 * @since 2023-10-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bpm_borrow_car_form")
public class BpmBorrowCarForm extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用车类型，字典表同名类型的值
     */
    private Integer borrowCarType;

    /**
     * 使用人id
     */
    private Long userId;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 司机用户id
     */
    private Long driverUserId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 所属公司，字典sub_company的值
     */
    private Integer subCompany;

    /**
     * 所属地区，字典sub_area的值
     */
    private Integer subArea;

    /**
     * 收款账号
     */
    private String bankAccount;

    /**
     * 收款银行
     */
    private String bankName;

    /**
     * 用车事由
     */
    private String borrowReason;

    /**
     * 行驶路线
     */
    private String driveRoute;

    /**
     * 车辆情况
     */
    private String carSituation;

    /**
     * 起始公里数照片url
     */
    private String startKilometersPicUrl;

    /**
     * 结束公里数照片url
     */
    private String endKilometersPicUrl;

    /**
     * 汽车排量，字典表同名类型的值
     */
    private Integer carDisplacementType;

    /**
     * 车牌号
     */
    private String carCode;

    /**
     * 付款项目，字典sub_company的值
     */
    private Integer payProject;

    /**
     * 流程定义id
     */
    private String processInstanceId;

    /**
     * 流程执行结果
     */
    private Integer result;

    /**
     * 行驶里程合计
     */
    private Double mileageTraveledTotal;

    /**
     * 报销金额程合计
     */
    private BigDecimal reimbursementAmountTotal;

    /**
     * 驾驶证照片URL
     */
    private String driverLicensePhotoUrl;

}
