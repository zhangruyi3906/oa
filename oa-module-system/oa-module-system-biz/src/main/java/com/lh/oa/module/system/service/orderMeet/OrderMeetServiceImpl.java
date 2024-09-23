package com.lh.oa.module.system.service.orderMeet;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.schedule.vo.ScheduleCreateReqVO;
import com.lh.oa.module.system.dal.dataobject.meetRoom.MeetRoom;
import com.lh.oa.module.system.dal.dataobject.meetingcontent.MeetingContentDO;
import com.lh.oa.module.system.dal.dataobject.orderMeet.*;
import com.lh.oa.module.system.dal.dataobject.schedule.ScheduleDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.mapper.OrderMeetMapper;
import com.lh.oa.module.system.service.meetRoom.IMeetRoomService;
import com.lh.oa.module.system.service.meetingcontent.MeetingContentService;
import com.lh.oa.module.system.service.schedule.ScheduleService;
import com.lh.oa.module.system.service.user.AdminUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-11
 */
@Service
public class OrderMeetServiceImpl implements IOrderMeetService {

    @Resource
    private OrderMeetMapper orderMeetMapper;

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private IMeetRoomService meetRoomService;

    @Resource
    private ScheduleService scheduleService;

    @Resource
    private MeetingContentService meetingContentService;


    @Override
    public void updateById(OrderMeet orderMeet) {

        OrderMeet meet = orderMeetMapper.selectById(orderMeet.getId());
        if(meet.getStatus()==1){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AFTER_APPROVAL_ON_FURTHER_MODIFICATIONS_CAN_BE_MADE);
        }
        Date meetStartTime = orderMeet.getMeetStartTime();
        Date meetEndTime = orderMeet.getMeetEndTime();
        List<OrderMeet> orderMeetList = orderMeetMapper.selectByRoomId(orderMeet.getRoomId());
        orderMeetList.stream().forEach(t -> {
            if (t.getOrderDate().compareTo(orderMeet.getOrderDate()) == 0 &&
                    ((t.getMeetStartTime().compareTo(meetStartTime) <= 0 || t.getMeetEndTime().compareTo(meetStartTime) > 0) ||
                            (t.getMeetStartTime().compareTo(meetEndTime) < 0 && t.getMeetEndTime().compareTo(meetEndTime) >= 0))) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.MEETING_IS_EXISTS);
            }

        });
        MeetRoom meetRoom = meetRoomService.selectById(orderMeet.getRoomId());
        orderMeet.setRoomName(meetRoom.getRoomName());
        orderMeetMapper.updateById(orderMeet);
    }

    @Override
    public void insert(OrderMeet orderMeet) {

        AdminUserDO user = adminUserService.getUser(getLoginUserId());
        MeetRoom meetRoom = meetRoomService.selectById(orderMeet.getRoomId());
        orderMeet.setRoomName(meetRoom.getRoomName());
        orderMeet.setUserId(user.getId());
        orderMeet.setUsername(user.getNickname());
        orderMeet.setStatus(0);
        Date meetStartTime = orderMeet.getMeetStartTime();
        Date meetEndTime = orderMeet.getMeetEndTime();
        Date orderDate = getOrderDate(meetStartTime);
        orderMeet.setOrderDate(orderDate);

        List<OrderMeet> orderMeetList = orderMeetMapper.selectByRoomId(orderMeet.getRoomId());
        orderMeetList.stream().forEach(t -> {
            if (t.getOrderDate().compareTo(orderMeet.getOrderDate()) == 0 &&
                    ((t.getMeetStartTime().compareTo(meetStartTime) <= 0 || t.getMeetEndTime().compareTo(meetStartTime) > 0) ||
                            (t.getMeetStartTime().compareTo(meetEndTime) < 0 && t.getMeetEndTime().compareTo(meetEndTime) >= 0))) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.MEETING_IS_EXISTS);
            }
        });
        //创建相关日程
        ScheduleCreateReqVO scheduleCreateReqVO = new ScheduleCreateReqVO();
        scheduleCreateReqVO.setTitle(orderMeet.getTitle());
        scheduleCreateReqVO.setDescription(orderMeet.getDescription());
        scheduleCreateReqVO.setExpireDate(orderMeet.getOrderDate().toString());
        scheduleCreateReqVO.setScheStartTime(orderMeet.getMeetStartTime());
        scheduleCreateReqVO.setScheEndTime(orderMeet.getMeetEndTime());
        scheduleCreateReqVO.setExpireDateDay(orderMeet.getOrderDate());
        Date start = orderMeet.getMeetStartTime();
        Date end = orderMeet.getMeetEndTime();

        //判断与日程是否冲突，添加到日程
        List<ScheduleDO> scheduleDOS = scheduleService.selectByTime(start, end, getLoginUserId());
        if (!scheduleDOS.isEmpty()) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SCHEDULE_IS_EXISTS);
        }
        scheduleService.createSchedule(scheduleCreateReqVO);
        orderMeetMapper.insert(orderMeet);
    }

    @NotNull
    private static Date getOrderDate(Date meetStartTime) {
        Calendar calendar = Calendar.getInstance();
        // 将日期部分设置为今天的0点0分0秒
        calendar.setTime(meetStartTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date orderDate = calendar.getTime();
        return orderDate;
    }

    @Override
    public void deleteById(Long id) {
        OrderMeet orderMeet = orderMeetMapper.selectById(id);
        deleteRepeatSchedule(orderMeet); //删除对应日程
        orderMeetMapper.deleteById(id);
    }

    @Override
    public OrderMeet selectById(Long id) {
        return orderMeetMapper.selectById(id);
    }

    @Override
    public List<OrderMeet> selectList() {
        return orderMeetMapper.selectList(null);
    }

    @Override
    public PageResult<OrderMeet> selectPage(OrderMeetQuery query) {
        return orderMeetMapper.selectPage(query);
    }

    @Override
    public void cancel(Long id) {
        OrderMeet orderMeet = orderMeetMapper.selectById(id);
        if (orderMeet.getStatus() != 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ORDER_MEET_STATUS_ERROR);
        }
        deleteRepeatSchedule(orderMeet); //删除对应日程
        orderMeet.setStatus(4);
        orderMeetMapper.updateById(orderMeet);
    }

    @Override
    public void audit(AuditMeet auditMeet) {
        OrderMeet orderMeet = orderMeetMapper.selectById(auditMeet.getId());
        if (orderMeet.getStatus() != 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUDIT_IS_EXISTS);
        }
       if (auditMeet.getStatus() == 3) {
            deleteRepeatSchedule(orderMeet); //删除对应日程
        }
        orderMeet.setStatus(auditMeet.getStatus());
        orderMeetMapper.updateById(orderMeet);
    }

    private void deleteRepeatSchedule(OrderMeet orderMeet) {
        Date start = orderMeet.getMeetStartTime();
        Date end = orderMeet.getMeetEndTime();
        List<ScheduleDO> scheduleDOS = scheduleService.selectByTime(start, end, getLoginUserId());
        if (!scheduleDOS.isEmpty())
            scheduleDOS.stream().forEach(scheduleDO -> scheduleService.deleteSchedule(scheduleDO.getId()));
    }


    @Override
    public List<OrderMeetRes> getByRoomId(MeetBooking meetBooking) {
        List<OrderMeet> orderMeets = orderMeetMapper.getByRoomId(meetBooking);
        List<OrderMeetRes> list = new ArrayList<>();
        orderMeets.stream().forEach(t -> {
            OrderMeetRes orderMeetRes = new OrderMeetRes();
            MeetingContentDO meetingContent = meetingContentService.getMeetingContentByMeetId(t.getId());
            BeanUtils.copyProperties(t, orderMeetRes);
            orderMeetRes.setMeetingContentDO(meetingContent);
            list.add(orderMeetRes);
        });
        return list;
    }

    @Override
    public PageResult<OrderMeet> waitAudit(OrderMeetQuery query) {
        return orderMeetMapper.selectStatusPage(query);
    }

    @Override
    public List<OrderMeetDay> getByDay() {
        List<OrderMeet> orderMeets = orderMeetMapper.getByDay();
        Map<Date, List<OrderMeet>> collect = orderMeets.stream().collect(Collectors.groupingBy(OrderMeet::getOrderDate));

        List<OrderMeetDay> collect1 = collect.entrySet().stream()
                .map(s -> {
                    OrderMeetDay orderMeetDay = new OrderMeetDay();
                    orderMeetDay.setOrderMeetDate(s.getKey());
                    orderMeetDay.setOrderMeetList(s.getValue());
                    return orderMeetDay;
                })
                .collect(Collectors.toList());
        return collect1;
    }

    @Override
    public List<OrderMeetRoom> getByRoom() {
        List<OrderMeet> orderMeets = orderMeetMapper.getByDay();
        Map<Long, List<OrderMeet>> collect = orderMeets.stream().collect(Collectors.groupingBy(OrderMeet::getRoomId));

        List<OrderMeetRoom> collect1 = collect.entrySet().stream()
                .map(s -> {
                    OrderMeetRoom orderMeetRoom = new OrderMeetRoom();
                    orderMeetRoom.setRoomId(s.getKey());
                    orderMeetRoom.setOrderMeetList(s.getValue());
                    return orderMeetRoom;
                })
                .collect(Collectors.toList());
        return collect1;
    }


}
