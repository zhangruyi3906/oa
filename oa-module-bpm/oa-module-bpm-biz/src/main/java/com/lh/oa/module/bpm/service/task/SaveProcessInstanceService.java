package com.lh.oa.module.bpm.service.task;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.task.vo.save.BpmSaveProcessInstancePageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.save.BpmSaveProcessInstanceResVO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmSaveInstanceDO;

public interface SaveProcessInstanceService {
    PageResult<BpmSaveProcessInstanceResVO> getMySaveProcessInstancePage(Long loginUserId, BpmSaveProcessInstancePageReqVO pageReqVO);

    BpmSaveInstanceDO getSaveProcessInstance(Long saveId);

    void deleteSaveProcessInstance(Long saveId);
}
