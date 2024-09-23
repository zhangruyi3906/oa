package com.lh.oa.module.system.controller.admin.worklog.vo;

import lombok.Data;

import java.util.List;

@Data
public class WorkLogMonthTotalVO {
    private List<Integer> dateList;
    private List<WorkLogPersonVO> personList;
}
