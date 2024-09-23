package com.lh.oa.module.system.service.impl;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.api.companyCar.CompanyCar;
import com.lh.oa.module.system.dal.dataobject.companyCar.CompanyCarQuery;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowDate;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowRes;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.mapper.CompanyCarMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-05
 */
@Service
public class CompanyCarServiceImpl implements ICompanyCarService {
    @Resource
    private CompanyCarMapper companyCarMapper;

    @Resource
    private ICompanyCarBorrowService companyCarBorrowService;

    @Override
    public List<CompanyCar> selectFreeList() {
        return companyCarMapper.free();
    }

    @Override
    public int updateById(CompanyCar companyCar) {
        validateCarExists(companyCar.getId());
        int i = companyCarMapper.updateById(companyCar);
        return i;
    }

    @Override
    public int insert(CompanyCar companyCar) {
        CompanyCar car = companyCarMapper.selectOne("plate_number", companyCar.getPlateNumber());
        if (ObjectUtils.isNotEmpty(car)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CAR_PLATE_NUMBER_EXIST);
        }
        Integer status = companyCar.getStatus();
        if (status == null) {
            companyCar.setStatus(0);
        }
        return companyCarMapper.insert(companyCar);
    }

    @Override
    public int deleteById(Long id) {
        validateCarExists(id);
        CompanyCar car = this.selectById(id);
        if (car.getStatus() == 1) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CAR_STATUS_NOT_FREE);
        }
        return companyCarMapper.deleteById(id);
    }

    @Override
    public CompanyCar selectById(Long id) {
        return companyCarMapper.selectById(id);
    }

    @Override
    public PageResult<CompanyCar> selectPage(CompanyCarQuery query) {
        return companyCarMapper.selectPage(query);
    }

    @Override
    public List<CompanyCar> free() {
        return companyCarMapper.free();
    }

    @Override
    public List<CompanyCarBorrowRes> getFreeList(CompanyCarBorrowDate companyCarBorrowDate) {
        List<CompanyCarBorrowRes> companyCarBorrowRes = companyCarBorrowService.selectFreeList(companyCarBorrowDate);
        return companyCarBorrowRes;
    }

    @Override
    public CompanyCar getByPlateNumber(String plateNumber) {
        return companyCarMapper.getByPlateNumber(plateNumber);
    }


    private void validateCarExists(Long id) {
        if (companyCarMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CAR_NOT_EXISTS);
        }
    }
}
