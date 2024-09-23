package com.lh.oa.module.system.full.service.attendance.impl;

import cn.hutool.core.util.StrUtil;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRulePositionEntity;
import com.lh.oa.module.system.full.mapper.attendance.SysAttendanceRulePositionMapper;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRulePositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.module.system.full.enums.FlagStateEnum.ENABLED;
import static com.lh.oa.module.system.full.utils.Utils.unixTime;

@Service
public class SysAttendanceRulePositionServiceImpl implements SysAttendanceRulePositionService {

    @Autowired
    private SysAttendanceRulePositionMapper mapper;

    @Transactional
    @Override
    public void saveAttendanceRulePosition(SysAttendanceRulePositionEntity attendanceRulePosition, int userId) {
        if (attendanceRulePosition.getAttendanceRuleId() == 0)
            throw new BusinessException("考勤规则不能为空，请确认后在提交");
        if (StrUtil.isBlank(attendanceRulePosition.getPosition()))
            throw new BusinessException("位置不能为空，请确认后在提交");
        if (attendanceRulePosition.getLongitude() == null || attendanceRulePosition.getLatitude() == null)
            throw new BusinessException("经纬度不能为空，请确认后在提交");
        if (attendanceRulePosition.getRange() == 0)
            throw new BusinessException("范围不能为空，请确认后在提交");
        attendanceRulePosition.setCreatedBy(userId);
        attendanceRulePosition.setCreatedTime(unixTime());
        attendanceRulePosition.setFlag(ENABLED.value());
        mapper.saveAttendanceRulePosition(attendanceRulePosition);
    }

    @Override
    public List<SysAttendanceRulePositionEntity> getAttendanceRulePositionListByAttendanceRuleId(int attendanceRuleId) {
        return mapper.getAttendanceRulePositionListByAttendanceRuleId(attendanceRuleId);
    }

    @Transactional
    @Override
    public void deleteAttendanceRulePositionByAttendanceRuleId(int attendanceRuleId, int userId) {
        mapper.deleteAttendanceRulePositionByAttendanceRuleId(attendanceRuleId, unixTime(), userId);
    }

    @Override
    public Map<Integer, List<SysAttendanceRulePositionEntity>> getRulePositionByRuleIds(String ruleIdsStr) {
        List<SysAttendanceRulePositionEntity> positionList = mapper.getRulePositionByRuleIds(ruleIdsStr);
        return positionList.stream().collect(Collectors.groupingBy(SysAttendanceRulePositionEntity::getAttendanceRuleId));
    }

}
