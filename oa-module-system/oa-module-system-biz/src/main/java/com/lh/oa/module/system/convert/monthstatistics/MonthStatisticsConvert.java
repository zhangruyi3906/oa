package com.lh.oa.module.system.convert.monthstatistics;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.monthstatistics.vo.MonthStatisticsCreateReqVO;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.MonthStatisticsExcelVO;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.MonthStatisticsRespVO;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.MonthStatisticsUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.monthstatistics.MonthStatisticsDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.*;

/**
 * 考勤月统计 Convert
 *
 * @author
 */
@Mapper
public interface MonthStatisticsConvert {

    MonthStatisticsConvert INSTANCE = Mappers.getMapper(MonthStatisticsConvert.class);

    MonthStatisticsDO convert(MonthStatisticsCreateReqVO bean);

    MonthStatisticsDO convert(MonthStatisticsUpdateReqVO bean);

    MonthStatisticsRespVO convert(MonthStatisticsDO bean);

    List<MonthStatisticsRespVO> convertList(List<MonthStatisticsDO> list);

    PageResult<MonthStatisticsRespVO> convertPage(PageResult<MonthStatisticsDO> page);

    List<MonthStatisticsExcelVO> convertList02(List<MonthStatisticsDO> list);

    List<MonthStatisticsDO> convertList03(List<MonthStatisticsCreateReqVO> list);

}
