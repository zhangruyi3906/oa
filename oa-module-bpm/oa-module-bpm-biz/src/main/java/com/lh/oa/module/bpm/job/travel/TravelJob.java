package com.lh.oa.module.bpm.job.travel;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRespVO;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.service.definition.BpmProcessDefinitionService;
import com.lh.oa.module.bpm.service.task.BpmProcessInstanceService;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.UserAndInformationDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Component
public class TravelJob {

    public static final String PROCESS_KEY = "chuchaliucheng";
    @Resource
    private BpmProcessInstanceService bpmProcessInstanceService;
    @Resource
    private BpmProcessDefinitionService bpmProcessDefinitionService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private HistoryService historyService;
    @Resource
    private AdminUserApi adminUserApi;


    @XxlJob("travelJob")
    public void execute() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(PROCESS_KEY).finished().list();
        for (HistoricProcessInstance historicProcessInstance : list) {
            String processInstanceId = historicProcessInstance.getId();
            String startUserId = historicProcessInstance.getStartUserId();
            BpmProcessInstanceRespVO processInstanceVO = bpmProcessInstanceService.getProcessInstanceVO(processInstanceId);
            if (processInstanceVO.getResult() == BpmProcessInstanceResultEnum.APPROVE.getResult()) {
                Map<String, Object> formVariables = processInstanceVO.getFormVariables();
                Object startTime = formVariables.get("startTime");
                java.time.LocalDate startDate;
                java.time.LocalDate endDate;
                if (startTime != null) {
                    String[] time = startTime.toString().split(",");
                    long start = Long.parseLong(time[0]) * 1000;
                    long end = Long.parseLong(time[1]) * 1000;
                    startDate = Instant.ofEpochMilli(start).atZone(ZoneId.systemDefault()).toLocalDate();
                    endDate = Instant.ofEpochMilli(end).atZone(ZoneId.systemDefault()).toLocalDate();
                    java.time.LocalDate now = LocalDate.now();
                    if (now.isEqual(startDate) && !now.isAfter(endDate)) {
                        CommonResult<UserAndInformationDTO> commonResult = adminUserApi.getUserAndInformation(Long.valueOf(startUserId));
                        UserAndInformationDTO userAndInformationDTO = commonResult.getData();
                        if (userAndInformationDTO != null) {
                            Boolean isOffsiteAttendance = userAndInformationDTO.getIsOffsiteAttendance();
                            if (!isOffsiteAttendance) {
                                userAndInformationDTO.setIsOffsiteAttendance(true);
                                userAndInformationDTO.setId(Long.valueOf(startUserId));
                                adminUserApi.updateUserAndInformation(userAndInformationDTO);
                            }
                        }
                    }
                    if (now.isEqual(endDate) && now.isAfter(startDate)) {
                        CommonResult<UserAndInformationDTO> commonResult = adminUserApi.getUserAndInformation(Long.valueOf(startUserId));
                        UserAndInformationDTO userAndInformationDTO = commonResult.getData();
                        if (userAndInformationDTO!= null) {
                            Boolean isOffsiteAttendance = userAndInformationDTO.getIsOffsiteAttendance();
                            if (isOffsiteAttendance) {
                                userAndInformationDTO.setIsOffsiteAttendance(false);
                                userAndInformationDTO.setId(Long.valueOf(startUserId));
                                adminUserApi.updateUserAndInformation(userAndInformationDTO);
                            }
                        }
                    }
                }
            }
        }
    }

    @XxlJob("test")
    public String execute(String param) {
        System.out.println("测试一下罢辽");
        return param;
    }

}
