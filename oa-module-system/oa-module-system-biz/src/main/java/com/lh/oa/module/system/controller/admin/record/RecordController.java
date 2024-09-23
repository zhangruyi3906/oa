package com.lh.oa.module.system.controller.admin.record;

import com.lh.oa.module.system.controller.admin.record.vo.*;
import com.lh.oa.module.system.dal.dataobject.record.RecordDO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import javax.annotation.security.PermitAll;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.pojo.CommonResult;
import static com.lh.oa.framework.common.pojo.CommonResult.success;

import com.lh.oa.framework.excel.core.util.ExcelUtils;

import com.lh.oa.module.system.controller.admin.record.vo.*;
import com.lh.oa.module.system.convert.record.RecordConvert;
import com.lh.oa.module.system.service.record.RecordService;

@Tag(name = "管理后台 - 打卡记录")
@RestController
@RequestMapping("/system/record")
@Validated
public class RecordController {

    @Resource
    private RecordService recordService;

    @PostMapping("/createDept")
    //@Operation(summary = "创建打卡记录")
//    //TODO
//    @PermitAll
    @PreAuthorize("@ss.hasPermission('system:record:create')")
    public CommonResult<Boolean> createRecordDept(@Valid @RequestBody RecordCreateReqVO createReqVO) {
        return success(recordService.createRecord(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新打卡记录")
    @PreAuthorize("@ss.hasPermission('system:record:update')")
    public CommonResult<Boolean> updateRecord(@Valid @RequestBody RecordUpdateReqVO updateReqVO) {
        recordService.updateRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除打卡记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:record:delete')")
    public CommonResult<Boolean> deleteRecord(@RequestParam("id") Long id) {
        recordService.deleteRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得打卡记录")
    //TODO
//    @PermitAll
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:record:query')")
    public CommonResult<RecordRespVO> getRecord(@RequestParam("id") Long id) {
        RecordDO records = recordService.getRecord(id);
        return success(RecordConvert.INSTANCE.convert(records));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得打卡记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:record:query')")
    public CommonResult<List<RecordRespVO>> getRecordList(@RequestParam("ids") Collection<Long> ids) {
        List<RecordDO> list = recordService.getRecordList(ids);
        return success(RecordConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得打卡记录分页")
    //TODO
//    @PermitAll
    @PreAuthorize("@ss.hasPermission('system:record:query')")
    public CommonResult<PageResult<RecordDO>> getRecordPage(@Valid RecordPageReqVO pageVO) {
        return success(recordService.getRecordPage(pageVO));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出打卡记录 Excel")
    @PreAuthorize("@ss.hasPermission('system:record:export')")
    //@Operation(type = EXPORT)
    public void exportRecordExcel(@Valid RecordExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<RecordDO> list = recordService.getRecordList(exportReqVO);
        // 导出 Excel
        List<RecordExcelVO> datas = RecordConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "打卡记录.xls", "数据", RecordExcelVO.class, datas);
    }



}
