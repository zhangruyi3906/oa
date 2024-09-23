package com.lh.oa.module.system.service.holidayInfo;

import com.lh.oa.module.system.dal.dataobject.holidayInfo.HolidayInfoDO;

import java.util.Date;
import java.util.List;

public interface HolidayInfoService {
    Integer getIsWork (Integer id);

    /**
     * 根据月份或者月内结束时间获取工作日天数
     *
     * @param month         月份
     * @param weekDayList   工作日对应的周内顺序
     * @param syncHoliday   是否同步法定节假日
     * @param endDate       结束时间
     * @return 工作日天数
     */
    Integer getWorkDayCountByMonth(Date month, List<Integer> weekDayList, Boolean syncHoliday, Date endDate);

    /**
     * 查询月内每日数据
     *
     * @param month 月份
     * @return 每日数据
     */
    List<HolidayInfoDO> getDayListByMonth(Date month);

    /**
     * 查询年内每日数据
     *
     * @param year 年份
     * @return 每日数据
     */
    List<HolidayInfoDO> getDayListByMonthYear(Date year);

    HolidayInfoDO getByDate(Date date);

}
