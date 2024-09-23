package com.lh.oa.module.bpm.controller.admin.task.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author tanghanlin
 * @since 2023/12/14
 */
@Getter
@Setter
@ToString
public class BpmProcessAttendanceExportVo implements Serializable {

    @ExcelProperty("流程发起人")
    private String userName;

    @ExcelProperty("部门")
    private String deptName;

    @ExcelProperty("关联项目")
    private String projectName;

    @ExcelProperty("流程名称")
    private String processName;

    @ExcelProperty("流程创建时间")
    private String processStartTime;

    @ExcelProperty("流程表单数据")
    private String formListStr;

    @ExcelProperty("节点表单数据")
    private String nodeListStr;

}
