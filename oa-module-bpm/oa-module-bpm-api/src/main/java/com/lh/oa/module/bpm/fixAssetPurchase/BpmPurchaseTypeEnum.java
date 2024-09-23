package com.lh.oa.module.bpm.fixAssetPurchase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BpmPurchaseTypeEnum {

    ADMINISTRATION(10, "行政类"),
    PRODUCE(20, "生产类")
    ;

    private final Integer type;
    private final String desc;
}