package com.lh.oa.module.system.api.sysAttendanceRule;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.api.sysAttendanceRule.to.SysAttendanceRuleTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(value = "system-server", path = "/api/attendance/rule", contextId = "SysAttendanceRuleApi")
@Tag(name = "RPC服务 - 项目打卡规则")
public interface SysAttendanceRuleApi {

    @PostMapping("/getAttendanceRuleByProjectIds")
    CommonResult<List<SysAttendanceRuleTO>> getAttendanceRuleByProjectIds(@RequestBody Set<Integer> projectIds);

}
