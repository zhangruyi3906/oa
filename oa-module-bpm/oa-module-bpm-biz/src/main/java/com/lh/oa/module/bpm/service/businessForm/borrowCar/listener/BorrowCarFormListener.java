package com.lh.oa.module.bpm.service.businessForm.borrowCar.listener;

import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRespVO;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceStatusEnum;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.bpm.service.businessForm.borrowCar.BorrowCarFormService;
import com.lh.oa.module.bpm.service.businessForm.borrowCar.BorrowCarFormServiceImpl;
import com.lh.oa.module.bpm.service.task.BpmProcessInstanceService;
import com.lh.oa.module.system.api.companyCar.CompanyCar;
import com.lh.oa.module.system.api.companyCar.CompanyCarService;
import com.lh.oa.module.system.api.companyCarBorrow.CompanyCarBorrow;
import com.lh.oa.module.system.api.companyCarBorrow.CompanyCarBorrowService;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tanghanlin
 * @since 2023/10/23
 */
@Component
public class BorrowCarFormListener extends BpmProcessInstanceResultEventListener {

    @Resource
    private BorrowCarFormService service;
    @Resource
    private BpmProcessInstanceService bpmProcessInstanceService;
    @Resource
    private CompanyCarBorrowService companyCarBorrowService;
    @Resource
    private CompanyCarService companyCarService;

    @Override
    protected String getProcessDefinitionKey() {
        return BorrowCarFormServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        if (event.getBusinessKey() == null) {
            return;
        }
        service.updateResult(Long.parseLong(event.getBusinessKey()), event.getResult());

    }
}
