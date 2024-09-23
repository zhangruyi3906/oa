package com.lh.oa.module.system.dal.mysql.customerservice;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServiceExportReqVO;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServicePageReqVO;
import com.lh.oa.module.system.dal.dataobject.customerservice.CustomerServiceDO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.system.controller.admin.customerservice.vo.*;

/**
 * 客户服务 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface CustomerServiceMapper extends BaseMapperX<CustomerServiceDO> {

    default PageResult<CustomerServiceDO> selectPage(CustomerServicePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CustomerServiceDO>()
                .eqIfPresent(CustomerServiceDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(CustomerServiceDO::getServiceContent, reqVO.getServiceContent())
                .eqIfPresent(CustomerServiceDO::getFeedback, reqVO.getFeedback())
                .eqIfPresent(CustomerServiceDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CustomerServiceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CustomerServiceDO::getId));
    }

    default List<CustomerServiceDO> selectList(CustomerServiceExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CustomerServiceDO>()
                .eqIfPresent(CustomerServiceDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(CustomerServiceDO::getServiceContent, reqVO.getServiceContent())
                .eqIfPresent(CustomerServiceDO::getFeedback, reqVO.getFeedback())
                .eqIfPresent(CustomerServiceDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CustomerServiceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CustomerServiceDO::getId));
    }

}
