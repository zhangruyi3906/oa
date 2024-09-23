package com.lh.oa.module.bpm.convert.officesuppliesrequest;

import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeVO;
import com.lh.oa.module.bpm.dal.dataobject.officesuppliesrequest.OfficeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OfficeConvert {
    OfficeConvert INSTANCE = Mappers.getMapper(OfficeConvert.class);
    OfficeDO convert(OfficeVO bean);
    OfficeVO convert(OfficeDO bean);
    List<OfficeDO> convertList(List<OfficeVO> list);
    List<OfficeVO> convertList1(List<OfficeDO> list);

}
