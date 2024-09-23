package com.lh.oa.module.system.controller.admin.kingdee.controller;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.controller.admin.kingdee.service.K3cloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/k3cloud")
public class K3cloudController {

    @Autowired
    private K3cloudService k3cloudService;

    @PostMapping("login")
    public CommonResult<String> checkLogin(){
        Boolean ifLogin = null;
        try {
            ifLogin = k3cloudService.checkLogin();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(500,"登录异常！");
        }
        if(!ifLogin){
            return CommonResult.error(500,"登录异常！");
        }
        return CommonResult.success("登录成功");
    }
}
