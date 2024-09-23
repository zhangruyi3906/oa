package com.lh.oa.module.system.controller.admin.fundtransfer;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.*;
import com.lh.oa.module.system.convert.fundtransfer.FundTransferConvert;
import com.lh.oa.module.system.dal.dataobject.fundtransfer.FundTransferDO;
import com.lh.oa.module.system.service.fundtransfer.FundTransferService;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.*;
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

@Tag(name = "管理后台 - 资金划拨")
@RestController
@RequestMapping("/system/fund-transfer")
@Validated
public class FundTransferController {

    @Resource
    private FundTransferService fundTransferService;

    @PostMapping("/create")
    //@Operation(summary = "创建资金划拨")
    @PreAuthorize("@ss.hasPermission('system:fund-transfer:create')")
    public CommonResult<Long> createFundTransfer(@Valid @RequestBody FundTransferCreateReqVO createReqVO) {
        return success(fundTransferService.createFundTransfer(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新资金划拨")
    @PreAuthorize("@ss.hasPermission('system:fund-transfer:update')")
    public CommonResult<Boolean> updateFundTransfer(@Valid @RequestBody FundTransferUpdateReqVO updateReqVO) {
        fundTransferService.updateFundTransfer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除资金划拨")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:fund-transfer:delete')")
    public CommonResult<Boolean> deleteFundTransfer(@RequestParam("id") Long id) {
        fundTransferService.deleteFundTransfer(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得资金划拨")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:fund-transfer:query')")
    public CommonResult<FundTransferRespVO> getFundTransfer(@RequestParam("id") Long id) {
        FundTransferDO fundTransfer = fundTransferService.getFundTransfer(id);
        return success(FundTransferConvert.INSTANCE.convert(fundTransfer));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得资金划拨列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:fund-transfer:query')")
    public CommonResult<List<FundTransferRespVO>> getFundTransferList(@RequestParam("ids") Collection<Long> ids) {
        List<FundTransferDO> list = fundTransferService.getFundTransferList(ids);
        return success(FundTransferConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得资金划拨分页")
    @PreAuthorize("@ss.hasPermission('system:fund-transfer:query')")
    public CommonResult<PageResult<FundTransferRespVO>> getFundTransferPage(@Valid FundTransferPageReqVO pageVO) {
        PageResult<FundTransferDO> pageResult = fundTransferService.getFundTransferPage(pageVO);
        return success(FundTransferConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出资金划拨 Excel")
    @PreAuthorize("@ss.hasPermission('system:fund-transfer:export')")
    //@Operation(type = EXPORT)
    public void exportFundTransferExcel(@Valid FundTransferExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<FundTransferDO> list = fundTransferService.getFundTransferList(exportReqVO);
        // 导出 Excel
        List<FundTransferExcelVO> datas = FundTransferConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "资金划拨.xls", "数据", FundTransferExcelVO.class, datas);
    }

}
