package com.lh.oa.module.bpm.api.definition.to;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BpmProcessDefinitionFromColumnTo implements Serializable {

    /**
     * 是否展示
     */
    private Boolean hidden = false;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 如果是input-type的表格字段，type是存在这个字段内的
     */
    @JsonProperty("quickEdit.type")
    private String quickEditType;

    /**
     * 字段标签
     */
    private String label;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段翻译方式
     */
    private String displayFormat;

    /**
     * 字段下拉列表，格式不统一，有的是对象有的是数组
     */
    private Object options;

    /**
     * 字段来源字符串，因为格式不统一所以只能这么处理
     */
    private Object source;

    /**
     * 字段来源
     */
    private BpmProcessDefinitionFromColumnSourceTo sourceTo;

    /**
     * 单位列表
     */
    private List<String> unitOptions;

    /**
     * 表格字段列表，type=input-table时，此字段会存储表格配置
     */
    private List<BpmProcessDefinitionFromColumnTo> columns;

    /**
     * type=input-table时，此字段下的source字段会存储表格配置
     */
    private Object quickEdit;

    /**
     * 表格字段列表，type=combo时，此字段会存储表格配置
     */
    private List<BpmProcessDefinitionFromColumnTo> items;

    /**
     * 表格字段列表，type=collapse，此字段下会有combo字段，但其他情况下可能为字符串
     */
    private Object body;

    /**
     * type=switch时，代表true的翻译值
     */
    private String onText;

    /**
     * type=switch时，代表false的翻译值
     */
    private String offText;

}
