package com.lh.oa.module.system.dal.mysql.volumestatistics;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsExportReqVO;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsPageReqVO;
import com.lh.oa.module.system.dal.dataobject.volumestatistics.VolumeStatisticsDO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.*;

/**
 * 员工方量统计 Mapper
 *
 * @author
 */
@Mapper
public interface VolumeStatisticsMapper extends BaseMapperX<VolumeStatisticsDO> {

    default PageResult<VolumeStatisticsDO> selectPage(VolumeStatisticsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VolumeStatisticsDO>()
                .eqIfPresent(VolumeStatisticsDO::getUserId, reqVO.getUserId())
                .eqIfPresent(VolumeStatisticsDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(VolumeStatisticsDO::getVolume, reqVO.getVolume())
                .betweenIfPresent(VolumeStatisticsDO::getVolumeDate, reqVO.getVolumeDate())
                .orderByDesc(VolumeStatisticsDO::getId));
    }

    default List<VolumeStatisticsDO> selectList(VolumeStatisticsExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<VolumeStatisticsDO>()
                .eqIfPresent(VolumeStatisticsDO::getUserId, reqVO.getUserId())
                .eqIfPresent(VolumeStatisticsDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(VolumeStatisticsDO::getVolume, reqVO.getVolume())
                .betweenIfPresent(VolumeStatisticsDO::getVolumeDate, reqVO.getVolumeDate())
                .orderByDesc(VolumeStatisticsDO::getId));
    }

}
