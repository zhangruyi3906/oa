package com.lh.oa.module.system.convert.dict;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.api.dict.dto.DictDataRespDTO;
import com.lh.oa.module.system.controller.admin.dict.vo.data.*;
import com.lh.oa.module.system.dal.dataobject.dict.DictDataDO;
import com.lh.oa.module.system.controller.admin.dict.vo.data.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DictDataConvert {

    DictDataConvert INSTANCE = Mappers.getMapper(DictDataConvert.class);

    List<DictDataSimpleRespVO> convertList(List<DictDataDO> list);

    DictDataRespVO convert(DictDataDO bean);

    PageResult<DictDataRespVO> convertPage(PageResult<DictDataDO> page);

    DictDataDO convert(DictDataUpdateReqVO bean);

    DictDataDO convert(DictDataCreateReqVO bean);

    List<DictDataExcelVO> convertList02(List<DictDataDO> bean);

    DictDataRespDTO convert02(DictDataDO bean);


}
