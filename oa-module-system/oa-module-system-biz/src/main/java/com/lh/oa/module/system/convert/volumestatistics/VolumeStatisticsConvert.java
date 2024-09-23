package com.lh.oa.module.system.convert.volumestatistics;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsCreateReqVO;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsExcelVO;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsRespVO;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.volumestatistics.VolumeStatisticsDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.*;

/**
 * 员工方量统计 Convert
 *
 * @author
 */
@Mapper
public interface VolumeStatisticsConvert {

    VolumeStatisticsConvert INSTANCE = Mappers.getMapper(VolumeStatisticsConvert.class);

    VolumeStatisticsDO convert(VolumeStatisticsCreateReqVO bean);

    VolumeStatisticsDO convert(VolumeStatisticsUpdateReqVO bean);

    VolumeStatisticsRespVO convert(VolumeStatisticsDO bean);

    List<VolumeStatisticsRespVO> convertList(List<VolumeStatisticsDO> list);

    PageResult<VolumeStatisticsRespVO> convertPage(PageResult<VolumeStatisticsDO> page);

    List<VolumeStatisticsExcelVO> convertList02(List<VolumeStatisticsDO> list);

}
