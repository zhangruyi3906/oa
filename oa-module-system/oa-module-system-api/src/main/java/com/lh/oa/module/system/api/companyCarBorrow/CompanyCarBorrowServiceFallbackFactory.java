package com.lh.oa.module.system.api.companyCarBorrow;

import com.lh.oa.framework.common.pojo.JsonResult;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CompanyCarBorrowServiceFallbackFactory implements FallbackFactory<CompanyCarBorrowService> {

    @Override
    public CompanyCarBorrowService create(Throwable cause) {
        return new CompanyCarBorrowService() {
            @Override
            public JsonResult saveOrUpdate(CompanyCarBorrow companyCarBorrow) {
                return JsonResult.error("添加失败");
            }

            @Override
            public JsonResult get(Long id) {
                return JsonResult.error("获取失败");
            }

            @Override
            public CompanyCarBorrow getByProcessInstanceId(String processInstanceId) {
                return null;
            }
        };
    }

}
