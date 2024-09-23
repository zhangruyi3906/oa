package com.lh.oa.module.bpm.controller.admin.task.vo.instance;

import lombok.Data;

import java.util.List;

@Data
public class BpmProcessInstanceRetractResVO {
    private String formConf;
    private List<String> formFields;
    private String formCustomCreatePath;
}
