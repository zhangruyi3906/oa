package com.lh.oa.module.system.api.holidayInfo;


import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = ApiConstants.NAME) //
@Tag(name = "RPC 服务 - 节假日")
public interface HolidayInfoApi {

    String PREFIX = ApiConstants.PREFIX + "/holiday";

    @GetMapping(PREFIX + "/get")
    @Operation(summary = "是否上班")
    @Parameter(name = "id", required = true)
    CommonResult<Integer> getWork(@RequestParam("id") Integer id);


}
