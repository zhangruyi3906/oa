package com.lh.oa.module.bpm.framework.flowable.core.listener;

import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRespVO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmTaskExtDO;
import com.lh.oa.module.bpm.dal.mysql.task.BpmTaskExtMapper;
import com.lh.oa.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceStatusEnum;
import com.lh.oa.module.bpm.framework.rpc.config.SpringContextHolder;
import com.lh.oa.module.bpm.service.task.BpmProcessInstanceService;
import com.lh.oa.module.bpm.service.task.BpmTaskService;
import com.lh.oa.module.system.api.companyCar.CompanyCar;
import com.lh.oa.module.system.api.companyCar.CompanyCarService;
import com.lh.oa.module.system.api.companyCarBorrow.CompanyCarBorrow;
import com.lh.oa.module.system.api.companyCarBorrow.CompanyCarBorrowService;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

public class CarBorrowResultListener implements JavaDelegate {

    private CompanyCarBorrowService companyCarBorrowService = SpringContextHolder.getBean(CompanyCarBorrowService.class);

    private AdminUserApi adminUserApi = SpringContextHolder.getBean(AdminUserApi.class);
    private BpmProcessInstanceService bpmProcessInstanceService = SpringContextHolder.getBean(BpmProcessInstanceService.class);
    private CompanyCarService companyCarService = SpringContextHolder.getBean(CompanyCarService.class);
    private BpmTaskExtMapper bpmTaskExtMapper = SpringContextHolder.getBean(BpmTaskExtMapper.class);
    //必须要
    @Autowired
    private RuntimeService runtimeservice;
    private HistoryService historyService = SpringContextHolder.getBean(HistoryService.class);

    @Override
    public void execute(DelegateExecution execution) {
        String currentActivityId = execution.getCurrentActivityId();
        String processInstanceId = execution.getProcessInstanceId();
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(currentActivityId)
                .singleResult();

        BpmTaskExtDO bpmTaskExtDO = bpmTaskExtMapper.selectByTaskId(historicTaskInstance.getId());
        Integer result = bpmTaskExtDO.getResult();
        CompanyCarBorrow companyCarBorrow = this.getCompanyCarBorrowByProcessInstanceId(processInstanceId);
        if (BpmProcessInstanceResultEnum.APPROVE.getResult() == result) {
            companyCarBorrow.setOpinion(1L);
            companyCarBorrowService.saveOrUpdate(companyCarBorrow);
        } else {
            companyCarBorrow.setOpinion(2L);
            companyCarBorrowService.saveOrUpdate(companyCarBorrow);
            Long carId = companyCarBorrow.getCarId();
            CompanyCar car = companyCarService.getById(carId);
            if (ObjectUtils.isNotEmpty(car)) {
                car.setStatus(0);
                if (car.getStatus() != 1) {
                    throw ServiceExceptionUtil.exception(ErrorCodeConstants.CAR_IS_RETURN);
                }
            }
            companyCarService.saveOrUpdate(car);
        }
    }

    private CompanyCarBorrow getCompanyCarBorrowByProcessInstanceId(String processInstanceId) {
        CompanyCarBorrow companyCarBorrow = companyCarBorrowService.getByProcessInstanceId(processInstanceId);
        ExceptionThrowUtils.throwIfNull(companyCarBorrow, ErrorCodeConstants.CARBOOROW_NOT_EXISTS);
        Long opinion = companyCarBorrow.getOpinion();
        if (opinion != 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CAR_BORROW_IS_NOT_PENDING);
        }
        return companyCarBorrow;
    }
}
