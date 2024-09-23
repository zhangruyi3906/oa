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
public class BpmProcessDefinitionFromBodyTo implements Serializable {

    /**
     * 表单节点名称
     */
    private String title;

    /**
     * 表单字段实体
     */
    private List<BpmProcessDefinitionFromColumnTo> body;

    public BpmProcessDefinitionFromBodyTo() {
    }

    public BpmProcessDefinitionFromBodyTo(String title, List<BpmProcessDefinitionFromColumnTo> body) {
        this.title = title;
        this.body = body;
    }

}
