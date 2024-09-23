package com.lh.oa.module.system.convert.schedule;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.schedule.vo.ScheduleCreateReqVO;
import com.lh.oa.module.system.controller.admin.schedule.vo.ScheduleExcelVO;
import com.lh.oa.module.system.controller.admin.schedule.vo.ScheduleRespVO;
import com.lh.oa.module.system.controller.admin.schedule.vo.ScheduleUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.schedule.ScheduleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.schedule.vo.*;

/**
 * 日程管理 Convert
 *
 * @author didida
 */
@Mapper
public interface ScheduleConvert {

    ScheduleConvert INSTANCE = Mappers.getMapper(ScheduleConvert.class);

    ScheduleDO convert(ScheduleCreateReqVO bean);

    ScheduleDO convert(ScheduleUpdateReqVO bean);

    ScheduleRespVO convert(ScheduleDO bean);

    List<ScheduleRespVO> convertList(List<ScheduleDO> list);

    PageResult<ScheduleRespVO> convertPage(PageResult<ScheduleDO> page);

    List<ScheduleExcelVO> convertList02(List<ScheduleDO> list);

}
