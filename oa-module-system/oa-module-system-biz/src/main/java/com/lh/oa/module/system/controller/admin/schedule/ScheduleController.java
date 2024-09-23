package com.lh.oa.module.system.controller.admin.schedule;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.schedule.vo.*;
import com.lh.oa.module.system.convert.schedule.ScheduleConvert;
import com.lh.oa.module.system.dal.dataobject.schedule.ScheduleDO;
import com.lh.oa.module.system.service.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 日程管理")
@RestController
@RequestMapping("/system/schedule")
@Validated
public class ScheduleController {

    @Resource
    private ScheduleService scheduleService;

    @PostMapping("/create")
    //@Operation(summary = "创建日程管理")
    @PreAuthorize("@ss.hasPermission('system:schedule:create')")
    public CommonResult<Long> createSchedule(@Valid @RequestBody ScheduleCreateReqVO createReqVO) {
        return success(scheduleService.createSchedule(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新日程管理")
    @PreAuthorize("@ss.hasPermission('system:schedule:update')")
    public CommonResult<Boolean> updateSchedule(@Valid @RequestBody ScheduleUpdateReqVO updateReqVO) {
        scheduleService.updateSchedule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除日程管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:schedule:delete')")
    public CommonResult<Boolean> deleteSchedule(@RequestParam("id") Long id) {
        scheduleService.deleteSchedule(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得日程管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:schedule:query')")
    public CommonResult<ScheduleRespVO> getSchedule(@RequestParam("id") Long id) {
        ScheduleDO schedule = scheduleService.getSchedule(id);
        return success(ScheduleConvert.INSTANCE.convert(schedule));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得日程管理分页")
    @PreAuthorize("@ss.hasPermission('system:schedule:query')")
    public CommonResult<PageResult<ScheduleRespVO>> getSchedulePage(@Valid SchedulePageReqVO pageVO) {
        PageResult<ScheduleDO> pageResult = scheduleService.getSchedulePage(pageVO);
        return success(ScheduleConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出日程管理 Excel")
    @PreAuthorize("@ss.hasPermission('system:schedule:export')")
    //@Operation(type = EXPORT)
    public void exportScheduleExcel(@Valid ScheduleExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ScheduleDO> list = scheduleService.getScheduleList(exportReqVO);
        // 导出 Excel
        List<ScheduleExcelVO> datas = ScheduleConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "日程管理.xls", "数据", ScheduleExcelVO.class, datas);
    }

    @GetMapping("/list")
    //@Operation(summary = "根据月份获得日程管理列表")
    @PreAuthorize("@ss.hasPermission('system:schedule:query')")
    public CommonResult<List<ScheduleMonthVO>> getScheduleList(@Valid ScheduleRespVO respVo) {
        List<ScheduleRespVO> scheduleRespVOS = ScheduleConvert.INSTANCE.convertList(scheduleService.getScheduleList(respVo));
        Map<Date, List<ScheduleRespVO>> collect = scheduleRespVOS.stream().collect(Collectors.groupingBy(ScheduleBaseVO::getExpireDateDay));
        List<ScheduleMonthVO> collect1 = collect.entrySet().stream()
                .map(s -> {
                    ScheduleMonthVO scheduleMonthVO = new ScheduleMonthVO();
                    scheduleMonthVO.setScheduleList(s.getValue());
                    scheduleMonthVO.setScheduleDate(s.getKey());
                    return scheduleMonthVO;
                })
                .collect(Collectors.toList());
        return success(collect1);
    }

}
