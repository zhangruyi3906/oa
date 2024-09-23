package com.lh.oa.module.system.service.customerservice;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.system.controller.admin.customerservice.vo.*;
import com.lh.oa.module.system.dal.dataobject.customerservice.CustomerServiceDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServiceCreateReqVO;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServiceExportReqVO;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServicePageReqVO;
import com.lh.oa.module.system.controller.admin.customerservice.vo.CustomerServiceUpdateReqVO;

/**
 * 客户服务 Service 接口
 *
 * @author 管理员
 */
public interface CustomerServiceService {

    /**
     * 创建客户服务
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCustomerService(@Valid CustomerServiceCreateReqVO createReqVO);

    /**
     * 更新客户服务
     *
     * @param updateReqVO 更新信息
     */
    void updateCustomerService(@Valid CustomerServiceUpdateReqVO updateReqVO);

    /**
     * 删除客户服务
     *
     * @param id 编号
     */
    void deleteCustomerService(Long id);

    /**
     * 获得客户服务
     *
     * @param id 编号
     * @return 客户服务
     */
    CustomerServiceDO getCustomerService(Long id);

    /**
     * 获得客户服务列表
     *
     * @param ids 编号
     * @return 客户服务列表
     */
    List<CustomerServiceDO> getCustomerServiceList(Collection<Long> ids);

    /**
     * 获得客户服务分页
     *
     * @param pageReqVO 分页查询
     * @return 客户服务分页
     */
    PageResult<CustomerServiceDO> getCustomerServicePage(CustomerServicePageReqVO pageReqVO);

    /**
     * 获得客户服务列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 客户服务列表
     */
    List<CustomerServiceDO> getCustomerServiceList(CustomerServiceExportReqVO exportReqVO);

}
