package com.lh.oa.module.system.full.service.attendance;

import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRulePositionEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysAttendanceRulePositionService {

    void saveAttendanceRulePosition(SysAttendanceRulePositionEntity attendanceRulePosition, int userId);

    List<SysAttendanceRulePositionEntity> getAttendanceRulePositionListByAttendanceRuleId(int attendanceRuleId);

    void deleteAttendanceRulePositionByAttendanceRuleId(int attendanceRuleId, int userId);

    Map<Integer, List<SysAttendanceRulePositionEntity>> getRulePositionByRuleIds(String ruleIdsStr);

}
