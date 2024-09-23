package com.lh.oa.module.bpm.fixAssetPurchase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BpmPurchaseStatusEnum {
    NORMAL(10, "正常"),
    IMPORTANT(20, "重要"),
    EMERGENT(30, "紧急")
    ;

    private final Integer type;
    private final String desc;
}
