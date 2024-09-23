package com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用车申请表单（新）
 *
 * @author tanghanlin
 * @since 2023-10-24
 */
@Getter
@Setter
@ToString
public class BorrowCarFormParam implements Serializable {

    @Schema(description = "用车类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用车类型不能为空")
    private Integer borrowCarType;

    @Schema(description = "使用人id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "使用人不能为空")
    private Long userId;

    @Schema(description = "使用人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;

    @Schema(description = "部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "部门不能为空")
    private Long deptId;

    @Schema(description = "部门", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;

    @Schema(description = "司机用户id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "司机不能为空")
    private Long driverUserId;

    @Schema(description = "司机用户", requiredMode = Schema.RequiredMode.REQUIRED)
    private String driverUserName;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "开始时间不能为空")
    private String startTimeStr;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "结束时间不能为空")
    private String endTimeStr;

    @Schema(description = "所属公司", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所属公司不能为空")
    private Integer subCompany;

    @Schema(description = "所属地区", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所属地区不能为空")
    private Integer subArea;

    @Schema(description = "收款账号", requiredMode = Schema.RequiredMode.REQUIRED)
//    @Pattern(regexp = "^[0-9 ]{16,23}$", message = "请输入正确的银行账号")
    private String bankAccount;

    @Schema(description = "收款银行", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankName;

    @Schema(description = "用车事由", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用车事由不能为空")
    private String borrowReason;

    @Schema(description = "行驶路线", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "行驶路线不能为空")
    private String driveRoute;

    @Schema(description = "车辆情况", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "车辆情况不能为空")
    private String carSituation;

    @Schema(description = "起始公里数照片url", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "起始公里数照片不能为空")
    private String startKilometersPicUrl;

    @Schema(description = "结束公里数照片url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String endKilometersPicUrl;

    @Schema(description = "汽车排量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer carDisplacementType;

    @Schema(description = "车牌号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "车牌号不能为空")
    private String carCode;

    @Schema(description = "付款项目", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer payProject;

    @Schema(description = "用车明细列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<BpmBorrowCarFormDetailParam> detailList;

    @Schema(description = "行驶里程合计", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double mileageTraveledTotal;

    @Schema(description = "报销金额程合计", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal reimbursementAmountTotal;


    @Schema(description = "驾驶证照片URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String driverLicensePhotoUrl;

}
