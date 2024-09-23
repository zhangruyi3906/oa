package com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.vo;

import com.lh.oa.module.bpm.dal.dataobject.businessForm.borrowCar.BpmBorrowCarFormDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class BorrowCarFormVo implements Serializable {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "用车类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String borrowCarType;

    @Schema(description = "使用人名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userNick;

    @Schema(description = "部门", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;

    @Schema(description = "司机用户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String driverUserNick;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String endTime;

    @Schema(description = "所属公司", requiredMode = Schema.RequiredMode.REQUIRED)
    private String subCompany;

    @Schema(description = "所属地区", requiredMode = Schema.RequiredMode.REQUIRED)
    private String subArea;

    @Schema(description = "收款账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankAccount;

    @Schema(description = "收款银行", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankName;

    @Schema(description = "用车事由", requiredMode = Schema.RequiredMode.REQUIRED)
    private String borrowReason;

    @Schema(description = "行驶路线", requiredMode = Schema.RequiredMode.REQUIRED)
    private String driveRoute;

    @Schema(description = "车辆情况", requiredMode = Schema.RequiredMode.REQUIRED)
    private String carSituation;

    @Schema(description = "起始公里数照片url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String startKilometersPicUrl;

    @Schema(description = "结束公里数照片url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String endKilometersPicUrl;

    @Schema(description = "汽车排量", requiredMode = Schema.RequiredMode.REQUIRED)
    private String carDisplacementType;

    @Schema(description = "车牌号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String carCode;

    @Schema(description = "付款项目", requiredMode = Schema.RequiredMode.REQUIRED)
    private String payProject;

    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createTime;

    @Schema(description = "状态,参见 bpm_process_instance_result 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer result;

    @Schema(description = "流程id")
    private String processInstanceId;

    @Schema(description = "用车明细列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<BorrowCarFormDetailVO> detailList;

    @Schema(description = "行驶里程合计", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double mileageTraveledTotal;

    @Schema(description = "报销金额程合计", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal reimbursementAmountTotal;


    @Schema(description = "驾驶证照片URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String driverLicensePhotoUrl;

}
