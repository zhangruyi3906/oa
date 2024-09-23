package com.lh.oa.module.system.convert.worklog;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogCreateReqVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogExcelVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogRespVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.worklog.WorkLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.worklog.vo.*;

/**
 * 员工工作日志 Convert
 *
 * @author 管理员
 */
@Mapper
public interface WorkLogConvert {

    WorkLogConvert INSTANCE = Mappers.getMapper(WorkLogConvert.class);

    WorkLogDO convert(WorkLogCreateReqVO bean);

    WorkLogDO convert(WorkLogUpdateReqVO bean);

    WorkLogRespVO convert(WorkLogDO bean);

    List<WorkLogRespVO> convertList(List<WorkLogDO> list);

    PageResult<WorkLogRespVO> convertPage(PageResult<WorkLogDO> page);

    List<WorkLogExcelVO> convertList02(List<WorkLogDO> list);

}
