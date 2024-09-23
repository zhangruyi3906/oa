package com.lh.oa.module.system.service.meetRoom;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.dal.dataobject.meetRoom.MeetRoom;
import com.lh.oa.module.system.dal.dataobject.meetRoom.MeetRoomQuery;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-11
 */
public interface IMeetRoomService {


    List<MeetRoom> selectList();

    PageResult<MeetRoom> selectPage(MeetRoomQuery query);

    void updateById(MeetRoom meetRoom);

    void insert(MeetRoom meetRoom);

    void deleteById(Long id);

    MeetRoom selectById(Long id);


}
