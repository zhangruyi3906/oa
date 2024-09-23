package com.lh.oa.module.bpm.service.definition;

import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormFieldShowDO;

import java.util.List;

public interface BpmFormFieldShowService {
    void saveOrUpdate(Long formId, List<BpmFormFieldShowDO> bpmFormFieldShowDOList);

    List<BpmFormFieldShowDO> getShowList(Long formId);
}
