package com.lh.oa.module.system.api.sysAttendanceRule.to;

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
public class SysAttendanceRecordQueryParam implements Serializable {

    /**
     * 用户ids
     */
    private Set<Long> userIds;

    /**
     * 查询月份
     */
    private Integer queryAttendanceMonth;

    /**
     * 查询最近几个月的数据
     */
    private Integer queryMonthCount = 1;

    public SysAttendanceRecordQueryParam() {
    }

    public SysAttendanceRecordQueryParam(Set<Long> userIds) {
        this.userIds = userIds;
    }

    public SysAttendanceRecordQueryParam(Set<Long> userIds, Integer queryAttendanceMonth) {
        this.userIds = userIds;
        this.queryAttendanceMonth = queryAttendanceMonth;
    }
}
