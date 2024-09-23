package com.lh.oa.module.system.full.entity.postsale;

import com.lh.oa.module.system.full.entity.base.BaseEntity;
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PostSaleUser extends BaseEntity {
    private OperateTypeEnum operateType;
    private String ids;

    private int oaUserId;
    private String username;
    private String password;
    private String phone;
    private String email;
}
