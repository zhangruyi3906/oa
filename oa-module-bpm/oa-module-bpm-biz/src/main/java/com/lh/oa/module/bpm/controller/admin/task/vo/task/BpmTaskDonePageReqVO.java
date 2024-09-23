package com.lh.oa.module.bpm.controller.admin.task.vo.task;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 流程任务的 Done 已办的分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskDonePageReqVO extends PageParam {

    @Schema(description = "流程发起人名字", example = "芋道")
    private String name;

    @Schema(description = "开始的创建收间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime beginCreateTime;

    @Schema(description = "结束的创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endCreateTime;
    private Long userId;
    //前端字段错了,修改字段

    private Long startTime;
    private Long endTime;
    @Schema(description = "流程名")
    private String instanceName;
    @Schema(description = "任务名")
    private String taskName;

    @Schema(description = "任务状态")
    private Integer result;

    //PC和APP通过关键字查询发起人或流程名，WEB分开查
    @Schema(description = "用于通过发起人或流程名查询")
    private String keyword;

}