package com.lh.oa.module.system.dal.dataobject.orderMeet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditMeet {
    private Long id;

    private Integer status;
}
