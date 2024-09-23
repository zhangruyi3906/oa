package com.lh.oa.module.system.enums.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 性别的枚举值
 *
 * @author
 */
@Getter
@AllArgsConstructor
public enum SexEnum {

    MALE(0, "男"),
    FEMALE(1, "女");

    private final Integer code;

    private final String name;

    public static String getNameByCode(Integer code) {
        for (SexEnum sexEnum : values()) {
            if (Objects.equals(sexEnum.getCode(), code)) {
                return sexEnum.getName();
            }
        }
        return "";
    }

    public static Integer getCodeByName(String name) {
        for (SexEnum sexEnum : values()) {
            if (Objects.equals(sexEnum.getName(), name)) {
                return sexEnum.getCode();
            }
        }
        return null;
    }

}
