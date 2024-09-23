package com.lh.oa.module.system.full.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class BaseEntity extends KeyValue {

    private static final long serialVersionUID = 1L;

    private int createdTime;

    private int createdBy;

    private int modifiedTime;

    private int modifiedBy;

    private int orderNumber;

    /**
     * 统一标记 1启用, 2锁定, 4隐藏, 8过期
     */
    private int flag;

}
