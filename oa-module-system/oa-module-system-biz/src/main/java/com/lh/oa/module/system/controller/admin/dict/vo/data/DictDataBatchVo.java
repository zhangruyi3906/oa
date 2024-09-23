package com.lh.oa.module.system.controller.admin.dict.vo.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class DictDataBatchVo implements Serializable {

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典值列表")
    private List<DictDataBaseVO> dataVoList;

}
