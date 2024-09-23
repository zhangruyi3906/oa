package com.lh.oa.module.system.controller.admin.joblevelsalary;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.*;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.*;
import com.lh.oa.module.system.convert.joblevelsalary.JobLevelSalaryConvert;
import com.lh.oa.module.system.dal.dataobject.joblevelsalary.JobLevelSalaryDO;
import com.lh.oa.module.system.service.joblevelsalary.JobLevelSalaryService;
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

@Tag(name = "管理后台 - 员工工种等级基础工资")
@RestController
@RequestMapping("/system/job-level-salary")
@Validated
public class JobLevelSalaryController {

    @Resource
    private JobLevelSalaryService jobLevelSalaryService;

    @PostMapping("/create")
    //@Operation(summary = "创建员工工种等级基础工资")
    @PreAuthorize("@ss.hasPermission('system:job-level-salary:create')")
    public CommonResult<Long> createJobLevelSalary(@Valid @RequestBody JobLevelSalaryCreateReqVO createReqVO) {
        return success(jobLevelSalaryService.createJobLevelSalary(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新员工工种等级基础工资")
    @PreAuthorize("@ss.hasPermission('system:job-level-salary:update')")
    public CommonResult<Boolean> updateJobLevelSalary(@Valid @RequestBody JobLevelSalaryUpdateReqVO updateReqVO) {
        jobLevelSalaryService.updateJobLevelSalary(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除员工工种等级基础工资")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:job-level-salary:delete')")
    public CommonResult<Boolean> deleteJobLevelSalary(@RequestParam("id") Long id) {
        jobLevelSalaryService.deleteJobLevelSalary(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得员工工种等级基础工资")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:job-level-salary:query')")
    public CommonResult<JobLevelSalaryRespVO> getJobLevelSalary(@RequestParam("id") Long id) {
        JobLevelSalaryDO jobLevelSalary = jobLevelSalaryService.getJobLevelSalary(id);
        return success(JobLevelSalaryConvert.INSTANCE.convert(jobLevelSalary));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得员工工种等级基础工资列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:job-level-salary:query')")
    public CommonResult<List<JobLevelSalaryRespVO>> getJobLevelSalaryList(@RequestParam("ids") Collection<Long> ids) {
        List<JobLevelSalaryDO> list = jobLevelSalaryService.getJobLevelSalaryList(ids);
        return success(JobLevelSalaryConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得员工工种等级基础工资分页")
    @PreAuthorize("@ss.hasPermission('system:job-level-salary:query')")
    public CommonResult<PageResult<JobLevelSalaryRespVO>> getJobLevelSalaryPage(@Valid JobLevelSalaryPageReqVO pageVO) {
        PageResult<JobLevelSalaryDO> pageResult = jobLevelSalaryService.getJobLevelSalaryPage(pageVO);
        return success(JobLevelSalaryConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出员工工种等级基础工资 Excel")
    @PreAuthorize("@ss.hasPermission('system:job-level-salary:export')")
    //@Operation(type = EXPORT)
    public void exportJobLevelSalaryExcel(@Valid JobLevelSalaryExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<JobLevelSalaryDO> list = jobLevelSalaryService.getJobLevelSalaryList(exportReqVO);
        // 导出 Excel
        List<JobLevelSalaryExcelVO> datas = JobLevelSalaryConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "员工工种等级基础工资.xls", "数据", JobLevelSalaryExcelVO.class, datas);
    }

}
