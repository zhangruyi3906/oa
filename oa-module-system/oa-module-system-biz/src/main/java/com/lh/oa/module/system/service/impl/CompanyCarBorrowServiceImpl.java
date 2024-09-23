package com.lh.oa.module.system.service.impl;


import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.security.core.util.SecurityFrameworkUtils;
import com.lh.oa.module.system.api.companyCar.CompanyCar;
import com.lh.oa.module.system.api.companyCarBorrow.CompanyCarBorrow;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowDate;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowQuery;
import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowRes;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.mapper.CompanyCarBorrowMapper;
import com.lh.oa.module.system.mapper.CompanyCarMapper;
import com.lh.oa.module.system.service.user.AdminUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-05
 */
@Service
public class CompanyCarBorrowServiceImpl implements ICompanyCarBorrowService {

    @Resource
    private CompanyCarBorrowMapper companyCarBorrowMapper;

    @Resource
    private CompanyCarMapper companyCarMapper;
    @Resource
    private AdminUserService adminUserService;

    @Override
    public List<CompanyCarBorrowRes> selectList() {
        return companyCarBorrowMapper.selectCompanyCarBorrowList();
    }

    @Override
    public int updateById(CompanyCarBorrow companyCarBorrow) {
        validateCarExists(companyCarBorrow.getId());
        return companyCarBorrowMapper.updateById(companyCarBorrow);
    }

    @Override
    public int insert(CompanyCarBorrow companyCarBorrow) {
        Long loginUserId = getLoginUserId();
        if("".equals(loginUserId) || loginUserId == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_LOGIN);
        }
        AdminUserDO user = adminUserService.getUser(loginUserId);
        companyCarBorrow.setUserId(user.getId());
        companyCarBorrow.setUsername(user.getNickname());
        companyCarBorrow.setOpinion(0L);

        return companyCarBorrowMapper.insert(companyCarBorrow);
    }

    @Override
    public int deleteById(Long id) {
        validateCarExists(id);
        return companyCarBorrowMapper.deleteById(id);
    }

    @Override
    public CompanyCarBorrowRes selectById(Long id) {
        return companyCarBorrowMapper.selectCompanyCarBorrow(id);
    }

    @Override
    public PageResult<CompanyCarBorrowRes> selectPage(CompanyCarBorrowQuery query) {
        PageResult<CompanyCarBorrow> companyCarBorrowPageResult = companyCarBorrowMapper.selectPage(query);
        List<CompanyCarBorrow> list = companyCarBorrowPageResult.getList();
        if(list.isEmpty()){
            return null;
        }
        List<CompanyCarBorrowRes> companyCarBorrowResList = new ArrayList<>();
        for (CompanyCarBorrow companyCarBorrow : list) {
            CompanyCarBorrowRes companyCarBorrowRes = new CompanyCarBorrowRes();
            BeanUtils.copyProperties(companyCarBorrow, companyCarBorrowRes);
            companyCarBorrowRes.setCompanyCar(companyCarMapper.selectById(companyCarBorrow.getCarId()));
            companyCarBorrowResList.add(companyCarBorrowRes);
        }

        return new PageResult<CompanyCarBorrowRes>(companyCarBorrowResList, companyCarBorrowPageResult.getTotal());
    }

    @Override
    public void audit(CompanyCarBorrow companyCarBorrow) {
        CompanyCarBorrowRes companyCarBorrowRes = this.selectById(companyCarBorrow.getId());
        Long opinion = companyCarBorrowRes.getOpinion();
        if (opinion != 0){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CARBOOROW_NOT_EXISTS);
        }
        Long loginUserId = getLoginUserId();
        AdminUserDO user = adminUserService.getUser(loginUserId);
        companyCarBorrow.setCarId(companyCarBorrowRes.getCarId());
        companyCarBorrow.setUserId(companyCarBorrowRes.getUserId());
        companyCarBorrow.setUsername(companyCarBorrowRes.getUsername());
        companyCarBorrow.setBorrowedTime(companyCarBorrowRes.getBorrowedTime());
        companyCarBorrow.setReturnTime(companyCarBorrowRes.getReturnTime());
        companyCarBorrow.setBorrowReason(companyCarBorrowRes.getBorrowReason());
        companyCarBorrow.setApproverId(loginUserId);
        companyCarBorrow.setApproverName(user.getNickname());
        updateById(companyCarBorrow);
    }

    @Override
    public List<CompanyCarBorrowRes> selectFreeList(CompanyCarBorrowDate companyCarBorrowDate) {
        List<CompanyCarBorrow> companyCarBorrows = companyCarBorrowMapper.selectFreeList(companyCarBorrowDate);
        if(companyCarBorrows.isEmpty()){
            return null;
        }
        List<CompanyCarBorrowRes> companyCarBorrowResList = new ArrayList<>();
        for (CompanyCarBorrow companyCarBorrow : companyCarBorrows) {
            CompanyCarBorrowRes companyCarBorrowRes = new CompanyCarBorrowRes();
            BeanUtils.copyProperties(companyCarBorrow, companyCarBorrowRes);
            companyCarBorrowRes.setCompanyCar(companyCarMapper.selectById(companyCarBorrow.getCarId()));
            companyCarBorrowResList.add(companyCarBorrowRes);
        }
        return companyCarBorrowResList;
    }

    @Override
    public CompanyCarBorrow getByProcessInstanceId(String processInstanceId) {
        return companyCarBorrowMapper.selectOne(CompanyCarBorrow::getProcessInstanceId, processInstanceId);
    }

    @Override
    public void returnCar(Long id) {

        CompanyCarBorrow companyCarBorrow = companyCarBorrowMapper.selectById(id);
        Long userId = companyCarBorrow.getUserId();
        Long opinion = companyCarBorrow.getOpinion();
        Long carId = companyCarBorrow.getCarId();
        if (userId != SecurityFrameworkUtils.getLoginUserId()) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USERID_IS_ERROR);
        }
        if(opinion != 1){
            throw new RuntimeException();
        }
        CompanyCar companyCar = companyCarMapper.selectById(carId);
        Integer status = companyCar.getStatus();
        if(status != 1){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CAR_IS_RETURN);
        }
        companyCarBorrow.setReturnTime(new Date());
        companyCarBorrowMapper.updateById(companyCarBorrow);
        companyCar.setStatus(0);
        companyCarMapper.updateById(companyCar);
    }


    private void validateCarExists(Long id) {
        if (companyCarBorrowMapper.selectCompanyCarBorrow(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CAR_NOT_EXISTS);
        }
    }
}
