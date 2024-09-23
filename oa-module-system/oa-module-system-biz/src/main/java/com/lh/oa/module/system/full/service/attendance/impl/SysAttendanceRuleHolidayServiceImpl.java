package com.lh.oa.module.system.full.service.attendance.impl;

import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleEntity;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleHoliday;
import com.lh.oa.module.system.full.entity.attandance.vo.SysAttendanceRuleHolidayVo;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRuleHolidayService;
import com.lh.oa.module.system.mapper.SysAttendanceRuleHolidayMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tanghanlin
 * @since 2023/11/10
 */
@Service
public class SysAttendanceRuleHolidayServiceImpl implements SysAttendanceRuleHolidayService {

    @Resource
    private SysAttendanceRuleHolidayMapper sysAttendanceRuleHolidayMapper;

    @Transactional
    @Override
    public void save(SysAttendanceRuleEntity rule, List<SysAttendanceRuleHolidayVo> params) {
        if (Objects.equals(rule.getEnableCustomHoliday(), false)) {
            return;
        }
        if (Objects.isNull(params)) {
            params = Collections.emptyList();
        }
        List<SysAttendanceRuleHoliday> holidays = sysAttendanceRuleHolidayMapper.selectList(new LambdaQueryWrapperX<SysAttendanceRuleHoliday>()
                .eq(SysAttendanceRuleHoliday::getDeleted, 0)
                .eq(SysAttendanceRuleHoliday::getSysAttendanceRuleId, rule.getId()));
        Map<Long, SysAttendanceRuleHoliday> holidayMap = holidays
                .stream()
                .collect(Collectors.toMap(SysAttendanceRuleHoliday::getSysAttendanceRuleHolidayId, Function.identity()));
        Set<Long> newIds = params
                .stream()
                .filter(param -> Objects.nonNull(param.getSysAttendanceRuleHolidayId()))
                .map(SysAttendanceRuleHolidayVo::getSysAttendanceRuleHolidayId)
                .collect(Collectors.toSet());
        // 获取需要删除的ids
        Set<Long> deleteIds = holidays
                .stream()
                .filter(holiday -> !newIds.contains(holiday.getSysAttendanceRuleHolidayId()))
                .map(SysAttendanceRuleHoliday::getSysAttendanceRuleHolidayId)
                .collect(Collectors.toSet());
        // 没有id的新增
        List<SysAttendanceRuleHoliday> insertList = params
                .stream()
                .filter(param -> Objects.isNull(param.getSysAttendanceRuleHolidayId()))
                .map(param -> this.buildSysAttendanceRuleHoliday(rule, param))
                .collect(Collectors.toList());
        // 其余的如果有变动，则更新
        List<SysAttendanceRuleHoliday> updateList = params
                .stream()
                .filter(param -> Objects.nonNull(param.getSysAttendanceRuleHolidayId())
                        &&!deleteIds.contains(param.getSysAttendanceRuleHolidayId())
                        && !param.same(holidayMap.get(param.getSysAttendanceRuleHolidayId())))
                .map(param -> this.updateSysAttendanceRuleHoliday((long) rule.getModifiedBy(), param))
                .collect(Collectors.toList());
        if (!deleteIds.isEmpty()) {
            sysAttendanceRuleHolidayMapper.deleteHoliday(deleteIds);
        }
        if (!insertList.isEmpty()) {
            sysAttendanceRuleHolidayMapper.insertBatch(insertList);
        }
        if (!updateList.isEmpty()) {
            sysAttendanceRuleHolidayMapper.updateBatch(updateList, updateList.size());
        }
    }

    @Override
    public List<SysAttendanceRuleHoliday> queryListByRuleIds(Set<Long> sysAttendanceRuleIds) {
        return sysAttendanceRuleHolidayMapper.selectList(new LambdaQueryWrapperX<SysAttendanceRuleHoliday>()
//                .eq(SysAttendanceRuleHoliday::getDeleted, 0)
                .in(SysAttendanceRuleHoliday::getSysAttendanceRuleId, sysAttendanceRuleIds));
    }

    private SysAttendanceRuleHoliday buildSysAttendanceRuleHoliday(SysAttendanceRuleEntity rule, SysAttendanceRuleHolidayVo param) {
        SysAttendanceRuleHoliday holiday = JsonUtils.covertObject(param, SysAttendanceRuleHoliday.class);
        Date date = TimeUtils.parseAsDate(param.getHolidayDate(), "yyyy-MM-dd");
        holiday.setSysAttendanceRuleId((long) rule.getId());
        holiday.setHolidayYear(TimeUtils.formatAsYYYY(date));
        holiday.setHolidayMonth(TimeUtils.formatAsMonth(date));
        holiday.setHolidayDate(date);
        holiday.setProjectId(rule.getProjectId().longValue());
        holiday.setCreator(rule.getCreatedBy() + "");
        holiday.setCreateTime(LocalDateTime.now());
        return holiday;
    }

    private SysAttendanceRuleHoliday updateSysAttendanceRuleHoliday(Long userId, SysAttendanceRuleHolidayVo param) {
        SysAttendanceRuleHoliday holiday = JsonUtils.covertObject(param, SysAttendanceRuleHoliday.class);
        Date date = TimeUtils.parseAsDate(param.getHolidayDate(), "yyyy-MM-dd");
        holiday.setHolidayYear(TimeUtils.formatAsYYYY(date));
        holiday.setHolidayMonth(TimeUtils.formatAsMonth(date));
        holiday.setHolidayDate(date);
        holiday.setUpdater(userId.toString());
        holiday.setCreateTime(LocalDateTime.now());
        return holiday;
    }

}
