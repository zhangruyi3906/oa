package com.lh.oa.module.bpm.convert.officesuppliesrequest;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestCreateVo;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestRespVo;
import com.lh.oa.module.bpm.dal.dataobject.officesuppliesrequest.OfficeSuppliesRequestDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OfficeSuppliesRequestConvert {
    OfficeSuppliesRequestConvert INSTANCE = Mappers.getMapper(OfficeSuppliesRequestConvert.class);
    OfficeSuppliesRequestDO convert(OfficeSuppliesRequestCreateVo bean);
    OfficeSuppliesRequestRespVo convert(OfficeSuppliesRequestDO bean);
    PageResult<OfficeSuppliesRequestRespVo> convertPage(PageResult<OfficeSuppliesRequestDO> page);

}
