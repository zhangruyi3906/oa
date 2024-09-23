package com.lh.oa.module.system.convert.projectworktype;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypeCreateReqVO;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypeExcelVO;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypeRespVO;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypeUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.projectworktype.ProjectWorkTypeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.*;

/**
 * 项目工种 Convert
 *
 * @author
 */
@Mapper
public interface ProjectWorkTypeConvert {

    ProjectWorkTypeConvert INSTANCE = Mappers.getMapper(ProjectWorkTypeConvert.class);

    ProjectWorkTypeDO convert(ProjectWorkTypeCreateReqVO bean);

    ProjectWorkTypeDO convert(ProjectWorkTypeUpdateReqVO bean);

    ProjectWorkTypeRespVO convert(ProjectWorkTypeDO bean);

    List<ProjectWorkTypeRespVO> convertList(List<ProjectWorkTypeDO> list);

    PageResult<ProjectWorkTypeRespVO> convertPage(PageResult<ProjectWorkTypeDO> page);

    List<ProjectWorkTypeExcelVO> convertList02(List<ProjectWorkTypeDO> list);

}
