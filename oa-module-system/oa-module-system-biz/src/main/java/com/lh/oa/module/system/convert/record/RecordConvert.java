package com.lh.oa.module.system.convert.record;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.dal.dataobject.record.RecordDO;
import com.lh.oa.module.system.controller.admin.record.vo.RecordCreateReqVO;
import com.lh.oa.module.system.controller.admin.record.vo.RecordExcelVO;
import com.lh.oa.module.system.controller.admin.record.vo.RecordRespVO;
import com.lh.oa.module.system.controller.admin.record.vo.RecordUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.record.vo.*;

/**
 * 打卡记录 Convert
 *
 * @author
 */
@Mapper
public interface RecordConvert {

    RecordConvert INSTANCE = Mappers.getMapper(RecordConvert.class);

    RecordDO convert(RecordCreateReqVO bean);

    RecordDO convert(RecordUpdateReqVO bean);

    RecordRespVO convert(RecordDO bean);

    List<RecordRespVO> convertList(List<RecordDO> list);

    PageResult<RecordRespVO> convertPage(PageResult<RecordDO> page);

    List<RecordExcelVO> convertList02(List<RecordDO> list);

}
