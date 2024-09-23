package com.lh.oa.module.system.dal.mysql.customers;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.customers.vo.CustomersExportReqVO;
import com.lh.oa.module.system.controller.admin.customers.vo.CustomersPageReqVO;
import com.lh.oa.module.system.dal.dataobject.customers.CustomersDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户基础信息 Mapper
 *
 * @author 狗蛋
 */
@Mapper
public interface CustomersMapper extends BaseMapperX<CustomersDO> {

    default PageResult<CustomersDO> selectPage(CustomersPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CustomersDO>()
                .eqIfPresent(CustomersDO::getEmail, reqVO.getEmail())
                .eqIfPresent(CustomersDO::getPhone, reqVO.getPhone())
                .likeIfPresent(CustomersDO::getCompanyName, reqVO.getCompanyName())
                .eqIfPresent(CustomersDO::getAddress, reqVO.getAddress())
                .eqIfPresent(CustomersDO::getCity, reqVO.getCity())
                .likeIfPresent(CustomersDO::getState, reqVO.getState())
                .eqIfPresent(CustomersDO::getPostalCode, reqVO.getPostalCode())
                .eqIfPresent(CustomersDO::getCountry, reqVO.getCountry())
                .eqIfPresent(CustomersDO::getCusLevel, reqVO.getCusLevel())
                .orderByDesc(CustomersDO::getId));
    }

    default List<CustomersDO> selectList(CustomersExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CustomersDO>()
                .eqIfPresent(CustomersDO::getEmail, reqVO.getEmail())
                .eqIfPresent(CustomersDO::getPhone, reqVO.getPhone())
                .likeIfPresent(CustomersDO::getCompanyName, reqVO.getCompanyName())
                .eqIfPresent(CustomersDO::getAddress, reqVO.getAddress())
                .eqIfPresent(CustomersDO::getCity, reqVO.getCity())
                .eqIfPresent(CustomersDO::getState, reqVO.getState())
                .eqIfPresent(CustomersDO::getPostalCode, reqVO.getPostalCode())
                .eqIfPresent(CustomersDO::getCountry, reqVO.getCountry())
                .eqIfPresent(CustomersDO::getCusLevel, reqVO.getCusLevel())
                .orderByDesc(CustomersDO::getId));
    }

}
