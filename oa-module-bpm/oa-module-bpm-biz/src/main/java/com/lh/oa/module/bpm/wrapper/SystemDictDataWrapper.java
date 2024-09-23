package com.lh.oa.module.bpm.wrapper;

import com.google.common.base.Joiner;
import com.lh.oa.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.vo.InvoicingApplyFormVo;
import com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.vo.PerformanceExamineDetailVo;
import com.lh.oa.module.system.api.dict.DictDataApi;
import com.lh.oa.module.system.api.dict.dto.DictDataBatchDTO;
import com.lh.oa.module.system.api.dict.dto.DictDataRespDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tanghanlin
 * @since 2023/10/24
 */
@Component
public class SystemDictDataWrapper {

    @Resource
    private DictDataApi dictDataApi;

    /**
     * 批量获取字典的名称
     *
     * @param dictType 字典type列表，逗号分隔
     * @return 字典映射，结构：Map<字典type, Map<字典value, 字典label>>
     */
    public Map<String, Map<String, String>> getDictTypeMap(String dictType) {
        CommonResult<List<DictDataBatchDTO>> dictResult = dictDataApi.queryDictList(dictType);
        ExceptionThrowUtils.throwIfTrue(
                !Objects.equals(HttpStatus.OK.value(), dictResult.getStatus()) || Objects.isNull(dictResult.getData()),
                GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
        return dictResult
                .getData()
                .stream()
                .collect(Collectors.toMap(
                        DictDataBatchDTO::getDictType,
                        vo -> vo.getDataVoList()
                                .stream()
                                .collect(Collectors.toMap(DictDataRespDTO::getValue, DictDataRespDTO::getLabel))));
    }

    /**
     * 批量获取字典的名称
     *
     * @param dictTypes 字典type列表，逗号分隔
     * @return 字典映射，结构：Map<字典type, Map<字典value, 字典label>>
     */
    public Map<String, Map<String, String>> getDictTypeMap(Set<String> dictTypes) {
        if (CollectionUtils.isEmpty(dictTypes)) {
            return Collections.emptyMap();
        }
        return getDictTypeMap(Joiner.on(",").join(dictTypes));
    }

}
