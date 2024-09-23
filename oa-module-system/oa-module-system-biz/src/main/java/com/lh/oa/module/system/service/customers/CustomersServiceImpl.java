package com.lh.oa.module.system.service.customers;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.customers.vo.*;
import com.lh.oa.module.system.convert.customers.CustomersConvert;
import com.lh.oa.module.system.dal.dataobject.baseRegion.BaseRegion;
import com.lh.oa.module.system.dal.dataobject.customers.CustomersDO;
import com.lh.oa.module.system.dal.dataobject.file.FileDO;
import com.lh.oa.module.system.dal.mysql.customers.CustomersMapper;
import com.lh.oa.module.system.dal.mysql.file.FileMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.mapper.BaseRegionMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户基础信息 Service 实现类
 *
 * @author 狗蛋
 */
@Service
@Validated
public class CustomersServiceImpl implements CustomersService {

    @Resource
    private CustomersMapper customersMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private BaseRegionMapper baseRegionMapper;

    @Override
    public CustomersDO createCustomers(CustomersCreateReqVO createReqVO) {
        // 插入
        CustomersDO customers = CustomersConvert.INSTANCE.convert(createReqVO);
        CustomersDO customersDO = customersMapper.selectOne("company_name", createReqVO.getCompanyName());
        if (customersDO != null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CUSTOMERS_NAME_IS_EXISTS);
        }
        if (createReqVO.getFileId() != null) {
            FileDO fileDO = fileMapper.selectById(createReqVO.getFileId());
            customers.setUrl(fileDO.getUrl());
        }

        Long areaId = createReqVO.getAreaId();
        Long cityId = createReqVO.getCityId();
        Long stateId = createReqVO.getStateId();
        if (areaId != null) {
            BaseRegion area = baseRegionMapper.selectOne("id", areaId);
            customers.setArea(area.getName());
        }
        if (cityId != null) {
            BaseRegion city = baseRegionMapper.selectOne("id", cityId);
            customers.setCity(city.getName());
        }
        if (stateId != null) {
            BaseRegion state = baseRegionMapper.selectOne("id", stateId);
            customers.setState(state.getName());
        }
        customersMapper.insert(customers);
        // 返回
        return customers;
    }

    @Override
    public void updateCustomers(CustomersUpdateReqVO updateReqVO) {
        // 校验存在
        validateCustomersExists(updateReqVO.getId());
        if (updateReqVO.getFileId() != null) {
            FileDO fileDO = fileMapper.selectById(updateReqVO.getFileId());
            updateReqVO.setUrl(fileDO.getUrl());
        }
        // 更新
        CustomersDO updateObj = CustomersConvert.INSTANCE.convert(updateReqVO);
        Long areaId = updateObj.getAreaId();
        Long cityId = updateObj.getCityId();
        Long stateId = updateObj.getStateId();
        if (areaId != null) {
            BaseRegion area = baseRegionMapper.selectOne("id", areaId);
            updateObj.setArea(area.getName());
        }
        if (cityId != null) {
            BaseRegion city = baseRegionMapper.selectOne("id", cityId);
            updateObj.setCity(city.getName());
        }
        if (stateId != null) {
            BaseRegion state = baseRegionMapper.selectOne("id", stateId);
            updateObj.setState(state.getName());
        }
        customersMapper.updateById(updateObj);
    }

    @Override
    public void deleteCustomers(Long id) {
        // 校验存在
        validateCustomersExists(id);
        // 删除
        customersMapper.deleteById(id);
    }

    private void validateCustomersExists(Long id) {
        if (customersMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CUSTOMERS_NOT_EXISTS);
        }
    }

    @Override
    public CustomersDO getCustomers(Long id) {
        return customersMapper.selectById(id);
    }

    @Override
    public List<CustomersDO> getCustomersList(Collection<Long> ids) {
        return customersMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CustomersDO> getCustomersPage(CustomersPageReqVO pageReqVO) {
        return customersMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CustomersDO> getCustomersList(CustomersExportReqVO exportReqVO) {
        return customersMapper.selectList(exportReqVO);
    }

    @Override
    public List<CustomersSimpleVO> getSimpleCustomers() {
        List<CustomersDO> customersDOS = customersMapper.selectList();
        List<CustomersSimpleVO> simpleVOS = customersDOS.stream()
                .map(customers -> new CustomersSimpleVO(customers.getId(), customers.getCompanyName()))
                .collect(Collectors.toList());
        return simpleVOS;
    }

}
