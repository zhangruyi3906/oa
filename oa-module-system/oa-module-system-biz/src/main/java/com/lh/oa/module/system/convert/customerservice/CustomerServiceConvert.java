package com.lh.oa.module.system.convert.customerservice;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServiceCreateReqVO;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServiceExcelVO;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServiceRespVO;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServiceUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.customerservice.CustomerServiceDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.customerservice.vo.*;

/**
 * 客户服务 Convert
 *
 * @author 管理员
 */
@Mapper
public interface CustomerServiceConvert {

    CustomerServiceConvert INSTANCE = Mappers.getMapper(CustomerServiceConvert.class);

    CustomerServiceDO convert(CustomerServiceCreateReqVO bean);

    CustomerServiceDO convert(CustomerServiceUpdateReqVO bean);

    CustomerServiceRespVO convert(CustomerServiceDO bean);

    List<CustomerServiceRespVO> convertList(List<CustomerServiceDO> list);

    PageResult<CustomerServiceRespVO> convertPage(PageResult<CustomerServiceDO> page);

    List<CustomerServiceExcelVO> convertList02(List<CustomerServiceDO> list);

}
