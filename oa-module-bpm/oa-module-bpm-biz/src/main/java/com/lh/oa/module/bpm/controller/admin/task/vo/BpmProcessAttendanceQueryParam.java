package com.lh.oa.module.bpm.controller.admin.task.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2023/12/14
 */
@Getter
@Setter
@ToString
public class BpmProcessAttendanceQueryParam extends PageParam {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 流程定义关键字
     */
    private String processName = "";

    /**
     * 查询开始时间
     */
    private Integer startDate;

    /**
     * 查询结束时间
     */
    private Integer endDate;

}
