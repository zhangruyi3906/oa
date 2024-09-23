package com.lh.oa.module.system.full.entity.attandance.vo;

import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleHoliday;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * 考勤规则自定义节假日表
 *
 * @author tanghanlin
 * @since 2023-11-10
 */
@Getter
@Setter
@ToString
public class SysAttendanceRuleHolidayVo implements Serializable {

    /**
     * id
     */
    private Long sysAttendanceRuleHolidayId;

    /**
     * 具体日期
     */
    private String holidayDate;

    /**
     * 类型，1休息日，2工作日，默认休息日
     */
    private Integer holidayType;

    /**
     * 说明
     */
    private String holidayExplain;

    public boolean same(SysAttendanceRuleHoliday holiday) {
        if (Objects.isNull(holiday)) {
            return false;
        }
        return Objects.equals(holidayDate, TimeUtils.formatAsDate(holiday.getHolidayDate())) &&
                Objects.equals(holidayType, holiday.getHolidayType()) &&
                Objects.equals(holidayExplain, holiday.getHolidayExplain());
    }

}
