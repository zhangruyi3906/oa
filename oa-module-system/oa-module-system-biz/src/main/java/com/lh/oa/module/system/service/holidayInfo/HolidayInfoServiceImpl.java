package com.lh.oa.module.system.service.holidayInfo;

import com.lh.oa.framework.common.util.StringUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.query.QueryWrapperX;
import com.lh.oa.module.system.dal.dataobject.holidayInfo.HolidayInfoDO;
import com.lh.oa.module.system.dal.mysql.holidayInfo.HolidayInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Validated
@Slf4j
public class HolidayInfoServiceImpl implements HolidayInfoService{
    @Resource
    private HolidayInfoMapper mapper;

    @Override
    public Integer getIsWork(Integer id) {
        return mapper.selectOne(new LambdaQueryWrapperX<HolidayInfoDO>().inIfPresent(HolidayInfoDO::getDate, id)).getWorkday();
    }

    @Override
    public Integer getWorkDayCountByMonth(Date month, List<Integer> weekDayList, Boolean syncHoliday, Date endDate) {
        String startDateStr = TimeUtils.formatAsYYYYMMDD(TimeUtils.getMonthStartDate(month));
        String endDateStr;
        if (Objects.nonNull(endDate)) {
            endDateStr = TimeUtils.formatAsYYYYMMDD(endDate);
        }
        else {
            endDateStr = TimeUtils.formatAsYYYYMMDD(TimeUtils.getMonthEndDate(month));
        }
        LambdaQueryWrapperX<HolidayInfoDO> queryWrapperX = new LambdaQueryWrapperX<HolidayInfoDO>()
                .betweenIfPresent(HolidayInfoDO::getDate, startDateStr, endDateStr);
        if (!syncHoliday) {
            queryWrapperX.inIfPresent(HolidayInfoDO::getWeek, weekDayList);
        }
        else {
            // 如果同步节假日，则过滤掉假期节假日
            queryWrapperX.and(wq -> wq.in(HolidayInfoDO::getWeek, weekDayList).or().ne(HolidayInfoDO::getHolidayOvertime, 10))
                    .eq(HolidayInfoDO::getHolidayRecess, 2);
        }
        List<HolidayInfoDO> holidayInfoS = mapper.selectList(queryWrapperX);
        return holidayInfoS.size();
    }

    @Override
    public List<HolidayInfoDO> getDayListByMonth(Date month) {
        String startDateStr = TimeUtils.formatAsYYYYMMDD(TimeUtils.getMonthStartDate(month));
        String endDateStr = TimeUtils.formatAsYYYYMMDD(TimeUtils.getMonthEndDate(month));
        LambdaQueryWrapperX<HolidayInfoDO> queryWrapperX = new LambdaQueryWrapperX<HolidayInfoDO>()
                .betweenIfPresent(HolidayInfoDO::getDate, startDateStr, endDateStr)
                .eq(HolidayInfoDO::getDeleted, 0);
        return mapper.selectList(queryWrapperX);
    }

    @Override
    public List<HolidayInfoDO> getDayListByMonthYear(Date year) {
        String yearStr = TimeUtils.formatAsYYYY(year);
        LambdaQueryWrapperX<HolidayInfoDO> queryWrapperX = new LambdaQueryWrapperX<HolidayInfoDO>()
                .eqIfPresent(HolidayInfoDO::getYear, Integer.parseInt(yearStr))
                .eq(HolidayInfoDO::getDeleted, 0);
        return mapper.selectList(queryWrapperX);
    }

    @Override
    public HolidayInfoDO getByDate(Date date) {
        String dateStr = TimeUtils.formatAsYYYYMMDD(date);
        return mapper.selectOne(new LambdaQueryWrapperX<HolidayInfoDO>()
                .eq(HolidayInfoDO::getDate, dateStr));
    }


}
