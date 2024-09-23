package com.lh.oa.module.infra.convert.logger;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import com.lh.oa.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogExcelVO;
import com.lh.oa.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogRespVO;
import com.lh.oa.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * API 错误日志 Convert
 *
 * @author
 */
@Mapper
public interface ApiErrorLogConvert {

    ApiErrorLogConvert INSTANCE = Mappers.getMapper(ApiErrorLogConvert.class);

    ApiErrorLogRespVO convert(ApiErrorLogDO bean);

    PageResult<ApiErrorLogRespVO> convertPage(PageResult<ApiErrorLogDO> page);

    List<ApiErrorLogExcelVO> convertList02(List<ApiErrorLogDO> list);

    ApiErrorLogDO convert(ApiErrorLogCreateReqDTO bean);

}
