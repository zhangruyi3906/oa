package com.lh.oa.module.bpm.framework.rpc.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(basePackages = "com.lh.oa.module")
//@EnableFeignClients(clients = {RoleApi.class, DeptApi.class, PostApi.class, AdminUserApi.class, DictDataApi.class, HolidayInfoApi.class, CompanyCarBorrowService.class, CompanyCarService.class})
public class RpcConfiguration {
}
