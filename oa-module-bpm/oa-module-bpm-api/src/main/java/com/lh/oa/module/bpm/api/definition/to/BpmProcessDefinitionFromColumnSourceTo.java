package com.lh.oa.module.bpm.api.definition.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author tanghanlin
 * @since 2023/12/15
 */
@Getter
@Setter
@ToString
public class BpmProcessDefinitionFromColumnSourceTo implements Serializable {

    /**
     * 表单参数
     */
    private String url;

    /**
     * 表单请求参数，key是参数，value是传的值
     */
    private Map<String, String> data;

}
