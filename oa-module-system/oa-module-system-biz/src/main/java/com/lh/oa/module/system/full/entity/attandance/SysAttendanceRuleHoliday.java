package com.lh.oa.module.system.full.entity.attandance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* 考勤规则自定义节假日表
*
* @author tanghanlin
* @since 2023-11-10
*/
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_attendance_rule_holiday")
public class SysAttendanceRuleHoliday extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "sys_attendance_rule_holiday_id", type = IdType.AUTO)
    private Long sysAttendanceRuleHolidayId;

    /**
     * 考勤规则id
     */
    private Long sysAttendanceRuleId;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 年
     */
    private String holidayYear;

    /**
     * 月份
     */
    private String holidayMonth;

    /**
     * 具体日期
     */
    private Date holidayDate;

    /**
     * 类型，1休息日，2工作日，默认休息日
     */
    private Integer holidayType;

    /**
     * 说明
     */
    private String holidayExplain;

}
