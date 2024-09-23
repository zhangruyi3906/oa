package com.lh.oa.module.system.controller.admin.monthstatistics;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.*;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.*;
import com.lh.oa.module.system.convert.monthstatistics.MonthStatisticsConvert;
import com.lh.oa.module.system.dal.dataobject.monthstatistics.MonthStatisticsDO;
import com.lh.oa.module.system.service.monthstatistics.MonthStatisticsService;
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

@Tag(name = "管理后台 - 考勤月统计")
@RestController
@RequestMapping("/system/month-statistics")
@Validated
public class MonthStatisticsController {

    @Resource
    private MonthStatisticsService monthStatisticsService;

    @PostMapping("/create")
    //@Operation(summary = "创建考勤月统计")
    @PreAuthorize("@ss.hasPermission('system:month-statistics:create')")
    public CommonResult<Long> createMonthStatistics(@Valid @RequestBody RecordMonthVO createReqVO) {
        return success(monthStatisticsService.createMonthStatistics(createReqVO));
    }

    @PostMapping("/create-project")
    //@Operation(summary = "创建project考勤月统计")
    @PreAuthorize("@ss.hasPermission('system:month-statistics:create')")
    public CommonResult<Long> createMonthProjectStatistics(@Valid @RequestBody RecordMonthVO createReqVO) {
        return success(monthStatisticsService.createMonthProjectStatistics(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新考勤月统计")
    @PreAuthorize("@ss.hasPermission('system:month-statistics:update')")
    public CommonResult<Boolean> updateMonthStatistics(@Valid @RequestBody MonthStatisticsUpdateReqVO updateReqVO) {
        monthStatisticsService.updateMonthStatistics(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除考勤月统计")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:month-statistics:delete')")
    public CommonResult<Boolean> deleteMonthStatistics(@RequestParam("id") Long id) {
        monthStatisticsService.deleteMonthStatistics(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得考勤月统计")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:month-statistics:query')")
    public CommonResult<MonthStatisticsRespVO> getMonthStatistics(@RequestParam("id") Long id) {
        MonthStatisticsDO monthStatistics = monthStatisticsService.getMonthStatistics(id);
        return success(MonthStatisticsConvert.INSTANCE.convert(monthStatistics));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得考勤月统计列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:month-statistics:query')")
    public CommonResult<List<MonthStatisticsRespVO>> getMonthStatisticsList(@RequestParam("ids") Collection<Long> ids) {
        List<MonthStatisticsDO> list = monthStatisticsService.getMonthStatisticsList(ids);
        return success(MonthStatisticsConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得考勤月统计分页")
    @PreAuthorize("@ss.hasPermission('system:month-statistics:query')")
    public CommonResult<PageResult<MonthStatisticsRespVO>> getMonthStatisticsPage(@Valid MonthStatisticsPageReqVO pageVO) {
        PageResult<MonthStatisticsDO> pageResult = monthStatisticsService.getMonthStatisticsPage(pageVO);
        return success(MonthStatisticsConvert.INSTANCE.convertPage(pageResult));
    }
    @GetMapping("/page-project")
    //@Operation(summary = "获得考勤月统计分页")
    @PreAuthorize("@ss.hasPermission('system:month-statistics:query')")
    public CommonResult<PageResult<MonthStatisticsRespVO>> getProjectMonthStatisticsPage(@Valid MonthStatisticsPageReqVO pageVO) {
        PageResult<MonthStatisticsDO> pageResult = monthStatisticsService.getProjectMonthStatisticsPage(pageVO);
        return success(MonthStatisticsConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出考勤月统计 Excel")
    @PreAuthorize("@ss.hasPermission('system:month-statistics:export')")
    //@Operation(type = EXPORT)
    public void exportMonthStatisticsExcel(@Valid MonthStatisticsExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<MonthStatisticsDO> list = monthStatisticsService.getMonthStatisticsList(exportReqVO);
        // 导出 Excel
        List<MonthStatisticsExcelVO> datas = MonthStatisticsConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "考勤月统计.xls", "数据", MonthStatisticsExcelVO.class, datas);
    }

}
