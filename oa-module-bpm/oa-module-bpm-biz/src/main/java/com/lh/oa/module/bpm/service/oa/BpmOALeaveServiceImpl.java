package com.lh.oa.module.bpm.service.oa;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceApi;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.controller.admin.oa.vo.BpmOALeaveCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.oa.vo.BpmOALeavePageReqVO;
import com.lh.oa.module.bpm.convert.oa.BpmOALeaveConvert;
import com.lh.oa.module.bpm.dal.dataobject.oa.BpmOALeaveDO;
import com.lh.oa.module.bpm.dal.mysql.oa.BpmOALeaveMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.system.api.holidayInfo.HolidayInfoApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.OA_LEAVE_NOT_EXISTS;

/**
 * OA 请假申请 Service 实现类
 *
 * @author jason
 * @author
 */
@Service
@Validated
public class BpmOALeaveServiceImpl implements BpmOALeaveService {

    /**
     * OA 请假对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "oa_leave";

    @Resource
    private BpmOALeaveMapper leaveMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Resource
    private HolidayInfoApi holidayInfoApi;

    public double calculateLeaveDays(LocalDateTime startTime, LocalDateTime endTime) {

        LocalDateTime workStartTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(9, 0));

        LocalDateTime workEndTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(18, 0));

        double leaveDays = 0;
        int i = endTime.toLocalDate().compareTo(startTime.toLocalDate());

        for (int j = 0; j <= i; j++) {
            if (startTime.isAfter(workStartTime)) {
                workStartTime = startTime;
            }
            if (endTime.isBefore(workEndTime)) {
                workEndTime = endTime;
            }
            int formattedDate = workStartTime.getYear() * 10000 + workStartTime.getMonthValue() * 100 + workStartTime.getDayOfMonth();
            Integer work = holidayInfoApi.getWork(formattedDate).getData();
            if (work == 1) {
                Duration duration = Duration.between(workStartTime, workEndTime);
                double leaveHours = duration.toHours();
                if(leaveHours <0){
                    break;
                }
                leaveDays += leaveHours / 9;
            } else {
                leaveDays += 0;
            }
            workStartTime = workEndTime.plusHours(15);
            workEndTime = workEndTime.plusHours(24);
        }

        BigDecimal two = BigDecimal.valueOf(leaveDays);
        return two.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createLeave(Long userId, BpmOALeaveCreateReqVO createReqVO) {
        //请假开始时间和结束时间计算天数
        createReqVO.setDays(calculateLeaveDays(createReqVO.getStartTime(), createReqVO.getEndTime()));
        BpmOALeaveDO leave = BpmOALeaveConvert.INSTANCE.convert(createReqVO)
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        leaveMapper.insert(leave);

        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("days", createReqVO.getDays());
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(leave.getId())));

        leaveMapper.updateById(new BpmOALeaveDO().setId(leave.getId()).setProcessInstanceId(processInstanceId));
        return leave.getId();
    }

    @Override
    public void updateLeaveResult(Long id, Integer result) {
        validateLeaveExists(id);
        leaveMapper.updateById(new BpmOALeaveDO().setId(id).setResult(result));
    }

    private void validateLeaveExists(Long id) {
        if (leaveMapper.selectById(id) == null) {
            throw exception(OA_LEAVE_NOT_EXISTS);
        }
    }

    @Override
    public BpmOALeaveDO getLeave(Long id) {
        return leaveMapper.selectById(id);
    }

    @Override
    public PageResult<BpmOALeaveDO> getLeavePage(Long userId, BpmOALeavePageReqVO pageReqVO) {
        return leaveMapper.selectPage(userId, pageReqVO);
    }

}
