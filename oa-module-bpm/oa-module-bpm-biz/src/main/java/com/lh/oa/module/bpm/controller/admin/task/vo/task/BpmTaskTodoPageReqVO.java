package com.lh.oa.module.bpm.controller.admin.task.vo.task;

import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 流程任务的 TODO 待办的分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskTodoPageReqVO extends PageParam {

    @Schema(description = "流程发起人名字", example = "芋道")
    private String startUserNickname;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    private Long userId;

    private Long startTime;

    private Long endTime;

    @Schema(description = "已读状态 0-未读 1-已读")
    private Integer readState;
    @Schema(description = "流程名字")
    private String instanceName;
    @Schema(description = "任务名")
    private String taskName;
    //PC和APP通过关键字查询发起人或流程名，WEB分开查
    @Schema(description = "用于通过发起人或流程名查询")
    private String keyword;
}
