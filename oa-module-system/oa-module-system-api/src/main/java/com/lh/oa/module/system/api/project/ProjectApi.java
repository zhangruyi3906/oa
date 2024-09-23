package com.lh.oa.module.system.api.project;

import com.lh.oa.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "system-server", path = "/admin-api/system/project", contextId = "ProjectApi")
@Tag(name = "RPC服务 - 项目")
public interface ProjectApi {

    @GetMapping("/getJNTProjectByIds")
    CommonResult<Map<String, String>> getJNTProjectByIds(@RequestParam("projectIds") String projectIds);

}
