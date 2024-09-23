package com.lh.oa.module.system.api.sysAttendanceRule;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.api.sysAttendanceRule.to.SysAttendanceRecordQueryParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(value = "system-server", path = "/api/attendance/record", contextId = "SysAttendanceRecordApi")
@Tag(name = "RPC服务 - 项目打卡记录")
public interface SysAttendanceRecordApi {

    @PostMapping("/getRecentlyMonthAttendanceDateList")
    CommonResult<Map<Integer, Map<Integer, List<Long>>>> getRecentlyMonthAttendanceDateList(@RequestBody SysAttendanceRecordQueryParam param);

}
