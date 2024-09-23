package com.lh.oa.module.system.mapper;



import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.api.companyCarBorrow.CompanyCarBorrow;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowDate;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowQuery;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2023-09-04
 */
@Mapper
public interface CompanyCarBorrowMapper extends BaseMapperX<CompanyCarBorrow> {

    List<CompanyCarBorrowRes> selectCompanyCarBorrowList();
    CompanyCarBorrowRes selectCompanyCarBorrow(Long id);
    default PageResult<CompanyCarBorrow> selectPage(CompanyCarBorrowQuery query){
        PageResult<CompanyCarBorrow> companyCarBorrowPageResult = selectPage(query, new LambdaQueryWrapperX<CompanyCarBorrow>()
                .eqIfPresent(CompanyCarBorrow::getOpinion, 1)
                .eqIfPresent(CompanyCarBorrow::getApproverName, query.getApproverName())
                .orderByDesc(CompanyCarBorrow::getId));
        return companyCarBorrowPageResult;
    };

    default List<CompanyCarBorrow> selectFreeList(CompanyCarBorrowDate companyCarBorrowDate){
        Date borrowedTime = companyCarBorrowDate.getBorrowedTime();
        Date expectTime = companyCarBorrowDate.getExpectTime();
        LambdaQueryWrapperX<CompanyCarBorrow> wrapper = new LambdaQueryWrapperX<>();
        wrapper.or(qw -> qw.lt(CompanyCarBorrow::getBorrowedTime, borrowedTime).or().gt(CompanyCarBorrow::getExpectTime, borrowedTime));
        wrapper.or(qw -> qw.lt(CompanyCarBorrow::getBorrowedTime, expectTime).or().gt(CompanyCarBorrow::getExpectTime, expectTime));
        wrapper.or(qw -> qw.eq(CompanyCarBorrow::getOpinion, 2));
       return selectList(wrapper);
    }
}
