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
public class BpmProcessAttendanceFormVo implements Serializable {

    /**
     * 表单字段名
     */
    private String formName;

    /**
     * 表单标签值
     */
    private String formLabel;

    /**
     * 表单值
     */
    private String formValue;

    public BpmProcessAttendanceFormVo() {
    }

    public BpmProcessAttendanceFormVo(String formName, String formLabel, String formValue) {
        this.formName = formName;
        this.formLabel = formLabel;
        this.formValue = formValue;
    }

}
