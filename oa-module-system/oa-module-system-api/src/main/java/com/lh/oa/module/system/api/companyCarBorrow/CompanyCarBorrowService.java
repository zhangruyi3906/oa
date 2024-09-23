package com.lh.oa.module.system.api.companyCarBorrow;

import com.lh.oa.framework.common.pojo.JsonResult;
import com.lh.oa.module.system.enums.CarBorrowConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "system-server", path = "/admin-api/system/companyCarBorrow",contextId = "CompanyCarBorrowService", fallbackFactory = CompanyCarBorrowServiceFallbackFactory.class)
public interface CompanyCarBorrowService {
    String PREFIX = CarBorrowConstants.PREFIX + "/companyCarBorrow";

    @PostMapping( "/save")
    JsonResult saveOrUpdate(@RequestBody CompanyCarBorrow companyCarBorrow);

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    JsonResult get(@RequestParam("id")Long id);

    @RequestMapping(value = "/getByProcessInstanceId",method = RequestMethod.GET)
    CompanyCarBorrow getByProcessInstanceId(@RequestParam("processInstanceId")String processInstanceId);
}
