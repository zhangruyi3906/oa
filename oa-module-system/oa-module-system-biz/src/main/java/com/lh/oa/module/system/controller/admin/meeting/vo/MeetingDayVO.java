package com.lh.oa.module.system.controller.admin.meeting.vo;

import com.lh.oa.module.system.dal.dataobject.meeting.MeetingDO;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;


@Data
@ToString(callSuper = true)
public class MeetingDayVO {
    private List<MeetingDO> MeetingList;
    private Date MeetingDate;
}