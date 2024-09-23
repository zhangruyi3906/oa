package com.lh.oa.module.system.api.holidayInfo;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.service.holidayInfo.HolidayInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.module.system.enums.ApiConstants.VERSION;


@RestController
@DubboService(version = VERSION)
@Validated
public class HolidayInfoApiImpl implements HolidayInfoApi{

    @Resource
    private HolidayInfoService service;


    @Override
    public CommonResult<Integer> getWork(Integer id) {
        return success(service.getIsWork(id));
    }
}
