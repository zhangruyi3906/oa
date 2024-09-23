package com.lh.oa.module.system.controller.admin.meetingcontent.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 会议记录 Excel VO
 *
 * @author didida
 */
@Data
public class MeetingContentExcelVO {

    @ExcelProperty("会议记录id")
    private Long id;

    @ExcelProperty("会议ID")
    private Integer meetingId;

    @ExcelProperty("会议议题")
    private String topic;

    @ExcelProperty("发言人")
    private String speaker;

    @ExcelProperty("会议内容")
    private String content;

    @ExcelProperty("记录创建时间")
    private Date createdAt;

    @ExcelProperty("记录人")
    private String writeName;

}
