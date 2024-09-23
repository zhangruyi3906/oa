package com.lh.oa.module.bpm.controller.admin.approvalprocess;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.ApprovalProcessCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.ApprovalProcessPageReqVO;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.ApprovalProcessRespVO;
import com.lh.oa.module.bpm.convert.approvalprocess.ApprovalProcessConvert;
import com.lh.oa.module.bpm.dal.dataobject.approvalprocess.ApprovalProcessDO;
import com.lh.oa.module.bpm.service.approvalprocess.ApprovalProcessService;
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

@Tag(name = "管理后台 - 项目立项")
@RestController
@RequestMapping("/bpm/approval-process")
@Validated
public class ApprovalProcessController {

    @Resource
    private ApprovalProcessService approvalProcessService;

    @PostMapping("/create")
//    @Operation(summary = "创建项目立项")
    @PreAuthorize("@ss.hasPermission('system:approval-process:create')")
    public CommonResult<Long> createApprovalProcess(@Valid @RequestBody ApprovalProcessCreateReqVO createReqVO) {
        return success(approvalProcessService.createApprovalProcess(getLoginUserId(),createReqVO));
    }

//    @PutMapping("/update")
//    @Operation(summary = "更新项目立项")
//    @PreAuthorize("@ss.hasPermission('system:approval-process:update')")
//    public CommonResult<Boolean> updateApprovalProcess(@Valid @RequestBody ApprovalProcessUpdateReqVO updateReqVO) {
//        approvalProcessService.updateApprovalProcess(updateReqVO);
//        return success(true);
//    }

    @DeleteMapping("/delete")
//    @Operation(summary = "删除项目立项")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:approval-process:delete')")
    public CommonResult<Boolean> deleteApprovalProcess(@RequestParam("id") Long id) {
        approvalProcessService.deleteApprovalProcess(id);
        return success(true);
    }

    @GetMapping("/get")
//    @Operation(summary = "获得项目立项")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:approval-process:query')")
    public CommonResult<ApprovalProcessRespVO> getApprovalProcess(@RequestParam("id") Long id) {
        ApprovalProcessDO approvalProcess = approvalProcessService.getApprovalProcess(id);
        return success(ApprovalProcessConvert.INSTANCE.convert(approvalProcess));
    }

    @GetMapping("/list")
//    @Operation(summary = "获得项目立项列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:approval-process:query')")
    public CommonResult<List<ApprovalProcessRespVO>> getApprovalProcessList(@RequestParam("ids") Collection<Long> ids) {
        List<ApprovalProcessDO> list = approvalProcessService.getApprovalProcessList(ids);
        return success(ApprovalProcessConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
//    @Operation(summary = "获得项目立项分页")
    @PreAuthorize("@ss.hasPermission('system:approval-process:query')")
    public CommonResult<PageResult<ApprovalProcessRespVO>> getApprovalProcessPage(@Valid ApprovalProcessPageReqVO pageVO) {
        PageResult<ApprovalProcessDO> pageResult = approvalProcessService.getApprovalProcessPage(pageVO);
        return success(ApprovalProcessConvert.INSTANCE.convertPage(pageResult));
    }

//    @GetMapping("/export-excel")
//    @Operation(summary = "导出项目立项 Excel")
//    @PreAuthorize("@ss.hasPermission('system:approval-process:export')")
//    @OperateLog(type = EXPORT)
//    public void exportApprovalProcessExcel(@Valid ApprovalProcessExportReqVO exportReqVO,
//              HttpServletResponse response) throws IOException {
//        List<ApprovalProcessDO> list = approvalProcessService.getApprovalProcessList(exportReqVO);
//        // 导出 Excel
//        List<ApprovalProcessExcelVO> datas = ApprovalProcessConvert.INSTANCE.convertList02(list);
////        ExcelUtils.write(response, "项目立项.xls", "数据", ApprovalProcessExcelVO.class, datas);
//    }

}
