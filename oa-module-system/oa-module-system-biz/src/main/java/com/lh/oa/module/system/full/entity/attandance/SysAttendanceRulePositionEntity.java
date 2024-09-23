package com.lh.oa.module.system.full.entity.attandance;

import com.lh.oa.module.system.full.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysAttendanceRulePositionEntity extends BaseEntity {
    private Integer deptId;
    private Integer projectId;
    private Integer attendanceRuleId;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String position;
    private int range;
}