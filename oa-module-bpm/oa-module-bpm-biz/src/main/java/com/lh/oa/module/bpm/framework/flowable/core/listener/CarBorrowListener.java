package com.lh.oa.module.bpm.framework.flowable.core.listener;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.bpm.framework.rpc.config.SpringContextHolder;
import com.lh.oa.module.system.api.companyCar.CompanyCar;
import com.lh.oa.module.system.api.companyCar.CompanyCarService;
import com.lh.oa.module.system.api.companyCarBorrow.CompanyCarBorrow;
import com.lh.oa.module.system.api.companyCarBorrow.CompanyCarBorrowService;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Slf4j
public class CarBorrowListener implements JavaDelegate {

    private CompanyCarBorrowService companyCarBorrowService = SpringContextHolder.getBean(CompanyCarBorrowService.class);

    private AdminUserApi adminUserApi = SpringContextHolder.getBean(AdminUserApi.class);

    private CompanyCarService companyCarService = SpringContextHolder.getBean(CompanyCarService.class);
    //必须要
    @Autowired
    private RuntimeService runtimeservice;

    @Override
    public void execute(DelegateExecution execution) {
        log.info("车辆借用创建监听，流程实例Id:{}", execution.getProcessInstanceId());
        CompanyCarBorrow companyCarBorrow = new CompanyCarBorrow();
        String borrowCarType = execution.getVariableInstances().get("borrowCarType").getTextValue();
        if ("1".equals(borrowCarType)) {
            String date = execution.getVariableInstances().get("date-range").getTextValue();
            Date startDate = null;
            Date expectDate = null;;
            if (ObjectUtils.isNotEmpty(date)) {
                String[] time = date.split(",");
                long start = Long.parseLong(time[0]) * 1000;
                long end = Long.parseLong(time[1]) * 1000;
                startDate = new Date(start);
                expectDate = new Date(end);
            }
            VariableInstance carCode = execution.getVariableInstances().get("carCode");
            if (ObjectUtils.isEmpty(carCode)) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.CAR_CODE_NOT_EXISTS);
            }
            String plateNumber = carCode.getTextValue();
            CompanyCar car = companyCarService.getByPlateNumber(plateNumber);
            if (ObjectUtils.isEmpty(car)) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.CAR_NOT_EXISTS);
            }
            if(car.getStatus() != 0){
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.CAR_BORROW_IS_EXISTS);
            }
            car.setStatus(1);
            companyCarService.saveOrUpdate(car);
            String borrowReason = execution.getVariableInstances().get("borrowReason").getTextValue();
            Long userId = execution.getVariableInstances().get("userId").getLongValue();

            companyCarBorrow.setCarId(car.getId());
            companyCarBorrow.setBorrowedTime(startDate);
            companyCarBorrow.setExpectTime(expectDate);
            companyCarBorrow.setBorrowReason(borrowReason);
            companyCarBorrow.setOpinion(0L);
            companyCarBorrow.setUserId(userId);
            CommonResult<AdminUserRespDTO> user = adminUserApi.getUser(userId);
            String nickname = user.getData().getNickname();
            companyCarBorrow.setUsername(nickname);
            companyCarBorrow.setProcessInstanceId(execution.getProcessInstanceId());
            companyCarBorrowService.saveOrUpdate(companyCarBorrow);
        }
    }
}
