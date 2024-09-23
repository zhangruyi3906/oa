package com.lh.oa.module.bpm.controller.admin.task.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author tanghanlin
 * @since 2023/12/14
 */
@Getter
@Setter
@ToString
public class BpmProcessAttendanceVo implements Serializable {

    /**
     * 流程发起人
     */
    private String userName;

    /**
     * 部门
     */
    private String deptName;

    /**
     * 项目
     */
    private String projectName;

    /**
     * 流程名
     */
    private String processName;

    /**
     * 流程实例ID
     */
    private String processId;

    /**
     * 流程发起时间
     */
    private String processStartTime;

    /**
     * 流程结束时间
     */
    private String processEndTime;

    /**
     * 表单开始时间
     */
    private String formStartTime;

    /**
     * 表单结束时间
     */
    private String formEndTime;

    /**
     * 流程表单数据列表字符串
     */
    private String formListStr;

    /**
     * 节点表单数据列表字符串
     */
    private String nodeListStr;

}
