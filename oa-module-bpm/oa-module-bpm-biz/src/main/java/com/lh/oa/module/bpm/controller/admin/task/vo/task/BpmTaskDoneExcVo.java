package com.lh.oa.module.bpm.controller.admin.task.vo.task;

import lombok.Data;

@Data
public class BpmTaskDoneExcVo {
    private String name;
    private Long userId;
    private Long deptId;
    private Long projectId;

//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer beginTime;

//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer endTime;
}
