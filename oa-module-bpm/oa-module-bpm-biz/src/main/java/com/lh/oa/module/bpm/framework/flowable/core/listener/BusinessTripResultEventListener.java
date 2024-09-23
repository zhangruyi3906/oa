package com.lh.oa.module.bpm.framework.flowable.core.listener;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmTaskExtDO;
import com.lh.oa.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.lh.oa.module.bpm.dal.mysql.task.BpmTaskExtMapper;
import com.lh.oa.module.bpm.enums.ErrorCodeConstants;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.lh.oa.module.bpm.service.task.BpmTaskServiceImpl;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.UserAndInformationDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.engine.HistoryService;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class BusinessTripResultEventListener extends BpmProcessInstanceResultEventListener {

    public static final String PROCESS_KEY = "chuchai";
    @Resource
    private HistoryService historyService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private BpmProcessInstanceExtMapper bpmProcessInstanceExtMapper;
    @Resource
    private BpmTaskExtMapper bpmTaskExtMapper;

    @Override
    protected String getProcessDefinitionKey() {
        return PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        log.info("出差流程实例结果事件，流程实例编号：{}，流程实例结果：{}", event.getId(), event.getResult());
        if (ObjectUtils.notEqual(BpmProcessInstanceResultEnum.APPROVE.getResult(), event.getResult())) {
            return;
        }
        String processInstanceId = event.getId();
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(BpmTaskServiceImpl.Business_Trip_Start_TASK_DEFINITION_KEY)
                .orderByHistoricTaskInstanceStartTime()
                .desc()
                .list();
        if (CollectionUtils.isEmpty(historicTaskInstanceList)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.TASK_NOT_EXIST);
        }
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(processInstanceId);
        if (ObjectUtils.isEmpty(bpmProcessInstanceExtDO)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PROCESS_INSTANCE_NOT_EXISTS);
        }
        Map<String, Object> formVariables = bpmProcessInstanceExtDO.getFormVariables();
        if (MapUtils.isEmpty(formVariables)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.TASK_NOT_EXIST);
        }
        if (historicTaskInstanceList.size() > 1) {
            List<HistoricTaskInstance> historicTaskList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricTaskInstanceStartTime()
                    .desc()
                    .list();
            // 获取发起人提交任务标识
            HistoricTaskInstance firstTask = historicTaskList.get(historicTaskList.size() - 1);
            String firstTaskKey = firstTask.getTaskDefinitionKey();
            // 获取所有发起人提交任务列表
            List<HistoricTaskInstance> firstTaskList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .taskDefinitionKey(firstTaskKey)
                    .orderByHistoricTaskInstanceStartTime()
                    .desc()
                    .list();
            if (firstTaskList.size() < 2) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_IS_CHANGE);
            }
            BpmTaskExtDO bpmTaskExtDO = bpmTaskExtMapper.selectByTaskId(firstTaskList.get(0).getId());
            if (ObjectUtils.isEmpty(bpmTaskExtDO)) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.TASK_NOT_EXIST);
            }
            formVariables = bpmTaskExtDO.getFormVariables();
        }
        //流程结束根据出差结束时间是否关闭异地打卡
        turnOffOffSiteClockingForBusinessTrips(formVariables);
    }

    private void turnOffOffSiteClockingForBusinessTrips(Map<String, Object> formVariables) {
        Object endTime = formVariables.get("end_time");
        Object businessUserId = formVariables.get("userId");
        LocalDateTime endDateTime;
        if (Objects.nonNull(businessUserId)) {
            //如果没填结束时间直接关闭
            if (Objects.nonNull(endTime)) {
                long end = Long.parseLong(endTime.toString()) * 1000;
                endDateTime = Instant.ofEpochMilli(end).atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime now = LocalDateTime.now();
                if (endDateTime.isAfter(now)) {
                    return;
                }
            }
            CommonResult<UserAndInformationDTO> commonResult = adminUserApi.getUserAndInformation(Long.parseLong(businessUserId.toString()));
            UserAndInformationDTO userAndInformationDTO = commonResult.getData();
            if (Objects.nonNull(userAndInformationDTO)) {
                Boolean isOffsiteAttendance = userAndInformationDTO.getIsOffsiteAttendance();
                if (isOffsiteAttendance) {
                    userAndInformationDTO.setId(Long.parseLong(businessUserId.toString()));
                    userAndInformationDTO.setIsOffsiteAttendance(false);
                    adminUserApi.updateUserAndInformation(userAndInformationDTO);
                }
            }
        }
    }
}

