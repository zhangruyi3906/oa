package com.lh.oa.module.bpm.service.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.date.DateUtils;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.number.NumberUtils;
import com.lh.oa.framework.common.util.object.PageUtils;
import com.lh.oa.framework.flowable.core.util.FlowableUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.controller.admin.task.vo.activity.BpmActivityRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRollbackReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskDoneExcVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskDonePageItemRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskDonePageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskRevokeReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskTodoPageItemRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskTodoPageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskUpdateAssigneeReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmUserTaskRespVO;
import com.lh.oa.module.bpm.convert.task.BpmTaskConvert;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmTaskAssignRuleDO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmTaskExtDO;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmProcessDefinitionExtMapper;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmTaskAssignRuleMapper;
import com.lh.oa.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.lh.oa.module.bpm.dal.mysql.task.BpmTaskExtMapper;
import com.lh.oa.module.bpm.enums.ErrorCodeConstants;
import com.lh.oa.module.bpm.enums.im.ImMessageBusinessType;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceDeleteReasonEnum;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.framework.flowable.core.behavior.copyScript.BpmTaskCopyScript;
import com.lh.oa.module.bpm.service.definition.BpmFormService;
import com.lh.oa.module.bpm.service.definition.BpmTaskAssignRuleService;
import com.lh.oa.module.bpm.service.message.BpmMessageService;
import com.lh.oa.module.bpm.wrapper.ImWrapper;
import com.lh.oa.module.bpm.wrapper.QiyeWechatMessageWrapper;
import com.lh.oa.module.bpm.wrapper.vo.ImCustomMsgContent;
import com.lh.oa.module.system.api.dept.DeptApi;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
import com.lh.oa.module.system.api.file.FileApi;
import com.lh.oa.module.system.api.file.dto.FileUpdateReqDTO;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import com.lh.oa.module.system.api.user.dto.UserAndInformationDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertMap;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertSet;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.PROCESS_INSTANCE_NOT_EXISTS;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.TASK_COMPLETE_FAIL_ASSIGN_NOT_SELF;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.TASK_COMPLETE_FAIL_NOT_EXISTS;
import static com.lh.oa.module.bpm.service.task.BpmProcessInstanceServiceImpl.toDateFormat;

/**
 *
 */
@Slf4j
@Service
public class BpmTaskServiceImpl implements BpmTaskService {

    private static final String TASK_TODO_INFO_URL = "/admin-api/bpm/process-instance/get";
    private static final String PROCESS_DEFINITION_TYPE_POST_SALE = "7";
    //出差流程上级审批任务标识
    public static final String Business_Trip_Start_TASK_DEFINITION_KEY = "Activity_0s8l4qm";
    @Value("${im.base.url:http://192.168.2.200:48080}")
    private String oaBaseUrl;

    @Resource
    private TaskService taskService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private HistoryService historyService;

    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private BpmTaskExtMapper taskExtMapper;
    @Resource
    private BpmMessageService messageService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService fTaskService;
    @Resource
    private ManagementService managementService;
    @Resource
    private ProcessEngineConfiguration processEngineConfiguration;
    @Resource
    private BpmTaskAssignRuleService bpmTaskAssignRuleService;
    @Resource
    private BpmTaskAssignRuleMapper bpmTaskAssignRuleMapper;
    @Resource
    private BpmCopyService bpmCopyService;
    @Resource
    private BpmProcessDefinitionExtMapper processDefinitionExtMapper;
    @Resource
    private BpmFormService bpmFormService;
    @Resource
    private BpmProcessInstanceExtMapper bpmProcessInstanceExtMapper;
    @Resource
    private BpmProcessInstanceService bpmProcessInstanceService;
    @Resource
    private ImWrapper imWrapper;
    @Resource
    private QiyeWechatMessageWrapper qiyeWechatMessageWrapper;
    @Resource
    private BpmActivityService bpmActivityService;
    @Resource
    private FileApi fileApi;
    private Map<Long, BpmTaskCopyScript> scriptMap = Collections.emptyMap();

    @Resource
    public void setScripts(List<BpmTaskCopyScript> scripts) {
        this.scriptMap = convertMap(scripts, script -> script.getEnum().getId());
    }

    @Override
    public PageResult<BpmTaskTodoPageItemRespVO> getTodoTaskPage(Long userId, BpmTaskTodoPageReqVO pageVO) {
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(String.valueOf(userId))
                .orderByTaskCreateTime().desc();
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (pageVO.getStartTime() != null) {
            Long startTime = pageVO.getStartTime();
            start = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime), ZoneId.systemDefault());
            taskQuery.taskCreatedAfter(DateUtils.of(start));
        }
        if (pageVO.getEndTime() != null) {
            Long endTime = pageVO.getEndTime();
            end = LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime), ZoneId.systemDefault());
            taskQuery.taskCreatedBefore(DateUtils.of(end));
        }
        if (StrUtil.isNotBlank(pageVO.getTaskName())) {
            taskQuery.taskNameLike("%" + pageVO.getTaskName() + "%");
        }

        if (StringUtils.isNotBlank(pageVO.getInstanceName())) {
            List<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOS = bpmProcessInstanceExtMapper.selectList(new LambdaQueryWrapperX<BpmProcessInstanceExtDO>().likeIfPresent(BpmProcessInstanceExtDO::getInstanceName, pageVO.getInstanceName()));
            if (CollUtil.isNotEmpty(bpmProcessInstanceExtDOS)) {
                List<String> instanceIds = bpmProcessInstanceExtDOS.stream().map(BpmProcessInstanceExtDO::getProcessInstanceId).collect(Collectors.toList());
                taskQuery.processInstanceIdIn(instanceIds);
            }
            //根据keyword查询流程名和发起人名字
        } else if (StringUtils.isNotBlank(pageVO.getKeyword())) {
            CommonResult<List<AdminUserRespDTO>> userListResult = adminUserApi.getListByNickname(pageVO.getKeyword());
            List<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOS = new ArrayList<>();
            if (CollUtil.isNotEmpty(userListResult.getData())) {
                List<Long> userIds = userListResult.getData().stream().map(AdminUserRespDTO::getId).collect(Collectors.toList());
                bpmProcessInstanceExtDOS = bpmProcessInstanceExtMapper.selectList(new LambdaQueryWrapperX<BpmProcessInstanceExtDO>().likeIfPresent(BpmProcessInstanceExtDO::getInstanceName, pageVO.getKeyword()).or().in(BpmProcessInstanceExtDO::getStartUserId, userIds));
            } else {
                bpmProcessInstanceExtDOS = bpmProcessInstanceExtMapper.selectList(new LambdaQueryWrapperX<BpmProcessInstanceExtDO>().likeIfPresent(BpmProcessInstanceExtDO::getInstanceName, pageVO.getKeyword()));
            }
            if (CollUtil.isNotEmpty(bpmProcessInstanceExtDOS)) {
                List<String> instanceIds = bpmProcessInstanceExtDOS.stream().map(BpmProcessInstanceExtDO::getProcessInstanceId).collect(Collectors.toList());
                taskQuery.processInstanceIdIn(instanceIds);
            }
        }

        List<Task> tasks;
        if (StringUtils.isNotBlank(pageVO.getStartUserNickname())) {
            tasks = taskQuery.list();
        } else {
            tasks = taskQuery.listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());
        }

        Long count = taskQuery.count();
        if (CollUtil.isEmpty(tasks)) {
            return PageResult.empty(count);
        }

        List<String> taskIds = tasks.stream().map(Task::getId).collect(Collectors.toList());
        List<BpmTaskExtDO> bpmTaskExtDOs = taskExtMapper.selectListByTaskIds(taskIds);
        Map<String, BpmTaskExtDO> taskExtMap = convertMap(bpmTaskExtDOs, BpmTaskExtDO::getTaskId);

        Map<String, ProcessInstance> processInstanceMap =
                processInstanceService.getProcessInstanceMap(convertSet(tasks, Task::getProcessInstanceId));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        List<BpmTaskTodoPageItemRespVO> resultList = BpmTaskConvert.INSTANCE.convertList1(tasks, processInstanceMap, userMap);

        if (StringUtils.isNotBlank(pageVO.getStartUserNickname())) {
            resultList = resultList.stream().filter(item -> item.getProcessInstance().getStartUserNickname().contains(pageVO.getStartUserNickname())).collect(Collectors.toList());
            if (CollUtil.isEmpty(resultList)) {
                return PageResult.empty();
            }
            count = resultList.stream().count();
            //处理分页数据
            resultList = getPagedList(resultList, pageVO.getPageNo(), pageVO.getPageSize());
        }
        // 设置已读状态
        resultList.forEach(result -> {
            if (!taskExtMap.containsKey(result.getId())) {
                result.setReadState(0);
            }
            String instanceId = result.getProcessInstance().getId();
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(instanceId);
            if (ObjectUtils.isNotEmpty(bpmProcessInstanceExtDO)) {
                result.setInstanceName(bpmProcessInstanceExtDO.getInstanceName());
                if (bpmProcessInstanceExtDO.getStartUserId().equals(userId)) {
                    result.setCancelAble(true);
                } else {
                    result.setCancelAble(false);
                }
            }
            BpmTaskExtDO taskExtDO = taskExtMap.get(result.getId());
            result.setReadState(taskExtDO.getReadState());
        });
        return new PageResult<>(resultList, count);
    }


    public PageResult<BpmTaskTodoPageItemRespVO> getNewTodoTaskPage(Long userId, BpmTaskTodoPageReqVO pageVO) {
        LambdaQueryWrapperX<BpmTaskExtDO> bpmTaskExtDOLambdaQueryWrapperX = new LambdaQueryWrapperX<BpmTaskExtDO>()
                .eqIfPresent(BpmTaskExtDO::getAssigneeUserId, userId)
                .eqIfPresent(BpmTaskExtDO::getResult, BpmProcessInstanceResultEnum.PROCESS.getResult())
                .likeIfPresent(BpmTaskExtDO::getName, pageVO.getTaskName())
                .betweenIfPresent(BpmTaskExtDO::getCreateTime, pageVO.getStartTime(), pageVO.getEndTime())
                .orderByDesc(BpmTaskExtDO::getCreateTime);
        //除流程名和发起人名筛选外的其他筛选结果
        List<BpmTaskExtDO> bpmTaskExtDOList = taskExtMapper.selectList(bpmTaskExtDOLambdaQueryWrapperX);
        if (CollectionUtils.isEmpty(bpmTaskExtDOList)) {
            return PageResult.empty();
        }
        Set<String> processInstanceIdSet = bpmTaskExtDOList.stream().map(BpmTaskExtDO::getProcessInstanceId).collect(Collectors.toSet());
        List<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOS = new ArrayList<>();

        //流程实例的查询wrapper
        LambdaQueryWrapper<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOLambdaQueryWrapper = new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .likeIfPresent(BpmProcessInstanceExtDO::getInstanceName, pageVO.getInstanceName())
                .inIfPresent(BpmProcessInstanceExtDO::getProcessInstanceId, processInstanceIdSet);
        CommonResult<List<AdminUserRespDTO>> userListResult = new CommonResult<>();
        //name和instanceName是web端查流程发起人和流程名的参数，keyword是pc和app同时查询流程发起人和流程名参数，两者不会同时存在
        if (StringUtils.isNotBlank(pageVO.getStartUserNickname())) {
            userListResult = adminUserApi.getListByNickname(pageVO.getStartUserNickname());
        }
        if (StringUtils.isNotBlank(pageVO.getKeyword())) {
            userListResult = adminUserApi.getListByNickname(pageVO.getKeyword());
            bpmProcessInstanceExtDOLambdaQueryWrapper = bpmProcessInstanceExtDOLambdaQueryWrapper.and(t -> t.like(BpmProcessInstanceExtDO::getInstanceName, pageVO.getKeyword()));
        }

        if (CollUtil.isNotEmpty(userListResult.getData())) {
            Set<Long> userIdSet = userListResult.getData().stream().map(AdminUserRespDTO::getId).collect(Collectors.toSet());
            if (StringUtils.isNotBlank(pageVO.getStartUserNickname())) {
                bpmProcessInstanceExtDOLambdaQueryWrapper = bpmProcessInstanceExtDOLambdaQueryWrapper.in(BpmProcessInstanceExtDO::getStartUserId, userIdSet);
            }
            if (StringUtils.isNotBlank(pageVO.getKeyword())) {
                bpmProcessInstanceExtDOLambdaQueryWrapper = bpmProcessInstanceExtDOLambdaQueryWrapper.or().in(BpmProcessInstanceExtDO::getStartUserId, userIdSet);
            }
        }
        bpmProcessInstanceExtDOS = bpmProcessInstanceExtMapper.selectList(bpmProcessInstanceExtDOLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(bpmProcessInstanceExtDOS)) {
            return PageResult.empty();
        }
        //包含流程名和流程发起人筛选后的已办任务所对应的流程实例idSet
        Set<String> processInstanceIdSetResult = bpmProcessInstanceExtDOS.stream().map(BpmProcessInstanceExtDO::getProcessInstanceId).collect(Collectors.toSet());
        //满足所有筛选条件的待办任务
        List<BpmTaskExtDO> bpmTaskExtDOS = bpmTaskExtDOList.stream().filter(bpmTaskExtDO -> processInstanceIdSetResult.contains(bpmTaskExtDO.getProcessInstanceId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bpmTaskExtDOS)) {
            return PageResult.empty();
        }
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskIds(convertSet(bpmTaskExtDOS, BpmTaskExtDO::getTaskId))
                .orderByTaskCreateTime()
                .desc();
        List<Task> tasks = taskQuery.listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());

        if (CollUtil.isEmpty(tasks)) {
            return PageResult.empty(taskQuery.count());
        }

        List<String> taskIds = tasks.stream().map(Task::getId).collect(Collectors.toList());
        List<BpmTaskExtDO> bpmTaskExtDOs = taskExtMapper.selectListByTaskIds(taskIds);
        Map<String, BpmTaskExtDO> taskExtMap = convertMap(bpmTaskExtDOs, BpmTaskExtDO::getTaskId);

        Map<String, ProcessInstance> processInstanceMap =
                processInstanceService.getProcessInstanceMap(convertSet(tasks, Task::getProcessInstanceId));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        List<BpmTaskTodoPageItemRespVO> resultList = BpmTaskConvert.INSTANCE.convertList1(tasks, processInstanceMap, userMap);

        // 设置已读状态
        resultList.forEach(result -> {
            if (!taskExtMap.containsKey(result.getId())) {
                result.setReadState(0);
            }
            String instanceId = result.getProcessInstance().getId();
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(instanceId);
            if (ObjectUtils.isNotEmpty(bpmProcessInstanceExtDO)) {
                result.setInstanceName(bpmProcessInstanceExtDO.getInstanceName());
                if (bpmProcessInstanceExtDO.getStartUserId().equals(userId)) {
                    result.setCancelAble(true);
                } else {
                    result.setCancelAble(false);
                }
            }
            BpmTaskExtDO taskExtDO = taskExtMap.get(result.getId());
            result.setReadState(taskExtDO.getReadState());
        });
        return new PageResult<>(resultList, taskQuery.count());
    }

    @Override
    public Integer getTodoTaskCount(Long userId) {
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(String.valueOf(userId))
                .orderByTaskCreateTime().desc();
        long count = taskQuery.count();
        return (int) count;
    }

    @Override
    public void saveTask(BpmTaskVo bpmTaskVO) {
        BpmTaskExtDO bpmTaskExtDO = taskExtMapper.selectByTaskId(bpmTaskVO.getId()); //因为前端传的Id，所以这儿用Id接
        if (ObjectUtils.isEmpty(bpmTaskExtDO))
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.TASK_NOT_EXIST);
        bpmTaskExtDO.setFormVariables(ObjectUtils.isNotEmpty(bpmTaskVO.getFormVariables()) ? bpmTaskVO.getFormVariables() : new HashMap<>());
        bpmTaskExtDO.setReason(ObjectUtils.isNotEmpty(bpmTaskVO.getReason()) ? bpmTaskVO.getReason() : "");
        bpmTaskExtDO.setCopyUserIds(bpmTaskVO.getCopyUserIds());
        taskExtMapper.updateById(bpmTaskExtDO);
    }

    @Override
    public Boolean getRevocable(String processInstanceId, String taskId) {
        List<BpmTaskRespVO> taskList = this.getTaskListByProcessInstanceId(processInstanceId);
        if (CollectionUtils.isEmpty(taskList)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.TASK_NOT_EXIST);
        }
        if (taskList.size() == 1) {
            return false;
        }
        Map<String, BpmTaskRespVO> bpmTaskRespVOMap = taskList.stream().collect(Collectors.toMap(BpmTaskRespVO::getId, bpmTaskRespVO -> bpmTaskRespVO));
        //传入taskId对应的任务
        BpmTaskRespVO bpmTaskRespVO = bpmTaskRespVOMap.get(taskId);
        if (ObjectUtils.isEmpty(bpmTaskRespVO)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.TASK_NOT_EXIST);
        }
        //传入的任务是否是自己审批的任务
        if (ObjectUtils.notEqual(bpmTaskRespVO.getAssigneeUser().getId(), getLoginUserId())) {
            return false;
        }
        //如果该任务未被处理过，不支持撤回
        if (!ObjectUtils.notEqual(bpmTaskRespVO.getResult(), BpmProcessInstanceResultEnum.PROCESS.getResult())) {
            return false;
        }
        // 获取当前任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        if (CollectionUtils.isEmpty(tasks)) {
            return false;
        }

        Task task = tasks.get(0);
        // 获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        // 获取当前任务节点元素
        List<SequenceFlow> sequenceFlowList = new ArrayList<>();
        if (flowElements != null) {
            for (FlowElement flowElement : flowElements) {
                if (flowElement instanceof SequenceFlow) {
                    sequenceFlowList.add((SequenceFlow) flowElement);
                }
            }
        }
        if (CollectionUtils.isEmpty(sequenceFlowList)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_IS_ERROR);
        }
        Map<String, List<SequenceFlow>> sourceToSequenceFlowListMap = sequenceFlowList.stream().collect(Collectors.groupingBy(SequenceFlow::getSourceRef));
        String taskDefinitionKey = bpmTaskRespVO.getDefinitionKey();
        //模型中传入节点对应的下一节点，可能有多个扭转条件，即下个节点可能有多个
        List<SequenceFlow> targetSequenceFlowList = sourceToSequenceFlowListMap.get(taskDefinitionKey);
        if (CollectionUtils.isEmpty(targetSequenceFlowList)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_IS_ERROR);
        }
        Set<String> targetTaskDefinitionKeySet = targetSequenceFlowList.stream().map(SequenceFlow::getTargetRef).collect(Collectors.toSet());
        //当前审批任务
        BpmTaskRespVO currentBpmTaskRespVO = taskList.get(0);
        //当前审批任务是否已被审批
        if (ObjectUtils.notEqual(currentBpmTaskRespVO.getResult(), BpmProcessInstanceResultEnum.PROCESS.getResult())) {
            return false;
        }
        String currentTaskDefinitionKey = currentBpmTaskRespVO.getDefinitionKey();
        //当 当前审批节点是传入节点的下一节点时
        if (targetTaskDefinitionKeySet.contains(currentTaskDefinitionKey)) {
            //当前审批任务是否有被审批过的任务
            List<HistoricTaskInstance> currentTaskKeyHistoricList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .taskDefinitionKey(currentTaskDefinitionKey)
                    .taskCompletedAfter(task.getCreateTime())
                    .list();

            if (CollectionUtils.isEmpty(currentTaskKeyHistoricList)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void revokeTask(BpmTaskRevokeReqVO bpmTaskRevokeReqVO) {
        if (getRevocable(bpmTaskRevokeReqVO.getProcessInstanceId(), bpmTaskRevokeReqVO.getTaskId())) {
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(bpmTaskRevokeReqVO.getTaskId()).singleResult();
            String revokeDefinitionKey = historicTaskInstance.getTaskDefinitionKey();
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(bpmTaskRevokeReqVO.getProcessInstanceId()).list();
            BpmProcessInstanceRollbackReqVO bpmProcessInstanceRollbackReqVO = new BpmProcessInstanceRollbackReqVO();
            bpmProcessInstanceRollbackReqVO.setTaskId(tasks.get(0).getId());
            bpmProcessInstanceRollbackReqVO.setRollbackId(revokeDefinitionKey);
            bpmProcessInstanceRollbackReqVO.setComment("上一级任务审批人撤回任务");
            bpmProcessInstanceRollbackReqVO.setResult(BpmProcessInstanceResultEnum.RECALL.getResult());
            this.taskReturn(bpmProcessInstanceRollbackReqVO);
        } else {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.TASK_NOT_REVOCABLE);
        }
    }

    private List<BpmTaskExtDO> getUnreadTaskList(Long userId, BpmTaskTodoPageReqVO pageVO) {
        LambdaQueryWrapper<BpmTaskExtDO> query = Wrappers.lambdaQuery();
        query.eq(BpmTaskExtDO::getAssigneeUserId, userId);
        query.eq(BpmTaskExtDO::getReadState, pageVO.getReadState());
        List<BpmTaskExtDO> taskExtDOList = taskExtMapper.selectList(query);
        return taskExtDOList;
    }

    @Override
    public PageResult<BpmTaskDonePageItemRespVO> getDoneTaskPage(Long userId, BpmTaskDonePageReqVO pageVO) {
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery().finished()
                .taskAssignee(String.valueOf(userId))
                .orderByHistoricTaskInstanceEndTime().desc();
        if (pageVO.getBeginCreateTime() != null) {
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getBeginCreateTime()));
        }
        if (pageVO.getEndCreateTime() != null) {
            taskQuery.taskCreatedBefore(DateUtils.of(pageVO.getEndCreateTime()));
        }
        //增加创建时间筛选
        if (pageVO.getStartTime() != null) {
            taskQuery.taskCreatedAfter(new Date(pageVO.getStartTime() * 1000));
            log.info("开始时间：{}", new Date(pageVO.getStartTime() * 1000));
        }
        if (pageVO.getEndTime() != null) {
            taskQuery.taskCreatedBefore(new Date(pageVO.getEndTime() * 1000));
            log.info("结束时间：{}", new Date(pageVO.getEndTime() * 1000));
        }
        if (StrUtil.isNotBlank(pageVO.getTaskName())) {
            taskQuery.taskNameLike("%" + pageVO.getTaskName() + "%");
        }
        List<HistoricTaskInstance> tasks;
        //根据keyword查询流程名和发起人名字
        if (StringUtils.isNotBlank(pageVO.getKeyword())) {
            CommonResult<List<AdminUserRespDTO>> userListResult = adminUserApi.getListByNickname(pageVO.getKeyword());
            List<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOS = new ArrayList<>();
            if (CollUtil.isNotEmpty(userListResult.getData())) {
                List<Long> userIds = userListResult.getData().stream().map(AdminUserRespDTO::getId).collect(Collectors.toList());
                bpmProcessInstanceExtDOS = bpmProcessInstanceExtMapper.selectList(new LambdaQueryWrapperX<BpmProcessInstanceExtDO>().likeIfPresent(BpmProcessInstanceExtDO::getInstanceName, pageVO.getKeyword()).or().in(BpmProcessInstanceExtDO::getStartUserId, userIds));
            } else {
                bpmProcessInstanceExtDOS = bpmProcessInstanceExtMapper.selectList(new LambdaQueryWrapperX<BpmProcessInstanceExtDO>().likeIfPresent(BpmProcessInstanceExtDO::getInstanceName, pageVO.getKeyword()));
            }
            if (CollUtil.isNotEmpty(bpmProcessInstanceExtDOS)) {
                List<String> instanceIds = bpmProcessInstanceExtDOS.stream().map(BpmProcessInstanceExtDO::getProcessInstanceId).collect(Collectors.toList());
                taskQuery.processInstanceIdIn(instanceIds);
            }
        }

        if (StrUtil.isBlank(pageVO.getName()) && StrUtil.isBlank(pageVO.getInstanceName()) && ObjectUtils.isEmpty(pageVO.getResult())) {
            //未输入名字筛选保留初始逻辑，增加查询效率
            tasks = taskQuery.listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());
        } else {
            tasks = taskQuery.list();
        }

        if (CollUtil.isEmpty(tasks)) {
            return PageResult.empty(taskQuery.count());
        }

        List<BpmTaskExtDO> bpmTaskExtDOs =
                taskExtMapper.selectListByTaskIds(convertSet(tasks, HistoricTaskInstance::getId));
        Map<String, BpmTaskExtDO> bpmTaskExtDOMap = convertMap(bpmTaskExtDOs, BpmTaskExtDO::getTaskId);
        Map<String, HistoricProcessInstance> historicProcessInstanceMap =
                processInstanceService.getHistoricProcessInstanceMap(
                        convertSet(tasks, HistoricTaskInstance::getProcessInstanceId));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSet(historicProcessInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        List<BpmTaskDonePageItemRespVO> bpmTaskDonePageItemRespVOS = BpmTaskConvert.INSTANCE.convertList2(tasks, bpmTaskExtDOMap, historicProcessInstanceMap, userMap);
        //暂时输入发起人姓名匹配
        if (StrUtil.isNotBlank(pageVO.getName())) {
            CommonResult<List<AdminUserRespDTO>> userList = adminUserApi.getListByNickname(pageVO.getName());
            if (ObjectUtils.isEmpty(userList.getData()) || userList.getData().size() == 0)
                return PageResult.empty();

            Set<Long> userIds = userList.getData().stream().map(AdminUserRespDTO::getId).collect(Collectors.toSet());
            bpmTaskDonePageItemRespVOS = bpmTaskDonePageItemRespVOS.stream().filter(bpmTaskDonePageItemRespVO -> userIds.contains(bpmTaskDonePageItemRespVO.getProcessInstance().getStartUserId())).collect(Collectors.toList());
        }
        //任务结果
        if (ObjectUtils.isNotEmpty(pageVO.getResult())) {
            bpmTaskDonePageItemRespVOS = bpmTaskDonePageItemRespVOS.stream().filter(bpmTaskDonePageItemRespVO -> !ObjectUtils.notEqual(pageVO.getResult(), bpmTaskDonePageItemRespVO.getResult())).collect(Collectors.toList());
        }

        bpmTaskDonePageItemRespVOS.forEach(result -> {
            String instanceId = result.getProcessInstance().getId();
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(instanceId);
            if (ObjectUtils.isNotEmpty(bpmProcessInstanceExtDO)) {
                result.setInstanceName(bpmProcessInstanceExtDO.getInstanceName());
            }
        });
        //流程名字
        if (StrUtil.isNotBlank(pageVO.getInstanceName())) {
            bpmTaskDonePageItemRespVOS = bpmTaskDonePageItemRespVOS.stream().filter(bpmTaskDonePageItemRespVO -> {
                if (StrUtil.isBlank(bpmTaskDonePageItemRespVO.getInstanceName())) {
                    return bpmTaskDonePageItemRespVO.getProcessInstance().getName().contains(pageVO.getInstanceName());
                }
                return bpmTaskDonePageItemRespVO.getInstanceName().contains(pageVO.getInstanceName());
            }).collect(Collectors.toList());
        }
        if (StrUtil.isBlank(pageVO.getInstanceName()) && StrUtil.isBlank(pageVO.getName()) && ObjectUtils.isEmpty(pageVO.getResult())) {
            return new PageResult<>(bpmTaskDonePageItemRespVOS, taskQuery.count());
        } else {
            List<BpmTaskDonePageItemRespVO> bpmTaskDonePageItemRespVOList = getPagedList(bpmTaskDonePageItemRespVOS, pageVO.getPageNo(), pageVO.getPageSize());
            //暂行方法，之后修改
            return new PageResult<>(bpmTaskDonePageItemRespVOList, Long.valueOf(bpmTaskDonePageItemRespVOS.size()));
        }
    }

    public PageResult<BpmTaskDonePageItemRespVO> getNewDoneTaskPage(Long userId, BpmTaskDonePageReqVO pageVO) {
        LambdaQueryWrapperX<BpmTaskExtDO> bpmTaskExtDOLambdaQueryWrapperX = new LambdaQueryWrapperX<BpmTaskExtDO>()
                .eqIfPresent(BpmTaskExtDO::getAssigneeUserId, userId)
                .neIfPresent(BpmTaskExtDO::getResult, BpmProcessInstanceResultEnum.PROCESS.getResult())
                .likeIfPresent(BpmTaskExtDO::getName, pageVO.getTaskName())
                .eqIfPresent(BpmTaskExtDO::getResult, pageVO.getResult())
                .betweenIfPresent(BpmTaskExtDO::getCreateTime, pageVO.getStartTime(), pageVO.getEndTime())
                .orderByDesc(BpmTaskExtDO::getCreateTime);
        //除流程名和发起人名筛选外的其他筛选结果
        List<BpmTaskExtDO> bpmTaskExtDOList = taskExtMapper.selectList(bpmTaskExtDOLambdaQueryWrapperX);
        if (CollectionUtils.isEmpty(bpmTaskExtDOList)) {
            return PageResult.empty();
        }
        Set<String> processInstanceIdSet = bpmTaskExtDOList.stream().map(BpmTaskExtDO::getProcessInstanceId).collect(Collectors.toSet());
        CommonResult<List<AdminUserRespDTO>> userListResult = new CommonResult<>();
        //name和instanceName是web端查流程发起人和流程名的参数，keyword是pc和app同时查询流程发起人和流程名参数，两者不会同时存在
        if (StringUtils.isNotBlank(pageVO.getName())) {
            userListResult = adminUserApi.getListByNickname(pageVO.getName());
        }
        if (StringUtils.isNotBlank(pageVO.getKeyword())) {
            userListResult = adminUserApi.getListByNickname(pageVO.getKeyword());
        }
        List<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOS = new ArrayList<>();

        //流程实例的查询wrapper
        LambdaQueryWrapper<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOLambdaQueryWrapper = new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .likeIfPresent(BpmProcessInstanceExtDO::getInstanceName, pageVO.getInstanceName())
                .inIfPresent(BpmProcessInstanceExtDO::getProcessInstanceId, processInstanceIdSet);

        if (CollUtil.isNotEmpty(userListResult.getData())) {
            Set<Long> userIdSet = userListResult.getData().stream().map(AdminUserRespDTO::getId).collect(Collectors.toSet());
            if (StringUtils.isNotBlank(pageVO.getName())) {
                bpmProcessInstanceExtDOLambdaQueryWrapper = bpmProcessInstanceExtDOLambdaQueryWrapper.in(BpmProcessInstanceExtDO::getStartUserId, userIdSet);
            }
            if (StringUtils.isNotBlank(pageVO.getKeyword())) {
                bpmProcessInstanceExtDOLambdaQueryWrapper = bpmProcessInstanceExtDOLambdaQueryWrapper.and(t -> t.like(BpmProcessInstanceExtDO::getInstanceName, pageVO.getKeyword()).or().in(BpmProcessInstanceExtDO::getStartUserId, userIdSet));
            }
        }
        //所有满足条件的已办任务所在的流程实例
        bpmProcessInstanceExtDOS = bpmProcessInstanceExtMapper.selectList(bpmProcessInstanceExtDOLambdaQueryWrapper);

        if (CollectionUtils.isEmpty(bpmProcessInstanceExtDOS)) {
            return PageResult.empty();
        }
        //包含流程名和流程发起人筛选后的已办任务所对应的流程实例idSet
        Set<String> processInstanceIdSetResult = bpmProcessInstanceExtDOS.stream().map(BpmProcessInstanceExtDO::getProcessInstanceId).collect(Collectors.toSet());
        //满足所有筛选条件的已办任务
        List<BpmTaskExtDO> bpmTaskExtDOS = bpmTaskExtDOList.stream().filter(bpmTaskExtDO -> processInstanceIdSetResult.contains(bpmTaskExtDO.getProcessInstanceId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bpmTaskExtDOS)) {
            return PageResult.empty();
        }
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
                .taskIds(convertSet(bpmTaskExtDOS, BpmTaskExtDO::getTaskId))
                .orderByHistoricTaskInstanceEndTime()
                .desc();
        List<HistoricTaskInstance> tasks = taskQuery.listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());

        if (CollUtil.isEmpty(tasks)) {
            return PageResult.empty(taskQuery.count());
        }

        List<BpmTaskExtDO> bpmTaskExtDOs =
                taskExtMapper.selectListByTaskIds(convertSet(tasks, HistoricTaskInstance::getId));
        Map<String, BpmTaskExtDO> bpmTaskExtDOMap = convertMap(bpmTaskExtDOs, BpmTaskExtDO::getTaskId);
        Map<String, HistoricProcessInstance> historicProcessInstanceMap =
                processInstanceService.getHistoricProcessInstanceMap(
                        convertSet(tasks, HistoricTaskInstance::getProcessInstanceId));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSet(historicProcessInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        List<BpmTaskDonePageItemRespVO> bpmTaskDonePageItemRespVOS = BpmTaskConvert.INSTANCE.convertList2(tasks, bpmTaskExtDOMap, historicProcessInstanceMap, userMap);

        bpmTaskDonePageItemRespVOS.forEach(result -> {
            String instanceId = result.getProcessInstance().getId();
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(instanceId);
            if (ObjectUtils.isNotEmpty(bpmProcessInstanceExtDO)) {
                result.setInstanceName(bpmProcessInstanceExtDO.getInstanceName());
            }
        });
        return new PageResult<>(bpmTaskDonePageItemRespVOS, taskQuery.count());
    }

    public static <T> List<T> getPagedList(List<T> list, int pageNo, int pageSize) {
        int startIndex = (pageNo - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, list.size());
        if (startIndex >= list.size()) {
            return Collections.emptyList();
        }
        return list.subList(startIndex, endIndex);
    }

    @Override
    public List<Task> getTasksByProcessInstanceIds(List<String> processInstanceIds) {
        if (CollUtil.isEmpty(processInstanceIds)) {
            return Collections.emptyList();
        }
        return taskService.createTaskQuery().processInstanceIdIn(processInstanceIds).list();
    }

    @Override
    public List<BpmTaskRespVO> getTaskListByProcessInstanceId(String processInstanceId) {
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().desc()
                .list();
        if (CollUtil.isEmpty(tasks)) {
            return Collections.emptyList();
        }

        List<BpmTaskExtDO> bpmTaskExtDOs = taskExtMapper.selectListByTaskIds(convertSet(tasks, HistoricTaskInstance::getId));
        Map<String, BpmTaskExtDO> bpmTaskExtDOMap = convertMap(bpmTaskExtDOs, BpmTaskExtDO::getTaskId);
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(processInstanceId);
        Set<Long> userIds = convertSet(tasks, task -> NumberUtils.parseLong(task.getAssignee()));
        userIds.add(NumberUtils.parseLong(processInstance.getStartUserId()));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(convertSet(userMap.values(), AdminUserRespDTO::getDeptId));

        Map<Long, AdminUserRespDTO> allUserMap = adminUserApi.getAllUserMap();
        List<BpmTaskRespVO> bpmTaskRespVOS = BpmTaskConvert.INSTANCE.convertList3(tasks, bpmTaskExtDOMap, processInstance, deptMap, allUserMap);
        if (ObjectUtils.isNotEmpty(bpmTaskRespVOS)) {
            List<BpmTaskRespVO> assigneeIsNotStartUserTaskList = bpmTaskRespVOS.stream()
                    .filter(item -> !item.getAssigneeUser().getId().equals(item.getProcessInstance().getStartUserId()))
                    .collect(Collectors.toList());
            BpmTaskRespVO firstBpmTaskRespVO = bpmTaskRespVOS.get(0);
            BpmTaskRespVO endBpmTaskRespVO = bpmTaskRespVOS.get(bpmTaskRespVOS.size() - 1);
            //当前任务是否是第一个任务
            currentTaskIsFirstTask(firstBpmTaskRespVO, endBpmTaskRespVO);
            firstBpmTaskRespVO.setStartUserIsAssignee(false);
            //全是流程发起人自己审批的情况,或者第一个节点不是流程发起人自己审批处于处理中但是其他节点是自己审批的情况
            boolean assigneeIsNotStartUserTaskExist = assigneeIsNotStartUserTaskIsExist(assigneeIsNotStartUserTaskList);
            //全是流程发起人自己审批的情况
            if (assigneeIsNotStartUserTaskExist) {
                firstBpmTaskRespVO.setStartUserIsAssignee(true);
            } else if (assigneeIsNotStartUserTaskList.size() == 1) {
                BpmTaskRespVO firstAssigneeIsNotStartUserTask = assigneeIsNotStartUserTaskList.get(0);
                //第一个节点不是流程发起人自己审批处于待处理但是其他节点是自己审批的情况
                if (firstAssigneeIsNotStartUserTask.getResult() == 1) {
                    firstBpmTaskRespVO.setStartUserIsAssignee(true);
                }
            }

            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(processInstanceId);

            Long assigneeId = firstBpmTaskRespVO.getAssigneeUser().getId();
            Long processStartUserId = bpmProcessInstanceExtDO.getStartUserId();
            //当前节点审批人是否是流程发起人
            if (processStartUserId.equals(assigneeId)) {
                firstBpmTaskRespVO.setCancelAble(true);
            } else {
                firstBpmTaskRespVO.setCancelAble(false);
            }
            Boolean deleteAble = false;
            Boolean returnAble = false;
            Long loginUserId = getLoginUserId();
            //判断是否是发起人提交节点,并且登录人是流程发起人，且流程未结束
            Boolean isDeleteAble = isDeleteAble(firstBpmTaskRespVO, endBpmTaskRespVO, bpmProcessInstanceExtDO, processStartUserId, loginUserId);
            if (isDeleteAble) {
                deleteAble = true;
            }
            //判断审批人是登录人,并且流程未结束
            if (assigneeId.equals(loginUserId) && bpmProcessInstanceExtDO.getResult() == 1) {
                returnAble = true;
            }
            firstBpmTaskRespVO.setDeleteAble(deleteAble);
            firstBpmTaskRespVO.setReturnAble(returnAble);
        }
        return bpmTaskRespVOS;
    }

    private static void currentTaskIsFirstTask(BpmTaskRespVO firstBpmTaskRespVO, BpmTaskRespVO endBpmTaskRespVO) {
        firstBpmTaskRespVO.setIsFirstTask(false);
        if (!ObjectUtils.notEqual(firstBpmTaskRespVO.getDefinitionKey(), endBpmTaskRespVO.getDefinitionKey())) {
            firstBpmTaskRespVO.setIsFirstTask(true);
        }
    }

    private static Boolean isDeleteAble(BpmTaskRespVO firstBpmTaskRespVO, BpmTaskRespVO endBpmTaskRespVO, BpmProcessInstanceExtDO bpmProcessInstanceExtDO, Long processStartUserId, Long loginUserId) {
        //判断是否是发起人提交节点,并且登录人是流程发起人,并且流程未结束
        Boolean isDeleteAble = isStartTask(firstBpmTaskRespVO, endBpmTaskRespVO) && !ObjectUtils.notEqual(loginUserId, processStartUserId) && bpmProcessInstanceExtDO.getResult() == 1;
        return isDeleteAble;
    }

    private static boolean isStartTask(BpmTaskRespVO firstBpmTaskRespVO, BpmTaskRespVO endBpmTaskRespVO) {
        return ObjectUtils.equals(firstBpmTaskRespVO.getDefinitionKey(), endBpmTaskRespVO.getDefinitionKey());
    }

    private static boolean assigneeIsNotStartUserTaskIsExist(List<BpmTaskRespVO> assigneeIsNotStartUserTaskList) {
        return CollectionUtils.isEmpty(assigneeIsNotStartUserTaskList) || assigneeIsNotStartUserTaskList.size() == 0;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveTask(Long userId, BpmTaskVo bpmTaskVO) {
        log.info("流程审核通过, userId:{}, param:{}", userId, JsonUtils.toJsonString(bpmTaskVO));
        Task task;
        bpmTaskVO.setComment(bpmTaskVO.getReason());
        bpmTaskVO.setTaskId(bpmTaskVO.getId());
        if (userId != null) {
            task = checkTask(userId, bpmTaskVO.getTaskId());
        } else {
            task = checkTask(getLoginUserId(), bpmTaskVO.getTaskId());
        }
        BpmTaskExtDO bpmTaskExtDO1 = taskExtMapper.selectByTaskId(bpmTaskVO.getTaskId());
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
        }
        int readAble = bpmTaskExtDO1.getReadAble();

        Map<String, Object> instanceVariables = runtimeService.getVariables(task.getExecutionId());
        if (MapUtils.isNotEmpty(instanceVariables) && MapUtils.isNotEmpty(bpmTaskVO.getFormVariables())) {
            instanceVariables.putAll(bpmTaskVO.getFormVariables());
            runtimeService.setVariables(task.getExecutionId(), instanceVariables);
        }
        //出差流程额外逻辑
        if (StringUtils.equals(Business_Trip_Start_TASK_DEFINITION_KEY, task.getTaskDefinitionKey())) {
            turnOnOffSiteClockingForBusinessTrips(task);
        }
        taskService.complete(task.getId(), instance.getProcessVariables());

        BpmTaskExtDO bpmTaskExtDO = taskExtMapper.selectByTaskId(task.getId());
        Long formId = bpmTaskExtDO.getFormId();
        if (ObjectUtils.isNotEmpty(formId) && formId != 0) {
            //判断是否是初始表单
            BpmProcessInstanceRespVO bpmProcessInstanceRespVO = bpmProcessInstanceService.getProcessInstanceVO(bpmTaskExtDO.getProcessInstanceId());
            if (ObjectUtils.isNotEmpty(bpmProcessInstanceRespVO) && ObjectUtils.isNotEmpty(bpmProcessInstanceRespVO.getProcessDefinition())) {
                Long definitionFormId = bpmProcessInstanceRespVO.getProcessDefinition().getFormId();
                //初始表单
                if (ObjectUtils.equals(formId, definitionFormId) && bpmProcessInstanceRespVO.getReadAble() == 1) {
                    Map<String, Object> formVariables = bpmTaskVO.getFormVariables();
                    BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(bpmTaskExtDO.getProcessInstanceId());
                    Map<String, Object> instanceExtDOFormVariables = bpmProcessInstanceExtDO.getFormVariables();
                    if (MapUtils.isNotEmpty(formVariables)) {
                        instanceExtDOFormVariables.putAll(formVariables);
                    }
                    bpmProcessInstanceExtMapper.updateByProcessInstanceId(new BpmProcessInstanceExtDO()
                            .setFormVariables(instanceExtDOFormVariables)
                            .setReadAble(1)
                            .setProcessInstanceId(bpmTaskExtDO.getProcessInstanceId()));
                } else {
                    //节点表单
                    List<BpmTaskExtDO> bpmTaskExtDOS = taskExtMapper.selectListByProcessInstanceId(bpmTaskExtDO.getProcessInstanceId());
                    List<BpmTaskExtDO> bpmTaskExtDOList = bpmTaskExtDOS.stream().filter(item -> ObjectUtils.isNotEmpty(item.getFormKey()) && ObjectUtils.equals(Long.valueOf(item.getFormKey()), formId) && item.getReadAble() == 1).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(bpmTaskExtDOList)) {
                        //上一该表单的节点
                        BpmTaskExtDO bpmTaskExtDO2 = bpmTaskExtDOList.get(0);
                        //当前节点新增的之前节点的表单数据
                        Map<String, Object> addFormVariables = bpmTaskVO.getFormVariables();
                        //之前节点的表单数据
                        Map<String, Object> formVariables = bpmTaskExtDO2.getFormVariables();
                        if (MapUtils.isNotEmpty(addFormVariables)) {
                            if (MapUtils.isNotEmpty(formVariables))
                                formVariables.putAll(addFormVariables);
                            else
                                formVariables = addFormVariables;
                            taskExtMapper.updateByTaskId(bpmTaskExtDO2.setFormVariables(formVariables));
                        }
                    }
                }
            }
        }

        //表单上传的文件与流程关联
        Map<String, Object> formVariables = bpmTaskVO.getFormVariables();
        if (ObjectUtils.isNotEmpty(bpmTaskExtDO.getFormConf()) && MapUtils.isNotEmpty(formVariables)) {
            this.fileRelevancyProcessInstance(task, bpmTaskExtDO, formVariables);
        }

        bpmTaskExtDO.setTaskId(task.getId())
                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult())
                .setReason(bpmTaskVO.getComment())
                .setFormKey(bpmTaskVO.getFormKey())
                .setReadAble(readAble)
                .setFormVariables(bpmTaskVO.getFormVariables())
                .setCopyUserIds(bpmTaskVO.getCopyUserIds());
        taskExtMapper.updateByTaskId(bpmTaskExtDO);
        bpmTaskVO.setProcInsId(instance.getProcessInstanceId());
        bpmTaskVO.setVariables(instance.getProcessVariables());
        bpmTaskVO.setTaskName(task.getName());

        // 处理抄送用户
        bpmCopyService.makeCopy(bpmTaskVO);
        //下一节点审批人与该节点相同并且不涉及表单则直接通过
        approveIfSameAssignee(task, bpmTaskExtDO);

    }

    private void approveIfSameAssignee(Task task, BpmTaskExtDO bpmTaskExtDO) {
        //存在多人审批情况
        List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        if (CollectionUtils.isNotEmpty(nextTaskList) && nextTaskList.size() == 1) {
            // 下一个任务
            Task nextTask = nextTaskList.get(0);
            BpmTaskExtDO nextTaskExtDO = taskExtMapper.selectByTaskId(nextTask.getId());
            if (ObjectUtils.isEmpty(nextTaskExtDO)) {
                return;
            }
            //节点可编辑字段
            List<String> nextTaskExtDOEditFields = nextTaskExtDO.getEditFields();
            //节点表单
            String nextTaskExtDOFormKey = nextTaskExtDO.getFormKey();
            //若该节点涉及表单，则不自动通过
            if (CollectionUtils.isNotEmpty(nextTaskExtDOEditFields) || StringUtils.isNotEmpty(nextTaskExtDOFormKey)) {
                return;
            }
            if (ObjectUtils.notEqual(nextTaskExtDO.getAssigneeUserId(), bpmTaskExtDO.getAssigneeUserId())) {
                return;
            }
            approveTask(getLoginUserId(), new BpmTaskVo().setTaskId(nextTask.getId()).setId(nextTask.getId()).setReason("与上一节点同一审批人，自动通过"));
        }
    }

    private void fileRelevancyProcessInstance(Task task, BpmTaskExtDO bpmTaskExtDO, Map<String, Object> formVariables) {
        String formConf = bpmTaskExtDO.getFormConf();
        JSONObject formConfObj = JsonUtils.parseObject(formConf, JSONObject.class);
        assert formConfObj == null;
        JSONArray bodys = JsonUtils.parseObject(JsonUtils.toJsonString(formConfObj.get("body")), JSONArray.class);
        assert bodys == null;
        JSONObject bodyJSONObject = JsonUtils.parseObject(JsonUtils.toJsonString(bodys.get(0)), JSONObject.class);
        assert bodyJSONObject == null;
        //表单配置的具体信息
        JSONArray formConfDetails = JsonUtils.parseObject(JsonUtils.toJsonString(bodyJSONObject.get("body")), JSONArray.class);//表单组件配置项集合
        assert formConfDetails != null;

        Set<String> urls = new HashSet<>();
        //input-file类型上传时数据为集合形式
        List<Object> fileConfList = formConfDetails.stream().filter(jsonObject -> {
            JSONObject field = JsonUtils.parseObject(JsonUtils.toJsonString(jsonObject), JSONObject.class);
            return !ObjectUtils.notEqual(field.get("type"), "input-file");
        }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(fileConfList)) {
            List<Object> fileNameList = new ArrayList<>();
            fileConfList.forEach(item -> {
                JSONObject field = JsonUtils.parseObject(JsonUtils.toJsonString(item), JSONObject.class);
                Object name = field.get("name");
                fileNameList.add(name);
            });
            if (CollectionUtils.isNotEmpty(fileNameList)) {
                for (Object fileName : fileNameList) {
                    Object fileValueObj = formVariables.get(fileName);
                    List fileValueList = JsonUtils.parseObject(JsonUtils.toJsonString(fileValueObj), List.class);
                    if (CollectionUtils.isEmpty(fileValueList)) {
                        continue;
                    }
                    fileValueList.forEach(fileValue -> {
                        JSONObject fieldValueObj = JsonUtils.parseObject(JsonUtils.toJsonString(fileValue), JSONObject.class);
                        Object url = fieldValueObj.get("url");
                        if (ObjectUtils.isNotEmpty(url)) {
                            urls.add(url.toString());
                        }
                    });
                }
            }
        }

        //input-image类型上传时数据为字符串拼接形式
        List<Object> imageConfList = formConfDetails.stream().filter(jsonObject -> {
            JSONObject field = JsonUtils.parseObject(JsonUtils.toJsonString(jsonObject), JSONObject.class);
            return !ObjectUtils.notEqual(field.get("type"), "input-image");
        }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(imageConfList)) {
            List<Object> imageFileNameList = new ArrayList<>();
            imageConfList.forEach(item -> {
                JSONObject field = JsonUtils.parseObject(JsonUtils.toJsonString(item), JSONObject.class);
                Object name = field.get("name");
                imageFileNameList.add(name);
            });
            if (CollectionUtils.isNotEmpty(imageFileNameList)) {
                for (Object fileName : imageFileNameList) {
                    Object fileValueObj = formVariables.get(fileName);
                    String fileValueStr = fileValueObj.toString();
                    if (StringUtils.isEmpty(fileValueStr)) {
                        continue;
                    }
                    String[] urlArray = fileValueStr.split(",");
                    List<String> urlList = Arrays.asList(urlArray);
                    urls.addAll(urlList);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(urls)) {
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(task.getProcessInstanceId());
            FileUpdateReqDTO fileUpdateReqDTO = new FileUpdateReqDTO();
            fileUpdateReqDTO.setUrls(urls);
            fileUpdateReqDTO.setInstanceName(bpmProcessInstanceExtDO.getInstanceName());
            fileUpdateReqDTO.setProcessInstanceId(bpmProcessInstanceExtDO.getProcessInstanceId());
            fileUpdateReqDTO.setFormId(Long.valueOf(bpmTaskExtDO.getFormKey()));
            fileApi.updateFile(fileUpdateReqDTO);
        }

    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void rejectTask(Long userId, @Valid BpmTaskVo bpmTaskVO) {
//        Task task = checkTask(userId, bpmTaskVO.getId());
//        String processDefinitionId = task.getProcessDefinitionId();
//        String taskDefinitionKey = task.getTaskDefinitionKey();
//        BpmProcessDefinitionExtDO bpmProcessDefinitionExtDO = processDefinitionExtMapper.selectByProcessDefinitionId(processDefinitionId);
//        //获取节点对应规则
//        BpmTaskAssignRuleDO bpmTaskAssignRuleDO = bpmTaskAssignRuleMapper.selectListByModelIdAndTaskDefinitionKey(bpmProcessDefinitionExtDO.getModelId(), taskDefinitionKey);
//
//        // 如果该任务节点的不通过退回节点不为空，则进行退回操作
//        if (StringUtils.isNotBlank(bpmTaskAssignRuleDO.getRejectToTaskDefinitionKey())) {
//            BpmProcessInstanceRollbackReqVO rollbackReqVO = new BpmProcessInstanceRollbackReqVO();
//            rollbackReqVO.setComment(bpmTaskVO.getReason());
//            rollbackReqVO.setTaskId(bpmTaskVO.getId());
//            rollbackReqVO.setRollbackId(bpmTaskAssignRuleDO.getRejectToTaskDefinitionKey());
//            taskReturn(rollbackReqVO);
//            return;
//        }
//
//        bpmTaskVO.setComment(bpmTaskVO.getReason());
//        bpmTaskVO.setTaskId(bpmTaskVO.getId());
//        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
//        if (instance == null) {
//            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
//        }
//        processInstanceService.updateProcessInstanceExtReject(instance.getProcessInstanceId(), bpmTaskVO.getComment());
//
//        taskExtMapper.updateByTaskId(
//                new BpmTaskExtDO().setTaskId(task.getId()).setResult(BpmProcessInstanceResultEnum.REJECT.getResult())
//                        .setEndTime(LocalDateTime.now()).setReason(bpmTaskVO.getComment()));
//        // 处理抄送用户
//        if (!bpmCopyService.makeCopy(bpmTaskVO)) {
//            throw new RuntimeException("抄送任务失败");
//        }
//    }

    @Override
    public void updateTaskAssignee(Long userId, BpmTaskUpdateAssigneeReqVO reqVO) {
        Task task;
        if (userId != null) {
            task = checkTask(userId, reqVO.getId());
        } else {
            task = checkTask(getLoginUserId(), reqVO.getId());
        }
        updateTaskAssignee(task.getId(), reqVO.getAssigneeUserId());
    }

    @Override
    public void updateTaskAssignee(String id, Long userId) {
        taskService.setAssignee(id, String.valueOf(userId));
    }

    /**
     * @param userId 用户 id
     * @param taskId task id
     */
    private Task checkTask(Long userId, String taskId) {
        Task task = getTask(taskId);
        if (task == null) {
            throw exception(TASK_COMPLETE_FAIL_NOT_EXISTS);
        }
        if (!Objects.equals(userId, NumberUtils.parseLong(task.getAssignee()))) {
            throw exception(TASK_COMPLETE_FAIL_ASSIGN_NOT_SELF);
        }
        return task;
    }

    @Override
    public void createTaskExt(Task task) {
        BpmTaskExtDO taskExtDO =
                BpmTaskConvert.INSTANCE.convert2TaskExt(task).setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        String processDefinitionId = task.getProcessDefinitionId();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        BpmProcessDefinitionExtDO bpmProcessDefinitionExtDO = processDefinitionExtMapper.selectByProcessDefinitionId(processDefinitionId);
        //获取节点对应规则
        BpmTaskAssignRuleDO bpmTaskAssignRuleDO = bpmTaskAssignRuleMapper.selectListByModelIdAndTaskDefinitionKey(bpmProcessDefinitionExtDO.getModelId(), taskDefinitionKey);
        String formKey = bpmTaskAssignRuleDO.getFormKey();
        if (StringUtils.isNotBlank(formKey) && !"0".equals(formKey)) {
            BpmFormDO form = bpmFormService.getForm(Long.valueOf(formKey));
            if (form != null)
                taskExtDO.setFormKey(formKey).setFormConf(form.getConf()).setFormFields(form.getFields());
        }
        Long formId = bpmTaskAssignRuleDO.getFormId();
        if (ObjectUtils.isNotEmpty(formId) && formId != 0) {
            taskExtDO.setFormId(formId);
            List<String> editFields = bpmTaskAssignRuleDO.getEditFields();
            List<String> downloadFields = bpmTaskAssignRuleDO.getDownloadFields();
            if (CollectionUtils.isNotEmpty(editFields) && editFields.size() > 0) {
                taskExtDO.setEditFields(editFields);
            }
            if (CollectionUtils.isNotEmpty(downloadFields) && downloadFields.size() > 0) {
                taskExtDO.setDownloadFields(downloadFields);
            }
        }

//        String copyUserIds = bpmTaskAssignRuleDO.getCopyUserIds();
        Set<Long> userIdSet = new HashSet<>();
        if (ObjectUtils.isNotEmpty(bpmTaskAssignRuleDO.getCopyUserOptions())) {
            CollUtil.addAll(userIdSet, getTaskCopyUsersByUser(bpmTaskAssignRuleDO));
        }
        if (ObjectUtils.isNotEmpty(bpmTaskAssignRuleDO.getCopyPostOptions())) {
            CollUtil.addAll(userIdSet, getTaskCopyUsersByPost(bpmTaskAssignRuleDO));
        }
        if (ObjectUtils.isNotEmpty(bpmTaskAssignRuleDO.getCopyScriptOptions())) {
            CollUtil.addAll(userIdSet, getTaskCopyUsersByScript(bpmTaskAssignRuleDO, task));
        }
        if (CollectionUtils.isNotEmpty(userIdSet)) {
            String copyUserIds = userIdSet.stream().map(String::valueOf).collect(Collectors.joining(","));
            taskExtDO.setCopyUserIds(copyUserIds);
        }

        //获取是否有同类型的驳回节点
        log.info("判断是否有同类型的驳回节点 processInstanceId:{}, taskId:{}", task.getProcessInstanceId(), task.getId());
        List<HistoricTaskInstance> runTaskList = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));

        if (runTaskKeyList.contains(taskDefinitionKey)) {
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .taskDefinitionKey(taskDefinitionKey)
                    .finished()
                    .orderByHistoricTaskInstanceStartTime()
                    .desc()
                    .list();
            if (ObjectUtils.isNotEmpty(list)) {
                HistoricTaskInstance historicTaskInstance = list.get(0);
                BpmTaskExtDO bpmTaskExtDO = taskExtMapper.selectByTaskId(historicTaskInstance.getId());
                if (ObjectUtils.isNotEmpty(bpmTaskExtDO)) {
                    log.info("表单为：{}，上一节点表单数据为{}", bpmTaskExtDO, bpmTaskExtDO.getFormVariables());
                    taskExtDO.setFormVariables(bpmTaskExtDO.getFormVariables());
                    taskExtDO.setFormFields(bpmTaskExtDO.getFormFields());
                    taskExtDO.setFormKey(bpmTaskExtDO.getFormKey());
                    taskExtDO.setFormConf(bpmTaskExtDO.getFormConf());
                }
                //将之前的同类型节点的表单配置置0
                list.forEach(item -> {
                    BpmTaskExtDO bpmTaskExtDO1 = taskExtMapper.selectByTaskId(item.getId());
                    if (ObjectUtils.isNotEmpty(bpmTaskExtDO1)) {
                        bpmTaskExtDO1.setReadAble(0);
                        taskExtMapper.updateByTaskId(bpmTaskExtDO1);
                    }
                });
            }
        }
        //判断是否是用户确认节点
        List<BpmActivityRespVO> activityList = bpmActivityService.getActivityListByProcessInstanceId(task.getProcessInstanceId());
        List<String> taskKeyList = activityList.stream().filter(item -> "userTask".equals(item.getType())).sorted(Comparator.comparing(BpmActivityRespVO::getStartTime)).map(BpmActivityRespVO::getKey).collect(Collectors.toList());
        //获取用户确认节点的key
        if (ObjectUtils.isNotEmpty(taskKeyList)) {
            String taskKey = taskKeyList.get(0);
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(task.getProcessInstanceId());
            bpmProcessInstanceExtDO.setDeleteAble(false);
            //如果当前节点是发起人提交节点
            if (taskKey.equals(taskDefinitionKey)) {
                if (ObjectUtils.isEmpty(bpmProcessInstanceExtDO)) {
                    log.error("流程定义不存在 processInstanceId:{},taskId:{}", task.getProcessInstanceId(), task.getId());
                    throw new BusinessException("流程定义不存在");
                }
                taskExtDO.setFormVariables(bpmProcessInstanceExtDO.getFormVariables());
                BpmProcessInstanceRespVO processInstanceVO = processInstanceService.getProcessInstanceVO(task.getProcessInstanceId());
                if (ObjectUtils.isEmpty(processInstanceVO)) {
                    log.error("流程定义的RespVO不存在 processInstanceId:{},taskId:{}", task.getProcessInstanceId(), task.getId());
                    throw new BusinessException("流程定义的RespVO不存在");
                }
                taskExtDO.setFormConf(processInstanceVO.getProcessDefinition().getFormConf());
                taskExtDO.setFormFields(processInstanceVO.getProcessDefinition().getFormFields());
                taskExtDO.setFormKey(String.valueOf(processInstanceVO.getProcessDefinition().getFormId()));
                bpmProcessInstanceExtDO.setReadAble(0);
                bpmProcessInstanceExtDO.setDeleteAble(true);
            }
            bpmProcessInstanceExtMapper.updateByProcessInstanceId(bpmProcessInstanceExtDO);
        }
        taskExtDO.setReadAble(1);
        taskExtMapper.insert(taskExtDO);

        // 如果是第一个发起人确认的内置节点 则直接完成且不发消息
        if (isFirstConfirmTask(task)) return;

        // 异步发送待办消息
        ThreadUtil.execAsync(() -> {
            if (Objects.nonNull(task.getAssignee())) {
                sendTodoTaskImMessage(task);
            }
        });
        //售后部门流程发送企业微信消息 -- 7
        if (PROCESS_DEFINITION_TYPE_POST_SALE.equals(bpmProcessDefinitionExtDO.getType())) {
            ThreadUtil.execAsync(() -> {
                if (Objects.nonNull(task.getAssignee())) {
                    sendPostSaleQiyeWechatMessage(task);
                }
            });
        }
    }

    private boolean isFirstConfirmTask(Task task) {
        ProcessInstance processInstance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (Objects.equals(task.getAssignee(), processInstance.getStartUserId()) && isStartUserNode(task.getId())) {
            List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .orderByHistoricTaskInstanceStartTime().desc()
                    .list();
            // 如果历史任务数大于1，表示是回退后的节点，不自动审批
            if (!CollUtil.isEmpty(tasks) && tasks.size() > 1) {
                return false;
            }

            taskService.complete(task.getId(), processInstance.getProcessVariables());

            BpmTaskExtDO bpmTaskExtDO = taskExtMapper.selectByTaskId(task.getId());
            bpmTaskExtDO.setTaskId(task.getId())
                    .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());
            taskExtMapper.updateByTaskId(bpmTaskExtDO);
            return true;
        }
        return false;
    }


    /**
     * 根据任务ID判断当前节点是否为开始节点后面的第一个用户任务节点
     *
     * @param taskId 任务Id
     * @return
     */
    public boolean isStartUserNode(String taskId) {
        //判断当前是否是第一个发起任务节点，若是就put变量isStartNode为True,让相应的表单可以编辑
        boolean isStartNode = false;
        if (Objects.nonNull(taskId)) {
            // 当前任务 task
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            // 获取流程定义信息
            if (task != null) {
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(task.getProcessDefinitionId()).singleResult();
                // 获取所有节点信息
                Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
                // 获取全部节点列表，包含子节点
                Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
                // 获取当前任务节点元素
                FlowElement source = null;
                if (allElements != null) {
                    for (FlowElement flowElement : allElements) {
                        // 类型为用户节点
                        if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                            // 获取节点信息
                            source = flowElement;
                            List<SequenceFlow> inFlows = FlowableUtils.getElementIncomingFlows(source);
                            if (inFlows.size() == 1) {
                                FlowElement sourceFlowElement = inFlows.get(0).getSourceFlowElement();
                                if (sourceFlowElement instanceof StartEvent) {// 源是开始节点
                                    isStartNode = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return isStartNode;
    }

    private void sendTodoTaskImMessage(Task task) {
        ThreadUtil.safeSleep(1000);
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(task.getProcessInstanceId());

        bpmProcessInstanceExtDO = circleGetBpmProcessInstanceExt(task, bpmProcessInstanceExtDO);

        CommonResult<String> sysUserAccount = adminUserApi.getSysUserAccount(task.getAssignee());
        AdminUserRespDTO startUser = adminUserApi.getUser(bpmProcessInstanceExtDO.getStartUserId()).getData();
        String content = bpmProcessInstanceExtDO.getName();
        String msg = String.format("您有一条新的待办消息【%s】，请及时处理！", content);

        ImCustomMsgContent msgContent = new ImCustomMsgContent();
        msgContent.setText(msg);
        msgContent.setBusinessType(ImMessageBusinessType.BPM_TASK_TODO.getBusinessType());

        JSONObject params = new JSONObject();
        params.put("oaUserId", task.getAssignee());
        params.put("processInstanceId", bpmProcessInstanceExtDO.getProcessInstanceId());
        params.put("processDefinitionId", bpmProcessInstanceExtDO.getProcessDefinitionId());
        params.put("taskId", task.getId());
        msgContent.setParams(JsonUtils.toJsonString(params));
        imWrapper.sendAccountCustomMessage(sysUserAccount.getData(), msgContent);
    }

    private BpmProcessInstanceExtDO circleGetBpmProcessInstanceExt(Task task, BpmProcessInstanceExtDO bpmProcessInstanceExtDO) {
        int time = 1;
        while (Objects.isNull(bpmProcessInstanceExtDO) && time <= 3) {
            bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(task.getProcessInstanceId());
            if (Objects.nonNull(bpmProcessInstanceExtDO)) {
                break;
            }
            ThreadUtil.safeSleep(1000);
            time++;
        }
        return bpmProcessInstanceExtDO;
    }

    @Override
    public void updateTaskExtComplete(Task task) {
        BpmTaskExtDO bpmTaskExtDO = taskExtMapper.selectByTaskId(task.getId());
        if (Objects.isNull(bpmTaskExtDO)) {
            log.info("[updateTaskExtAssign][taskId({}) 查找不到对应的记录，可能存在问题]", task.getId());
            return;
        }
        BpmTaskExtDO taskExtDO = BpmTaskConvert.INSTANCE.convert2TaskExt(task)
                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult())
                .setEndTime(LocalDateTime.now())
                .setReadAble(bpmTaskExtDO.getReadAble())
                .setName(bpmTaskExtDO.getName());
        taskExtMapper.updateByTaskId(taskExtDO);
    }

    private void turnOnOffSiteClockingForBusinessTrips(Task task) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .orderByHistoricTaskInstanceStartTime();
        List<HistoricTaskInstance> historicTaskInstanceList = historicTaskInstanceQuery.asc().list();
        HistoricTaskInstance firstHistoricTaskInstance = historicTaskInstanceList.get(0);
        String firstTaskDefinitionKey = firstHistoricTaskInstance.getTaskDefinitionKey();
        List<HistoricTaskInstance> firstTaskDefinitionKeyTaskList = historicTaskInstanceQuery.taskDefinitionKey(firstTaskDefinitionKey).list();
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(task.getProcessInstanceId());
        //默认初始表单
        Map<String, Object> formVariables = bpmProcessInstanceExtDO.getFormVariables();
        //当回退到第一个任务节点时，获取第一个任务节点的表单数据
        if (firstTaskDefinitionKeyTaskList.size() > 1) {
            HistoricTaskInstance historicTaskInstance = firstTaskDefinitionKeyTaskList.get(firstTaskDefinitionKeyTaskList.size() - 1);
            BpmTaskExtDO formVariablesBpmTaskExtDO = taskExtMapper.selectByTaskId(historicTaskInstance.getId());
            if (Objects.nonNull(formVariablesBpmTaskExtDO.getFormVariables())) {
                formVariables = formVariablesBpmTaskExtDO.getFormVariables();
            }
        }
        log.info("出差流程表单内容：{}", JsonUtils.toJsonString(formVariables));
        Object startTime = formVariables.get("start_time");
        Object businessUserId = formVariables.get("userId");
        LocalDate startDate;
        if (Objects.nonNull(startTime) && Objects.nonNull(businessUserId)) {
            long start = Long.parseLong(startTime.toString()) * 1000;
            startDate = Instant.ofEpochMilli(start).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();
            if (now.isEqual(startDate) || now.isAfter(startDate)) {
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

    @Override
    public void updateTaskExtCancel(String taskId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                HistoricTaskInstance task = getHistoricTask(taskId);
                if (task == null) {
                    return;
                }

                BpmTaskExtDO taskExt = taskExtMapper.selectByTaskId(taskId);
                if (taskExt == null) {
                    log.error("[updateTaskExtCancel][taskId({}) 查找不到对应的记录，可能存在问题]", taskId);
                    return;
                }
                if (BpmProcessInstanceResultEnum.isEndResult(taskExt.getResult())) {
                    log.error("[updateTaskExtCancel][taskId({}) 处于结果({})，无需进行更新]", taskId, taskExt.getResult());
                    return;
                }

                taskExtMapper.updateById(taskExt.setResult(BpmProcessInstanceResultEnum.CANCEL.getResult())
                        .setEndTime(LocalDateTime.now()).setReason(BpmProcessInstanceDeleteReasonEnum.translateReason(task.getDeleteReason())));
            }

        });
    }

    @Override
    public void updateTaskExtAssign(Task task) {
        BpmTaskExtDO bpmTaskExtDO = taskExtMapper.selectByTaskId(task.getId());
        if (Objects.isNull(bpmTaskExtDO)) {
            log.info("[updateTaskExtAssign][taskId({}) 查找不到对应的记录，可能存在问题]", task.getId());
            return;
        }
        String name = bpmTaskExtDO.getName();
        Long assigneeUserId = bpmTaskExtDO.getAssigneeUserId();
        CommonResult<AdminUserRespDTO> user = adminUserApi.getUser(assigneeUserId);
        if (ObjectUtils.isEmpty(user.getData())) {
            log.info("[user][assigneeUserId({}) 无法获取用户信息]", assigneeUserId);
        } else {
            String nickname = user.getData().getNickname();
            name = name + "（由" + nickname + "转发）";
        }
        bpmTaskExtDO.setAssigneeUserId(NumberUtils.parseLong(task.getAssignee()));
        bpmTaskExtDO.setName(name);
        taskExtMapper.updateByTaskId(bpmTaskExtDO);
        // 异步发送待办消息
        ThreadUtil.execAsync(() -> {
            if (Objects.nonNull(task.getAssignee())) {
                sendTodoTaskImMessage(task);
            }
        });
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                ProcessInstance processInstance =
                        processInstanceService.getProcessInstance(task.getProcessInstanceId());
                AdminUserRespDTO startUser = adminUserApi.getUser(Long.valueOf(processInstance.getStartUserId())).getCheckedData();
                messageService.sendMessageWhenTaskAssigned(
                        BpmTaskConvert.INSTANCE.convert(processInstance, startUser, task));
            }
        });
    }

    private Task getTask(String id) {
        return taskService.createTaskQuery().taskId(id).singleResult();
    }

    private HistoricTaskInstance getHistoricTask(String id) {
        return historyService.createHistoricTaskInstanceQuery().taskId(id).singleResult();
    }


    @Override
    public Integer unreadTodoTaskCount(Long userId) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskAssignee(String.valueOf(userId));

        List<Task> taskList = taskQuery.list();

        List<String> taskIds = taskList.stream().map(Task::getId).collect(Collectors.toList());
        List<BpmTaskExtDO> bpmTaskExtDOs = taskExtMapper.selectListByTaskIds(taskIds);

        return bpmTaskExtDOs.stream()
                .filter(t -> Objects.equals(t.getReadState(), 0))
                .collect(Collectors.toList()).size();
    }

    public List<BpmUserTaskRespVO> findReturnTaskList(String taskId) {
        // 获取当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        // 获取当前任务节点元素
        UserTask source = null;
        if (flowElements != null) {
            for (FlowElement flowElement : flowElements) {
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = (UserTask) flowElement;
                }
            }
        }
        // 获取节点的所有路线
        List<List<UserTask>> roads = FlowableUtils.findRoad(source, null, null, null);
        // 可退回的节点列表
        List<UserTask> userTaskList = new ArrayList<>();
        for (List<UserTask> road : roads) {
            if (userTaskList.size() == 0) {
                // 还没有可回退节点直接添加
                userTaskList = road;
            } else {
                // 如果已有回退节点，则比对取交集部分
                userTaskList.retainAll(road);
            }
        }
        // 去重
        List<UserTask> userTaskListRtn = new ArrayList<>();
        for (UserTask t : userTaskList) {
            boolean b = true;
            for (UserTask tr : userTaskListRtn) {
                if (tr.getId().equals(t.getId())) {
                    b = false;
                    break;
                }
            }
            if (b) {
                userTaskListRtn.add(t);
            }
        }

        return BpmTaskConvert.INSTANCE.copyList(userTaskListRtn, BpmUserTaskRespVO.class);
    }

    @Override
    public void taskReturn(BpmProcessInstanceRollbackReqVO rollbackReqVO) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(rollbackReqVO.getTaskId()).singleResult();
        if (task.isSuspended()) {
            throw new BusinessException("任务处于挂起状态");
        }
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        // 获取跳转的节点元素
        FlowElement target = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 当前任务节点元素
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = flowElement;
                }
                // 跳转的节点元素
                if (flowElement.getId().equals(rollbackReqVO.getRollbackId())) {
                    target = flowElement;
                }
            }
        }

        // 从当前节点向前扫描
        // 如果存在路线上不存在目标节点，说明目标节点是在网关上或非同一路线上，不可跳转
        // 否则目标节点相对于当前节点，属于串行
        Boolean isSequential = FlowableUtils.iteratorCheckSequentialReferTarget(source, rollbackReqVO.getRollbackId(), null, null);
        if (!isSequential) {
            throw new BusinessException("当前节点相对于目标节点，不属于串行关系，无法回退");
        }

        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需退回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(target, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        if (CollectionUtils.isEmpty(runTaskList)) {
            throw new BusinessException("当前任务节点已被处理");
        }

        List<HistoricTaskInstance> currentTaskKeyHistoricList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .taskDefinitionKey(task.getTaskDefinitionKey())
                .taskCompletedAfter(task.getCreateTime())
                .list();
        if (CollectionUtils.isNotEmpty(currentTaskKeyHistoricList)) {
            throw new BusinessException("当前节点已有任务被处理，无法撤回");
        }
        try {
            // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetKey 跳转到的节点(1)
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdsToSingleActivityId(currentIds, rollbackReqVO.getRollbackId()).changeState();

            // 设置回退意见
            currentTaskIds.forEach(currentTaskId ->
                    taskExtMapper.updateByTaskId(new BpmTaskExtDO().setTaskId(currentTaskId).setEndTime(LocalDateTime.now()).setResult(rollbackReqVO.getResult()).setReason(rollbackReqVO.getComment())));
        } catch (FlowableObjectNotFoundException e) {
            throw new BusinessException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new BusinessException("无法取消或开始活动");
        }
    }

    @Override
    @Transactional
    public void rejectTask(Long userId, @Valid BpmTaskVo bpmTaskVO) {
        bpmTaskVO.setTaskId(bpmTaskVO.getId());
        if (taskService.createTaskQuery().taskId(bpmTaskVO.getTaskId()).singleResult().isSuspended()) {
            throw new BusinessException("任务处于挂起状态!");
        }
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(bpmTaskVO.getTaskId()).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();

        // 如果配置了节点向前回退位置，则进行定向回退
        if (taskJump(bpmTaskVO, task, processDefinition)) return;

        // 否则回退上一节点
        List<BpmActivityRespVO> activityList = bpmActivityService.getActivityListByProcessInstanceId(task.getProcessInstanceId());
        List<BpmActivityRespVO> bpmActivityRespVOList = activityList.stream()
                .filter(item -> "userTask".equals(item.getType()))
                .sorted(Comparator.comparing(BpmActivityRespVO::getStartTime))
                .collect(Collectors.toList());
        if (ObjectUtils.isNotEmpty(bpmActivityRespVOList) && bpmActivityRespVOList.size() > 1) {
            BpmActivityRespVO bpmActivityRespVO = bpmActivityRespVOList.get(bpmActivityRespVOList.size() - 2);
            if (ObjectUtils.isNotEmpty(bpmActivityRespVO)) {
                taskExtMapper.updateByTaskId(new BpmTaskExtDO().setTaskId(bpmActivityRespVO.getTaskId()).setReadAble(0).setCopyUserIds(bpmTaskVO.getCopyUserIds()));
            }
        }

        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    // 获取节点信息
                    source = flowElement;
                }
            }
        }

        // 目的获取所有跳转到的节点 targetIds
        // 获取当前节点的所有父级用户任务节点
        // 深度优先算法思想：延边迭代深入
        List<UserTask> parentUserTaskList = FlowableUtils.iteratorFindParentUserTasks(source, null, null);
        if (parentUserTaskList == null || parentUserTaskList.size() == 0) {
            throw new BusinessException("当前节点为初始任务节点，不能退回");
        }
        // 获取活动 ID 即节点 Key
        List<String> parentUserTaskKeyList = new ArrayList<>();
        parentUserTaskList.forEach(item -> parentUserTaskKeyList.add(item.getId()));
        // 获取全部历史节点活动实例，即已经走过的节点历史，数据采用开始时间升序
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().list();
        // 数据清洗，将回滚导致的脏数据清洗掉
        List<String> lastHistoricTaskInstanceList = FlowableUtils.historicTaskInstanceClean(Objects.requireNonNull(allElements), historicTaskInstanceList);
        // 此时历史任务实例为倒序，获取最后走的节点
        List<String> targetIds = new ArrayList<>();
        // 循环结束标识，遇到当前目标节点的次数
        int number = 0;
        StringBuilder parentHistoricTaskKey = new StringBuilder();
        for (String historicTaskInstanceKey : lastHistoricTaskInstanceList) {
            // 当会签时候会出现特殊的，连续都是同一个节点历史数据的情况，这种时候跳过
            if (String.valueOf(parentHistoricTaskKey).equals(historicTaskInstanceKey)) {
                continue;
            }
            parentHistoricTaskKey = new StringBuilder(historicTaskInstanceKey);
            if (historicTaskInstanceKey.equals(task.getTaskDefinitionKey())) {
                number++;
            }
            // 在数据清洗后，历史节点就是唯一一条从起始到当前节点的历史记录，理论上每个点只会出现一次
            // 在流程中如果出现循环，那么每次循环中间的点也只会出现一次，再出现就是下次循环
            // number == 1，第一次遇到当前节点
            // number == 2，第二次遇到，代表最后一次的循环范围
            if (number == 2) {
                break;
            }
            // 如果当前历史节点，属于父级的节点，说明最后一次经过了这个点，需要退回这个点
            if (parentUserTaskKeyList.contains(historicTaskInstanceKey)) {
                targetIds.add(historicTaskInstanceKey);
            }
        }


        // 目的获取所有需要被跳转的节点 currentIds
        // 取其中一个父级任务，因为后续要么存在公共网关，要么就是串行公共线路
        UserTask oneUserTask = parentUserTaskList.get(0);
        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需驳回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(oneUserTask, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));


        // 规定：并行网关之前节点必须需存在唯一用户任务节点，如果出现多个任务节点，则并行网关节点默认为结束节点，原因为不考虑多对多情况
        if (targetIds.size() > 1 && currentIds.size() > 1) {
            throw new BusinessException("任务出现多对多情况，无法撤回");
        }

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));

        try {
            // 如果父级任务多于 1 个，说明当前节点不是并行节点，原因为不考虑多对多情况
            if (targetIds.size() > 1) {
                // 1 对 多任务跳转，currentIds 当前节点(1)，targetIds 跳转到的节点(多)
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId()).
                        moveSingleActivityIdToActivityIds(currentIds.get(0), targetIds).changeState();
            }
            // 如果父级任务只有一个，因此当前任务可能为网关中的任务
            if (targetIds.size() == 1) {
                // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetIds.get(0) 跳转到的节点(1)
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId())
                        .moveActivityIdsToSingleActivityId(currentIds, targetIds.get(0)).changeState();
            }

            // 设置驳回意见
            currentTaskIds.forEach(currentTaskId ->
                    taskExtMapper.updateByTaskId(new BpmTaskExtDO().setTaskId(task.getId())
                            .setEndTime(LocalDateTime.now())
                            .setResult(BpmProcessInstanceResultEnum.REJECT.getResult())
                            .setReason(bpmTaskVO.getReason())
                            .setCopyUserIds(bpmTaskVO.getCopyUserIds())));

            // 处理抄送用户
            bpmTaskVO.setProcInsId(task.getProcessInstanceId());
            bpmCopyService.makeCopy(bpmTaskVO);
        } catch (FlowableObjectNotFoundException e) {
            throw new BusinessException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new BusinessException("无法取消或开始活动");
        }

    }

    private boolean taskJump(BpmTaskVo bpmTaskVO, Task task, ProcessDefinition processDefinition) {
        String taskDefinitionKey = task.getTaskDefinitionKey();
        BpmProcessDefinitionExtDO bpmProcessDefinitionExtDO = processDefinitionExtMapper.selectByProcessDefinitionId(processDefinition.getId());
        //获取节点对应规则
        BpmTaskAssignRuleDO bpmTaskAssignRuleDO = bpmTaskAssignRuleMapper.selectListByModelIdAndTaskDefinitionKey(bpmProcessDefinitionExtDO.getModelId(), taskDefinitionKey);

        // 如果该任务节点的不通过退回节点不为空，则进行退回操作
        if (StringUtils.isNotBlank(bpmTaskAssignRuleDO.getRejectToTaskDefinitionKey())) {
            BpmProcessInstanceRollbackReqVO rollbackReqVO = new BpmProcessInstanceRollbackReqVO();
            rollbackReqVO.setComment(bpmTaskVO.getReason());
            rollbackReqVO.setTaskId(bpmTaskVO.getId());
            rollbackReqVO.setRollbackId(bpmTaskAssignRuleDO.getRejectToTaskDefinitionKey());
            rollbackReqVO.setResult(BpmProcessInstanceResultEnum.BACK.getResult());
            try {
                taskReturn(rollbackReqVO);
            } catch (Exception e) {
                // 退回异常则退回上一个节点
                return false;
            }

            clearMiddleReadAble(task, bpmTaskAssignRuleDO);
            return true;
        }
        return false;
    }


    private void clearMiddleReadAble(Task task, BpmTaskAssignRuleDO bpmTaskAssignRuleDO) {
        List<BpmActivityRespVO> activityList = bpmActivityService.getActivityListByProcessInstanceId(task.getProcessInstanceId());
        List<BpmActivityRespVO> bpmActivityRespVOList = activityList.stream()
                .filter(item -> "userTask".equals(item.getType()))
                .sorted(Comparator.comparing(BpmActivityRespVO::getStartTime))
                .collect(Collectors.toList());
        if (ObjectUtils.isNotEmpty(bpmActivityRespVOList)) {
            BpmActivityRespVO bpmActivityRespVO = bpmActivityRespVOList.stream()
                    .filter(item -> item.getKey().equals(bpmTaskAssignRuleDO.getRejectToTaskDefinitionKey()))
                    .max(Comparator.comparing(BpmActivityRespVO::getStartTime))
                    .get();
            if (ObjectUtils.isNotEmpty(bpmActivityRespVO)) {
                List<BpmActivityRespVO> bpmActivityRespVOList1 = bpmActivityRespVOList.stream().filter(item -> item.getStartTime().isAfter(bpmActivityRespVO.getStartTime())).collect(Collectors.toList());
                bpmActivityRespVOList1.forEach(item -> {
                    taskExtMapper.updateByTaskId(new BpmTaskExtDO().setTaskId(item.getTaskId()).setReadAble(0));
                });
            }
        }
    }

    @Override
    public void readTodoTask(String taskId) {
        BpmTaskExtDO taskExtDO = taskExtMapper.selectByTaskId(taskId);
        if (Objects.isNull(taskExtDO)) {
            throw new BusinessException("待办任务不存在");
        }

        taskExtDO.setReadState(1);
        taskExtMapper.updateByTaskId(taskExtDO);
    }

    public void revokeProcess(String processInstanceId, String message) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if (task == null) {
            throw new BusinessException("流程未启动或已执行完成，无法撤回");
        }

        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .orderByTaskCreateTime()
                .asc()
                .list();
        String myTaskId = null;
        HistoricTaskInstance myTask = null;
        for (HistoricTaskInstance hti : htiList) {
            if (getLoginUserId().toString().equals(hti.getAssignee())) {
                myTaskId = hti.getId();
                myTask = hti;
                break;
            }
        }
        if (null == myTaskId) {
            throw new BusinessException("该任务非当前用户提交，无法撤回");
        }

        String processDefinitionId = myTask.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        String myActivityId = null;
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
                .executionId(myTask.getExecutionId()).finished().list();
        for (HistoricActivityInstance hai : haiList) {
            if (myTaskId.equals(hai.getTaskId())) {
                myActivityId = hai.getActivityId();
                break;
            }
        }
        FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);

        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<>(flowNode.getOutgoingFlows());
        flowNode.getOutgoingFlows().clear();

        List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("sid-" + UUID.randomUUID().toString());
        newSequenceFlow.setSourceFlowElement(flowNode);
        newSequenceFlow.setTargetFlowElement(myFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        flowNode.setOutgoingFlows(newSequenceFlowList);

        taskService.resolveTask(task.getId());
        taskService.claim(task.getId(), getLoginUserId().toString());
        taskService.complete(task.getId());
        taskExtMapper.updateByTaskId(new BpmTaskExtDO().setTaskId(task.getId()).setResult(BpmProcessInstanceResultEnum.RECALL.getResult()).setReason(message));
        flowNode.setOutgoingFlows(oriSequenceFlows);
    }


    //TODO 测试导出所有已办流程
    @Override
    public void exportAllTask(BpmTaskDoneExcVo excVO, HttpServletRequest request, HttpServletResponse response) {
        HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery().finished().orderByProcessInstanceEndTime().desc();
        if (excVO.getBeginTime() != null) {
            processInstanceQuery.startedAfter(DateUtil.parse(toDateFormat(excVO.getBeginTime()), DatePattern.NORM_DATE_PATTERN));
        }
        if (excVO.getEndTime() != null) {
            processInstanceQuery.finishedBefore(DateUtil.parse(toDateFormat(excVO.getEndTime()), DatePattern.NORM_DATE_PATTERN));
        }
        if (excVO.getUserId() != null) {
            processInstanceQuery.startedBy(excVO.getUserId().toString());
        }

        List<HistoricProcessInstance> list = processInstanceQuery.list();
        List<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOS =
                bpmProcessInstanceExtMapper.selectListByProcessInstanceIds(list.stream().map(HistoricProcessInstance::getId).collect(Collectors.toList()));
        if (StrUtil.isNotBlank(excVO.getName())) {
            bpmProcessInstanceExtDOS = bpmProcessInstanceExtDOS.stream()
                    .filter(bpmProcessInstanceExtDO -> excVO.getName().equals(bpmProcessInstanceExtDO.getName())).collect(Collectors.toList());
        }
        if (excVO.getDeptId() != null) {
            bpmProcessInstanceExtDOS = bpmProcessInstanceExtDOS.stream().filter(t -> {
                Long startUserId = t.getStartUserId();
                CommonResult<AdminUserRespDTO> user = adminUserApi.getUser(startUserId);

                // 使用 Objects.nonNull 来检查 user 和 user.getData() 是否为null
                if (Objects.nonNull(user) && Objects.nonNull(user.getData())) {
                    Long deptId = user.getData().getDeptId();
                    return excVO.getDeptId().equals(deptId);
                }
                return false; // 返回false以过滤掉不符合条件的数据
            }).collect(Collectors.toList());
        }

        Map<String, List<BpmProcessInstanceExtDO>> processInstanceExtMap = bpmProcessInstanceExtDOS.stream()
                .collect(Collectors.groupingBy(t -> {
                    String processName = t.getName();
                    if (processName.contains("/") || processName.contains("\\"))
                        processName = processName.replace("/", "、").replace("\\", "、");
                    return processName;
                }));

        ExcelWriter writer = ExcelUtil.getWriter();
        String filename = "流程.xlsx";

        for (String processName : processInstanceExtMap.keySet()) {
            List<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOList = processInstanceExtMap.get(processName);
            //获取并设置Excel表头
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtDOList.get(0);

            String processDefinitionId = bpmProcessInstanceExtDO.getProcessDefinitionId();
            BpmProcessDefinitionExtDO bpmProcessDefinitionExtDO = processDefinitionExtMapper.selectByProcessDefinitionId(processDefinitionId);
            List<String> formFields = bpmProcessDefinitionExtDO.getFormFields();
            if (processName.contains("/") || processName.contains("\\"))
                processName = processName.replace("/", "、").replace("\\", "、");
            writer.setSheet(processName);
            CellStyle headerCellStyle = createHeaderCellStyle(writer.getWorkbook());
            if (formFields != null && !formFields.isEmpty()) {
                int columnIndex = 0;
                for (String formField : formFields) {
                    JSONObject jsonObject = JSONObject.parseObject(formField);
                    String title = jsonObject.getString("title");
                    String column = getColumnLabel(columnIndex++);
                    column = column + 1;

                    writer.writeCellValue(column, title);
                    writer.disableDefaultStyle();
                    writer.setRowStyleIfHasData(0, headerCellStyle);
                }
            }
            //添加数据
            int num = 1;
            for (BpmProcessInstanceExtDO bpmProcessInstanceExt : bpmProcessInstanceExtDOList) {

                String processDefinitionId2 = bpmProcessInstanceExt.getProcessDefinitionId();
                BpmProcessDefinitionExtDO bpmProcessDefinitionExtDO2 = processDefinitionExtMapper.selectByProcessDefinitionId(processDefinitionId2);
                List<String> formFields2 = bpmProcessDefinitionExtDO2.getFormFields();
                Map<String, Object> formVariables = bpmProcessInstanceExt.getFormVariables();

                if (formFields2 != null && !formFields2.isEmpty()) {
                    int columnIndex = 0;
                    num++;
                    for (String formField : formFields2) {
                        JSONObject jsonObject = JSONObject.parseObject(formField);
                        String field = jsonObject.getString("field");
                        String column = getColumnLabel(columnIndex++);
                        column = column + num;
                        Object value = formVariables.get(field);
                        if (field != null) {
                            if (field.equals("userId")) {
                                CommonResult<AdminUserRespDTO> user = adminUserApi.getUser(Long.valueOf(value.toString()));
                                if (Objects.nonNull(user) && Objects.nonNull(user.getData())) {
                                    value = user.getData().getNickname();
                                }
                            }
                            if (field.equals("deptId")) {
                                CommonResult<DeptRespDTO> dept = deptApi.getDept(Long.valueOf(value.toString()));
                                if (Objects.nonNull(dept) && Objects.nonNull(dept.getData())) {
                                    value = dept.getData().getName();
                                }
                            }
                        }
                        if (value == null) {
                            writer.writeCellValue(column, "");
                        } else {
                            writer.writeCellValue(column, value.toString());
                        }
                    }
                }
            }
        }
        this.setFilenameHeader(request, response, filename);
        try {
            OutputStream os = response.getOutputStream();
            writer.flush(os, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.close();
        log.info("流程导出成功：{}", filename);
    }

    //设置表头样式
    public CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // 设置字体样式
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        return style;
    }

    private static void setFilenameHeader(HttpServletRequest req, HttpServletResponse resp, String filename) {
        try {
            String encodedFilename = filename;
            String userAgent = req.getHeader("User-Agent");

            // 根据不同的浏览器设置不同的文件名编码
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                if (userAgent.contains("msie") || userAgent.contains("trident")) {
                    // IE浏览器
                    encodedFilename = URLEncoder.encode(filename, "UTF-8");
                    encodedFilename = encodedFilename.replace("+", "%20");  // 替换空格为%20
                } else if (userAgent.contains("firefox")) {
                    // 火狐浏览器
                    encodedFilename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
                } else if (userAgent.contains("chrome")) {
                    // Chrome浏览器
                    encodedFilename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
                } else {
                    // 其他浏览器
                    encodedFilename = URLEncoder.encode(filename, "UTF-8");
                }
            }

            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFilename + "\"");
        } catch (Exception e) {
            log.error("文件名编码出错", e);
            throw new BusinessException("文件编码出错");
        }
    }

    // 辅助方法：将索引映射为 Excel 列标识符
    private String getColumnLabel(int index) {
        int dividend = index + 1;
        StringBuilder columnLabel = new StringBuilder();

        while (dividend > 0) {
            int remainder = (dividend - 1) % 26;
            char columnChar = (char) (65 + remainder); // ASCII码 'A' 是 65
            columnLabel.insert(0, columnChar);
            dividend = (dividend - 1) / 26;
        }

        return columnLabel.toString();
    }


    private List<String> getCurrentUnfinishedTaskKeys(String processInstanceId) {
        List<String> unfinishedTaskKeys = new ArrayList<>();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance != null) {
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .active()
                    .list();

            for (Task task : tasks) {
                HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                        .taskId(task.getId())
                        .singleResult();

                if (historicTaskInstance == null || historicTaskInstance.getEndTime() == null) {
                    unfinishedTaskKeys.add(task.getTaskDefinitionKey());
                }
            }
        }

        return unfinishedTaskKeys;
    }

    /**
     * 推送售后企业微信消息
     */
    private void sendPostSaleQiyeWechatMessage(Task task) {
        ThreadUtil.safeSleep(1000);
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(task.getProcessInstanceId());
        bpmProcessInstanceExtDO = circleGetBpmProcessInstanceExt(task, bpmProcessInstanceExtDO);
        JSONObject params = new JSONObject();
        String format = bpmProcessInstanceExtDO.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        params.put("startTime", format);
        params.put("oaUserId", task.getAssignee());
        log.info("审批人id:{}", task.getAssignee());
        params.put("processInstanceId", bpmProcessInstanceExtDO.getProcessInstanceId());
        params.put("processDefinitionId", bpmProcessInstanceExtDO.getProcessDefinitionId());
        params.put("taskId", task.getId());
        params.put("taskName", bpmProcessInstanceExtDO.getName());
        params.put("startUserId", getLoginUserId());
        qiyeWechatMessageWrapper.pushQiyeWechatMessage(params);
    }

    private Set<Long> getTaskCopyUsersByPost(BpmTaskAssignRuleDO rule) {
        List<AdminUserRespDTO> users = adminUserApi.getUserListByPostIds(rule.getCopyPostOptions()).getCheckedData();
        Set<Long> userIdSet = convertSet(users, AdminUserRespDTO::getId);
        return userIdSet;
    }

    private Set<Long> getTaskCopyUsersByUser(BpmTaskAssignRuleDO rule) {
        return rule.getCopyUserOptions();
    }


    private Set<Long> getTaskCopyUsersByScript(BpmTaskAssignRuleDO rule, Task task) {
        List<BpmTaskCopyScript> scripts = new ArrayList<>(rule.getOptions().size());
        rule.getCopyScriptOptions().forEach(id -> {
            BpmTaskCopyScript script = scriptMap.get(id);
            if (script == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.TASK_ASSIGN_SCRIPT_NOT_EXISTS, id);
            }
            scripts.add(script);
        });
        Set<Long> userIds = new HashSet<>();
        scripts.forEach(script -> CollUtil.addAll(userIds, script.calculateTaskCopyUsers(task)));
        return userIds;
    }
}
