package com.lh.oa.module.system.controller.admin.companyCar;


import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.JsonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.api.companyCar.CompanyCar;
import com.lh.oa.module.system.dal.dataobject.companyCar.CompanyCarQuery;
import com.lh.oa.module.system.dal.dataobject.companyCar.CompanyCarSimple;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowDate;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowRes;
import com.lh.oa.module.system.service.impl.ICompanyCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/system/companyCar")
@Validated
public class CompanyCarController {

    @Autowired
    public ICompanyCarService companyCarService;

    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@Validated @RequestBody CompanyCar companyCar) {
        int data;
        if (companyCar.getId() != null) {
            data = companyCarService.updateById(companyCar);
        } else {
            data = companyCarService.insert(companyCar);
        }
        return JsonResult.success(data);
    }

    /**
     * 删除对象
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JsonResult delete(@RequestParam("id") Long id) {
        return JsonResult.success(companyCarService.deleteById(id));
    }

    /**
     * 获取对象
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public JsonResult get(@RequestParam("id") Long id) {
        return JsonResult.success(companyCarService.selectById(id));
    }


    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public CompanyCar getById(@RequestParam("id") Long id) {
        return companyCarService.selectById(id);
    }

    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public JsonResult page(@Valid CompanyCarQuery query) {
        PageResult<CompanyCar> page = companyCarService.selectPage(query);
        return JsonResult.success(page);
    }


    /**
     * 查询所有空闲车辆
     */
    @RequestMapping(value = "/list/free", method = RequestMethod.GET)
    public JsonResult free() {
        return JsonResult.success(companyCarService.free());
    }


    @GetMapping("/list-car-simple")
//    @Operation(summary = "获取车辆精简信息列表")
    public CommonResult<List<CompanyCarSimple>> getPressUsers() {
        // 获用户门列表，只要开启状态的
        List<CompanyCar> companyCars = companyCarService.selectFreeList();
        List<CompanyCarSimple> list = new ArrayList<>();
        companyCars.forEach(s -> {
            CompanyCarSimple companyCarSimple = new CompanyCarSimple();
            companyCarSimple.setLabel(s.getType() + "(" + s.getPlateNumber() + ")");
            companyCarSimple.setValue(s.getId());
            list.add(companyCarSimple);
        });
        // 排序后，返回给前端
        return success(list);
    }

    /**
     * 空闲
     */
    @RequestMapping(value="/freeList",method= RequestMethod.GET)
    public JsonResult getFreeList(CompanyCarBorrowDate companyCarBorrowDate){
        List<CompanyCarBorrowRes> companyCars = companyCarService.getFreeList(companyCarBorrowDate);
        return JsonResult.success(companyCars);
    }
    /**
     * 根据车牌号查询
     */
    @RequestMapping(value = "/getByPlateNumber", method = RequestMethod.GET)
    public CompanyCar getByPlateNumber(@RequestParam("plateNumber") String plateNumber) {
        return companyCarService.getByPlateNumber(plateNumber);
    }


}
