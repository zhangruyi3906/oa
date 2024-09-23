package com.lh.oa.module.bpm.api.definition.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author tanghanlin
 * @since 2023/12/15
 */
@Getter
@Setter
@ToString
public class BpmProcessDefinitionFromTo implements Serializable {

    /**
     * 表单实体
     */
    private List<BpmProcessDefinitionFromBodyTo> body;

}
