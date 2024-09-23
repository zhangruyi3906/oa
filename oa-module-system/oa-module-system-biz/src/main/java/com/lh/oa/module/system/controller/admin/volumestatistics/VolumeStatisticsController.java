package com.lh.oa.module.system.controller.admin.volumestatistics;

import com.lh.oa.module.system.controller.admin.volumestatistics.vo.*;
import com.lh.oa.module.system.dal.dataobject.volumestatistics.VolumeStatisticsDO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.pojo.CommonResult;
import static com.lh.oa.framework.common.pojo.CommonResult.success;

import com.lh.oa.framework.excel.core.util.ExcelUtils;

import com.lh.oa.module.system.controller.admin.volumestatistics.vo.*;
import com.lh.oa.module.system.convert.volumestatistics.VolumeStatisticsConvert;
import com.lh.oa.module.system.service.volumestatistics.VolumeStatisticsService;

@Tag(name = "管理后台 - 员工方量统计")
@RestController
@RequestMapping("/system/volume-statistics")
@Validated
public class VolumeStatisticsController {

    @Resource
    private VolumeStatisticsService volumeStatisticsService;

    @PostMapping("/create")
    //@Operation(summary = "创建员工方量统计")
    @PreAuthorize("@ss.hasPermission('system:volume-statistics:create')")
    public CommonResult<Long> createVolumeStatistics(@Valid @RequestBody VolumeStatisticsCreateReqVO createReqVO) {
        return success(volumeStatisticsService.createVolumeStatistics(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新员工方量统计")
    @PreAuthorize("@ss.hasPermission('system:volume-statistics:update')")
    public CommonResult<Boolean> updateVolumeStatistics(@Valid @RequestBody VolumeStatisticsUpdateReqVO updateReqVO) {
        volumeStatisticsService.updateVolumeStatistics(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除员工方量统计")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:volume-statistics:delete')")
    public CommonResult<Boolean> deleteVolumeStatistics(@RequestParam("id") Long id) {
        volumeStatisticsService.deleteVolumeStatistics(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得员工方量统计")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:volume-statistics:query')")
    public CommonResult<VolumeStatisticsRespVO> getVolumeStatistics(@RequestParam("id") Long id) {
        VolumeStatisticsDO volumeStatistics = volumeStatisticsService.getVolumeStatistics(id);
        return success(VolumeStatisticsConvert.INSTANCE.convert(volumeStatistics));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得员工方量统计列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:volume-statistics:query')")
    public CommonResult<List<VolumeStatisticsRespVO>> getVolumeStatisticsList(@RequestParam("ids") Collection<Long> ids) {
        List<VolumeStatisticsDO> list = volumeStatisticsService.getVolumeStatisticsList(ids);
        return success(VolumeStatisticsConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得员工方量统计分页")
    @PreAuthorize("@ss.hasPermission('system:volume-statistics:query')")
    public CommonResult<PageResult<VolumeStatisticsRespVO>> getVolumeStatisticsPage(@Valid VolumeStatisticsPageReqVO pageVO) {
        PageResult<VolumeStatisticsDO> pageResult = volumeStatisticsService.getVolumeStatisticsPage(pageVO);
        return success(VolumeStatisticsConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出员工方量统计 Excel")
    @PreAuthorize("@ss.hasPermission('system:volume-statistics:export')")
    //@Operation(type = EXPORT)
    public void exportVolumeStatisticsExcel(@Valid VolumeStatisticsExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<VolumeStatisticsDO> list = volumeStatisticsService.getVolumeStatisticsList(exportReqVO);
        // 导出 Excel
        List<VolumeStatisticsExcelVO> datas = VolumeStatisticsConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "员工方量统计.xls", "数据", VolumeStatisticsExcelVO.class, datas);
    }

}
