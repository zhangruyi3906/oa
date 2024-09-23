package com.lh.oa.module.system.convert.customers;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.customers.vo.CustomersCreateReqVO;
import com.lh.oa.module.system.controller.admin.customers.vo.CustomersExcelVO;
import com.lh.oa.module.system.controller.admin.customers.vo.CustomersRespVO;
import com.lh.oa.module.system.controller.admin.customers.vo.CustomersUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.customers.CustomersDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.customers.vo.*;

/**
 * 客户基础信息 Convert
 *
 * @author 狗蛋
 */
@Mapper
public interface CustomersConvert {

    CustomersConvert INSTANCE = Mappers.getMapper(CustomersConvert.class);

    CustomersDO convert(CustomersCreateReqVO bean);

    CustomersDO convert(CustomersUpdateReqVO bean);

    CustomersRespVO convert(CustomersDO bean);

    List<CustomersRespVO> convertList(List<CustomersDO> list);

    PageResult<CustomersRespVO> convertPage(PageResult<CustomersDO> page);

    List<CustomersExcelVO> convertList02(List<CustomersDO> list);

}
