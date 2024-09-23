package com.lh.oa.module.system.full.entity.jnt;

import com.lh.oa.module.system.full.entity.base.BaseEntity;
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.SourceEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class JntDept extends BaseEntity {

    private OperateTypeEnum operateType;
    private String ids;

    private int oaOrgId;
    private int oaDeptId;
    private int oaParentDeptId;
    private SourceEnum source;
    private int orderNumber;

}