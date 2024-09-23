package com.lh.oa.module.system.service.erp;

import com.lh.oa.module.system.controller.admin.erp.vo.*;

import java.util.List;

public interface ErpInfoService {
    List<ErpInventoryVO> getErpInventoryList(Integer pageSize, Integer pageNo, String materialName);

    List<ErpWarehouseVO> getErpWarehouseList(Integer pageSize, Integer pageNo, String name);

    List<ErpSaleCustomerVO> getSaleCustomerList(Integer pageSize, Integer pageNo, String name);

    List<ErpSaleManVO> getSaleManList(Integer pageSize, Integer pageNo, String name);

    List<PostSaleOrderVO> getPostSaleOrderList(String number, Integer pageSize, Integer pageNo);
}
