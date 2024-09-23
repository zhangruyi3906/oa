package com.lh.oa.module.system.controller.admin.erp;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.controller.admin.erp.vo.*;
import com.lh.oa.module.system.service.erp.ErpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.List;

/**
 * 获取erp库存 物料 相关
 */
@RestController
@RequestMapping("/system/erp")
public class ErpInfoController {
    @Autowired
    private ErpInfoService erpInfoService;

    /**
     * 获取物料库存
     *
     * @param pageSize
     * @param pageNo
     * @return
     */
    @GetMapping("/getErpInventoryList")
    @PermitAll
    public CommonResult<List<ErpInventoryVO>> getErpInventoryList(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                  @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                                  @RequestParam(value = "keywords", required = false) String materialName) {
        return CommonResult.success(erpInfoService.getErpInventoryList(pageSize, pageNo, materialName));
    }

    /**
     * 获取仓库列表
     *
     * @param pageSize
     * @param pageNo
     * @return
     */
    @GetMapping("/getWarehouseList")
    @PermitAll
    public CommonResult<List<ErpWarehouseVO>> getErpWarehouseList(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                  @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                                  @RequestParam(value = "name", required = false) String name) {
        return CommonResult.success(erpInfoService.getErpWarehouseList(pageSize, pageNo, name));
    }

    /**
     * 获取售后工单
     *
     * @return
     */
    @GetMapping("/postSaleOrderList")
    @PermitAll
    public CommonResult<List<PostSaleOrderVO>> getPostSaleOrderList(@RequestParam(value = "keywords", required = false) String number,
                                                                    @RequestParam(value = "perPage", required = false) Integer pageSize,
                                                                    @RequestParam(value = "page", required = false) Integer pageNo) {
        return CommonResult.success(erpInfoService.getPostSaleOrderList(number, pageSize, pageNo));
    }

    /**
     * 配件下单流程获取erp客户列表
     */
    @GetMapping("/get/sale/data/customer")
    @PermitAll
    public CommonResult<List<ErpSaleCustomerVO>> getSaleCustomerList(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                     @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                                     @RequestParam(value = "keywords", required = false) String name) {
        return CommonResult.success(erpInfoService.getSaleCustomerList(pageSize, pageNo, name));
    }

    /**
     * 配件下单流程获取erp主成单销售员
     */
    @GetMapping("/get/erp/sale/data/sale/man")
    @PermitAll
    public CommonResult<List<ErpSaleManVO>> getSaleManList(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                           @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                           @RequestParam(value = "keywords", required = false) String name) {
        return CommonResult.success(erpInfoService.getSaleManList(pageSize, pageNo, name));
    }
}
