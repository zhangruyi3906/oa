package com.lh.oa.module.system.full.entity.attandance.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author tanghanlin
 * @since 2023/10/27
 */
@Getter
@Setter
@ToString
public class ProcessReportColumnTo implements Serializable {

    /**
     * 开始日期字段id
     */
    private String startDayId;

    /**
     * 开始时间字段id
     */
    private String startTimeId;

    /**
     * 结束日期字段id
     */
    private String endDayId;

    /**
     * 结束时间字段id
     */
    private String endTimeId;

    /**
     * 业务时间区间，新控件配的流程表单都使用这种格式
     */
    private String dateTimeRange;

    public ProcessReportColumnTo() {
    }

    public ProcessReportColumnTo(String startDayId, String startTimeId, String endDayId, String endTimeId) {
        this.startDayId = startDayId;
        this.startTimeId = startTimeId;
        this.endDayId = endDayId;
        this.endTimeId = endTimeId;
    }

    public ProcessReportColumnTo(String startDayId, String startTimeId, String endDayId, String endTimeId, String dateTimeRange) {
        this.startDayId = startDayId;
        this.startTimeId = startTimeId;
        this.endDayId = endDayId;
        this.endTimeId = endTimeId;
        this.dateTimeRange = dateTimeRange;
    }
}
