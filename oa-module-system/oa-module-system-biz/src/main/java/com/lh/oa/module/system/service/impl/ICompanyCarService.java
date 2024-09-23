package com.lh.oa.module.system.service.impl;


import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.api.companyCar.CompanyCar;
import com.lh.oa.module.system.dal.dataobject.companyCar.CompanyCarQuery;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowDate;
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
public interface ICompanyCarService {

    List<CompanyCar> selectFreeList();

    int updateById(CompanyCar companyCar);

    int insert(CompanyCar companyCar);

    int deleteById(Long id);

    CompanyCar selectById(Long id);


    PageResult<CompanyCar> selectPage(CompanyCarQuery query);

    List<CompanyCar> free();

    List<CompanyCarBorrowRes> getFreeList(CompanyCarBorrowDate companyCarBorrowDate);


    CompanyCar getByPlateNumber(String plateNumber);
}
