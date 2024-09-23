package com.lh.oa.module.bpm.api.task.dto;

import com.lh.oa.framework.common.pojo.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2023/11/3
 */
@Getter
@Setter
@ToString
public class BpmProcessInstanceParam extends PageParam {

    /**
     * 用户ids
     */
    private Set<Long> userIds;

    /**
     * 考勤月份
     */
    private Integer attendMonth;

    /**
     * 流程定义关键字
     */
    private String processName;

    /**
     * 查询开始时间
     */
    private Integer startTime;

    /**
     * 查询结束时间
     */
    private Integer endTime;

    public BpmProcessInstanceParam() {
    }

    public BpmProcessInstanceParam(Set<Long> userIds, Integer attendMonth, String processName) {
        this.userIds = userIds;
        this.attendMonth = attendMonth;
        this.processName = processName;
    }
}
