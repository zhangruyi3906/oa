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
public class BpmProcessDefinitionFromColumnEditTo implements Serializable {

    /**
     * 字段来源
     */
    private BpmProcessDefinitionFromColumnSourceTo source;

    /**
     * 字段类型
     */
    private String type;

}
