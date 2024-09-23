package com.lh.oa.module.system.mapper;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.dal.dataobject.meetRoom.MeetRoom;
import com.lh.oa.module.system.dal.dataobject.meetRoom.MeetRoomQuery;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2023-09-11
 */
@Mapper
public interface MeetRoomMapper extends BaseMapperX<MeetRoom> {


    default PageResult<MeetRoom> selectPage(MeetRoomQuery query){
        PageResult<MeetRoom> meetRoomPageResult = selectPage(query, new LambdaQueryWrapperX<MeetRoom>()
                .likeIfPresent(MeetRoom::getRoomName, query.getRoomName())
                .eqIfPresent(MeetRoom::getContain, query.getContain())
                .orderByDesc(MeetRoom::getId)
        );
        return meetRoomPageResult;


    }

}
