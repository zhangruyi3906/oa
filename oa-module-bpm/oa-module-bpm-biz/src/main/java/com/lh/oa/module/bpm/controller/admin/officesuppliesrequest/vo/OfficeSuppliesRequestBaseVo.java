package com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OfficeSuppliesRequestBaseVo {
    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String number;
    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
    @Schema(description = "部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long deptId;
    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;
    @Schema(description = "原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String reason;
    @Schema(description = "办公地点", requiredMode = Schema.RequiredMode.REQUIRED)
    private String place;
    @Schema(description = "总价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal allPrice;

    List<OfficeVO> list;
}
