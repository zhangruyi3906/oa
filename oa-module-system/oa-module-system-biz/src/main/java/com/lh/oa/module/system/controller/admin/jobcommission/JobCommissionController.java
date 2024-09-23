package com.lh.oa.module.system.controller.admin.jobcommission;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.*;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.*;
import com.lh.oa.module.system.convert.jobcommission.JobCommissionConvert;
import com.lh.oa.module.system.dal.dataobject.jobcommission.JobCommissionDO;
import com.lh.oa.module.system.service.jobcommission.JobCommissionService;
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

@Tag(name = "管理后台 - 项目工种提成")
@RestController
@RequestMapping("/system/job-commission")
@Validated
public class JobCommissionController {

    @Resource
    private JobCommissionService jobCommissionService;

    @PostMapping("/create")
    //@Operation(summary = "创建项目工种提成")
    @PreAuthorize("@ss.hasPermission('system:job-commission:create')")
    public CommonResult<Long> createJobCommission(@Valid @RequestBody JobCommissionCreateReqVO createReqVO) {
        return success(jobCommissionService.createJobCommission(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新项目工种提成")
    @PreAuthorize("@ss.hasPermission('system:job-commission:update')")
    public CommonResult<Boolean> updateJobCommission(@Valid @RequestBody JobCommissionUpdateReqVO updateReqVO) {
        jobCommissionService.updateJobCommission(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除项目工种提成")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:job-commission:delete')")
    public CommonResult<Boolean> deleteJobCommission(@RequestParam("id") Long id) {
        jobCommissionService.deleteJobCommission(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得项目工种提成")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:job-commission:query')")
    public CommonResult<JobCommissionRespVO> getJobCommission(@RequestParam("id") Long id) {
        JobCommissionDO jobCommission = jobCommissionService.getJobCommission(id);
        return success(JobCommissionConvert.INSTANCE.convert(jobCommission));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得项目工种提成列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:job-commission:query')")
    public CommonResult<List<JobCommissionRespVO>> getJobCommissionList(@RequestParam("ids") Collection<Long> ids) {
        List<JobCommissionDO> list = jobCommissionService.getJobCommissionList(ids);
        return success(JobCommissionConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得项目工种提成分页")
    @PreAuthorize("@ss.hasPermission('system:job-commission:query')")
    public CommonResult<PageResult<JobCommissionRespVO>> getJobCommissionPage(@Valid JobCommissionPageReqVO pageVO) {
        PageResult<JobCommissionDO> pageResult = jobCommissionService.getJobCommissionPage(pageVO);
        return success(JobCommissionConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出项目工种提成 Excel")
    @PreAuthorize("@ss.hasPermission('system:job-commission:export')")
    //@Operation(type = EXPORT)
    public void exportJobCommissionExcel(@Valid JobCommissionExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<JobCommissionDO> list = jobCommissionService.getJobCommissionList(exportReqVO);
        // 导出 Excel
        List<JobCommissionExcelVO> datas = JobCommissionConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "项目工种提成.xls", "数据", JobCommissionExcelVO.class, datas);
    }

}
