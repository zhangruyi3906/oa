package com.lh.oa.module.system.service.impl;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.dal.dataobject.contract.Contract;
import com.lh.oa.module.system.dal.dataobject.contract.ContractQuery;
import com.lh.oa.module.system.dal.dataobject.file.FileDO;
import com.lh.oa.module.system.dal.mysql.file.FileMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.mapper.ContractMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-08
 */
@Service
public class ContractServiceImpl implements IContractService {
    @Resource
    private ContractMapper contractMapper;

    @Resource
    private FileMapper fileMapper;

    @Override
    public List<Contract> selectList() {
        return contractMapper.selectList(null);
    }

    @Override
    public Contract selectById(Long id) {
        return contractMapper.selectById(id);
    }

    @Override
    public void deleteById(Long id) {
        contractMapper.deleteById(id);
    }

    @Override
    public void updateById(Contract contract) {
        Contract contract1 = contractMapper.selectByContractName(contract.getContractName());
        if (contract1 != null && contract1.getId() != contract.getId()) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CONTRACT_IS_EXISTS);
        }
        FileDO fileDO = fileMapper.selectById(contract.getFileId());
        contract.setUrl(fileDO.getUrl());
        contractMapper.updateById(contract);
    }

    @Override
    public void insert(Contract contract) {
        Contract contract1 = contractMapper.selectByContractName(contract.getContractName());
        if (contract1 != null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CONTRACT_IS_EXISTS);
        }
        FileDO fileDO = fileMapper.selectById(contract.getFileId());
        contract.setUrl(fileDO.getUrl());
        contractMapper.insert(contract);
    }

    @Override
    public PageResult<Contract> selectPage(ContractQuery query) {
        return contractMapper.selectPage(query);
    }

    @Override
    public List<Contract> customerList(String customerName) {
        return contractMapper.selectList("customer_name", customerName);
    }

    @Override
    public List<Contract> totalById(Long id) {
        Contract contract = contractMapper.selectOne("id", id);
        List<Contract> contractList = contractMapper.selectList("parent_id", id);
        List<Contract> contracts = new ArrayList<>();
        contracts.add(contract);
        contracts.addAll(contractList);
        return contracts;
    }
}
