package com.lh.oa.module.system.convert.jobcommission;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionCreateReqVO;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionExcelVO;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionRespVO;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.jobcommission.JobCommissionDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.*;

/**
 * 项目工种提成 Convert
 *
 * @author
 */
@Mapper
public interface JobCommissionConvert {

    JobCommissionConvert INSTANCE = Mappers.getMapper(JobCommissionConvert.class);

    JobCommissionDO convert(JobCommissionCreateReqVO bean);

    JobCommissionDO convert(JobCommissionUpdateReqVO bean);

    JobCommissionRespVO convert(JobCommissionDO bean);

    List<JobCommissionRespVO> convertList(List<JobCommissionDO> list);

    PageResult<JobCommissionRespVO> convertPage(PageResult<JobCommissionDO> page);

    List<JobCommissionExcelVO> convertList02(List<JobCommissionDO> list);

}
