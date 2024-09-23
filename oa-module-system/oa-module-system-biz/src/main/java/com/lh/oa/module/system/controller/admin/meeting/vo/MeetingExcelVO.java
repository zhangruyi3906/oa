package com.lh.oa.module.system.controller.admin.meeting.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 会议组织 Excel VO
 *
 * @author
 */
@Data
public class MeetingExcelVO {

    @ExcelProperty("会议ID")
    private Long id;

    @ExcelProperty("会议标题")
    private String title;

    @ExcelProperty("会议描述")
    private String description;

    @ExcelProperty("会议开始时间")
    private Date startTime;

    @ExcelProperty("会议结束时间")
    private Date endTime;

    @ExcelProperty("组织者")
    private String organizer;

    @ExcelProperty("会议地点")
    private String location;

    @ExcelProperty("会议状态")
    private Boolean status;

    @ExcelProperty("记录创建时间")
    private Date createdAt;

    @ExcelProperty("组织者id")
    private Long userId;

}
