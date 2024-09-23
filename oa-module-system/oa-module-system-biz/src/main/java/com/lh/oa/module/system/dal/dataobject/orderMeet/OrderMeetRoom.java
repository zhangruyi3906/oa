package com.lh.oa.module.system.dal.dataobject.orderMeet;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class OrderMeetRoom {
    private List<OrderMeet> orderMeetList;
    private Long roomId;
}
