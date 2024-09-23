package com.lh.oa.module.system.full.entity.jnt;

import com.lh.oa.module.system.full.entity.base.BaseEntity;
import com.lh.oa.module.system.full.enums.FlagStateEnum;
import com.lh.oa.module.system.full.enums.jnt.EmployTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.GenderEnum;
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.SourceEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class JntUser extends BaseEntity {
    private OperateTypeEnum operateType;
    private String ids;

    private int oaUserId;
    private int oaOrgId;
    private Integer oaDeptId;
    private Integer oaPostId;

    private String account;
    private String password;
    private String mobile;
    private GenderEnum gender;
    private EmployTypeEnum employType;
    private FlagStateEnum state;
    private SourceEnum source;
    private int orderNumber;
    private String identityCard;
    /**
     * 车牌号
     */
    private String carNumber;

}