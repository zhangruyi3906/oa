package com.lh.oa.module.system.convert.meeting;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingCreateReqVO;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingExcelVO;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingRespVO;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.meeting.MeetingDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.meeting.vo.*;

/**
 * 会议组织 Convert
 *
 * @author
 */
@Mapper
public interface MeetingConvert {

    MeetingConvert INSTANCE = Mappers.getMapper(MeetingConvert.class);

    MeetingDO convert(MeetingCreateReqVO bean);

    MeetingDO convert(MeetingUpdateReqVO bean);

    MeetingRespVO convert(MeetingDO bean);

    List<MeetingRespVO> convertList(List<MeetingDO> list);

    PageResult<MeetingRespVO> convertPage(PageResult<MeetingDO> page);

    List<MeetingExcelVO> convertList02(List<MeetingDO> list);

}
