package com.lh.oa.module.system.full.entity.jnt;

import com.lh.oa.module.system.full.entity.base.BaseEntity;
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.SourceEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class JntPost extends BaseEntity {
    private OperateTypeEnum operateType;
    private String ids;

    private int oaPostId;
    private int oaOrgId;
    private int oaDeptId;
    private String description;
    private SourceEnum source;
    private int orderNumber;

}