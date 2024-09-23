package com.lh.oa.module.system.convert.projectrecord;


import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordCreateReqVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordRespVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.projectrecord.ProjectRecordDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectRecordConvert {
    ProjectRecordConvert INSTANCE = Mappers.getMapper(ProjectRecordConvert.class);

    ProjectRecordDO convert(ProjectRecordCreateReqVO bean);

    ProjectRecordDO convert1(ProjectRecordUpdateReqVO createReqVO);

    PageResult<ProjectRecordRespVO> convertPage(PageResult<ProjectRecordDO> page);


}
