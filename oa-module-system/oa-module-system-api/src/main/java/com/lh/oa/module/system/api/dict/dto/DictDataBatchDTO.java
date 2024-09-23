package com.lh.oa.module.system.api.dict.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class DictDataBatchDTO implements Serializable {

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典值列表")
    private List<DictDataRespDTO> dataVoList;

}
