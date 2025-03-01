package com.lh.oa.module.bpm.convert.definition;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.definition.vo.group.BpmUserGroupCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.group.BpmUserGroupRespVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.group.BpmUserGroupUpdateReqVO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmUserGroupDO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户组 Convert
 *
 * @author
 */
@Mapper
public interface BpmUserGroupConvert {

    BpmUserGroupConvert INSTANCE = Mappers.getMapper(BpmUserGroupConvert.class);

    BpmUserGroupDO convert(BpmUserGroupCreateReqVO bean);

    BpmUserGroupDO convert(BpmUserGroupUpdateReqVO bean);

    BpmUserGroupRespVO convert(BpmUserGroupDO bean);

    List<BpmUserGroupRespVO> convertList(List<BpmUserGroupDO> list);

    PageResult<BpmUserGroupRespVO> convertPage(PageResult<BpmUserGroupDO> page);

    @Named("convertList2")
    List<BpmUserGroupRespVO> convertList2(List<BpmUserGroupDO> list);

}
