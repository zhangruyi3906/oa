package com.lh.oa.module.system.dal.dataobject.attendanceRule;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalTime;

/**
 * 打卡规则（部门） DO
 *
 * @author
 */
@TableName("attendance_rule")
//("attendance_rule_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRuleDO extends BaseDO {

    /**
     * 规则ID
     */
    @TableId
    private Long id;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 打卡半径（米）
     */
    private Object punchRadius;

    /**
     * 上班时间
     */
    private LocalTime flexibleCheckInStart;
    /**
     * 下班时间
     */
    private LocalTime flexibleCheckInEnd;
    /**
     * 描述
     */
    private String description;
    /**
     * 打卡经度纬度
     */
    private String latiLong;

    private String deptName;
    private String punchTypeName;
    private String workDays;
    private Integer syncHolidays;



}
