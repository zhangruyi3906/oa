package com.lh.oa.module.system.controller.admin.salary;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryExportExcelVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryExportTemplateVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryExportVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryImportExcelVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryMonthSimpleVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryPageReqVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryResVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryYearResVO;
import com.lh.oa.module.system.dal.dataobject.salary.SalaryDO;
import com.lh.oa.module.system.service.salary.SalaryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Slf4j
@Tag(name = "管理后台 - 工资条")
@RestController
@RequestMapping("/system/salary")
@Validated
public class SalaryController {
    @Resource
    private SalaryService salaryService;

    @PostMapping("/import")
    @Parameters({
            @Parameter(name = "file", description = "Excel 文件", required = true),
            @Parameter(name = "month", description = "月份", example = "true"),
            @Parameter(name = "updateSupport", description = "是否支持更新，默认为 false", example = "true")
    })
    @SneakyThrows
    public CommonResult<String> importExcel(@RequestParam("file") MultipartFile file,
                                            @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport,
                                            HttpServletResponse response) {
        Map<String, Object> salaryList;

        try {
            salaryList = ExcelUtils.readSalaryList(file, SalaryImportExcelVO.class);
        } catch (Exception e) {
            log.error("导入失败", e);
            return CommonResult.error(500, "导入数据数据格式异常");
        }
        Object o = salaryList.get("month");
        if (o == null) {
            return CommonResult.error(500, "月份未导入");
        }
        String month = o.toString();
        Object salary = salaryList.get(month);
        if (salary == null) {
            return CommonResult.error(500, "月份数据为空");
        }
        List<SalaryImportExcelVO> salaryImportExcelVOList = JsonUtils.parseArray(JsonUtils.toJsonString(salary), SalaryImportExcelVO.class);
        List<SalaryImportExcelVO> result = salaryService.importSalaryList(month, salaryImportExcelVOList, updateSupport);
        if (!result.isEmpty()) {
            ExcelUtils.write(response, "错误数据.xls", "工资列表", SalaryImportExcelVO.class, result);
            return CommonResult.halfSuccess("部分数据存在问题");
        } else {
            return success();
        }
    }

    @GetMapping("/page")
    public CommonResult<PageResult<SalaryDO>> getSalaryPage(@Valid SalaryPageReqVO salaryPageReqVO) {
        PageResult<SalaryDO> salaryDOPage = salaryService.getSalaryPage(salaryPageReqVO);
        return success(salaryDOPage);
    }

    @GetMapping("/get-import-template")
    public void getImportTemplate(HttpServletResponse response) throws IOException {
        SalaryExportTemplateVO salaryExportTemplateVO = new SalaryExportTemplateVO();
        salaryExportTemplateVO.setOrderNumber("1");
        salaryExportTemplateVO.setDeptName("测试部门");
        salaryExportTemplateVO.setUsername("测试姓名");
        salaryExportTemplateVO.setMobile("13800000000");
        salaryExportTemplateVO.setHireDate("2023-12-22");
        salaryExportTemplateVO.setAttendance(0.0);
        salaryExportTemplateVO.setAttendanceDays(0.0);
        salaryExportTemplateVO.setSalaryAttendance(0.0);
        salaryExportTemplateVO.setAttendanceSalary(BigDecimal.ONE);
        salaryExportTemplateVO.setSenioritySalary(BigDecimal.ONE);
        salaryExportTemplateVO.setOvertimeSalary(BigDecimal.ONE);
        salaryExportTemplateVO.setQuantitySalary(BigDecimal.ONE);
        salaryExportTemplateVO.setPerformance(BigDecimal.ONE);
        salaryExportTemplateVO.setAnnualLeaveSalary(BigDecimal.ONE);
        salaryExportTemplateVO.setSickLeaveSalary(BigDecimal.ONE);
        salaryExportTemplateVO.setPerformanceSalary(BigDecimal.ONE);
        salaryExportTemplateVO.setSubsidies(BigDecimal.ONE);
        salaryExportTemplateVO.setShouldSalary(BigDecimal.ONE);
        salaryExportTemplateVO.setAttendanceDeduction(BigDecimal.ONE);
        salaryExportTemplateVO.setAssessmentDeduction(BigDecimal.ONE);
        salaryExportTemplateVO.setSocialSecurity(BigDecimal.ONE);
        salaryExportTemplateVO.setPersonalTax(BigDecimal.ONE);
        salaryExportTemplateVO.setBorrowing(BigDecimal.ONE);
        salaryExportTemplateVO.setOtherDeduction(BigDecimal.ONE);
        salaryExportTemplateVO.setRealSalary(BigDecimal.ONE);
        salaryExportTemplateVO.setRemark("备注");
        List<SalaryExportTemplateVO> dataList = new ArrayList<>();
        dataList.add(salaryExportTemplateVO);
        ExcelUtils.write(response, "工资条导入模板.xls", "工资条导入模板", SalaryExportTemplateVO.class, dataList);
    }

    @GetMapping("/export")
    //@Operation(summary = "导出用户")
    public void exportSalary(@Validated SalaryExportVO salaryExportVO,
                             HttpServletResponse response) throws IOException {
        List<SalaryExportExcelVO> exportSalaryExcelVOList = salaryService.exportSalary(salaryExportVO);
        // 输出
        ExcelUtils.write(response, "工资条数据.xls", "工资条", SalaryExportExcelVO.class, exportSalaryExcelVOList);
    }

    @GetMapping("/getSalaryListByMobileAndMonth")
    public CommonResult<List<SalaryResVO>> getSalaryListByMobileAndMonth(@RequestParam("userId") Long userId,
                                                                         @RequestParam(required = false) String mobile,
                                                                         @RequestParam(required = false) String month) {
        List<SalaryResVO> salaryResVOList = salaryService.getSalaryListByMobileAndMonth(userId, mobile, month);
        return success(salaryResVOList);
    }

    @GetMapping("/getSalaryListByMobileAndYear")
    public CommonResult<SalaryYearResVO> getSalaryListByMobileAndYear(@RequestParam("userId") Long userId,
                                                                      @RequestParam(required = false) String mobile,
                                                                      @RequestParam(required = false) String year) {
        SalaryYearResVO salaryYearResVO = salaryService.getSalaryListByMobileAndYear(userId, mobile, year);
        return success(salaryYearResVO);
    }

    @GetMapping("/getSalaryMonthList")
    public CommonResult<List<SalaryMonthSimpleVO>> getSalaryMonthList(@RequestParam("userId") Long userId) {
        List<SalaryMonthSimpleVO> monthList = salaryService.getSalaryMonthList(userId);
        return success(monthList);
    }

    @DeleteMapping("/deleteByMonth")
    public CommonResult<Boolean> deleteByMonth(@RequestParam("month") String month) {
        salaryService.deleteByMonth(month);
        return success(true);
    }

}
