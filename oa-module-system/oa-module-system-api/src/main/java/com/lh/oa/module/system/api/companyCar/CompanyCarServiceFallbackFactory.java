package com.lh.oa.module.system.api.companyCar;

import com.lh.oa.framework.common.pojo.JsonResult;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CompanyCarServiceFallbackFactory implements FallbackFactory<CompanyCarService> {
    @Override
    public CompanyCarService create(Throwable cause) {
        return new CompanyCarService() {
            @Override
            public JsonResult saveOrUpdate(CompanyCar companyCar) {
                return JsonResult.error();
            }

            @Override
            public CompanyCar getById(Long id) {
                return null;
            }

            @Override
            public CompanyCar getByPlateNumber(String plateNumber) {
                return null;
            }
        };
    }
}
