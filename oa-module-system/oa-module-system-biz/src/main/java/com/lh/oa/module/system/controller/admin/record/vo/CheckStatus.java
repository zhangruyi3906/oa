package com.lh.oa.module.system.controller.admin.record.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckStatus {

    private Long checkId;
    private Byte checkStatus;
}
