package com.lh.oa.module.system.controller.admin.budgetcapital;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.*;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.*;
import com.lh.oa.module.system.convert.budgetcapital.BudgetCapitalConvert;
import com.lh.oa.module.system.dal.dataobject.budgetcapital.BudgetCapitalDO;
import com.lh.oa.module.system.service.budgetcapital.BudgetCapitalService;
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

@Tag(name = "管理后台 - 资金预算")
@RestController
@RequestMapping("/system/budget-capital")
@Validated
public class BudgetCapitalController {

    @Resource
    private BudgetCapitalService budgetCapitalService;

    @PostMapping("/create")
    //@Operation(summary = "创建资金预算")
    @PreAuthorize("@ss.hasPermission('system:budget-capital:create')")
    public CommonResult<Long> createBudgetCapital(@Valid @RequestBody BudgetCapitalCreateReqVO createReqVO) {
        return success(budgetCapitalService.createBudgetCapital(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新资金预算")
    @PreAuthorize("@ss.hasPermission('system:budget-capital:update')")
    public CommonResult<Boolean> updateBudgetCapital(@Valid @RequestBody BudgetCapitalUpdateReqVO updateReqVO) {
        budgetCapitalService.updateBudgetCapital(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除资金预算")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:budget-capital:delete')")
    public CommonResult<Boolean> deleteBudgetCapital(@RequestParam("id") Long id) {
        budgetCapitalService.deleteBudgetCapital(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得资金预算")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:budget-capital:query')")
    public CommonResult<BudgetCapitalRespVO> getBudgetCapital(@RequestParam("id") Long id) {
        BudgetCapitalDO budgetCapital = budgetCapitalService.getBudgetCapital(id);
        return success(BudgetCapitalConvert.INSTANCE.convert(budgetCapital));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得资金预算列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:budget-capital:query')")
    public CommonResult<List<BudgetCapitalRespVO>> getBudgetCapitalList(@RequestParam("ids") Collection<Long> ids) {
        List<BudgetCapitalDO> list = budgetCapitalService.getBudgetCapitalList(ids);
        return success(BudgetCapitalConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得资金预算分页")
    @PreAuthorize("@ss.hasPermission('system:budget-capital:query')")
    public CommonResult<PageResult<BudgetCapitalRespVO>> getBudgetCapitalPage(@Valid BudgetCapitalPageReqVO pageVO) {
        PageResult<BudgetCapitalDO> pageResult = budgetCapitalService.getBudgetCapitalPage(pageVO);
        return success(BudgetCapitalConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出资金预算 Excel")
    @PreAuthorize("@ss.hasPermission('system:budget-capital:export')")
    //@Operation(type = EXPORT)
    public void exportBudgetCapitalExcel(@Valid BudgetCapitalExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<BudgetCapitalDO> list = budgetCapitalService.getBudgetCapitalList(exportReqVO);
        // 导出 Excel
        List<BudgetCapitalExcelVO> datas = BudgetCapitalConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "资金预算.xls", "数据", BudgetCapitalExcelVO.class, datas);
    }

}
