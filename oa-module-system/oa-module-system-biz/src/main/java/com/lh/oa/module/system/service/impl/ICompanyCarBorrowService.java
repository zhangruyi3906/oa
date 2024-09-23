package com.lh.oa.module.system.service.impl;


import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.api.companyCarBorrow.CompanyCarBorrow;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowDate;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowQuery;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowRes;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-05
 */
public interface ICompanyCarBorrowService {

    List<CompanyCarBorrowRes> selectList();

    int updateById(CompanyCarBorrow companyCarBorrow);

    int insert(CompanyCarBorrow companyCarBorrow);

    int deleteById(Long id);

    CompanyCarBorrowRes selectById(Long id);


    PageResult<CompanyCarBorrowRes> selectPage(CompanyCarBorrowQuery query);

    void audit(CompanyCarBorrow companyCarBorrow);

    List<CompanyCarBorrowRes> selectFreeList(CompanyCarBorrowDate companyCarBorrowDate);

    CompanyCarBorrow getByProcessInstanceId(String processInstanceId);

    void returnCar(Long id);
}
