package com.lh.oa.module.system.controller.admin.worklog.vo;

import lombok.Data;

import java.util.List;

@Data
public class WorkLogPersonVO {
    private String name;
    private List<Integer> dataList;
    private List<Integer> totalList;
    private Integer total;
}
