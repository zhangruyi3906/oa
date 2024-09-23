package com.lh.oa.module.bpm.service.definition;

import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormFieldExportDO;

import java.util.List;

public interface BpmFormFieldExportService {
    void saveOrUpdate(Long formId, List<BpmFormFieldExportDO> bpmFormFieldExportDOList);

    List<BpmFormFieldExportDO> getExportList(Long formId);
}
