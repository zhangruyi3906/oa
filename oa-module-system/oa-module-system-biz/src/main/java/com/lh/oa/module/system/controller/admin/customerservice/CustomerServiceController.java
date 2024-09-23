package com.lh.oa.module.system.controller.admin.customerservice;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.customerservice.vo.*;
import com.lh.oa.module.system.controller.admin.customerservice.vo.*;
import com.lh.oa.module.system.convert.customerservice.CustomerServiceConvert;
import com.lh.oa.module.system.dal.dataobject.customerservice.CustomerServiceDO;
import com.lh.oa.module.system.service.customerservice.CustomerServiceService;
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

@Tag(name = "管理后台 - 客户服务")
@RestController
@RequestMapping("/system/customer-service")
@Validated
public class CustomerServiceController {

    @Resource
    private CustomerServiceService customerServiceService;

    @PostMapping("/create")
    //@Operation(summary = "创建客户服务")
    @PreAuthorize("@ss.hasPermission('system:customer-service:create')")
    public CommonResult<Long> createCustomerService(@Valid @RequestBody CustomerServiceCreateReqVO createReqVO) {
        return success(customerServiceService.createCustomerService(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新客户服务")
    @PreAuthorize("@ss.hasPermission('system:customer-service:update')")
    public CommonResult<Boolean> updateCustomerService(@Valid @RequestBody CustomerServiceUpdateReqVO updateReqVO) {
        customerServiceService.updateCustomerService(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除客户服务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:customer-service:delete')")
    public CommonResult<Boolean> deleteCustomerService(@RequestParam("id") Long id) {
        customerServiceService.deleteCustomerService(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得客户服务")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:customer-service:query')")
    public CommonResult<CustomerServiceRespVO> getCustomerService(@RequestParam("id") Long id) {
        CustomerServiceDO customerService = customerServiceService.getCustomerService(id);
        return success(CustomerServiceConvert.INSTANCE.convert(customerService));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得客户服务列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:customer-service:query')")
    public CommonResult<List<CustomerServiceRespVO>> getCustomerServiceList(@RequestParam("ids") Collection<Long> ids) {
        List<CustomerServiceDO> list = customerServiceService.getCustomerServiceList(ids);
        return success(CustomerServiceConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得客户服务分页")
    @PreAuthorize("@ss.hasPermission('system:customer-service:query')")
    public CommonResult<PageResult<CustomerServiceRespVO>> getCustomerServicePage(@Valid CustomerServicePageReqVO pageVO) {
        PageResult<CustomerServiceDO> pageResult = customerServiceService.getCustomerServicePage(pageVO);
        return success(CustomerServiceConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出客户服务 Excel")
    @PreAuthorize("@ss.hasPermission('system:customer-service:export')")
    //@Operation(type = EXPORT)
    public void exportCustomerServiceExcel(@Valid CustomerServiceExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<CustomerServiceDO> list = customerServiceService.getCustomerServiceList(exportReqVO);
        // 导出 Excel
        List<CustomerServiceExcelVO> datas = CustomerServiceConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "客户服务.xls", "数据", CustomerServiceExcelVO.class, datas);
    }

}
