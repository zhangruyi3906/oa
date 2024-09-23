package com.lh.oa.module.system.mapper;


import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.dal.dataobject.contract.Contract;
import com.lh.oa.module.system.dal.dataobject.contract.ContractQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2023-09-08
 */
@Mapper
public interface ContractMapper extends BaseMapperX<Contract> {

    default PageResult<Contract> selectPage(ContractQuery query) {
        PageResult<Contract> contractPageResult = selectPage(query, new LambdaQueryWrapperX<Contract>()
                .likeIfPresent(Contract::getProjectName, query.getProjectName())
                .likeIfPresent(Contract::getContractName, query.getContractName())
                .likeIfPresent(Contract::getCustomerName, query.getCustomerName())
                .likeIfPresent(Contract::getPayWay, query.getPayWay())
                .eqIfPresent(Contract::getType, query.getType())
                .eqIfPresent(Contract::getCategory, query.getCategory())
                .orderByDesc(Contract::getId)
        );
        return contractPageResult;
    }

    Contract selectByContractName(String contractName);
}
