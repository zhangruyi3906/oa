package com.lh.oa.module.system.dal.dataobject.monthstatistics;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 考勤月统计 DO
 *
 * @author
 */
@TableName("attendance_month_statistics")
//("attendance_month_statistics_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthStatisticsDO extends BaseDO {

    /**
     * 统计编号
     */
    @TableId
    private Long id;
    /**
     * 员工编号
     */
    private Long userId;
    /**
     * 项目编号
     */
    private Long projectId;
    /**
     * 部门编号
     */
    private Long deptId;
    /**
     * 考勤月份
     */
    private int attendanceMonth;
    /**
     * 总工作天数
     */
    private Integer totalWorkingDays;
    /**
     * 实际工作天数
     */
    private Integer actualWorkingDays;
    /**
     * 总加班小时数
     */
    private Integer totalOvertimeHours;
    /**
     * 总请假天数
     */
    private Integer totalLeaveDays;
    /**
     * 总缺勤天数
     */
    private Integer totalAbsenceDays;


    private Integer tenantId;

    private String userName;

    private String deptName;
    private String projectName;

    private Integer attStatus;


}
