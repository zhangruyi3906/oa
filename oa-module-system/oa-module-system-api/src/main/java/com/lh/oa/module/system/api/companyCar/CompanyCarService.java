package com.lh.oa.module.system.api.companyCar;

import com.lh.oa.framework.common.pojo.JsonResult;
import com.lh.oa.module.system.enums.CarBorrowConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "system-server", path = "/admin-api/system/companyCar",contextId = "CompanyCarService", fallbackFactory = CompanyCarServiceFallbackFactory.class)
public interface CompanyCarService {
    String PREFIX = CarBorrowConstants.PREFIX + "/companyCar";
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    JsonResult saveOrUpdate(@Validated @RequestBody CompanyCar companyCar);

    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    CompanyCar getById(@RequestParam("id") Long id);

    @RequestMapping(value = "/getByPlateNumber", method = RequestMethod.GET)
    CompanyCar getByPlateNumber(@RequestParam("plateNumber") String plateNumber);
}
