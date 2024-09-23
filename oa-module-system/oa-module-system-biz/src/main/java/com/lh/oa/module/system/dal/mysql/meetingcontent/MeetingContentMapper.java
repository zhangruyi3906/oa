package com.lh.oa.module.system.dal.mysql.meetingcontent;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentExportReqVO;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentPageReqVO;
import com.lh.oa.module.system.dal.dataobject.meetingcontent.MeetingContentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 会议记录 Mapper
 *
 * @author didida
 */
@Mapper
public interface MeetingContentMapper extends BaseMapperX<MeetingContentDO> {

    default PageResult<MeetingContentDO> selectPage(MeetingContentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MeetingContentDO>()
                .eqIfPresent(MeetingContentDO::getMeetingId, reqVO.getMeetingId())
                .eqIfPresent(MeetingContentDO::getTopic, reqVO.getTopic())
                .eqIfPresent(MeetingContentDO::getSpeaker, reqVO.getSpeaker())
                .eqIfPresent(MeetingContentDO::getContent, reqVO.getContent())
                .eqIfPresent(MeetingContentDO::getCreatedAt, reqVO.getCreatedAt())
                .eqIfPresent(MeetingContentDO::getWriteName, reqVO.getWriteName())
                .orderByDesc(MeetingContentDO::getId));
    }

    default List<MeetingContentDO> selectList(MeetingContentExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MeetingContentDO>()
                .eqIfPresent(MeetingContentDO::getMeetingId, reqVO.getMeetingId())
                .eqIfPresent(MeetingContentDO::getTopic, reqVO.getTopic())
                .eqIfPresent(MeetingContentDO::getSpeaker, reqVO.getSpeaker())
                .eqIfPresent(MeetingContentDO::getContent, reqVO.getContent())
                .eqIfPresent(MeetingContentDO::getCreatedAt, reqVO.getCreatedAt())
                .eqIfPresent(MeetingContentDO::getWriteName, reqVO.getWriteName())
                .orderByDesc(MeetingContentDO::getId));
    }

    default MeetingContentDO getMeetingContentByMeetId(Long meetId) {
        return selectOne(MeetingContentDO::getMeetingId, meetId);
    }
}
