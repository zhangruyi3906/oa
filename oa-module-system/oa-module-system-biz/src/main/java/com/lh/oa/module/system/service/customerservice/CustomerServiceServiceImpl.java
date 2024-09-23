package com.lh.oa.module.system.service.customerservice;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServiceCreateReqVO;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServiceExportReqVO;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServicePageReqVO;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServiceUpdateReqVO;
import com.lh.oa.module.system.convert.customerservice.CustomerServiceConvert;
import com.lh.oa.module.system.dal.dataobject.customerservice.CustomerServiceDO;
import com.lh.oa.module.system.dal.mysql.customerservice.CustomerServiceMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.lh.oa.module.system.controller.admin.customerservice.vo.*;
import com.lh.oa.framework.common.pojo.PageResult;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 客户服务 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class CustomerServiceServiceImpl implements CustomerServiceService {

    @Resource
    private CustomerServiceMapper customerServiceMapper;

    @Override
    public Long createCustomerService(CustomerServiceCreateReqVO createReqVO) {
        // 插入
        CustomerServiceDO customerService = CustomerServiceConvert.INSTANCE.convert(createReqVO);
        customerServiceMapper.insert(customerService);
        // 返回
        return customerService.getId();
    }

    @Override
    public void updateCustomerService(CustomerServiceUpdateReqVO updateReqVO) {
        // 校验存在
        validateCustomerServiceExists(updateReqVO.getId());
        // 更新
        CustomerServiceDO updateObj = CustomerServiceConvert.INSTANCE.convert(updateReqVO);
        customerServiceMapper.updateById(updateObj);
    }

    @Override
    public void deleteCustomerService(Long id) {
        // 校验存在
        validateCustomerServiceExists(id);
        // 删除
        customerServiceMapper.deleteById(id);
    }

    private void validateCustomerServiceExists(Long id) {
        if (customerServiceMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CUSTOMER_SERVICE_NOT_EXISTS);
        }
    }

    @Override
    public CustomerServiceDO getCustomerService(Long id) {
        return customerServiceMapper.selectById(id);
    }

    @Override
    public List<CustomerServiceDO> getCustomerServiceList(Collection<Long> ids) {
        return customerServiceMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CustomerServiceDO> getCustomerServicePage(CustomerServicePageReqVO pageReqVO) {
        return customerServiceMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CustomerServiceDO> getCustomerServiceList(CustomerServiceExportReqVO exportReqVO) {
        return customerServiceMapper.selectList(exportReqVO);
    }

}
