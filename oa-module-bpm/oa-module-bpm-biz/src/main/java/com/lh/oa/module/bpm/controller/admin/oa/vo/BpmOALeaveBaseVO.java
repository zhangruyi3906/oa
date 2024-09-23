package com.lh.oa.module.bpm.controller.admin.oa.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 请假申请 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BpmOALeaveBaseVO {

    @Schema(description = "请假人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;
    @Schema(description = "请假人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
    @Schema(description = "部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long deptId;
    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;
    @Schema(description = "请假附件url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String leaveUrl;
    @Schema(description = "调休时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date leaveDay;
    @Schema(description = "工作承接人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long otherUserId;
    @Schema(description = "工作承接人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String otherUserName;
    @Schema(description = "工作内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String workContent;
    @Schema(description = "请假天数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double days;
    @Schema(description = "请假的开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;
    @Schema(description = "请假的结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    @Schema(description = "请假类型,参见 bpm_oa_type 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String reason;

}