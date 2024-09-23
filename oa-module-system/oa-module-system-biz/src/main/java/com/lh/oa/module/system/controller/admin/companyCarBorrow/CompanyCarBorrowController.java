package com.lh.oa.module.system.controller.admin.companyCarBorrow;

import com.lh.oa.framework.common.pojo.JsonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.api.companyCarBorrow.CompanyCarBorrow;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowDate;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowQuery;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowRes;
import com.lh.oa.module.system.service.impl.ICompanyCarBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/system/companyCarBorrow")
@Validated
public class CompanyCarBorrowController {

    @Autowired
    public ICompanyCarBorrowService companyCarBorrowService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CompanyCarBorrow companyCarBorrow){
        int data;
        if(companyCarBorrow.getId()!=null){
            data = companyCarBorrowService.updateById(companyCarBorrow);
        }else{
            data = companyCarBorrowService.insert(companyCarBorrow);
        }
        return JsonResult.success(data);
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/delete",method=RequestMethod.DELETE)
    public JsonResult delete(@RequestParam("id") Long id){
        return JsonResult.success(companyCarBorrowService.deleteById(id));
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public JsonResult get(@RequestParam("id")Long id){
        return JsonResult.success(companyCarBorrowService.selectById(id));
    }

    /**
     * 获取对象
     */
    @RequestMapping(value = "/getByProcessInstanceId",method = RequestMethod.GET)
    public CompanyCarBorrow getByProcessInstanceId(@RequestParam("processInstanceId")String processInstanceId){
        return companyCarBorrowService.getByProcessInstanceId(processInstanceId);
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(companyCarBorrowService.selectList());
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public JsonResult page(@Valid CompanyCarBorrowQuery query){
        PageResult<CompanyCarBorrowRes> page = companyCarBorrowService.selectPage(query);
        return JsonResult.success(page);
    }


    /**
     * 审核
     */
    @RequestMapping(value="/audit",method= RequestMethod.PUT)
    public JsonResult audit(@RequestBody CompanyCarBorrow companyCarBorrow){
        companyCarBorrowService.audit(companyCarBorrow);
        return JsonResult.success();
    }

    /**
     * 空闲
     */
    @RequestMapping(value="/freeList",method= RequestMethod.PUT)
    public JsonResult selectFreeList(@RequestBody CompanyCarBorrowDate companyCarBorrowDate){
        List<CompanyCarBorrowRes> companyCarBorrowRes = companyCarBorrowService.selectFreeList(companyCarBorrowDate);
        return JsonResult.success(companyCarBorrowRes);
    }

    @RequestMapping(value = "/returnCar", method = RequestMethod.PUT)
    public JsonResult returnCar(@RequestParam("id") Long id) {
        companyCarBorrowService.returnCar(id);
        return JsonResult.success();
    }
}
