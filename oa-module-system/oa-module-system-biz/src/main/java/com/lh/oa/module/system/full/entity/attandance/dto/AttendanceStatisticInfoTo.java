package com.lh.oa.module.system.full.entity.attandance.dto;

import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceTo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class AttendanceStatisticInfoTo implements Serializable {

    /**
     * 用户-项目-小时差距的映射关系
     */
    private Map<Integer, Map<Integer, BigDecimal>> userAndProjectWorkTime = new HashMap<>();

    /**
     * 用户-项目-表单值列表的映射关系
     */
    private Map<Integer, Map<Integer, List<Map<String, Object>>>> userAndProjectProcessFormMap = new HashMap<>();

    /**
     * 用户-项目-表单实例列表的映射关系
     */
    private Map<Integer, Map<Integer, List<BpmProcessInstanceTo>>> userAndProjectProcessMap = new HashMap<>();

}