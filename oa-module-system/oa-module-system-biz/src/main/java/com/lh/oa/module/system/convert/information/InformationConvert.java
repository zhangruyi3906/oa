package com.lh.oa.module.system.convert.information;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.information.vo.InformationCreateReqVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationExcelVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationRespVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationUpdateReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserImportExcelVO;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 员工信息 Convert
 *
 * @author
 */
@Mapper
public interface InformationConvert {

    InformationConvert INSTANCE = Mappers.getMapper(InformationConvert.class);

    InformationDO convert(InformationCreateReqVO bean);

    InformationDO convert(InformationUpdateReqVO bean);

    InformationRespVO convert(InformationDO bean);

    List<InformationRespVO> convertList(List<InformationDO> list);

    PageResult<InformationRespVO> convertPage(PageResult<InformationDO> page);

    List<InformationExcelVO> convertList02(List<InformationDO> list);

}
