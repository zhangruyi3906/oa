package com.lh.oa.module.system.api.sal;

import com.lh.oa.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(value = "system-server", path = "/sal", contextId = "SysAttendanceRecordApi")
@Tag(name = "RPC服务 - 项目打卡记录")
public interface SalSaleApi {

    @GetMapping("/getSalSaleOrderParams")
    CommonResult<String> getSalSaleOrderParams(Map<String, Object> variableInstances);
}
