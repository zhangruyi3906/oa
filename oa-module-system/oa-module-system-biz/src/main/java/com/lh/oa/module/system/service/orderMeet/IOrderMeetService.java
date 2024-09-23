package com.lh.oa.module.system.service.orderMeet;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.dal.dataobject.orderMeet.*;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-11
 */
public interface IOrderMeetService {

    void updateById(OrderMeet orderMeet);

    void insert(OrderMeet orderMeet);

    void deleteById(Long id);

    OrderMeet selectById(Long id);

    List<OrderMeet> selectList();

    PageResult<OrderMeet> selectPage(OrderMeetQuery query);

    void cancel(Long id);

    void audit(AuditMeet auditMeet);

    List<OrderMeetRes> getByRoomId(MeetBooking meetBooking);

    PageResult<OrderMeet> waitAudit(OrderMeetQuery query);

    List<OrderMeetDay> getByDay();

    List<OrderMeetRoom> getByRoom();
}
