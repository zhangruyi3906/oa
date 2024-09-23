package com.lh.oa.module.system.service.schedule;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.schedule.vo.*;
import com.lh.oa.module.system.dal.dataobject.schedule.ScheduleDO;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 日程管理 Service 接口
 *
 * @author didida
 */
public interface ScheduleService {

    /**
     * 创建日程管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSchedule(@Valid ScheduleCreateReqVO createReqVO);

    /**
     * 更新日程管理
     *
     * @param updateReqVO 更新信息
     */
    void updateSchedule(@Valid ScheduleUpdateReqVO updateReqVO);

    /**
     * 删除日程管理
     *
     * @param id 编号
     */
    void deleteSchedule(Long id);

    /**
     * 获得日程管理
     *
     * @param id 编号
     * @return 日程管理
     */
    ScheduleDO getSchedule(Long id);

    /**
     * 获得日程管理列表
     *
     * @param ids 编号
     * @return 日程管理列表
     */
    List<ScheduleDO> getScheduleList(ScheduleRespVO respVo);

    /**
     * 获得日程管理分页
     *
     * @param pageReqVO 分页查询
     * @return 日程管理分页
     */
    PageResult<ScheduleDO> getSchedulePage(SchedulePageReqVO pageReqVO);

    /**
     * 获得日程管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 日程管理列表
     */
    List<ScheduleDO> getScheduleList(ScheduleExportReqVO exportReqVO);

    List<ScheduleDO> selectByTime(Date start, Date end, Long userId);

}
