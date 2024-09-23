package com.lh.oa.module.system.convert.userProject;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.userProject.vo.UserProjectCreateReqVO;
import com.lh.oa.module.system.controller.admin.userProject.vo.UserProjectExcelVO;
import com.lh.oa.module.system.controller.admin.userProject.vo.UserProjectRespVO;
import com.lh.oa.module.system.controller.admin.userProject.vo.UserProjectUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.userProject.vo.*;

/**
 * 人员项目 Convert
 *
 * @author
 */
@Mapper
public interface UserProjectConvert {

    UserProjectConvert INSTANCE = Mappers.getMapper(UserProjectConvert.class);

    UserProjectDO convert(UserProjectCreateReqVO bean);

    UserProjectDO convert(UserProjectUpdateReqVO bean);

    UserProjectRespVO convert(UserProjectDO bean);

    List<UserProjectRespVO> convertList(List<UserProjectDO> list);

    PageResult<UserProjectRespVO> convertPage(PageResult<UserProjectDO> page);

    List<UserProjectExcelVO> convertList02(List<UserProjectDO> list);

}
