package com.lh.oa.module.system.service.impl;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.dal.dataobject.contract.Contract;
import com.lh.oa.module.system.dal.dataobject.contract.ContractQuery;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-08
 */
public interface IContractService {


    List<Contract> selectList();

    Object selectById(Long id);

    void deleteById(Long id);

    void updateById(Contract contract);

    void insert(Contract contract);

    PageResult<Contract> selectPage(ContractQuery query);

    List<Contract> customerList(String customerName);

    List<Contract> totalById(Long id);
}
