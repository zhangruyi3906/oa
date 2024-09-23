package com.lh.oa.module.system.api.kingdee;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.controller.admin.kingdee.service.K3cloudService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import static com.lh.oa.module.system.enums.ApiConstants.VERSION;

@RestController
@DubboService(version = VERSION)
@Validated
public class K3cloudApiImpl implements K3cloudApi{
    @Autowired
    private K3cloudService k3cloudService;
    public CommonResult<String> saveSALSaleOrder(String data)  {
        return CommonResult.success(k3cloudService.saveSALSaleOrder(data));
    }

    @Override
    public CommonResult<Boolean> login() {
        try {
            return CommonResult.success(k3cloudService.checkLogin());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
