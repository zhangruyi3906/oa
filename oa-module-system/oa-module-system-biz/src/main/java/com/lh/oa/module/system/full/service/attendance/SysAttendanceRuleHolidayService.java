package com.lh.oa.module.system.full.service.attendance;

import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleEntity;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleHoliday;
import com.lh.oa.module.system.full.entity.attandance.vo.SysAttendanceRuleHolidayVo;

import java.util.List;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2023/11/10
 */
public interface SysAttendanceRuleHolidayService {

    /**
     * 保存自定义节假日设置
     *
     * @param rule  考勤规则
     * @param param 保存参数
     */
    void save(SysAttendanceRuleEntity rule, List<SysAttendanceRuleHolidayVo> param);

    /**
     * 查询已设置的自定义节假日
     *
     * @param sysAttendanceRuleIds ids
     * @return 自定义节假日列表
     */
    List<SysAttendanceRuleHoliday> queryListByRuleIds(Set<Long> sysAttendanceRuleIds);

}
