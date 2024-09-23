package com.lh.oa.module.bpm.api.definition.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author tanghanlin
 * @since 2023/12/15
 */
@Getter
@Setter
@ToString
public class BpmProcessDefinitionFromOptionsTo implements Serializable {

    /**
     * 字段标签
     */
    private String label;

    /**
     * 字段选项
     */
    private String value;

    public BpmProcessDefinitionFromOptionsTo() {
    }

    public BpmProcessDefinitionFromOptionsTo(String label, String value) {
        this.label = label;
        this.value = value;
    }
}
