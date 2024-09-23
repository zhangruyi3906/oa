package com.lh.oa.module.system.controller.admin.userProject.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(description = "管理后台 - 人员项目 Excel 导出 Request VO，参数和 UserProjectPageReqVO 是一致的")
@Data
public class UserProjectExportReqVO {

    @Schema(description = "人员id")
    private Long userId;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "人员是否已经离开（是0，否1）")
    private Byte status;

    @Schema(description = "开始时间，格式为10位的时间戳，数字类型的")
    private Long startTime;

    @Schema(description = "结束时间，格式为10位的时间戳，数字类型的")
    private Long endTime;

    private Integer type;

    private Integer isRecord;

}
