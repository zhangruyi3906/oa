package com.lh.oa.module.system.controller.admin.systemDeptPunish;

import com.lh.oa.framework.common.pojo.JsonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.dal.dataobject.systemDeptPunish.SystemDeptPunish;
import com.lh.oa.module.system.dal.dataobject.systemDeptPunish.SystemDeptPunishQuery;
import com.lh.oa.module.system.service.impl.ISystemDeptPunishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/system/systemDeptPunish")
@Validated
public class SystemDeptPunishController {

    @Autowired
    public ISystemDeptPunishService systemDeptPunishService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/create",method= RequestMethod.POST)
    public JsonResult create(@Valid @RequestBody SystemDeptPunish systemDeptPunish){
        systemDeptPunishService.insert(systemDeptPunish);
        return JsonResult.success();
    }


    /**
     * 保存和修改公用的
     */
    @RequestMapping(value="/update",method= RequestMethod.PUT)
    public JsonResult update(@Valid @RequestBody SystemDeptPunish systemDeptPunish){
        systemDeptPunishService.updateById(systemDeptPunish);
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@RequestParam("id") Long id){
        systemDeptPunishService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public JsonResult get(@RequestParam("id")Long id){
        return JsonResult.success(systemDeptPunishService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(systemDeptPunishService.selectList());
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public JsonResult page(@Valid SystemDeptPunishQuery query){
        PageResult page = systemDeptPunishService.selectPage(query);
        return JsonResult.success(page);
    }
}
