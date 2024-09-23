package com.lh.oa.module.system.service.volumestatistics;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.*;
import com.lh.oa.module.system.dal.dataobject.volumestatistics.VolumeStatisticsDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsCreateReqVO;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsExportReqVO;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsPageReqVO;
import com.lh.oa.module.system.controller.admin.volumestatistics.vo.VolumeStatisticsUpdateReqVO;

/**
 * 员工方量统计 Service 接口
 *
 * @author
 */
public interface VolumeStatisticsService {

    /**
     * 创建员工方量统计
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVolumeStatistics(@Valid VolumeStatisticsCreateReqVO createReqVO);

    /**
     * 更新员工方量统计
     *
     * @param updateReqVO 更新信息
     */
    void updateVolumeStatistics(@Valid VolumeStatisticsUpdateReqVO updateReqVO);

    /**
     * 删除员工方量统计
     *
     * @param id 编号
     */
    void deleteVolumeStatistics(Long id);

    /**
     * 获得员工方量统计
     *
     * @param id 编号
     * @return 员工方量统计
     */
    VolumeStatisticsDO getVolumeStatistics(Long id);

    /**
     * 获得员工方量统计列表
     *
     * @param ids 编号
     * @return 员工方量统计列表
     */
    List<VolumeStatisticsDO> getVolumeStatisticsList(Collection<Long> ids);

    /**
     * 获得员工方量统计分页
     *
     * @param pageReqVO 分页查询
     * @return 员工方量统计分页
     */
    PageResult<VolumeStatisticsDO> getVolumeStatisticsPage(VolumeStatisticsPageReqVO pageReqVO);

    /**
     * 获得员工方量统计列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 员工方量统计列表
     */
    List<VolumeStatisticsDO> getVolumeStatisticsList(VolumeStatisticsExportReqVO exportReqVO);

}
