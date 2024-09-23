package com.lh.oa.module.system.service.volumestatistics;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsCreateReqVO;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsExportReqVO;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsPageReqVO;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsUpdateReqVO;
import com.lh.oa.module.system.convert.volumestatistics.VolumeStatisticsConvert;
import com.lh.oa.module.system.dal.dataobject.volumestatistics.VolumeStatisticsDO;
import com.lh.oa.module.system.dal.mysql.volumestatistics.VolumeStatisticsMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.*;
import com.lh.oa.framework.common.pojo.PageResult;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 员工方量统计 Service 实现类
 *
 * @author
 */
@Service
@Validated
public class VolumeStatisticsServiceImpl implements VolumeStatisticsService {

    @Resource
    private VolumeStatisticsMapper volumeStatisticsMapper;

    @Override
    public Long createVolumeStatistics(VolumeStatisticsCreateReqVO createReqVO) {
        // 插入
        VolumeStatisticsDO volumeStatistics = VolumeStatisticsConvert.INSTANCE.convert(createReqVO);
        volumeStatisticsMapper.insert(volumeStatistics);
        // 返回
        return volumeStatistics.getId();
    }

    @Override
    public void updateVolumeStatistics(VolumeStatisticsUpdateReqVO updateReqVO) {
        // 校验存在
        validateVolumeStatisticsExists(updateReqVO.getId());
        // 更新
        VolumeStatisticsDO updateObj = VolumeStatisticsConvert.INSTANCE.convert(updateReqVO);
        volumeStatisticsMapper.updateById(updateObj);
    }

    @Override
    public void deleteVolumeStatistics(Long id) {
        // 校验存在
        validateVolumeStatisticsExists(id);
        // 删除
        volumeStatisticsMapper.deleteById(id);
    }

    private void validateVolumeStatisticsExists(Long id) {
        if (volumeStatisticsMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.VOLUME_STATISTICS_NOT_EXISTS);
        }
    }

    @Override
    public VolumeStatisticsDO getVolumeStatistics(Long id) {
        return volumeStatisticsMapper.selectById(id);
    }

    @Override
    public List<VolumeStatisticsDO> getVolumeStatisticsList(Collection<Long> ids) {
        return volumeStatisticsMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<VolumeStatisticsDO> getVolumeStatisticsPage(VolumeStatisticsPageReqVO pageReqVO) {
        return volumeStatisticsMapper.selectPage(pageReqVO);
    }

    @Override
    public List<VolumeStatisticsDO> getVolumeStatisticsList(VolumeStatisticsExportReqVO exportReqVO) {
        return volumeStatisticsMapper.selectList(exportReqVO);
    }

}
