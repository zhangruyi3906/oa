package com.lh.oa.module.system.dal.dataobject.projectAttendanceRule;

import lombok.*;

import java.time.LocalTime;

import com.baomidou.mybatisplus.annotation.*;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;

/**
 * 打卡规则（项目） DO
 *
 * @author
 */
@TableName("attendance_project_rule")
//("attendance_project_rule_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAttendanceRuleDO extends BaseDO {

    /**
     * 项目规则id
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private String punchName;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 打卡半径
     */
    private String punchRadius;
    /**
     * 打卡经纬度
     */
    private String latiLong;


    private String projectName;

    private Integer punchType;

    private Integer allopatricStatus;

    private Integer syncHolidays;

    private String punchTypeName;

    private String workDays;

    private LocalTime endNextDay;
    private LocalTime flexibleCheckInStart;

    private LocalTime flexibleCheckInEnd;
}
