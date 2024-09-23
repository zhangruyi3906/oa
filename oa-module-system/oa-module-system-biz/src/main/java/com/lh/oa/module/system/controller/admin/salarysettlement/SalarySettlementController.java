package com.lh.oa.module.system.controller.admin.salarysettlement;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.*;
import com.lh.oa.module.system.convert.salarysettlement.SalarySettlementConvert;
import com.lh.oa.module.system.dal.dataobject.salarysettlement.SalarySettlementDO;
import com.lh.oa.module.system.service.salarysettlement.SalarySettlementService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 员工工资结算")
@RestController
@RequestMapping("/system/salary-settlement")
@Validated
public class SalarySettlementController {

    @Resource
    private SalarySettlementService salarySettlementService;

    @PostMapping("/create")
    //@Operation(summary = "创建员工工资结算")
    @PreAuthorize("@ss.hasPermission('system:salary-settlement:create')")
//    @PreAuthenticated
    public CommonResult<Long> createSalarySettlement(@Valid @RequestBody MonthAttendance createReqVO) {
        return success(salarySettlementService.createSalarySettlement(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新员工工资结算")
    @PreAuthorize("@ss.hasPermission('system:salary-settlement:update')")
    public CommonResult<Boolean> updateSalarySettlement(@Valid @RequestBody SalarySettlementUpdateReqVO updateReqVO) {
        salarySettlementService.updateSalarySettlement(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除员工工资结算")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:salary-settlement:delete')")
    public CommonResult<Boolean> deleteSalarySettlement(@RequestParam("id") Long id) {
        salarySettlementService.deleteSalarySettlement(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得员工工资结算")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:salary-settlement:query')")
    public CommonResult<SalarySettlementRespVO> getSalarySettlement(@RequestParam("id") Long id) {
        SalarySettlementDO salarySettlement = salarySettlementService.getSalarySettlement(id);
        return success(SalarySettlementConvert.INSTANCE.convert(salarySettlement));
    }

    @GetMapping("/salary-settlement")
    //@Operation(summary = "获得员工工资结算列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:salary-settlement:query')")
    public CommonResult<List<SalarySettlementRespVO>> getSalarySettlementList(@RequestParam("ids") Collection<Long> ids) {
        List<SalarySettlementDO> list = salarySettlementService.getSalarySettlementList(ids);
        return success(SalarySettlementConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得员工工资结算分页")
    @PreAuthorize("@ss.hasPermission('system:salary-settlement:query')")
    public CommonResult<PageResult<SalarySettlementRespVO>> getSalarySettlementPage(@Valid SalarySettlementPageReqVO pageVO) {
        PageResult<SalarySettlementDO> pageResult = salarySettlementService.getSalarySettlementPage(pageVO);
        return success(SalarySettlementConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出员工工资结算 Excel")
    @PreAuthorize("@ss.hasPermission('system:salary-settlement:export')")
    //@Operation(type = EXPORT)
    public void exportSalarySettlementExcel(@Valid SalarySettlementExportReqVO exportReqVO,
                                            HttpServletResponse response, HttpServletRequest request) throws IOException {
        List<SalarySettlementDO> list = salarySettlementService.getSalarySettlementList(exportReqVO);
        // 导出 Excel
        List<SalarySettlementExcelVO> datas = SalarySettlementConvert.INSTANCE.convertList02(list);
        salarySettlementService.export(datas, request, response);
//        ExcelUtils.write(response, "员工工资结算.xlsx", "数据", SalarySettlementExcelVO.class, datas);
    }



}
