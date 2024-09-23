package com.lh.oa.module.system.api.dict;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.api.dict.dto.DictDataBatchDTO;
import com.lh.oa.module.system.api.dict.dto.DictDataRespDTO;
import com.lh.oa.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.PermitAll;
import java.util.Collection;
import java.util.List;

@FeignClient(name = ApiConstants.NAME) //
@Tag(name = "RPC 服务 - 字典数据")
public interface DictDataApi {

    String PREFIX = ApiConstants.PREFIX + "/dict-data";

    @GetMapping(PREFIX + "/valid")
    //@Operation(summary = "校验字典数据们是否有效")
    @Parameters({
            @Parameter(name = "dictType", description = "字典类型", example = "SEX", required = true),
            @Parameter(name = "descriptions", description = "字典数据值的数组", example = "1,2", required = true)
    })
    CommonResult<Boolean> validateDictDatas(@RequestParam("dictType") String dictType,
                                            @RequestParam("values") Collection<String> values);

    @GetMapping(PREFIX + "/get")
    //@Operation(summary = "获得指定的字典数据")
    @Parameters({
            @Parameter(name = "dictType", description = "字典类型", example = "SEX", required = true),
            @Parameter(name = "description", description = "字典数据值", example = "1", required = true)
    })
    CommonResult<DictDataRespDTO> getDictData(@RequestParam("dictType") String dictType,
                                              @RequestParam("value") String value);

    @GetMapping(PREFIX + "/parse")
    //@Operation(summary = "解析获得指定的字典数据")
    @Parameters({
            @Parameter(name = "dictType", description = "字典类型", example = "SEX", required = true),
            @Parameter(name = "label", description = "字典标签", example = "男", required = true)
    })
    CommonResult<DictDataRespDTO> parseDictData(@RequestParam("dictType") String dictType,
                                                @RequestParam("label") String label);

    /**
     * 批量查询字典值
     */
    @GetMapping(PREFIX + "/batchQuery")
    @Parameters({
            @Parameter(name = "dictTypes", description = "字典类型集合", required = true)
    })
    CommonResult<List<DictDataBatchDTO>> queryDictList(@RequestParam("dictTypes") String dictTypes);

}