package com.lh.oa.module.system.service.meetRoom;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.dal.dataobject.meetRoom.MeetRoom;
import com.lh.oa.module.system.dal.dataobject.meetRoom.MeetRoomQuery;
import com.lh.oa.module.system.dal.dataobject.orderMeet.MeetBooking;
import com.lh.oa.module.system.dal.dataobject.orderMeet.OrderMeetRes;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.mapper.MeetRoomMapper;
import com.lh.oa.module.system.service.orderMeet.IOrderMeetService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-11
 */
@Service
public class MeetRoomServiceImpl implements IMeetRoomService {

    @Resource
    private MeetRoomMapper meetRoomMapper;

    @Resource
    private IOrderMeetService orderMeetService;

    @Override
    public List<MeetRoom> selectList() {
        return meetRoomMapper.selectList(null);
    }

    @Override
    public PageResult<MeetRoom> selectPage(MeetRoomQuery query) {
        return meetRoomMapper.selectPage(query);
    }

    @Override
    public void updateById(MeetRoom meetRoom) {
        List<MeetRoom> meetRooms = meetRoomMapper.selectList(MeetRoom::getRoomName, meetRoom.getRoomName());
        if (meetRooms != null && meetRooms.size() > 0) {
            List<MeetRoom> collect = meetRooms.stream().filter(t -> t.getId() != meetRoom.getId()).collect(Collectors.toList());
            if (collect != null && collect.size() > 0)
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.MEET_NAME_IS_EXISTS);
        }
        meetRoomMapper.updateById(meetRoom);
    }

    @Override
    public void insert(MeetRoom meetRoom) {
        MeetRoom meetRoom1 = meetRoomMapper.selectOne("room_name", meetRoom.getRoomName());
        if (meetRoom1 != null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MEET_NAME_IS_EXISTS);
        }
        meetRoomMapper.insert(meetRoom);
    }

    @Override
    public void deleteById(Long id) {
        List<OrderMeetRes> lists = orderMeetService.getByRoomId(new MeetBooking().setRoomId(id));
        if (ObjectUtils.isNotEmpty(lists)) {
            lists.forEach(t -> {
                Date orderDate = t.getOrderDate();
                if (ObjectUtils.isNotEmpty(orderDate) && orderDate.after(new Date())) {
                    throw ServiceExceptionUtil.exception(ErrorCodeConstants.MEET_ROOM_IS_USED);
                }
            });
        }
        meetRoomMapper.deleteById(id);
    }

    @Override
    public MeetRoom selectById(Long id) {
        return meetRoomMapper.selectById(id);
    }


}
