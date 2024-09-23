package com.lh.oa.module.bpm.controller.admin.task.vo.instance;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BpmProcessInstanceExportVO {
    //开始时间
    private Date createTime;
    //结束时间
    private Date endTime;
    //流程类型
    private String type;
    //流程实例名称
    private String instanceName;
    //发起人id
    private List<Long> userIds;
    //发起人部门id
    private List<Long> deptIds;
    //精准搜索
    private String keyWord;
}
