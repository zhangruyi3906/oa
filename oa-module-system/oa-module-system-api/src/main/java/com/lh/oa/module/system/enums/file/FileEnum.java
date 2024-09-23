package com.lh.oa.module.system.enums.file;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum FileEnum {

    /** 公共 */
    PUBLIC("public"),
    /** 流程 */
    PROCEDURE("procedure"),
    /* 合同 */
    CONTRACT("contract"),
    /* 客户 */
    CUSTOMER("customer"),

    COMMON("common");



    /**
     * 性别
     */
    private final String source;

}
