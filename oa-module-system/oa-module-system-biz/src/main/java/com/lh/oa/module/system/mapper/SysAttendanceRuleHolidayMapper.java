package com.lh.oa.module.system.mapper;

import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleHoliday;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


/**
 * @author tanghanlin
 * @since 2023-11-10
 */
@Mapper
public interface SysAttendanceRuleHolidayMapper extends BaseMapperX<SysAttendanceRuleHoliday> {

    void deleteHoliday(@Param("ids")Set<Long> ids);

}
