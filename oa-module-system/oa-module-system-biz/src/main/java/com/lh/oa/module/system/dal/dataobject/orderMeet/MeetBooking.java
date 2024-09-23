package com.lh.oa.module.system.dal.dataobject.orderMeet;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetBooking {

    private Long roomId;

    private Date orderDate;

//    private String startDate;

    private Long startDate;

    private Long endDate;
}
