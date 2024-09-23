package com.lh.oa.module.system.dal.dataobject.orderMeet;

import com.baomidou.mybatisplus.annotations.TableField;
import com.lh.oa.module.system.dal.dataobject.meetingcontent.MeetingContentDO;
import lombok.Data;

@Data
public class OrderMeetRes extends OrderMeet{

    @TableField(exist = false)
    protected MeetingContentDO meetingContentDO;
}
