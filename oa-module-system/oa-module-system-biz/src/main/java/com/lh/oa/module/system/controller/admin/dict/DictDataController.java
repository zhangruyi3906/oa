package com.lh.oa.module.system.controller.admin.dict;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.dict.vo.data.DictDataBatchVo;
import com.lh.oa.module.system.controller.admin.dict.vo.data.DictDataCreateReqVO;
import com.lh.oa.module.system.controller.admin.dict.vo.data.DictDataExcelVO;
import com.lh.oa.module.system.controller.admin.dict.vo.data.DictDataExportReqVO;
import com.lh.oa.module.system.controller.admin.dict.vo.data.DictDataPageReqVO;
import com.lh.oa.module.system.controller.admin.dict.vo.data.DictDataRespVO;
import com.lh.oa.module.system.controller.admin.dict.vo.data.DictDataSimpleRespVO;
import com.lh.oa.module.system.controller.admin.dict.vo.data.DictDataUpdateReqVO;
import com.lh.oa.module.system.convert.dict.DictDataConvert;
import com.lh.oa.module.system.dal.dataobject.dict.DictDataDO;
import com.lh.oa.module.system.service.dict.DictDataService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name =  "管理后台 - 字典数据")
@RestController
@RequestMapping("/system/dict-data")
@Validated
@Slf4j
public class DictDataController {

    @Resource
    private DictDataService dictDataService;

    @PostMapping("/create")
    ////@Operation(summary = "新增字典数据")
    @PreAuthorize("@ss.hasPermission('system:dict:create')")
    public CommonResult<Long> createDictData(@Valid @RequestBody DictDataCreateReqVO reqVO) {
        Long dictDataId = dictDataService.createDictData(reqVO);
        return success(dictDataId);
    }

    @PutMapping("update")
    ////@Operation(summary = "修改字典数据")
    @PreAuthorize("@ss.hasPermission('system:dict:update')")
    public CommonResult<Boolean> updateDictData(@Valid @RequestBody DictDataUpdateReqVO reqVO) {
        dictDataService.updateDictData(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    ////@Operation(summary = "删除字典数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:dict:delete')")
    public CommonResult<Boolean> deleteDictData(Long id) {
        dictDataService.deleteDictData(id);
        return success(true);
    }

    @GetMapping("/list-all-simple")
    ////@Operation(summary = "获得全部字典数据列表", description = "一般用于管理后台缓存字典数据在本地")
    // 无需添加权限认证，因为前端全局都需要
    public CommonResult<List<DictDataSimpleRespVO>> getSimpleDictDataList(DictDataExportReqVO dictDataExportReqVO) {
        List<DictDataDO> list = dictDataService.getDictDataList(dictDataExportReqVO);
        return success(DictDataConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    ////@Operation(summary = "/获得字典类型的分页列表")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public CommonResult<PageResult<DictDataRespVO>> getDictTypePage(@Valid DictDataPageReqVO reqVO) {
        return success(DictDataConvert.INSTANCE.convertPage(dictDataService.getDictDataPage(reqVO)));
    }


    @GetMapping("/list")
    ////@Operation(summary = "/获得字典类型的列表")
//    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public CommonResult<List<DictDataSimpleRespVO>> getDictTypeList(@Valid DictDataExportReqVO reqVO) {
        List<DictDataDO> list = dictDataService.getDictDataList(reqVO);
        List<DictDataSimpleRespVO> data = DictDataConvert.INSTANCE.convertList(list);
        return success(data);
    }

    @GetMapping(value = "/get")
    ////@Operation(summary = "/查询字典数据详细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public CommonResult<DictDataRespVO> getDictData(@RequestParam("id") Long id) {
        return success(DictDataConvert.INSTANCE.convert(dictDataService.getDictData(id)));
    }

    @GetMapping("/export")
    ////@Operation(summary = "导出字典数据")
    @PreAuthorize("@ss.hasPermission('system:dict:export')")
    //@Operation(type = EXPORT)
    public void export(HttpServletResponse response, @Valid DictDataExportReqVO reqVO) throws IOException {
        List<DictDataDO> list = dictDataService.getDictDataList(reqVO);
        List<DictDataExcelVO> data = DictDataConvert.INSTANCE.convertList02(list);
        // 输出
        ExcelUtils.write(response, "字典数据.xls", "数据列表", DictDataExcelVO.class, data);
    }

    @GetMapping("/batchQuery")
    @PermitAll
    @Parameter(name = "dictTypes", description = "字典值，如果有多个，用英文逗号分隔", required = true)
    public CommonResult<List<DictDataBatchVo>> batchQueryDictData(@RequestParam("dictTypes") String dictTypes) {
        log.info("根据字典类型批量获取字典值列表，dictTypes:{}", dictTypes);
        List<DictDataBatchVo> result = dictDataService.batchQueryDictData(dictTypes);
        return CommonResult.success(result);
    }

}