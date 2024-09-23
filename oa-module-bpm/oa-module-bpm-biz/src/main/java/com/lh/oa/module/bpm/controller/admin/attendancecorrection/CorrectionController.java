package com.lh.oa.module.bpm.controller.admin.attendancecorrection;

import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionPageReqVO;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionRespVO;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionUpdateReqVO;
import com.lh.oa.module.bpm.convert.attendancecorrection.CorrectionConvert;
import com.lh.oa.module.bpm.dal.dataobject.attendancecorrection.CorrectionDO;
import com.lh.oa.module.bpm.service.attendancecorrection.CorrectionService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import javax.validation.*;
import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.pojo.CommonResult;
import static com.lh.oa.framework.common.pojo.CommonResult.success;

//import com.lh.oa.framework.excel.core.util.ExcelUtils;

import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.*;

@Tag(name = "管理后台 - 补卡流程")
@RestController
@RequestMapping("/bpm/correction")
@Validated
public class CorrectionController {

    @Resource
    private CorrectionService correctionService;

    @PostMapping("/create")
//    @Operation(summary = "创建补卡流程")
    @PreAuthorize("@ss.hasPermission('bpm:correction:create')")
    public CommonResult<Long> createCorrection(@Valid @RequestBody CorrectionCreateReqVO createReqVO) {
        return success(correctionService.createCorrection(getLoginUserId(),createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新补卡流程")
    @PreAuthorize("@ss.hasPermission('bpm:correction:update')")
    public CommonResult<Boolean> updateCorrection(@Valid @RequestBody CorrectionUpdateReqVO updateReqVO) {
        correctionService.updateCorrection(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除补卡流程")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('bpm:correction:delete')")
    public CommonResult<Boolean> deleteCorrection(@RequestParam("id") Long id) {
        correctionService.deleteCorrection(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得补卡流程")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:correction:query')")
    public CommonResult<CorrectionRespVO> getCorrection(@RequestParam("id") Long id) {
        CorrectionDO correction = correctionService.getCorrection(id);
        return success(CorrectionConvert.INSTANCE.convert(correction));
    }

    @GetMapping("/list")
//    @Operation(summary = "获得补卡流程列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('bpm:correction:query')")
    public CommonResult<List<CorrectionRespVO>> getCorrectionList(@RequestParam("ids") Collection<Long> ids) {
        List<CorrectionDO> list = correctionService.getCorrectionList(ids);
        return success(CorrectionConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
//    @Operation(summary = "获得补卡流程分页")
    @PreAuthorize("@ss.hasPermission('bpm:correction:query')")
    public CommonResult<PageResult<CorrectionRespVO>> getCorrectionPage(@Valid CorrectionPageReqVO pageVO) {
        PageResult<CorrectionDO> pageResult = correctionService.getCorrectionPage(pageVO);
        return success(CorrectionConvert.INSTANCE.convertPage(pageResult));
    }
//
//    @GetMapping("/export-excel")
//    @Operation(summary = "导出补卡流程 Excel")
//    @PreAuthorize("@ss.hasPermission('bpm:correction:export')")
//    @OperateLog(type = EXPORT)
//    public void exportCorrectionExcel(@Valid CorrectionExportReqVO exportReqVO,
//              HttpServletResponse response) throws IOException {
//        List<CorrectionDO> list = correctionService.getCorrectionList(exportReqVO);
//        // 导出 Excel
//        List<CorrectionExcelVO> datas = CorrectionConvert.INSTANCE.convertList02(list);
////        ExcelUtils.write(response, "补卡流程.xls", "数据", CorrectionExcelVO.class, datas);
//    }

}
