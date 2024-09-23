package com.lh.oa.module.system.api.materialApply;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "system-server", path = "/material/apply", contextId = "SysAttendanceRecordApi")
@Tag(name = "RPC服务 - 物料申请流程物料入库")
public interface MaterialApplyApi {
    /**
     * 物料入库
     */
    @PostMapping("/materialApplyStorage")
    String materialApplyStorage(@RequestBody Map<String, Object> variableInstances);
}
