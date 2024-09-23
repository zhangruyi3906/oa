package com.lh.oa.module.system.service.monthstatistics;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.*;
import com.lh.oa.module.system.dal.dataobject.monthstatistics.MonthStatisticsDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.MonthStatisticsExportReqVO;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.MonthStatisticsPageReqVO;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.MonthStatisticsUpdateReqVO;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.RecordMonthVO;

/**
 * 考勤月统计 Service 接口
 *
 * @author
 */
public interface MonthStatisticsService {

    /**
     * 创建考勤月统计
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMonthStatistics(@Valid RecordMonthVO createReqVO);


    Long createMonthProjectStatistics(RecordMonthVO createReqVO);

    /**
     * 更新考勤月统计
     *
     * @param updateReqVO 更新信息
     */
    void updateMonthStatistics(@Valid MonthStatisticsUpdateReqVO updateReqVO);

    /**
     * 删除考勤月统计
     *
     * @param id 编号
     */
    void deleteMonthStatistics(Long id);

    /**
     * 获得考勤月统计
     *
     * @param id 编号
     * @return 考勤月统计
     */
    MonthStatisticsDO getMonthStatistics(Long id);

    /**
     * 获得考勤月统计列表
     *
     * @param ids 编号
     * @return 考勤月统计列表
     */
    List<MonthStatisticsDO> getMonthStatisticsList(Collection<Long> ids);

    /**
     * 获得考勤月统计分页
     *
     * @param pageReqVO 分页查询
     * @return 考勤月统计分页
     */
    PageResult<MonthStatisticsDO> getMonthStatisticsPage(MonthStatisticsPageReqVO pageReqVO);
    PageResult<MonthStatisticsDO> getProjectMonthStatisticsPage(MonthStatisticsPageReqVO pageReqVO);

    /**
     * 获得考勤月统计列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 考勤月统计列表
     */
    List<MonthStatisticsDO> getMonthStatisticsList(MonthStatisticsExportReqVO exportReqVO);

}
