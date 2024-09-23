package com.lh.oa.module.bpm.controller.admin.task.vo.save;

import com.lh.oa.module.bpm.dal.dataobject.task.BpmSaveInstanceDO;
import lombok.Data;

@Data
public class BpmSaveProcessInstanceResVO extends BpmSaveInstanceDO {
    private String processDefinitionId;
    /**
     * 用户名
     */
    private String username;
}
