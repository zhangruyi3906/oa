package com.lh.oa.module.system.api.kingdee;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ApiConstants.NAME) //
@Tag(name = "RPC 服务 - 金蝶云服务")
public interface K3cloudApi {
    String PREFIX = ApiConstants.PREFIX + "/kingdee";
    @PostMapping(PREFIX + "/saveSALSaleOrder")
    CommonResult<String>  saveSALSaleOrder(@RequestBody String data)throws Exception;
    @GetMapping(PREFIX + "/login")
    CommonResult<Boolean> login();
}

