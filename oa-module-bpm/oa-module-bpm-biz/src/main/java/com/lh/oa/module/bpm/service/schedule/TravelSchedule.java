package com.lh.oa.module.bpm.service.schedule;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmTaskExtDO;
import com.lh.oa.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.lh.oa.module.bpm.dal.mysql.task.BpmTaskExtMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.framework.flowable.core.listener.BusinessTripResultEventListener;
import com.lh.oa.module.bpm.service.task.BpmTaskServiceImpl;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.UserAndInformationDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@Slf4j
public class TravelSchedule {
    @Resource
    private HistoryService historyService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private BpmProcessInstanceExtMapper bpmProcessInstanceExtMapper;
    @Resource
    private BpmTaskExtMapper bpmTaskExtMapper;
    /**
     * 出差流程发起人提交节点
     */
    public static final String BUSINESS_TRIP_USER_START_TASK_DEFINITION_KEY = "Activity_0bahdv3";

    /**
     * 每天凌晨0点检测是否开启异地打卡
     */
    @Scheduled(cron = "2 0 0 * * ?")
    public void execute() {
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(BusinessTripResultEventListener.PROCESS_KEY)
                .orderByProcessInstanceStartTime()
                .asc()
                .list();
        //所有出差流程的流程Id
        Set<String> allHistoryProcessInstanceIdSet = historicProcessInstanceList.stream()
                .map(HistoricProcessInstance::getId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(allHistoryProcessInstanceIdSet)) {
            return;
        }
        List<String> allHistoryProcessInstanceIdList = allHistoryProcessInstanceIdSet.stream().collect(Collectors.toList());
        //所有的出差流程中上级审批的任务
        List<HistoricTaskInstance> allLeaderTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey(BusinessTripResultEventListener.PROCESS_KEY)
                .taskDefinitionKey(BpmTaskServiceImpl.Business_Trip_Start_TASK_DEFINITION_KEY)
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();
        //所有的出差流程中发起人提交的任务
        List<HistoricTaskInstance> allStartTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey(BusinessTripResultEventListener.PROCESS_KEY)
                .taskDefinitionKey(BUSINESS_TRIP_USER_START_TASK_DEFINITION_KEY)
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();

        List<BpmProcessInstanceExtDO> allBpmProcessInstanceExtDOList = bpmProcessInstanceExtMapper.selectListByProcessInstanceIds(allHistoryProcessInstanceIdList);
        Map<String, BpmProcessInstanceExtDO> processInstanceIdToProcessInstanceExtDOMap = allBpmProcessInstanceExtDOList.stream().collect(Collectors.toMap(BpmProcessInstanceExtDO::getProcessInstanceId, item -> item));
        //流程id -> 上级审批的任务集合
        Map<String, List<HistoricTaskInstance>> instanceIdToLeaderHistoricTaskInstanceList = allLeaderTaskInstanceList.stream()
                .sorted(Comparator.comparing(HistoricTaskInstance::getStartTime))
                .collect(Collectors.groupingBy(HistoricTaskInstance::getProcessInstanceId));

        //流程id -> 发起人提交的任务集合
        Map<String, List<HistoricTaskInstance>> instanceIdToStartHistoricTaskInstanceList = allStartTaskInstanceList.stream()
                .sorted(Comparator.comparing(HistoricTaskInstance::getStartTime))
                .collect(Collectors.groupingBy(HistoricTaskInstance::getProcessInstanceId));

        //所有上级审批任务id集合
        List<String> allLeaderTaskIdList = allLeaderTaskInstanceList.stream().map(HistoricTaskInstance::getId).collect(Collectors.toList());
        //所有发起人提交任务id集合
        List<String> allStartTaskIdList = allStartTaskInstanceList.stream().map(HistoricTaskInstance::getId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(allLeaderTaskIdList)) {
            return;
        }
        if (CollectionUtils.isEmpty(allStartTaskIdList)) {
            return;
        }
        allStartTaskIdList.addAll(allLeaderTaskIdList);
        //所有发起人提交和领导审批的节点
        List<BpmTaskExtDO> bpmTaskExtDOList = bpmTaskExtMapper.selectListByTaskIds(allStartTaskIdList);
        Map<String, BpmTaskExtDO> taskIdToTaskExtDOMap = bpmTaskExtDOList.stream().collect(Collectors.toMap(BpmTaskExtDO::getTaskId, item -> item));

        for (String historyProcessInstanceId : allHistoryProcessInstanceIdList) {
            //所有上级审批任务
            List<HistoricTaskInstance> leaderHistoricTaskInstanceList = instanceIdToLeaderHistoricTaskInstanceList.get(historyProcessInstanceId);
            if (CollectionUtils.isEmpty(leaderHistoricTaskInstanceList)) {
                continue;
            }
            HistoricTaskInstance historicTaskInstance = leaderHistoricTaskInstanceList.get(leaderHistoricTaskInstanceList.size() - 1);
            BpmTaskExtDO leaderBpmTaskExtDO = taskIdToTaskExtDOMap.get(historicTaskInstance.getId());
            if (ObjectUtils.isEmpty(leaderBpmTaskExtDO)) {
                log.info("上级领导节点拓展表为空, historyProcessInstanceId: {}, taskId: {}", historyProcessInstanceId, historicTaskInstance.getId());
                continue;
            }
            //只有最后一个上级审批节点为通过，才进行处理
            if (ObjectUtils.notEqual(leaderBpmTaskExtDO.getResult(), BpmProcessInstanceResultEnum.APPROVE.getResult())) {
                continue;
            }
            List<HistoricTaskInstance> startHistoricTaskInstanceList = instanceIdToStartHistoricTaskInstanceList.get(historyProcessInstanceId);
            if (CollectionUtils.isEmpty(startHistoricTaskInstanceList)) {
                continue;
            }
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = processInstanceIdToProcessInstanceExtDOMap.get(historyProcessInstanceId);
            if (MapUtils.isEmpty(bpmProcessInstanceExtDO.getFormVariables())) {
                log.error("流程拓展表数据有问题 processInstanceId: {}, formVariables: {}", historyProcessInstanceId, bpmProcessInstanceExtDO.getFormVariables());
                continue;
            }
            Map<String, Object> formVariables = bpmProcessInstanceExtDO.getFormVariables();
            if (startHistoricTaskInstanceList.size() > 1) {
                HistoricTaskInstance endTaskInstance = startHistoricTaskInstanceList.get(startHistoricTaskInstanceList.size() - 1);
                BpmTaskExtDO bpmTaskExtDO = taskIdToTaskExtDOMap.get(endTaskInstance.getId());
                if (!ObjectUtils.notEqual(bpmTaskExtDO.getResult(), BpmProcessInstanceResultEnum.APPROVE.getResult())) {
                    formVariables = bpmTaskExtDO.getFormVariables();
                    if (MapUtils.isEmpty(formVariables)) {
                        log.error("任务拓展表数据有问题 processInstanceId: {},taskId: {}, formVariables: {}", historyProcessInstanceId, endTaskInstance, formVariables);
                        continue;
                    }
                }
            }
            turnOnOffSiteClockingForBusinessTrips(formVariables);
        }
    }


    /**
     * 每天凌晨0点检测是否关闭异地打卡
     */
    @Scheduled(cron = "3 0 0 * * ?")
    public void executeOffOffSiteClocking() {
        //所有结束的出差流程
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery()
                .finished()
                .processDefinitionKey(BusinessTripResultEventListener.PROCESS_KEY)
                .orderByProcessInstanceStartTime()
                .asc()
                .list();
        //所有结束的流程Id
        Set<String> finishHistoryProcessInstanceIdSet = historicProcessInstanceList.stream()
                .filter(item -> ObjectUtils.isNotEmpty(item.getEndTime()))
                .map(HistoricProcessInstance::getId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(finishHistoryProcessInstanceIdSet)) {
            return;
        }
        List<String> finishHistoryProcessInstanceIdList = finishHistoryProcessInstanceIdSet.stream().collect(Collectors.toList());
        List<BpmProcessInstanceExtDO> finishBpmProcessInstanceExtDOList = bpmProcessInstanceExtMapper.selectListByProcessInstanceIds(finishHistoryProcessInstanceIdList);
        //所有已通过的流程实例
        List<BpmProcessInstanceExtDO> approveBpmProcessInstanceExtDOList = finishBpmProcessInstanceExtDOList.stream()
                .filter(item -> !ObjectUtils.notEqual(item.getResult(), BpmProcessInstanceResultEnum.APPROVE.getResult()))
                .collect(Collectors.toList());
        //流程id -> 对应流程实例
        Map<String, BpmProcessInstanceExtDO> processInstanceIdToApproveProcessInstanceExtDOMap = approveBpmProcessInstanceExtDOList.stream().collect(Collectors.toMap(BpmProcessInstanceExtDO::getProcessInstanceId, item -> item));
        Set<String> approveBpmProcessInstanceIdSet = approveBpmProcessInstanceExtDOList.stream().map(BpmProcessInstanceExtDO::getProcessInstanceId).collect(Collectors.toSet());
        //所有已完成的出差流程中发起人提交的任务
        List<HistoricTaskInstance> allApproveStartTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceIdIn(approveBpmProcessInstanceIdSet)
                .processDefinitionKey(BusinessTripResultEventListener.PROCESS_KEY)
                .taskDefinitionKey(BUSINESS_TRIP_USER_START_TASK_DEFINITION_KEY)
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();
        Set<String> approveTaskIdSet = allApproveStartTaskInstanceList.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet());
        //所有发起人提交的任务
        List<BpmTaskExtDO> bpmTaskExtDOList = bpmTaskExtMapper.selectListByTaskIds(approveTaskIdSet);
        Map<String, BpmTaskExtDO> taskIdToTaskExtDOMap = bpmTaskExtDOList.stream().collect(Collectors.toMap(BpmTaskExtDO::getTaskId, item -> item));
        //流程id -> 发起人提交的任务集合
        Map<String, List<HistoricTaskInstance>> instanceIdToApproveStartHistoricTaskInstanceList = allApproveStartTaskInstanceList.stream()
                .sorted(Comparator.comparing(HistoricTaskInstance::getStartTime))
                .collect(Collectors.groupingBy(HistoricTaskInstance::getProcessInstanceId));

        for (String approveBpmProcessInstanceId : approveBpmProcessInstanceIdSet) {
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = processInstanceIdToApproveProcessInstanceExtDOMap.get(approveBpmProcessInstanceId);
            if (ObjectUtils.isEmpty(bpmProcessInstanceExtDO)) {
                log.info("该出差流程对应的流程实例不存在，processInstanceId: {}", approveBpmProcessInstanceId);
                continue;
            }
            Map<String, Object> formVariables = bpmProcessInstanceExtDO.getFormVariables();
            if (MapUtils.isEmpty(formVariables)) {
                log.error("流程拓展表数据有问题 processInstanceId: {}, formVariables: {}", approveBpmProcessInstanceId, bpmProcessInstanceExtDO.getFormVariables());
                continue;
            }

            List<HistoricTaskInstance> startHistoricTaskInstanceList = instanceIdToApproveStartHistoricTaskInstanceList.get(approveBpmProcessInstanceId);
            if (CollectionUtils.isEmpty(startHistoricTaskInstanceList)) {
                log.info("该出差流程对应的流程实例没有发起人提交的任务，processInstanceId: {}", approveBpmProcessInstanceId);
                continue;
            }
            if (startHistoricTaskInstanceList.size() > 1) {
                HistoricTaskInstance endTaskInstance = startHistoricTaskInstanceList.get(startHistoricTaskInstanceList.size() - 1);
                BpmTaskExtDO bpmTaskExtDO = taskIdToTaskExtDOMap.get(endTaskInstance.getId());
                if (!ObjectUtils.notEqual(bpmTaskExtDO.getResult(), BpmProcessInstanceResultEnum.APPROVE.getResult())) {
                    formVariables = bpmTaskExtDO.getFormVariables();
                    if (MapUtils.isEmpty(formVariables)) {
                        log.error("任务拓展表数据有问题 processInstanceId: {},taskId: {}, formVariables: {}", approveBpmProcessInstanceId, endTaskInstance, formVariables);
                        continue;
                    }
                }
            }
            turnOffOffSiteClockingForBusinessTrips(formVariables);
        }

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

    private void turnOnOffSiteClockingForBusinessTrips(Map<String, Object> formVariables) {
        Object startTime = formVariables.get("start_time");
        Object businessUserId = formVariables.get("userId");
        LocalDate startDate;
        if (Objects.nonNull(startTime) && Objects.nonNull(businessUserId)) {
            long start = Long.parseLong(startTime.toString()) * 1000;
            startDate = Instant.ofEpochMilli(start).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();
            if (now.isEqual(startDate)) {
                CommonResult<UserAndInformationDTO> commonResult = adminUserApi.getUserAndInformation(Long.parseLong(businessUserId.toString()));
                UserAndInformationDTO userAndInformationDTO = commonResult.getData();
                if (Objects.nonNull(userAndInformationDTO)) {
                    Boolean isOffsiteAttendance = userAndInformationDTO.getIsOffsiteAttendance();
                    if (!isOffsiteAttendance) {
                        userAndInformationDTO.setId(Long.parseLong(businessUserId.toString()));
                        userAndInformationDTO.setIsOffsiteAttendance(true);
                        adminUserApi.updateUserAndInformation(userAndInformationDTO);
                    }
                }
            }
        }
    }
}
