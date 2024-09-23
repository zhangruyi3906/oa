package com.lh.oa.module.system.controller.admin.contract;

import com.lh.oa.framework.common.pojo.JsonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.dal.dataobject.contract.Contract;
import com.lh.oa.module.system.dal.dataobject.contract.ContractQuery;
import com.lh.oa.module.system.service.impl.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/system/contract")
@Validated
public class ContractController {

    @Autowired
    public IContractService contractService;

    /**
    * 保存和修改合同
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult save(@Validated @RequestBody Contract contract){
        contractService.insert(contract);
        return JsonResult.success();
    }

    @RequestMapping(value="/update",method= RequestMethod.PUT)
    public JsonResult Update(@Validated @RequestBody Contract contract){
        contractService.updateById(contract);
        return JsonResult.success();
    }

    /**
    * 删除合同
    */
    @RequestMapping(value="/delete",method=RequestMethod.DELETE)
    public JsonResult delete(@RequestParam("id") Long id){
        contractService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取合同
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@RequestParam("id")Long id){
        return JsonResult.success(contractService.selectById(id));
    }


    /**
    * 查询所有合同
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(contractService.selectList());
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public JsonResult page(@Valid ContractQuery query){
        PageResult<Contract> page = contractService.selectPage(query);
        return JsonResult.success(page);
    }


    /**
     * 查询目标合同及其子合同
     */
    @RequestMapping(value = "/totalById",method = RequestMethod.GET)
    public JsonResult totalById(@RequestParam("id") Long id){
        return JsonResult.success(contractService.totalById(id));
    }


    /**
     * 查询客户历史合同
     */
    @RequestMapping(value = "/customer",method = RequestMethod.GET)
    public JsonResult customerList(@RequestParam("customerName") String customerName){
        return JsonResult.success(contractService.customerList(customerName));
    }

}
