package com.lh.oa.module.system.mapper;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.dal.dataobject.orderMeet.MeetBooking;
import com.lh.oa.module.system.dal.dataobject.orderMeet.OrderMeet;
import com.lh.oa.module.system.dal.dataobject.orderMeet.OrderMeetQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2023-09-11
 */
@Mapper
public interface OrderMeetMapper extends BaseMapperX<OrderMeet> {

    default PageResult<OrderMeet> selectPage(OrderMeetQuery query){
        PageResult<OrderMeet> orderMeetPageResult = selectPage(query, new LambdaQueryWrapperX<OrderMeet>()
                .eqIfPresent(OrderMeet::getOrderDate, query.getOrderDate()));
        return orderMeetPageResult;
    }

    default PageResult<OrderMeet> selectStatusPage(OrderMeetQuery query){
        PageResult<OrderMeet> orderMeetPageResult = selectPage(query, new LambdaQueryWrapperX<OrderMeet>()
                .eqIfPresent(OrderMeet::getOrderDate, query.getOrderDate())
                .eqIfPresent(OrderMeet::getStatus, 0)
        );
        return orderMeetPageResult;
    }


    List<OrderMeet> selectByRoomId(Long roomId);

    default List<OrderMeet> getByDay() {
        return selectList(new LambdaQueryWrapperX<OrderMeet>().neIfPresent(OrderMeet::getStatus, 3)); //忽视状态为取消的预约
    }

    default List<OrderMeet> getByRoomId(MeetBooking meetBooking) {
        Date start = null;
        Date end = null;
        if(meetBooking.getStartDate() != null){
            start = new Date(meetBooking.getStartDate());
        }
        if(meetBooking.getEndDate() != null){
            end = new Date(meetBooking.getEndDate());
        }

        LambdaQueryWrapperX<OrderMeet> orderMeetLambdaQueryWrapperX = new LambdaQueryWrapperX<OrderMeet>()
                .eqIfPresent(OrderMeet::getRoomId, meetBooking.getRoomId())
                .eqIfPresent(OrderMeet::getOrderDate, meetBooking.getOrderDate())
                .neIfPresent(OrderMeet::getStatus, 3)
                .neIfPresent(OrderMeet::getStatus, 4)
                .betweenIfPresent(OrderMeet::getMeetEndTime,start, end);
        return selectList(orderMeetLambdaQueryWrapperX); //忽视状态为取消的预约
    }

}
