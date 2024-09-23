package com.lh.oa.module.bpm.service.definition;

import java.util.List;

public interface BpmModelDeptService {

    void saveOrUpdate(String modelId, List<Long> deptIds);
}
