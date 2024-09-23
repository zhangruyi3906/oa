package com.lh.oa.module.system.convert.project;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.project.vo.*;
import com.lh.oa.module.system.dal.dataobject.project.ProjectDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.project.vo.*;

/**
 * 项目 Convert
 *
 * @author 狗蛋
 */
@Mapper
public interface ProjectConvert {

    ProjectConvert INSTANCE = Mappers.getMapper(ProjectConvert.class);

    ProjectDO convert(ProjectCreateReqVO bean);

    ProjectDO convert(ProjectUpdateReqVO bean);


    ProjectRespVO convert(ProjectDO bean);

    List<ProjectRespVO> convertList(List<ProjectDO> list);

    PageResult<ProjectRespVO> convertPage(PageResult<ProjectDO> page);

    List<ProjectExcelVO> convertList02(List<ProjectDO> list);
    List<ProjectSimpleRespVO> convertList03(List<ProjectDO> list);

}
