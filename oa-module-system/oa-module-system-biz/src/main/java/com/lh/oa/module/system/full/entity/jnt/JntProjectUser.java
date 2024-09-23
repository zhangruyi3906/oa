package com.lh.oa.module.system.full.entity.jnt;

import com.lh.oa.module.system.full.entity.base.BaseEntity;
import com.lh.oa.module.system.full.enums.jnt.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class JntProjectUser extends BaseEntity {
    private OperateTypeEnum operateType;

    private int oaProjectId;
    private int oaUserId;
    private int entrantTime;

    private ProjectUserTypeEnum staffType;
    private ProjectAttendanceStateEnum attendState;

    private SourceEnum source;

}