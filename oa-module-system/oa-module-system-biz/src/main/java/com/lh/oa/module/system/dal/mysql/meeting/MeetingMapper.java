package com.lh.oa.module.system.dal.mysql.meeting;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.dal.dataobject.meeting.MeetingDO;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingExportReqVO;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingPageReqVO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.system.controller.admin.meeting.vo.*;

/**
 * 会议组织 Mapper
 *
 * @author didida
 */
@Mapper
public interface MeetingMapper extends BaseMapperX<MeetingDO> {

    default PageResult<MeetingDO> selectPage(MeetingPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MeetingDO>()
                .eqIfPresent(MeetingDO::getTitle, reqVO.getTitle())
                .eqIfPresent(MeetingDO::getDescription, reqVO.getDescription())
                .betweenIfPresent(MeetingDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(MeetingDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(MeetingDO::getOrganizer, reqVO.getOrganizer())
                .eqIfPresent(MeetingDO::getLocation, reqVO.getLocation())
                .eqIfPresent(MeetingDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MeetingDO::getCreatedAt, reqVO.getCreatedAt())
                .eqIfPresent(MeetingDO::getUserId, reqVO.getUserId())
                .orderByDesc(MeetingDO::getId));
    }

    default List<MeetingDO> selectList(MeetingExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MeetingDO>()
                .eqIfPresent(MeetingDO::getTitle, reqVO.getTitle())
                .eqIfPresent(MeetingDO::getDescription, reqVO.getDescription())
                .betweenIfPresent(MeetingDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(MeetingDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(MeetingDO::getOrganizer, reqVO.getOrganizer())
                .eqIfPresent(MeetingDO::getLocation, reqVO.getLocation())
                .eqIfPresent(MeetingDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MeetingDO::getCreatedAt, reqVO.getCreatedAt())
                .eqIfPresent(MeetingDO::getUserId, reqVO.getUserId())
                .orderByDesc(MeetingDO::getId));
    }

}
