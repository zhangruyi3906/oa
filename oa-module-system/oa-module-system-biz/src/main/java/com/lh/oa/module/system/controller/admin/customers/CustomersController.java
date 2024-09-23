package com.lh.oa.module.system.controller.admin.customers;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.customers.vo.*;
import com.lh.oa.module.system.convert.customers.CustomersConvert;
import com.lh.oa.module.system.dal.dataobject.customers.CustomersDO;
import com.lh.oa.module.system.service.customers.CustomersService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 客户基础信息")
@RestController
@RequestMapping("/system/customers")
@Validated
public class CustomersController {

    @Resource
    private CustomersService customersService;

    @PostMapping("/create")
    //@Operation(summary = "创建客户基础信息")
    @PreAuthorize("@ss.hasPermission('system:customers:create')")
    public CommonResult<CustomersDO> createCustomers(@Valid @RequestBody CustomersCreateReqVO createReqVO) {
        return success(customersService.createCustomers(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新客户基础信息")
    @PreAuthorize("@ss.hasPermission('system:customers:update')")
    public CommonResult<Boolean> updateCustomers(@Valid @RequestBody CustomersUpdateReqVO updateReqVO) {
        customersService.updateCustomers(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除客户基础信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:customers:delete')")
    public CommonResult<Boolean> deleteCustomers(@RequestParam("id") Long id) {
        customersService.deleteCustomers(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得客户基础信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:customers:query')")
    public CommonResult<CustomersRespVO> getCustomers(@RequestParam("id") Long id) {
        CustomersDO customers = customersService.getCustomers(id);
        return success(CustomersConvert.INSTANCE.convert(customers));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得客户基础信息列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:customers:query')")
    public CommonResult<List<CustomersRespVO>> getCustomersList(@RequestParam("ids") Collection<Long> ids) {
        List<CustomersDO> list = customersService.getCustomersList(ids);
        return success(CustomersConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得客户基础信息分页")
    @PreAuthorize("@ss.hasPermission('system:customers:query')")
    public CommonResult<PageResult<CustomersRespVO>> getCustomersPage(@Valid CustomersPageReqVO pageVO) {
        PageResult<CustomersDO> pageResult = customersService.getCustomersPage(pageVO);
        return success(CustomersConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出客户基础信息 Excel")
    @PreAuthorize("@ss.hasPermission('system:customers:export')")
    //@Operation(type = EXPORT)
    public void exportCustomersExcel(@Valid CustomersExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<CustomersDO> list = customersService.getCustomersList(exportReqVO);
        // 导出 Excel
        List<CustomersExcelVO> datas = CustomersConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "客户基础信息.xls", "数据", CustomersExcelVO.class, datas);
    }


    @GetMapping("/list-all-simple")
    //@Operation(summary = "获取客户精简信息列表", description = "只包含被开启的用户，主要用于前端的下拉选项")
    public CommonResult<List<CustomersSimpleVO>> getSimpleCustomers() {
        List<CustomersSimpleVO> simpleCustomers = customersService.getSimpleCustomers();
        return success(simpleCustomers);
    }

}
