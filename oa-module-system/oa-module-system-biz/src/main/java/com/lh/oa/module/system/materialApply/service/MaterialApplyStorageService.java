package com.lh.oa.module.system.materialApply.service;

import java.util.Map;

public interface MaterialApplyStorageService {
    /**
     * 物料申请流程物料自动入库
     * @param variableInstances
     */
    String materialApplyStorage(Map<String, Object> variableInstances);
}
