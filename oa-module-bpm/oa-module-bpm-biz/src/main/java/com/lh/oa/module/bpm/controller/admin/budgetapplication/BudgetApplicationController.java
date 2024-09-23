package com.lh.oa.module.bpm.controller.admin.budgetapplication;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.BudgetApplicationCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.BudgetApplicationPageReqVO;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.BudgetApplicationRespVO;
import com.lh.oa.module.bpm.convert.budgetapplication.BudgetApplicationConvert;
import com.lh.oa.module.bpm.dal.dataobject.budgetapplication.BudgetApplicationDO;
import com.lh.oa.module.bpm.service.budgetapplication.BudgetApplicationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 资金预算申请")
@RestController
@RequestMapping("/bpm/budget-application")
@Validated
public class BudgetApplicationController {

    @Resource
    private BudgetApplicationService budgetApplicationService;

    @PostMapping("/create")
//    @Operation(summary = "创建资金预算申请")
    @PreAuthorize("@ss.hasPermission('bpm:budget-application:create')")
    public CommonResult<Long> createBudgetApplication(@Valid @RequestBody BudgetApplicationCreateReqVO createReqVO) {
        return success(budgetApplicationService.createBudgetApplication(getLoginUserId(),createReqVO));
    }

//    @PutMapping("/update")
//    @Operation(summary = "更新资金预算申请")
//    @PreAuthorize("@ss.hasPermission('bpm:budget-application:update')")
//    public CommonResult<Boolean> updateBudgetApplication(@Valid @RequestBody BudgetApplicationUpdateReqVO updateReqVO) {
//        budgetApplicationService.updateBudgetApplication(updateReqVO);
//        return success(true);
//    }

    @DeleteMapping("/delete")
//    @Operation(summary = "删除资金预算申请")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('bpm:budget-application:delete')")
    public CommonResult<Boolean> deleteBudgetApplication(@RequestParam("id") Long id) {
        budgetApplicationService.deleteBudgetApplication(id);
        return success(true);
    }

    @GetMapping("/get")
//    @Operation(summary = "获得资金预算申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:budget-application:query')")
    public CommonResult<BudgetApplicationRespVO> getBudgetApplication(@RequestParam("id") Long id) {
        BudgetApplicationDO budgetApplication = budgetApplicationService.getBudgetApplication(id);
        return success(BudgetApplicationConvert.INSTANCE.convert(budgetApplication));
    }

    @GetMapping("/list")
//    @Operation(summary = "获得资金预算申请列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('bpm:budget-application:query')")
    public CommonResult<List<BudgetApplicationRespVO>> getBudgetApplicationList(@RequestParam("ids") Collection<Long> ids) {
        List<BudgetApplicationDO> list = budgetApplicationService.getBudgetApplicationList(ids);
        return success(BudgetApplicationConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
//    @Operation(summary = "获得资金预算申请分页")
    @PreAuthorize("@ss.hasPermission('bpm:budget-application:query')")
    public CommonResult<PageResult<BudgetApplicationRespVO>> getBudgetApplicationPage(@Valid BudgetApplicationPageReqVO pageVO) {
        PageResult<BudgetApplicationDO> pageResult = budgetApplicationService.getBudgetApplicationPage(pageVO);
        return success(BudgetApplicationConvert.INSTANCE.convertPage(pageResult));
    }

//    @GetMapping("/export-excel")
//    @Operation(summary = "导出资金预算申请 Excel")
//    @PreAuthorize("@ss.hasPermission('bpm:budget-application:export')")
//    @OperateLog(type = EXPORT)
//    public void exportbudgetApplicationExcel(@Valid budgetApplicationExportReqVO exportReqVO,
//              HttpServletResponse response) throws IOException {
//        List<budgetApplicationDO> list = budgetApplicationService.getbudgetApplicationList(exportReqVO);
//        // 导出 Excel
//        List<budgetApplicationExcelVO> datas = budgetApplicationConvert.INSTANCE.convertList02(list);
//        ExcelUtils.write(response, "资金预算申请.xls", "数据", budgetApplicationExcelVO.class, datas);
//    }

}
