package com.lh.oa.module.system.controller.admin.baseRegion;


import com.lh.oa.framework.common.pojo.JsonResult;
import com.lh.oa.module.system.service.impl.IBaseRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/baseRegion")
public class BaseRegionController {

    @Autowired
    public IBaseRegionService baseRegionService;
    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(baseRegionService.selectList());
    }


    @GetMapping("/list/address")
    public JsonResult getAddress(){
        return JsonResult.success(baseRegionService.treeData());
    }
}
