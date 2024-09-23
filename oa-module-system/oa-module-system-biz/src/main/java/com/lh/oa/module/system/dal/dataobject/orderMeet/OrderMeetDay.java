package com.lh.oa.module.system.dal.dataobject.orderMeet;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString(callSuper = true)
public class OrderMeetDay {
    private List<OrderMeet> orderMeetList;
    private Date orderMeetDate;
}