package com.lh.oa.module.system.convert.notice;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.notice.vo.NoticeCreateReqVO;
import com.lh.oa.module.system.controller.admin.notice.vo.NoticeRespVO;
import com.lh.oa.module.system.controller.admin.notice.vo.NoticeUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.notice.NoticeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NoticeConvert {

    NoticeConvert INSTANCE = Mappers.getMapper(NoticeConvert.class);

    PageResult<NoticeRespVO> convertPage(PageResult<NoticeDO> page);

    NoticeRespVO convert(NoticeDO bean);

    NoticeDO convert(NoticeUpdateReqVO bean);

    NoticeDO convert(NoticeCreateReqVO bean);

}
