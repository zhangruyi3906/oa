package com.lh.oa.module.bpm.convert.attendancecorrection;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionRespVO;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.*;
import com.lh.oa.module.bpm.dal.dataobject.attendancecorrection.CorrectionDO;

/**
 * 补卡流程 Convert
 *
 * @author 狗蛋
 */
@Mapper
public interface CorrectionConvert {

    CorrectionConvert INSTANCE = Mappers.getMapper(CorrectionConvert.class);

    CorrectionDO convert(CorrectionCreateReqVO bean);

    CorrectionDO convert(CorrectionUpdateReqVO bean);

    CorrectionRespVO convert(CorrectionDO bean);

    List<CorrectionRespVO> convertList(List<CorrectionDO> list);

    PageResult<CorrectionRespVO> convertPage(PageResult<CorrectionDO> page);

//    List<CorrectionExcelVO> convertList02(List<CorrectionDO> list);

}
