package com.lh.oa.module.system.sal.controller;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.sal.service.SalSaleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Tag(name = "售后平台相关接口")
@RestController
@RequestMapping("/sal")
public class SalSaleController {
    @Resource
    private SalSaleService salSaleService;

    @GetMapping("/getSalSaleOrderParams")
    private CommonResult<String> getSalSaleOrderParams(Map<String, Object> variableInstances) {
        String result = salSaleService.getSalSaleOrderParams(variableInstances);
        return CommonResult.success(result);
    }
}
