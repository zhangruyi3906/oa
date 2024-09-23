package com.lh.oa.module.system.convert.logger;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.api.logger.dto.LoginLogCreateReqDTO;
import com.lh.oa.module.system.controller.admin.logger.vo.loginlog.LoginLogExcelVO;
import com.lh.oa.module.system.controller.admin.logger.vo.loginlog.LoginLogRespVO;
import com.lh.oa.module.system.dal.dataobject.logger.LoginLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LoginLogConvert {

    LoginLogConvert INSTANCE = Mappers.getMapper(LoginLogConvert.class);

    PageResult<LoginLogRespVO> convertPage(PageResult<LoginLogDO> page);

    List<LoginLogExcelVO> convertList(List<LoginLogDO> list);

    LoginLogDO convert(LoginLogCreateReqDTO bean);

}
