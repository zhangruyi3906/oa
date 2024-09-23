package com.lh.oa.module.system.convert.meetingcontent;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentCreateReqVO;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentExcelVO;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentRespVO;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.meetingcontent.MeetingContentDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.*;

/**
 * 会议记录 Convert
 *
 * @author didida
 */
@Mapper
public interface MeetingContentConvert {

    MeetingContentConvert INSTANCE = Mappers.getMapper(MeetingContentConvert.class);

    MeetingContentDO convert(MeetingContentCreateReqVO bean);

    MeetingContentDO convert(MeetingContentUpdateReqVO bean);

    MeetingContentRespVO convert(MeetingContentDO bean);

    List<MeetingContentRespVO> convertList(List<MeetingContentDO> list);

    PageResult<MeetingContentRespVO> convertPage(PageResult<MeetingContentDO> page);

    List<MeetingContentExcelVO> convertList02(List<MeetingContentDO> list);

}
