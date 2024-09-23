package com.lh.oa.module.system.service.customers;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.customers.vo.*;
import com.lh.oa.module.system.dal.dataobject.customers.CustomersDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 客户基础信息 Service 接口
 *
 * @author 狗蛋
 */
public interface CustomersService {

    /**
     * 创建客户基础信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    CustomersDO createCustomers(@Valid CustomersCreateReqVO createReqVO);

    /**
     * 更新客户基础信息
     *
     * @param updateReqVO 更新信息
     */
    void updateCustomers(@Valid CustomersUpdateReqVO updateReqVO);

    /**
     * 删除客户基础信息
     *
     * @param id 编号
     */
    void deleteCustomers(Long id);

    /**
     * 获得客户基础信息
     *
     * @param id 编号
     * @return 客户基础信息
     */
    CustomersDO getCustomers(Long id);

    /**
     * 获得客户基础信息列表
     *
     * @param ids 编号
     * @return 客户基础信息列表
     */
    List<CustomersDO> getCustomersList(Collection<Long> ids);

    /**
     * 获得客户基础信息分页
     *
     * @param pageReqVO 分页查询
     * @return 客户基础信息分页
     */
    PageResult<CustomersDO> getCustomersPage(CustomersPageReqVO pageReqVO);

    /**
     * 获得客户基础信息列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 客户基础信息列表
     */
    List<CustomersDO> getCustomersList(CustomersExportReqVO exportReqVO);

    List<CustomersSimpleVO> getSimpleCustomers();
}
