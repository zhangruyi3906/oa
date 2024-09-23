package com.lh.oa.module.bpm.service.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.lh.oa.framework.common.exception.ServiceException;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeTransUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.api.definition.to.BpmProcessDefinitionFromColumnEditTo;
import com.lh.oa.module.bpm.api.definition.to.BpmProcessDefinitionFromColumnSourceTo;
import com.lh.oa.module.bpm.api.definition.to.BpmProcessDefinitionFromColumnTo;
import com.lh.oa.module.bpm.api.definition.to.BpmProcessDefinitionFromOptionsTo;
import com.lh.oa.module.bpm.api.definition.to.BpmProcessDefinitionFromTo;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceParam;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceTo;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.BpmProcessAttendanceExportVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.BpmProcessAttendanceFileVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.BpmProcessAttendanceFormVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.BpmProcessAttendanceQueryParam;
import com.lh.oa.module.bpm.controller.admin.task.vo.BpmProcessAttendanceVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceCancelReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceFormVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceMyPageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstancePageItemRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRetractResVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskRespVO;
import com.lh.oa.module.bpm.convert.task.BpmProcessInstanceConvert;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormFieldExportDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormFieldShowDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmSaveInstanceDO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmTaskExtDO;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmFormFieldExportMapper;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmModelExtMapper;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmProcessDefinitionExtMapper;
import com.lh.oa.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.lh.oa.module.bpm.dal.mysql.task.BpmSaveInstanceMapper;
import com.lh.oa.module.bpm.dal.mysql.task.BpmTaskExtMapper;
import com.lh.oa.module.bpm.enums.ErrorCodeConstants;
import com.lh.oa.module.bpm.enums.im.ImMessageBusinessType;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceDeleteReasonEnum;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceStatusEnum;
import com.lh.oa.module.bpm.enums.task.BpmSaveProcessTypeEnum;
import com.lh.oa.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventPublisher;
import com.lh.oa.module.bpm.service.definition.BpmFormFieldShowService;
import com.lh.oa.module.bpm.service.definition.BpmProcessDefinitionService;
import com.lh.oa.module.bpm.service.message.BpmMessageService;
import com.lh.oa.module.bpm.wrapper.AdminUserWrapper;
import com.lh.oa.module.bpm.wrapper.DeptWrapper;
import com.lh.oa.module.bpm.wrapper.ImWrapper;
import com.lh.oa.module.bpm.wrapper.PostWrapper;
import com.lh.oa.module.bpm.wrapper.ProjectWrapper;
import com.lh.oa.module.bpm.wrapper.SystemDictDataWrapper;
import com.lh.oa.module.bpm.wrapper.UserProjectWrapper;
import com.lh.oa.module.bpm.wrapper.vo.ImCustomMsgContent;
import com.lh.oa.module.system.api.dept.DeptApi;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
import com.lh.oa.module.system.api.dept.dto.PostTO;
import com.lh.oa.module.system.api.dict.dto.DictDataRespDTO;
import com.lh.oa.module.system.api.file.FileApi;
import com.lh.oa.module.system.api.file.dto.FileUpdateReqDTO;
import com.lh.oa.module.system.api.permission.PermissionApi;
import com.lh.oa.module.system.api.permission.dto.DeptDataPermissionRespDTO;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import com.lh.oa.module.system.api.user.dto.UserProjectTo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.event.FlowableCancelledEvent;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertList;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.PROCESS_DEFINITION_IS_SUSPENDED;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.PROCESS_DEFINITION_NOT_EXISTS;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.PROCESS_INSTANCE_CANCEL_FAIL_NOT_EXISTS;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.PROCESS_INSTANCE_CANCEL_FAIL_NOT_SELF;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.PROCESS_INSTANCE_NOT_EXISTS;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.PROCESS_INSTANCE_RETRACT_FAIL_IS_END;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.PROCESS_INSTANCE_RETRACT_FAIL_NOT_SELF;
import static com.lh.oa.module.bpm.framework.flowable.core.listener.CustomerEndListener.PROCESS_KEY2;

/**
 * 流程实例 Service 实现类
 *
 * @author
 */
@Service
@Validated
@Slf4j
public class BpmProcessInstanceServiceImpl implements BpmProcessInstanceService {
    /**
     * 流程保存标识
     */
    public static final String FLOW_MARK = "savePress:";

    @Value(value = "${process.start-user-id}")
    public Long startUserId;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private BpmProcessInstanceExtMapper processInstanceExtMapper;
    @Resource
    @Lazy
    private BpmTaskService bpmTaskService;
    @Resource
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private HistoryService historyService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private BpmProcessInstanceResultEventPublisher processInstanceResultEventPublisher;
    @Resource
    private BpmMessageService messageService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private TaskService taskService;

    @Resource
    private BpmSaveInstanceMapper bpmSaveInstanceMapper;
    @Resource
    private PermissionApi permissionApi;
    @Resource
    private BpmTaskExtMapper bpmTaskExtMapper;
    @Resource
    private BpmFormFieldShowService bpmFormFieldShowService;
    @Resource
    private AdminUserWrapper adminUserWrapper;

    @Resource
    private DeptWrapper deptWrapper;

    @Resource
    private PostWrapper postWrapper;

    @Resource
    private UserProjectWrapper userProjectWrapper;

    @Resource
    private SystemDictDataWrapper systemDictDataWrapper;

    @Resource
    private BpmModelExtMapper bpmModelExtMapper;
    @Resource
    private BpmProcessDefinitionExtMapper bpmProcessDefinitionExtMapper;
    @Resource
    private BpmFormFieldExportMapper bpmFormFieldExportMapper;
    @Resource
    private ImWrapper imWrapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ProjectWrapper projectWrapper;

    private final String FORM_TYPE_SELECT = "select";
    private final String FORM_TYPE_REDIOS = "radios";
    private final String FORM_TYPE_TABLE = "table";

    @Override
    public ProcessInstance getProcessInstance(String id) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    @Override
    public List<ProcessInstance> getProcessInstances(Set<String> ids) {
        return runtimeService.createProcessInstanceQuery().processInstanceIds(ids).list();
    }

    @Override
    public PageResult<BpmProcessInstancePageItemRespVO> getMyProcessInstancePage(Long userId, BpmProcessInstanceMyPageReqVO pageReqVO) {
        PageResult<BpmProcessInstanceExtDO> pageResult = new PageResult<>();
        CommonResult<DeptDataPermissionRespDTO> deptDataPermission = permissionApi.getDeptDataPermission(getLoginUserId());
        log.info("userId:{},getLoginUserId:{}", userId, getLoginUserId());

        if (ObjectUtils.isEmpty(userId)) {
            if (ObjectUtils.isNotEmpty(deptDataPermission.getData())) {
                Set<Long> deptIds = deptDataPermission.getData().getDeptIds();
                List<Long> ids = new ArrayList<>();
                if (ObjectUtils.isEmpty(pageReqVO.getUsername()) && deptDataPermission.getData().getSelf()) {
                    pageResult = processInstanceExtMapper.selectPage(getLoginUserId(), pageReqVO);
                } else if (ObjectUtils.isNotEmpty(pageReqVO.getUsername())) {
                    CommonResult<List<AdminUserRespDTO>> listByNickname = adminUserApi.getListByNickname(pageReqVO.getUsername());
                    List<AdminUserRespDTO> data = listByNickname.getData();
                    if (CollUtil.isNotEmpty(data)) {
                        ids = data.stream().map(AdminUserRespDTO::getId).collect(Collectors.toList());
                    }
                    if (deptDataPermission.getData().getSelf()) {
                        pageResult = processInstanceExtMapper.selectPage(getLoginUserId(), pageReqVO, ids, deptIds);
                    } else {
                        pageResult = processInstanceExtMapper.selectPage(null, pageReqVO, ids, deptIds);
                    }
                } else {
                    pageResult = processInstanceExtMapper.selectPage(null, pageReqVO, deptIds);
                }
            }
        } else {
            pageResult = processInstanceExtMapper.selectPage(userId, pageReqVO);
        }
        if (CollUtil.isEmpty(pageResult.getList())) {
            return new PageResult<>(pageResult.getTotal());
        }
        List<String> processInstanceIds = convertList(pageResult.getList(), BpmProcessInstanceExtDO::getProcessInstanceId);
        Map<String, List<Task>> taskMap = bpmTaskService.getTaskMapByProcessInstanceIds(processInstanceIds);
        PageResult<BpmProcessInstancePageItemRespVO> bpmProcessInstancePageItemRespVOPageResult = BpmProcessInstanceConvert.INSTANCE.convertPage(pageResult, taskMap);
        bpmProcessInstancePageItemRespVOPageResult.getList().forEach(t -> {
            BpmProcessInstanceExtDO bpmProcessInstanceExtDO = processInstanceExtMapper.selectByProcessInstanceId(t.getId());
            Long startUserId1 = bpmProcessInstanceExtDO.getStartUserId();
            CommonResult<AdminUserRespDTO> user = adminUserApi.getUser(startUserId1);
            if (ObjectUtils.isNotEmpty(user.getData())) {
                t.setStartUserName(user.getData().getNickname());
            }
        });
        return bpmProcessInstancePageItemRespVOPageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcessInstance(Map<String, Object> variables) {
        String processDefinitionId = (String) variables.get("processDefinitionId");
        String saveId = (String) variables.get("saveId");
        if (ObjectUtils.isNotEmpty(saveId)) {
            bpmSaveInstanceMapper.deleteBySaveId(saveId);
        }
        //获取该流程最新的流程定义
        BpmProcessDefinitionExtDO bpmProcessDefinitionExtDO = bpmProcessDefinitionExtMapper.selectByProcessDefinitionId(processDefinitionId);
        Model model = repositoryService.getModel(bpmProcessDefinitionExtDO.getModelId());
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(model.getKey()).active().singleResult();

        Boolean isExist = stringRedisTemplate.opsForHash().hasKey(FLOW_MARK + processDefinitionId, getLoginUserId().toString());
        if (isExist) {
            stringRedisTemplate.opsForHash().delete(FLOW_MARK + processDefinitionId, getLoginUserId().toString());
        }
        return createProcessInstance0(null, definition, variables, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqVO createReqVO) {
        ProcessDefinition definition = processDefinitionService.getProcessDefinition(createReqVO.getProcessDefinitionId());
        Long loginUserId = getLoginUserId();
        Boolean isExist = stringRedisTemplate.opsForHash().hasKey(FLOW_MARK + createReqVO.getProcessDefinitionId(), loginUserId.toString());
        if (isExist) {
            stringRedisTemplate.opsForHash().delete(FLOW_MARK + createReqVO.getProcessDefinitionId(), loginUserId.toString());
        }
        return createProcessInstance0(userId, definition, createReqVO.getVariables(), null);
    }

    @Override
    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqDTO createReqDTO) {
        ProcessDefinition definition = processDefinitionService.getActiveProcessDefinition(createReqDTO.getProcessDefinitionKey());
        return createProcessInstance0(userId, definition, createReqDTO.getVariables(), createReqDTO.getBusinessKey());
    }

    @Override
    public BpmProcessInstanceRespVO getProcessInstanceVO(String id) {
        HistoricProcessInstance processInstance = getHistoricProcessInstance(id);
        if (processInstance == null) {
            return null;
        }
        BpmProcessInstanceExtDO processInstanceExt = processInstanceExtMapper.selectByProcessInstanceId(id);
        Assert.notNull(processInstanceExt, "流程实例拓展({}) 不存在", id);

        // 获得流程定义
        ProcessDefinition processDefinition = processDefinitionService
                .getProcessDefinition(processInstance.getProcessDefinitionId());
        Assert.notNull(processDefinition, "流程定义({}) 不存在", processInstance.getProcessDefinitionId());
        BpmProcessDefinitionExtDO processDefinitionExt = processDefinitionService.getProcessDefinitionExt(
                processInstance.getProcessDefinitionId());
        Assert.notNull(processDefinitionExt, "流程定义拓展({}) 不存在", id);
        String bpmnXml = processDefinitionService.getProcessDefinitionBpmnXML(processInstance.getProcessDefinitionId());

        AdminUserRespDTO startUser = adminUserApi.getUser(processInstanceExt.getStartUserId()).getCheckedData();
        DeptRespDTO dept = null;
        if (startUser != null) {
            dept = deptApi.getDept(startUser.getDeptId()).getCheckedData();

        }
        BpmProcessInstanceRespVO bpmProcessInstanceRespVO = BpmProcessInstanceConvert.INSTANCE.convert2(processInstance, processInstanceExt,
                processDefinition, processDefinitionExt, bpmnXml, startUser, dept);
        if (ObjectUtil.isNotEmpty(bpmProcessInstanceRespVO)) {
            Map<String, Object> formVariables = bpmProcessInstanceRespVO.getFormVariables();
            List<BpmTaskRespVO> taskListByProcessInstanceId = bpmTaskService.getTaskListByProcessInstanceId(id);
            Map<String, Object> result = formVariables;
            for (BpmTaskRespVO bpmTaskRespVO : taskListByProcessInstanceId) {
                if (ObjectUtil.isNotEmpty(bpmTaskRespVO.getFormVariables())) {
                    result = Stream.concat(result.entrySet().stream(),
                            bpmTaskRespVO.getFormVariables().entrySet().stream())
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (oldValue, newValue) -> oldValue));
                    bpmProcessInstanceRespVO.setFormVariables(result);
                }
            }
        }
        bpmProcessInstanceRespVO.setFormKey(bpmProcessInstanceRespVO.getProcessDefinition().getFormId());
        return bpmProcessInstanceRespVO;
    }

    @Override
    public void cancelProcessInstance(Long userId, @Valid BpmProcessInstanceCancelReqVO cancelReqVO) {
        ProcessInstance instance = getProcessInstance(cancelReqVO.getId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_EXISTS);
        }
        if (!Objects.equals(instance.getStartUserId(), String.valueOf(userId))) {
            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_SELF);
        }

        deleteProcessInstance(cancelReqVO.getId(),
                BpmProcessInstanceDeleteReasonEnum.CANCEL_TASK.getReason());
    }

    /**
     * 获得历史的流程实例
     *
     * @param id 流程实例的编号
     * @return 历史的流程实例
     */
    @Override
    public HistoricProcessInstance getHistoricProcessInstance(String id) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    @Override
    public List<HistoricProcessInstance> getHistoricProcessInstances(Set<String> ids) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceIds(ids).list();
    }

    @Override
    public void createProcessInstanceExt(ProcessInstance instance) {
        ProcessDefinition definition = processDefinitionService.getProcessDefinition2(instance.getProcessDefinitionId());
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO()
                .setProcessInstanceId(instance.getId())
                .setProcessDefinitionId(definition.getId())
                .setName(instance.getProcessDefinitionName())
                .setStartUserId(Long.valueOf(instance.getStartUserId()))
                .setCategory(definition.getCategory())
                .setStatus(BpmProcessInstanceStatusEnum.RUNNING.getStatus())
                .setReadAble(1)
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());

        if (ObjectUtils.isNotEmpty(instance.getBusinessKey()) && instance.getBusinessKey().equals(definition.getKey())) {
            instanceExtDO.setStartUserId(startUserId);
        }
        CommonResult<AdminUserRespDTO> user = adminUserApi.getUser(instanceExtDO.getStartUserId());
        String nickname = "";
        if (ObjectUtils.isNotEmpty(user.getData())) {
            instanceExtDO.setDeptId(user.getData().getDeptId());
            nickname = user.getData().getNickname();
        }
        String instanceName = definition.getName() + "_" + nickname + "_" + TimeUtils.formatAsDate(new Date());
        instanceExtDO.setInstanceName(instanceName);
        processInstanceExtMapper.insert(instanceExtDO);
    }

    @Override
    public void updateProcessInstanceExtCancel(FlowableCancelledEvent event) {
        if (BpmProcessInstanceDeleteReasonEnum.isRejectReason((String) event.getCause())) {
            return;
        }
        HistoricProcessInstance processInstance = getHistoricProcessInstance(event.getProcessInstanceId());
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO()
                .setProcessInstanceId(event.getProcessInstanceId())
                .setEndTime(LocalDateTime.now())
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.CANCEL.getResult());
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = processInstanceExtMapper.selectByProcessInstanceId(event.getProcessInstanceId());
        if (ObjectUtil.isNotEmpty(bpmProcessInstanceExtDO)) {
            instanceExtDO.setReadAble(bpmProcessInstanceExtDO.getReadAble());
        }
        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);
        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(
                BpmProcessInstanceConvert.INSTANCE.convert(this, processInstance, instanceExtDO.getResult()));
    }

    @Override
    public void updateProcessInstanceExtComplete(ProcessInstance instance) {
        HistoricProcessInstance processInstance = getHistoricProcessInstance(instance.getId());
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO()
                .setProcessInstanceId(instance.getProcessInstanceId())
                .setEndTime(LocalDateTime.now())
                .setReadAble(1)
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());

        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery().processInstanceId(instance.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().list();
        if (ObjectUtils.isNotEmpty(taskList)) {
            String taskDefinitionKey = taskList.get(0).getTaskDefinitionKey();
            //获取所有的发起人确认的任务
            List<HistoricTaskInstance> firstTaskList = taskList.stream().filter(task -> ObjectUtils.equals(task.getTaskDefinitionKey(), taskDefinitionKey)).collect(Collectors.toList());
            if (ObjectUtils.isNotEmpty(firstTaskList) && firstTaskList.size() > 1) {
                List<String> taskIds = firstTaskList.stream().map(HistoricTaskInstance::getId).collect(Collectors.toList());
                //所有发起人确认任务
                List<BpmTaskExtDO> firstKeyBpmTaskExtDOS = bpmTaskExtMapper.selectListByTaskIds(taskIds);
                //含有formKey的发起热虐人任务
                List<BpmTaskExtDO> formKeyBpmTaskExtDOS = firstKeyBpmTaskExtDOS.stream().filter(task -> ObjectUtils.isNotEmpty(task.getFormKey())).collect(Collectors.toList());
                if (ObjectUtils.isNotEmpty(formKeyBpmTaskExtDOS) && formKeyBpmTaskExtDOS.size() > 0) {
                    instanceExtDO.setReadAble(0);
                }
            }
        }
        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);

        messageService.sendMessageWhenProcessInstanceApprove(BpmProcessInstanceConvert.INSTANCE.convert2ApprovedReq(instance));

        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(BpmProcessInstanceConvert.INSTANCE.convert(this, processInstance, instanceExtDO.getResult()));

        // 异步发送归档消息
        ThreadUtil.execAsync(() -> {
            sendDoneProcessImMessage(instanceExtDO.getProcessInstanceId());
        });
    }

    private void sendDoneProcessImMessage(String processInstanceId) {
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = processInstanceExtMapper.selectByProcessInstanceId(processInstanceId);
        CommonResult<String> sysUserAccount = adminUserApi.getSysUserAccount(bpmProcessInstanceExtDO.getStartUserId().toString());
        AdminUserRespDTO startUser = adminUserApi.getUser(bpmProcessInstanceExtDO.getStartUserId()).getData();
        String content = "";
        if (bpmProcessInstanceExtDO.getName().contains(startUser.getNickname())) {
            content = bpmProcessInstanceExtDO.getName();
        } else {
            content = bpmProcessInstanceExtDO.getName() + "-" + startUser.getNickname() + "-" + bpmProcessInstanceExtDO.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        String msg = String.format("您有一条新的待办消息【归档】【%s】，请及时处理！", content);

        ImCustomMsgContent msgContent = new ImCustomMsgContent();
        msgContent.setText(msg);
        msgContent.setBusinessType(ImMessageBusinessType.BPM_TASK_COPY.getBusinessType());

        JSONObject params = new JSONObject();
        params.put("oaUserId", bpmProcessInstanceExtDO.getStartUserId());
        params.put("processInstanceId", bpmProcessInstanceExtDO.getProcessInstanceId());
        params.put("processDefinitionId", bpmProcessInstanceExtDO.getProcessDefinitionId());
        msgContent.setParams(JsonUtils.toJsonString(params));
        imWrapper.sendAccountCustomMessage(sysUserAccount.getData(), msgContent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProcessInstanceExtReject(String id, String reason) {
        ProcessInstance processInstance = getProcessInstance(id);
        deleteProcessInstance(id, StrUtil.format(BpmProcessInstanceDeleteReasonEnum.REJECT_TASK.format(reason)));
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO().setProcessInstanceId(id)
                .setEndTime(LocalDateTime.now())
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.REJECT.getResult());
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = processInstanceExtMapper.selectByProcessInstanceId(id);
        if (ObjectUtils.isNotEmpty(bpmProcessInstanceExtDO)) {
            instanceExtDO.setReadAble(bpmProcessInstanceExtDO.getReadAble());
        }
        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);
        messageService.sendMessageWhenProcessInstanceReject(BpmProcessInstanceConvert.INSTANCE.convert2RejectReq(processInstance, reason));

        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(
                BpmProcessInstanceConvert.INSTANCE.convert(this, processInstance, instanceExtDO.getResult()));
    }

    @Override
    public void saveProcess(Map<String, Object> variables) {
        String processDefinitionId = (String) variables.get("processDefinitionId");
        log.info("保存流程: processDefinitionId：{}，variables： {}", processDefinitionId, variables);
        ProcessDefinition definition = processDefinitionService.getProcessDefinition(processDefinitionId);
        BpmProcessDefinitionExtDO processDefinitionExt = processDefinitionService.getProcessDefinitionExt(processDefinitionId);
        String name = definition.getName();
        BpmSaveInstanceDO bpmSaveInstanceDO = new BpmSaveInstanceDO();
        Long loginUserId = getLoginUserId();
        CommonResult<AdminUserRespDTO> user = adminUserApi.getUser(loginUserId);
        if (ObjectUtils.isNotEmpty(user.getData())) {
            name = name + "-" + user.getData().getNickname() + "-" + TimeUtils.formatAsDate(new Date());
        } else {
            name = name + "-" + TimeUtils.formatAsDate(new Date());
        }
        bpmSaveInstanceDO.setName(name);
        bpmSaveInstanceDO.setModelId(processDefinitionExt.getModelId());
        String value = JSONObject.toJSONString(variables);
        bpmSaveInstanceDO.setFormConf(processDefinitionExt.getFormConf());
        bpmSaveInstanceDO.setFormVariables(value);
        bpmSaveInstanceDO.setUserId(loginUserId);
        bpmSaveInstanceDO.setType(BpmSaveProcessTypeEnum.SAVE.getStatus());
        String saveId = (String) variables.get("saveId");
        if (ObjectUtils.isNotEmpty(saveId)) {
            bpmSaveInstanceDO.setSaveId(Long.valueOf(saveId));
            bpmSaveInstanceMapper.update(bpmSaveInstanceDO, new UpdateWrapper<BpmSaveInstanceDO>().eq("save_id", saveId));
        } else {
            bpmSaveInstanceMapper.insert(bpmSaveInstanceDO);
        }
    }


    private void deleteProcessInstance(String id, String reason) {
        runtimeService.deleteProcessInstance(id, reason);
    }

    private String createProcessInstance0(Long userId, ProcessDefinition definition,
                                          Map<String, Object> variables, String businessKey) {
        if (definition == null) {
            throw exception(PROCESS_DEFINITION_NOT_EXISTS);
        }
        if (definition.isSuspended()) {
            throw exception(PROCESS_DEFINITION_IS_SUSPENDED);
        }
        if (ObjectUtils.isNotEmpty(variables.get(PROCESS_KEY2))) {
            businessKey = (String) variables.get(PROCESS_KEY2);
        }

        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder()
                .processDefinitionId(definition.getId())
                .businessKey(businessKey)
                .name(definition.getName().trim())
                .variables(variables);
        if (ObjectUtils.isNotEmpty(variables.get("instanceName"))) {
            processInstanceBuilder.name((String) variables.get("instanceName"));
        }
        ProcessInstance instance = processInstanceBuilder.start();
        ((ExecutionEntityImpl) instance).setStartUserId(String.valueOf(ObjectUtils.isNotEmpty(userId) ? userId : getLoginUserId()));
        String startTime = (String) variables.get("start_time");
        String endTime = (String) variables.get("end_time");
        LocalDateTime formStartTime = null;
        LocalDateTime formEndTime = null;
        if (ObjectUtils.isNotEmpty(startTime)) {
            startTime = startTime + "000";
            formStartTime = TimeUtils.parseAsLocalDateTimeByTimestamp(Long.valueOf(startTime));
        }
        if (ObjectUtils.isNotEmpty(endTime)) {
            endTime = endTime + "000";
            formEndTime = TimeUtils.parseAsLocalDateTimeByTimestamp(Long.valueOf(endTime));
        }
        // 考勤补充说明单独处理
        if (instance.getProcessDefinitionId().contains("kaoqin")) {
            String date2 = MapUtils.getString(variables, "date2");
            if (StringUtils.isNotBlank(date2)) {
                formEndTime = TimeUtils.parseAsLocalDateTimeByTimestamp(Long.valueOf(date2 + "000"));
            }
        }

        runtimeService.setProcessInstanceName(instance.getId(), instance.getName());
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = new BpmProcessInstanceExtDO()
                .setProcessInstanceId(instance.getId())
                .setFormVariables(variables)
                .setName(instance.getName())
                .setReadAble(1)
                .setStartUserId(Long.valueOf(instance.getStartUserId()))
                .setFormStartTime(formStartTime)
                .setFormEndTime(formEndTime);
        String instanceName = "";
        if (ObjectUtils.isNotEmpty(instance.getProcessVariables()) && ObjectUtils.isNotEmpty(instance.getProcessVariables().get("instanceName"))) {
            instanceName = (String) instance.getProcessVariables().get("instanceName");
            bpmProcessInstanceExtDO.setInstanceName(instanceName);
        }
        //表单上传的文件与流程关联
        BpmProcessDefinitionExtDO bpmProcessDefinitionExtDO = bpmProcessDefinitionExtMapper.selectByProcessDefinitionId(definition.getId());
        if (ObjectUtils.isNotEmpty(bpmProcessDefinitionExtDO)) {
            this.fileRelevancyProcessInstance(variables, bpmProcessInstanceExtDO, bpmProcessDefinitionExtDO);
        }
        // 处理流程实例名称后缀
        this.processInstanceNameAssignmentSuffix(variables, bpmProcessInstanceExtDO, instanceName);

        processInstanceExtMapper.updateByProcessInstanceId(bpmProcessInstanceExtDO);
        return instance.getId();
    }

    private void fileRelevancyProcessInstance(Map<String, Object> variables, BpmProcessInstanceExtDO bpmProcessInstanceExtDO, BpmProcessDefinitionExtDO bpmProcessDefinitionExtDO) {
        try {
            String formConf = bpmProcessDefinitionExtDO.getFormConf();
            JSONObject formConfObj = JsonUtils.parseObject(formConf, JSONObject.class);
            log.info("formConfObj:{}", formConfObj);
            assert formConfObj == null;
            JSONArray bodys = JsonUtils.parseObject(JsonUtils.toJsonString(formConfObj.get("body")), JSONArray.class);
            log.info("bodys:{}", bodys);
            assert bodys == null;
            JSONObject bodyJSONObject = JsonUtils.parseObject(JsonUtils.toJsonString(bodys.get(0)), JSONObject.class);
            log.info("bodyJSONObject:{}", bodyJSONObject);
            assert bodyJSONObject == null;
            //表单配置的具体信息
            JSONArray formConfDetails = JsonUtils.parseObject(JsonUtils.toJsonString(bodyJSONObject.get("body")), JSONArray.class);//表单组件配置项集合
            log.info("formConfDetails: {}", formConfDetails);
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
                        Object fileValueObj = variables.get(fileName);
                        // 避免表单有上传文件选项时，没有上传文件情况
                        if (ObjectUtils.isEmpty(fileValueObj)) {
                            continue;
                        }
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
                        Object fileValueObj = variables.get(fileName);
                        // 避免表单有上传文件选项时，没有上传文件情况
                        if (ObjectUtils.isEmpty(fileValueObj)) {
                            continue;
                        }
                        String fileValueStr = fileValueObj.toString();
                        String[] urlArray = fileValueStr.split(",");
                        List<String> urlList = Arrays.asList(urlArray);
                        urls.addAll(urlList);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(urls)) {
                FileUpdateReqDTO fileUpdateReqDTO = new FileUpdateReqDTO();
                fileUpdateReqDTO.setUrls(urls);
                fileUpdateReqDTO.setInstanceName(bpmProcessInstanceExtDO.getInstanceName());
                fileUpdateReqDTO.setProcessInstanceId(bpmProcessInstanceExtDO.getProcessInstanceId());
                fileUpdateReqDTO.setFormId(bpmProcessDefinitionExtDO.getFormId());
                fileApi.updateFile(fileUpdateReqDTO);
            }
        } catch (Exception e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SAVE_PROCESS_FILE_UPLOAD_ERROR);
        }
    }

    private void processInstanceNameAssignmentSuffix(Map<String, Object> variables, BpmProcessInstanceExtDO bpmProcessInstanceExtDO, String instanceName) {
        String processInstanceId = bpmProcessInstanceExtDO.getProcessInstanceId();
        BpmProcessInstanceExtDO bpmProcessInstanceExtDO1 = processInstanceExtMapper.selectByProcessInstanceId(processInstanceId);
        if (ObjectUtils.isEmpty(bpmProcessInstanceExtDO1)) {
            return;
        }
        String processDefinitionId = bpmProcessInstanceExtDO1.getProcessDefinitionId();
        BpmProcessDefinitionExtDO processDefinitionExt = processDefinitionService.getProcessDefinitionExt(processDefinitionId);
        if (ObjectUtils.isNotEmpty(processDefinitionExt)) {
            Long formId = processDefinitionExt.getFormId();
            if (ObjectUtils.isNotEmpty(formId)) {
                List<BpmFormFieldShowDO> bpmFormFieldShowDOList = bpmFormFieldShowService.getShowList(formId);
                if (ObjectUtils.isNotEmpty(bpmFormFieldShowDOList)) {
                    StringJoiner stringJoiner = new StringJoiner(",", "(", ")");
                    for (BpmFormFieldShowDO bpmFormFieldShowDO : bpmFormFieldShowDOList) {
                        String field = bpmFormFieldShowDO.getField();
                        Object value = variables.get(field);
                        if (ObjectUtils.isNotEmpty(value)) {
                            if (field.contains("user") && value.toString().matches("\\d+")) { //判断是否是数字
                                log.info("value:" + value);
                                CommonResult<AdminUserRespDTO> user = adminUserApi.getUser(Long.valueOf(value.toString()));
                                if (ObjectUtils.isNotEmpty(user.getData())) {
                                    value = user.getData().getNickname();
                                    log.info("人员名:" + value);
                                }
                            }
                            if (field.contains("dept") && value.toString().matches("\\d+")) {
                                log.info("value:" + value);
                                CommonResult<DeptRespDTO> dept = deptApi.getDept(Long.valueOf(value.toString()));
                                if (ObjectUtils.isNotEmpty(dept.getData())) {
                                    value = dept.getData().getName();
                                    log.info("部门名:" + value);
                                }
                            }
                            stringJoiner.add(bpmFormFieldShowDO.getFieldName() + ":" + value);
                        }
                    }
                    if (ObjectUtils.isNotEmpty(stringJoiner)) {
                        if (ObjectUtils.isEmpty(instanceName)) {
                            instanceName = bpmProcessInstanceExtDO1.getInstanceName();
                        }
                        instanceName = instanceName + stringJoiner;
                        bpmProcessInstanceExtDO.setInstanceName(instanceName);
                        bpmProcessInstanceExtDO.setName(bpmProcessInstanceExtDO.getName() + stringJoiner);
                    }
                }
            }
        }
    }

    @Override
    public Double getAttendanceLeaveCount(Long userId, Integer attendanceMonth) {
        DateTime beginOfMonth = null;
        DateTime endOfMonth = null;
        if (attendanceMonth != 0) {
            DateTime month = DateUtil.parse(this.toDateFormat(attendanceMonth), DatePattern.NORM_DATE_PATTERN);
            beginOfMonth = DateUtil.beginOfMonth(month);
            endOfMonth = DateUtil.endOfMonth(month);
        }
        LambdaQueryWrapperX<BpmProcessInstanceExtDO> leave = new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .eqIfPresent(BpmProcessInstanceExtDO::getStartUserId, userId)
                .eqIfPresent(BpmProcessInstanceExtDO::getResult, BpmProcessInstanceResultEnum.APPROVE.getResult())
                .likeIfPresent(BpmProcessInstanceExtDO::getName, "请假")
                .betweenIfPresent(BpmProcessInstanceExtDO::getEndTime, beginOfMonth, endOfMonth);
        List<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOS = processInstanceExtMapper.selectList(leave);
        Double attendanceTravelCount = 0.0;
        for (BpmProcessInstanceExtDO bpmProcessInstanceExtDO : bpmProcessInstanceExtDOS) {
            Map<String, Object> formVariables = bpmProcessInstanceExtDO.getFormVariables();
            Object days = formVariables.get("days");//TODO 具体的请假时间的字段
            try {
                Double day = Double.valueOf(days.toString());
                attendanceTravelCount = attendanceTravelCount + day;
            } catch (Exception e) {
                e.printStackTrace();
                log.info("出差天数类型转换异常");
            }
        }
        return attendanceTravelCount;
    }

    @Override
    public Integer getAttendanceAddCount(Long userId, Integer attendanceMonth) {
        DateTime beginOfMonth = null;
        DateTime endOfMonth = null;
        if (attendanceMonth != 0) {
            DateTime month = DateUtil.parse(this.toDateFormat(attendanceMonth), DatePattern.NORM_DATE_PATTERN);
            beginOfMonth = DateUtil.beginOfMonth(month);
            endOfMonth = DateUtil.endOfMonth(month);
        }
        LambdaQueryWrapperX<BpmProcessInstanceExtDO> add = new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .eqIfPresent(BpmProcessInstanceExtDO::getStartUserId, userId)
                .eqIfPresent(BpmProcessInstanceExtDO::getResult, BpmProcessInstanceResultEnum.APPROVE.getResult())
                .likeIfPresent(BpmProcessInstanceExtDO::getName, "加班")
                .betweenIfPresent(BpmProcessInstanceExtDO::getEndTime, beginOfMonth, endOfMonth);
        List<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOS = processInstanceExtMapper.selectList(add);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Integer attendanceAddCount = 0;
        for (BpmProcessInstanceExtDO bpmProcessInstanceExtDO : bpmProcessInstanceExtDOS) {
            Map<String, Object> formVariables = bpmProcessInstanceExtDO.getFormVariables();
            Object startDate = formVariables.get("startDate");//具体的加班开始日期的字段
            Object endDate = formVariables.get("endDate");//具体的加班结束日期的字段
            Object startTime = formVariables.get("startTime");//具体的加班开始时间的字段
            Object endTime = formVariables.get("endTime");//具体的加班结束时间的字段
            LocalDate start;
            LocalDate end;
            LocalTime startTime1;
            LocalTime endTime1;
            LocalDateTime startTime2;
            LocalDateTime endTime2;
            try {
                if (startDate != null && endDate != null && startTime != null && endTime != null) {
                    start = LocalDate.parse(startDate.toString(), formatter);
                    end = LocalDate.parse(endDate.toString(), formatter);
                    startTime1 = LocalTime.parse(startTime.toString());
                    endTime1 = LocalTime.parse(endTime.toString());
                    startTime2 = LocalDateTime.of(start, startTime1);
                    endTime2 = LocalDateTime.of(end, endTime1);
                    // 如果日期转换成功,进行日期计算
                    if (start != null && end != null && startTime1 != null && endTime1 != null && startTime2 != null && endTime2 != null) {
                        Duration duration = Duration.between(startTime2, endTime2);
                        long days = duration.toDays();
                        long hours = duration.toHours();
                        if (hours >= 8L) {
                            hours = 8L;
                        }
                        attendanceAddCount = Math.toIntExact(attendanceAddCount + days * 8 + hours);
                    } else {
                        log.info("加班日期转换异常");
                    }
                }
            } catch (DateTimeParseException e) {
                log.info("日期或时间格式无效: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return attendanceAddCount;
    }


    @Override
    public Integer getAttendanceTravelCount(Long userId, Integer attendanceMonth) {
        DateTime beginOfMonth = null;
        DateTime endOfMonth = null;
        if (attendanceMonth != 0) {
            DateTime month = DateUtil.parse(this.toDateFormat(attendanceMonth), DatePattern.NORM_DATE_PATTERN);
            beginOfMonth = DateUtil.beginOfMonth(month);
            endOfMonth = DateUtil.endOfMonth(month);
        }
        LambdaQueryWrapperX<BpmProcessInstanceExtDO> travel = new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .eqIfPresent(BpmProcessInstanceExtDO::getStartUserId, userId)
                .eqIfPresent(BpmProcessInstanceExtDO::getResult, BpmProcessInstanceResultEnum.APPROVE.getResult())
                .likeIfPresent(BpmProcessInstanceExtDO::getName, "出差")
                .betweenIfPresent(BpmProcessInstanceExtDO::getEndTime, beginOfMonth, endOfMonth);
        List<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOS = processInstanceExtMapper.selectList(travel);
        Integer attendanceTravelCount = 0;
        for (BpmProcessInstanceExtDO bpmProcessInstanceExtDO : bpmProcessInstanceExtDOS) {
            Map<String, Object> formVariables = bpmProcessInstanceExtDO.getFormVariables();
            Object days = formVariables.get("totalDays");//TODO 相关流程表中具体的出差时间对应的字段,测试服的为“Fmp11nwegop7n9”
            try {
                Integer integer = Integer.valueOf(days.toString());
                attendanceTravelCount = attendanceTravelCount + integer;
            } catch (Exception e) {
                e.printStackTrace();
                log.info("出差天数类型转换异常");
            }
        }
        return attendanceTravelCount;
    }

    @Override
    public BpmProcessInstanceRetractResVO retractProcessInstance(Long userId, String processInstanceId) {
        BpmProcessInstanceRetractResVO bpmProcessInstanceRetractResVO = new BpmProcessInstanceRetractResVO();
        BpmSaveInstanceDO bpmSaveInstanceDO = new BpmSaveInstanceDO();
        if (StringUtils.isNotBlank(processInstanceId)) {
            BpmProcessInstanceExtDO processInstanceExtDO = processInstanceExtMapper.selectByProcessInstanceId(processInstanceId);
            if (Objects.nonNull(processInstanceExtDO)) {
                // 校验是否有权限撤回
                if (userId.equals(processInstanceExtDO.getStartUserId())) {
                    List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                            .processInstanceId(processInstanceId)
                            .orderByHistoricTaskInstanceStartTime().desc()
                            .list();

                    // 校验任务是否被处理过;  //全是流程发起人自己审批的情况,或者第一个节点不是流程发起人自己审批处于处理中但是其他节点是自己审批的情况
//                    List<HistoricTaskInstance> approvedTaskList = tasks.stream().filter(task -> !Objects.equals(task.getAssignee(), userId.toString()) && Objects.nonNull(task.getEndTime())).collect(Collectors.toList());
                    List<BpmTaskRespVO> taskList = bpmTaskService.getTaskListByProcessInstanceId(processInstanceId);
                    if (CollectionUtils.isNotEmpty(taskList) || taskList.get(0).getStartUserIsAssignee()) {
                        BpmProcessDefinitionExtDO processDefinitionExtDO = processDefinitionService.getProcessDefinitionExt(processInstanceExtDO.getProcessDefinitionId());
                        bpmSaveInstanceDO.setFormConf(processDefinitionExtDO.getFormConf());
                        String name = processInstanceExtDO.getName();
                        bpmSaveInstanceDO.setName(name);
                        bpmSaveInstanceDO.setModelId(processDefinitionExtDO.getModelId());
                        Map<String, Object> formVariables = processInstanceExtDO.getFormVariables();
                        if (Objects.nonNull(formVariables)) {
                            Long loginUserId = getLoginUserId();
                            String value = JSONObject.toJSONString(formVariables);
                            bpmSaveInstanceDO.setUserId(loginUserId);
                            bpmSaveInstanceDO.setFormVariables(value);
                            bpmSaveInstanceDO.setType(BpmSaveProcessTypeEnum.RETRACT.getStatus());
                            bpmSaveInstanceMapper.insert(bpmSaveInstanceDO);
                        }
                        this.deleteProcessInstance(processInstanceId,
                                BpmProcessInstanceDeleteReasonEnum.RETRACT_TASK.getReason());
                        processInstanceExtMapper.deleteById(processInstanceExtDO.getId());
                        taskList.forEach(task -> {
                            historyService.deleteHistoricTaskInstance(task.getId());
                            bpmTaskExtMapper.delete(new LambdaQueryWrapperX<BpmTaskExtDO>().eqIfPresent(BpmTaskExtDO::getTaskId, task.getId()));
                        });

                    } else {
                        throw new ServiceException(PROCESS_INSTANCE_RETRACT_FAIL_IS_END);
                    }
                } else {
                    throw new ServiceException(PROCESS_INSTANCE_RETRACT_FAIL_NOT_SELF);
                }
            } else {
                throw new ServiceException(PROCESS_INSTANCE_NOT_EXISTS);
            }
        }
        return bpmProcessInstanceRetractResVO;
    }

    @Override
    public List<BpmProcessInstanceFormVo> getMyProcessInstanceList(Long loginUserId, BpmProcessInstanceExtDO bpmProcessInstanceExtDO) {
        List<BpmProcessInstanceExtDO> bpmProcessInstanceExtDOS = processInstanceExtMapper.selectList(loginUserId, bpmProcessInstanceExtDO);
        if (bpmProcessInstanceExtDOS.size() == 0) {
            return Collections.emptyList();
        }
        List<BpmProcessInstanceFormVo> bpmProcessInstanceFormVoList = new ArrayList<>();
        bpmProcessInstanceExtDOS.forEach(bpmProcessInstanceExt -> {
            BpmProcessInstanceFormVo bpmProcessInstanceFormVo = new BpmProcessInstanceFormVo();
            bpmProcessInstanceFormVo.setProcessInstanceId(bpmProcessInstanceExt.getProcessInstanceId());
            if (adminUserApi.getUser(bpmProcessInstanceExt.getStartUserId()).getData() != null) {
                String nickname = adminUserApi.getUser(bpmProcessInstanceExt.getStartUserId()).getData().getNickname();
                bpmProcessInstanceFormVo.setProcessName(bpmProcessInstanceExt.getName() + "-" + nickname + "-" + bpmProcessInstanceExt.getCreateTime().toLocalDate().toString());
            } else {
                bpmProcessInstanceFormVo.setProcessName(bpmProcessInstanceExt.getName() + "-" + bpmProcessInstanceExt.getCreateTime().toLocalDate().toString());
            }
            bpmProcessInstanceFormVoList.add(bpmProcessInstanceFormVo);
        });
        return bpmProcessInstanceFormVoList;
    }

    @Override
    public Map<String, Object> getSaveProcess(String processDefinitionId) {
        Object variables = stringRedisTemplate.opsForHash().get(FLOW_MARK + processDefinitionId, getLoginUserId().toString());
        if (Objects.isNull(variables)) {
            return Collections.emptyMap();
        }
        Map<String, Object> value = JsonUtils.parseObject(variables.toString(), Map.class);
        return value;
    }

    @Override
    public List<BpmProcessInstanceTo> getProcessByUserIdAndMonth(BpmProcessInstanceParam param) {
        // 获取所有请假的流程实例
        String startTime = TimeUtils.formatAsDateTime(TimeUtils.getMonthStartDate(TimeTransUtils.transSecondTimestamp2Date(param.getAttendMonth())));
        String endTime = TimeUtils.formatAsDateTime(TimeUtils.getMonthEndDate(TimeTransUtils.transSecondTimestamp2Date(param.getAttendMonth())));
        LambdaQueryWrapperX<BpmProcessInstanceExtDO> lambdaQueryWrapper = new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .inIfPresent(BpmProcessInstanceExtDO::getStartUserId, param.getUserIds())
                .eqIfPresent(BpmProcessInstanceExtDO::getResult, BpmProcessInstanceResultEnum.APPROVE.getResult())
                .likeIfPresent(BpmProcessInstanceExtDO::getProcessDefinitionId, param.getProcessName())
                .betweenIfPresent(BpmProcessInstanceExtDO::getFormEndTime, startTime, endTime);
        List<BpmProcessInstanceExtDO> processDOS = processInstanceExtMapper.selectList(lambdaQueryWrapper);
        return JsonUtils.covertList(processDOS, BpmProcessInstanceTo.class);
    }

    @Override
    public PageResult<BpmProcessAttendanceVo> getAttendanceProcessByParamPage(BpmProcessAttendanceQueryParam param) {
        log.info("分页查询考勤流程列表, param:{}", JsonUtils.toJsonString(param));
        LambdaQueryWrapperX<BpmProcessInstanceExtDO> lambdaQueryWrapper = this.buildAttendanceInstanceQueryWrapper(param);
        if (Objects.isNull(lambdaQueryWrapper)) {
            return new PageResult<>(0L);
        }
        PageResult<BpmProcessInstanceExtDO> pageList = processInstanceExtMapper.selectPage(param, lambdaQueryWrapper);
        if (pageList.getList().isEmpty()) {
            return new PageResult<>(0L);
        }
        List<BpmProcessAttendanceVo> result = this.transProcessInstance2AttendanceVoWithOutForm(pageList.getList());
        return new PageResult<>(result, pageList.getTotal());
    }

    @Override
    public List<BpmProcessAttendanceExportVo> getAttendanceProcessExportListByParam(BpmProcessAttendanceQueryParam param) {
        log.info("查询考勤流程导出列表, param:{}", JsonUtils.toJsonString(param));
        LambdaQueryWrapperX<BpmProcessInstanceExtDO> lambdaQueryWrapper = this.buildAttendanceInstanceQueryWrapper(param);
        if (Objects.isNull(lambdaQueryWrapper)) {
            return Collections.emptyList();
        }
        List<BpmProcessInstanceExtDO> list = processInstanceExtMapper.selectList(lambdaQueryWrapper);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        List<BpmProcessAttendanceVo> attendanceVos = this.transProcessInstance2AttendanceVo(list);
        return JsonUtils.covertList(attendanceVos, BpmProcessAttendanceExportVo.class);
    }

    private LambdaQueryWrapperX<BpmProcessInstanceExtDO> buildAttendanceInstanceQueryWrapper(BpmProcessAttendanceQueryParam param) {
        String startTime = null;
        if (Objects.nonNull(param.getStartDate())) {
            startTime = TimeUtils.formatAsDateTime(TimeTransUtils.transSecondTimestamp2Date(param.getStartDate()));
        }
        String endTime = null;
        if (Objects.nonNull(param.getEndDate())) {
            endTime = TimeUtils.formatAsDateTime(TimeTransUtils.transSecondTimestamp2Date(param.getEndDate()));
        }
        Set<Long> userIds = adminUserWrapper.getContainUserList(param.getUserId(), param.getDeptId(), param.getProjectId());
        if ((Objects.nonNull(param.getUserId()) || Objects.nonNull(param.getDeptId()) || Objects.nonNull(param.getProjectId())) && userIds.isEmpty()) {
            return null;
        }
        LambdaQueryWrapperX<BpmProcessInstanceExtDO> lambdaQueryWrapper = new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .inIfPresent(BpmProcessInstanceExtDO::getStartUserId, userIds)
                .eqIfPresent(BpmProcessInstanceExtDO::getResult, BpmProcessInstanceResultEnum.APPROVE.getResult())
                .betweenIfPresent(BpmProcessInstanceExtDO::getCreateTime, startTime, endTime);
        if (StringUtils.isBlank(param.getProcessName())) {
            lambdaQueryWrapper.and(wq -> wq.like(BpmProcessInstanceExtDO::getProcessDefinitionId, "qingjia")
                    .or().like(BpmProcessInstanceExtDO::getProcessDefinitionId, "jiaban")
                    .or().like(BpmProcessInstanceExtDO::getProcessDefinitionId, "chuchai")
                    .or().like(BpmProcessInstanceExtDO::getProcessDefinitionId, "kaoqin"));
        } else {
            lambdaQueryWrapper.like(BpmProcessInstanceExtDO::getProcessDefinitionId, param.getProcessName());
        }
        lambdaQueryWrapper.orderByDesc(BpmProcessInstanceExtDO::getId);
        return lambdaQueryWrapper;
    }

    /**
     * 组装流程实例信息，但不组装流程表单的具体信息
     */
    private List<BpmProcessAttendanceVo> transProcessInstance2AttendanceVoWithOutForm(List<BpmProcessInstanceExtDO> list) {
        // 获取表单的翻译数据
        Set<Long> startUserIds = list.stream().map(BpmProcessInstanceExtDO::getStartUserId).collect(Collectors.toSet());
        List<AdminUserRespDTO> userList = adminUserWrapper.getByIds(startUserIds);
        List<DeptRespDTO> deptList = deptWrapper.getDeptList(userList.stream().map(AdminUserRespDTO::getDeptId).collect(Collectors.toSet()));
        Map<Long, AdminUserRespDTO> userMap = userList.stream().collect(Collectors.toMap(AdminUserRespDTO::getId, Function.identity()));
        Map<Long, String> deptMap = deptList.stream().collect(Collectors.toMap(DeptRespDTO::getId, DeptRespDTO::getName));

        List<BpmProcessAttendanceVo> result = new LinkedList<>();
        list.forEach(instance -> {
            AdminUserRespDTO user = userMap.get(instance.getStartUserId());
            if (Objects.isNull(user)) {
                return;
            }
            String deptName = deptMap.getOrDefault(user.getDeptId(), "");

            BpmProcessAttendanceVo attendanceVo = new BpmProcessAttendanceVo();
            attendanceVo.setUserName(user.getNickname());
            attendanceVo.setDeptName(deptName);
            attendanceVo.setProcessName(instance.getName());
            attendanceVo.setProcessId(instance.getProcessInstanceId());
            attendanceVo.setProcessStartTime(TimeUtils.formatAsDateTime(instance.getCreateTime()));
            attendanceVo.setProcessEndTime(TimeUtils.formatAsDateTime(instance.getEndTime()));
            attendanceVo.setFormStartTime(TimeUtils.formatAsDateTime(instance.getFormStartTime()));
            attendanceVo.setFormEndTime(TimeUtils.formatAsDateTime(instance.getFormEndTime()));
            result.add(attendanceVo);
        });
        return result;
    }

    /**
     * 组装流程实例信息，并组装表单信息
     */
    private List<BpmProcessAttendanceVo> transProcessInstance2AttendanceVo(List<BpmProcessInstanceExtDO> list) {
        Set<String> instanceIds = list.stream().map(BpmProcessInstanceExtDO::getProcessInstanceId).collect(Collectors.toSet());
        Set<String> definitionIds = list.stream().map(BpmProcessInstanceExtDO::getProcessDefinitionId).collect(Collectors.toSet());
        List<BpmProcessDefinitionExtDO> definitionList = processDefinitionService.getByDefinitionIds(definitionIds);
        Map<String, BpmProcessDefinitionFromTo> formDefinitionMap = definitionList
                .stream()
                .filter(definition -> StringUtils.isNotBlank(definition.getFormConf()))
                .collect(
                        Collectors.toMap(
                                BpmProcessDefinitionExtDO::getProcessDefinitionId,
                                definition -> JsonUtils.parseObject(definition.getFormConf(), BpmProcessDefinitionFromTo.class)));
        List<BpmProcessDefinitionFromColumnTo> processFormList = formDefinitionMap.values()
                .stream()
                .filter(definition -> Objects.nonNull(definition)
                        && CollectionUtils.isNotEmpty(definition.getBody())
                        && Objects.nonNull(definition.getBody().get(0).getBody()))
                .map(this::getFromColumnToByDefinition)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        // 查出填写了节点表单的数据，补充表单内的下拉数据
        List<BpmTaskExtDO> bpmTaskExtList = bpmTaskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>()
                .in(BpmTaskExtDO::getProcessInstanceId, instanceIds)
                .isNotNull(BpmTaskExtDO::getFormVariables)
                .ne(BpmTaskExtDO::getFormVariables, "{}"));
        Map<String, List<BpmTaskExtDO>> taskExtMap = bpmTaskExtList
                .stream()
                .collect(Collectors.groupingBy(BpmTaskExtDO::getProcessInstanceId));

        // 获取节点表单的表单配置
        List<BpmProcessDefinitionFromColumnTo> taskFormList = bpmTaskExtList
                .stream()
                .filter(taskExt -> StringUtils.isNotBlank(taskExt.getFormConf()))
                .map(taskExt -> {
                    BpmProcessDefinitionFromTo definition = JsonUtils.parseObject(taskExt.getFormConf(), BpmProcessDefinitionFromTo.class);
                    return this.getFromColumnToByDefinition(definition);
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        // 获取表单下拉翻译映射
        Map<String, Map<String, String>> optionTypeMap = this.getOptionTypeMap(list, processFormList, bpmTaskExtList, taskFormList);

        // 获取表单的翻译数据
        Set<Long> startUserIds = list.stream().map(BpmProcessInstanceExtDO::getStartUserId).collect(Collectors.toSet());
        List<AdminUserRespDTO> userList = adminUserWrapper.getByIds(startUserIds);
        List<DeptRespDTO> deptList = deptWrapper.getDeptList(userList.stream().map(AdminUserRespDTO::getDeptId).collect(Collectors.toSet()));
        List<UserProjectTo> userProjectList = userProjectWrapper.getUserProjectList(startUserIds);
        Map<Long, AdminUserRespDTO> userMap = userList.stream().collect(Collectors.toMap(AdminUserRespDTO::getId, Function.identity()));
        Map<Long, String> deptMap = deptList.stream().collect(Collectors.toMap(DeptRespDTO::getId, DeptRespDTO::getName));
        Map<Long, List<String>> userAndProjectNameMap = userProjectList
                .stream()
                .collect(
                        Collectors.groupingBy(
                                UserProjectTo::getUserId,
                                Collectors.mapping(UserProjectTo::getProjectName, Collectors.toList())));

        List<BpmProcessAttendanceVo> result = new LinkedList<>();
        list.forEach(instance -> {
            AdminUserRespDTO user = userMap.get(instance.getStartUserId());
            if (Objects.isNull(user)) {
                return;
            }
            String deptName = deptMap.getOrDefault(user.getDeptId(), "");
            List<String> projectNameList = userAndProjectNameMap.getOrDefault(user.getId(), Collections.emptyList());

            // 翻译表单数据
            List<String> formList = new LinkedList<>();
            BpmProcessDefinitionFromTo formConfTo = formDefinitionMap.get(instance.getProcessDefinitionId());
            Map<String, Object> formVariables = instance.getFormVariables();
            if (Objects.nonNull(formConfTo) && CollectionUtils.isNotEmpty(formConfTo.getBody())
                    && Objects.nonNull(formConfTo.getBody().get(0).getBody())) {
                formConfTo.getBody().get(0).getBody().forEach(formColumnTo -> {
                    if (Objects.isNull(formColumnTo) || formColumnTo.getHidden()) {
                        return;
                    }
                    String type = formColumnTo.getType();
                    BpmProcessAttendanceFormVo formVo = this.buildProcessFromVo(formColumnTo, formVariables, optionTypeMap, type);
                    if (Objects.isNull(formVo)) {
                        return;
                    }
                    formList.add(formVo.getFormLabel() + ":" + formVo.getFormValue());
                });
            }

            // 组装节点表单数据
            List<String> nodeList = new LinkedList<>();
            List<BpmTaskExtDO> taskExtList = taskExtMap.get(instance.getProcessInstanceId());
            if (CollectionUtils.isNotEmpty(taskExtList)) {
                taskExtList.forEach(taskExt -> {
                    Map<String, Object> nodeFormVariables = taskExt.getFormVariables();
                    BpmProcessDefinitionFromTo nodeDefinitionTo = JsonUtils.parseObject(taskExt.getFormConf(), BpmProcessDefinitionFromTo.class);
                    if (Objects.nonNull(nodeDefinitionTo) && CollectionUtils.isNotEmpty(nodeDefinitionTo.getBody())
                            && Objects.nonNull(nodeDefinitionTo.getBody().get(0).getBody())) {
                        List<String> nodeFormStrList = new LinkedList<>();
                        nodeDefinitionTo.getBody().get(0).getBody().forEach(formColumnTo -> {
                            if (Objects.isNull(formColumnTo) || formColumnTo.getHidden()) {
                                return;
                            }
                            String type = formColumnTo.getType();
                            BpmProcessAttendanceFormVo formVo = this.buildProcessFromVo(formColumnTo, nodeFormVariables, optionTypeMap, type);
                            if (Objects.isNull(formVo)) {
                                return;
                            }
                            nodeFormStrList.add(formVo.getFormLabel() + ":" + formVo.getFormValue());
                        });
                        nodeList.add(taskExt.getName() + ":【" + Joiner.on(",").join(nodeFormStrList) + "】");
                    }
                });
            }

            BpmProcessAttendanceVo attendanceVo = new BpmProcessAttendanceVo();
            attendanceVo.setUserName(user.getNickname());
            attendanceVo.setDeptName(deptName);
            attendanceVo.setProjectName(Joiner.on(",").join(projectNameList));
            attendanceVo.setProcessName(instance.getName());
            attendanceVo.setProcessId(instance.getProcessInstanceId());
            attendanceVo.setProcessStartTime(TimeUtils.formatAsDateTime(instance.getCreateTime()));
            attendanceVo.setProcessEndTime(TimeUtils.formatAsDateTime(instance.getEndTime()));
            attendanceVo.setFormStartTime(TimeUtils.formatAsDateTime(instance.getFormStartTime()));
            attendanceVo.setFormEndTime(TimeUtils.formatAsDateTime(instance.getFormEndTime()));
            attendanceVo.setFormListStr(Joiner.on(",").join(formList));
            if (!nodeList.isEmpty()) {
                attendanceVo.setNodeListStr("[" + Joiner.on(",").join(nodeList) + "]");
            }
            result.add(attendanceVo);
        });
        return result;
    }

    /**
     * 从流程定义中提取出全部的字段配置列表
     *
     * @param definition 流程定义
     * @return 字段配置列表
     */
    private List<BpmProcessDefinitionFromColumnTo> getFromColumnToByDefinition(BpmProcessDefinitionFromTo definition) {
        List<BpmProcessDefinitionFromColumnTo> columnList = new LinkedList<>();
        if (Objects.nonNull(definition)) {
            List<BpmProcessDefinitionFromColumnTo> bodyList = definition.getBody().get(0).getBody();
            // 旧表单的子表格数据都是存在表格下的quickEdit的source里，为了能统一处理，全部移到外部的source字段且转为json
            columnList.addAll(bodyList
                    .stream()
                    .filter(body -> CollectionUtils.isNotEmpty(body.getColumns()))
                    .map(body -> {
                        body.getColumns().forEach(to -> {
                            if (Objects.nonNull(to.getQuickEdit()) && JsonUtils.isJson(to.getQuickEdit().toString())) {
                                BpmProcessDefinitionFromColumnEditTo quickEdit = JsonUtils.covertObject(to.getQuickEdit(), BpmProcessDefinitionFromColumnEditTo.class);
                                to.setSource(JsonUtils.covertObject2Map(quickEdit.getSource()));
                                // type有时候是存在quickEdit的type里，有时候存在最外层的quickEdit.type字段里，也要统一移到外部的type字段里
                                to.setType(to.getQuickEditType());
                                if (StringUtils.isNotBlank(quickEdit.getType())) {
                                    to.setType(quickEdit.getType());
                                }
                            }
                        });
                        return body.getColumns();
                    })
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList()));
            // 有些表单会配置组合输入框，也需要一起翻译
            columnList.addAll(bodyList
                    .stream()
                    .filter(body -> CollectionUtils.isNotEmpty(body.getItems()))
                    .collect(Collectors.toList()));
            // 有些表格的字段配置和输入是分离的，需要单独处理
            columnList.addAll(bodyList
                    .stream()
                    .filter(body -> Objects.nonNull(body.getBody()) && JsonUtils.isJson(body.getBody().toString()))
                    .map(body -> {
                        List<BpmProcessDefinitionFromColumnTo> bodies = JsonUtils.covertObject(body.getBody(),
                                new TypeReference<List<BpmProcessDefinitionFromColumnTo>>() {
                                });
                        List<BpmProcessDefinitionFromColumnTo> toList = new LinkedList<>();
                        List<BpmProcessDefinitionFromColumnTo> items = bodies.get(0).getItems();
                        if (CollectionUtils.isNotEmpty(items)) {
                            // 有些表格的字段配置，里面还会包一层group，group下面的body才是真正存字段配置的地方
                            if (items.size() == 1 && items.get(0).getType().equals("collapse-group")) {
                                List<BpmProcessDefinitionFromColumnTo> itemBodies = JsonUtils.covertObject(items.get(0).getBody(),
                                        new TypeReference<List<BpmProcessDefinitionFromColumnTo>>() {
                                        });
                                toList.addAll(JsonUtils.covertObject(itemBodies.get(0).getBody(),
                                        new TypeReference<List<BpmProcessDefinitionFromColumnTo>>() {
                                        }));
                            } else {
                                toList.addAll(items);
                            }
                        }
                        return toList;
                    })
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList()));
            columnList.addAll(bodyList);
        }
        // 处理非对象来源数据
        return columnList
                .stream()
                .filter(column -> Objects.nonNull(column.getSource()) && JsonUtils.isJson(column.getSource().toString()))
                .peek(column -> column.setSourceTo(JsonUtils.covertObject(column.getSource(), BpmProcessDefinitionFromColumnSourceTo.class)))
                .collect(Collectors.toList());
    }

    /**
     * 构建表单字段展示VO
     *
     * @param formColumnTo  表单字段配置
     * @param formVariables 表单填写值配置
     * @param optionTypeMap 表单选择项翻译映射关系
     * @param type          当前表单字段类型
     * @return 表单字段展示VO
     */
    private BpmProcessAttendanceFormVo buildProcessFromVo(BpmProcessDefinitionFromColumnTo formColumnTo, Map<String, Object> formVariables,
                                                          Map<String, Map<String, String>> optionTypeMap, String type) {
        // 有些表单配置不规范，或者配置了定义和填写分离的子表格，这种填写的子表格配置就不需要翻译了，否则会重复
        if (StringUtils.isBlank(type) || type.equals("table")) {
            return null;
        }
        // 有些表单使用的是子表格定义和填写框分离的配置，这种具体的字段配置一般是存在下面的字段里的
        if (type.equals("collapse")) {
            // 有些表单下面还会嵌套一层分组，真正的表格名称配置存在这里
            if (StringUtils.isBlank(formColumnTo.getLabel()) && StringUtils.isBlank(formColumnTo.getName())) {
                List<BpmProcessDefinitionFromColumnTo> bodyList = JsonUtils.covertObject(formColumnTo.getBody(),
                        new TypeReference<List<BpmProcessDefinitionFromColumnTo>>() {
                        });
                if (CollectionUtils.isEmpty(bodyList)) {
                    return null;
                }
                BpmProcessDefinitionFromColumnTo bodyFormTo = bodyList.get(0);
                formColumnTo.setName(bodyFormTo.getName());
            }
            // 有些表单是没有配label但是配了name的
            if (StringUtils.isBlank(formColumnTo.getLabel())) {
                formColumnTo.setLabel("表格");
            }
        }
        // 组装需要用到的来源数据
        if (Objects.nonNull(formColumnTo.getSource()) && JsonUtils.isJson(formColumnTo.getSource().toString())) {
            formColumnTo.setSourceTo(JsonUtils.covertObject(formColumnTo.getSource(), BpmProcessDefinitionFromColumnSourceTo.class));
        }
        BpmProcessAttendanceFormVo formVo = new BpmProcessAttendanceFormVo();
        formVo.setFormLabel(formColumnTo.getLabel());
        // 很多旧表单配置不规范，这里只是为了兜底
        if (StringUtils.isBlank(formColumnTo.getLabel())) {
            formVo.setFormLabel(formColumnTo.getName());
        }
        // 有些属性不知道为啥会配置带个.label之类的东西，需要处理一下
        if (StringUtils.isNotBlank(formColumnTo.getName()) && formColumnTo.getName().contains("\\.")) {
            String finalColumnName = formColumnTo.getName().split("\\.")[0];
            formVo.setFormName(finalColumnName);
            formColumnTo.setName(finalColumnName);
        }
        // 很多表单只有一个子表格且没配label和name，都用的table做name，所以还是得适配下...TnT
        if ((type.equals("input-table") || type.contains("combo")) && StringUtils.isBlank(formColumnTo.getName())) {
            formVo.setFormLabel("表格");
            formVo.setFormName("table");
            formColumnTo.setName("table");
        }
        // 很多表单会配置一个流程名，但是没有label，这里处理下
        if (Objects.equals(formColumnTo.getName(), "instanceName") && StringUtils.isBlank(formColumnTo.getLabel())) {
            formVo.setFormLabel("流程名称");
        }
        String value = MapUtils.getString(formVariables, formColumnTo.getName());
        if (StringUtils.isBlank(value)) {
            return null;
        }

        formVo.setFormValue(value);
        if (type.contains("select") || type.contains("radios")) {
            // 有些旧表单配置不规范，为了避免报错，需要过滤掉
            if (Objects.isNull(formColumnTo.getOptions()) && Objects.isNull(formColumnTo.getSource())) {
                return null;
            }
            Map<String, String> optionMap = new HashMap<>();
            List<BpmProcessDefinitionFromOptionsTo> optionsList = JsonUtils.covertObject(formColumnTo.getOptions(),
                    new TypeReference<List<BpmProcessDefinitionFromOptionsTo>>() {
                    });
            // 没有内置选择项，就是从接口拿的下拉数据
            if (CollectionUtils.isEmpty(optionsList)) {
                BpmProcessDefinitionFromColumnSourceTo source = formColumnTo.getSourceTo();
                if (source.getUrl().contains("user")) {
                    optionMap = optionTypeMap.getOrDefault("userMap", Collections.emptyMap());
                }
                if (source.getUrl().contains("dept")) {
                    optionMap = optionTypeMap.getOrDefault("deptMap", Collections.emptyMap());
                }
                if (source.getUrl().contains("post")) {
                    optionMap = optionTypeMap.getOrDefault("postMap", Collections.emptyMap());
                }
                if (source.getUrl().contains("project")) {
                    optionMap = optionTypeMap.getOrDefault("projectMap", Collections.emptyMap());
                }
                if (source.getUrl().contains("dict-data")) {
                    String dictType;
                    // 判断一下从哪里取，参数可能是单独存的，也有可能是直接拼在url上的
                    if (Objects.nonNull(source.getData())) {
                        dictType = new ArrayList<>(source.getData().values()).get(0);
                    } else {
                        dictType = source.getUrl().split("=")[1];
                    }
                    optionMap = optionTypeMap.getOrDefault("dict" + dictType, Collections.emptyMap());
                }
            }
            // 有内置选择项，就直接获取下拉数据
            else {
                optionMap = optionsList
                        .stream()
                        .collect(Collectors.toMap(BpmProcessDefinitionFromOptionsTo::getValue, BpmProcessDefinitionFromOptionsTo::getLabel));
            }
            // 某些情况下会是一个对象，这时需要单独取出id来
            if (JsonUtils.isJson(value)) {
                // 有些存的是个map，用字符串转的话转不出来
                Object objDict = MapUtils.getObject(formVariables, formColumnTo.getName());
                ;
                DictDataRespDTO dictTo = JsonUtils.covertObject(objDict, DictDataRespDTO.class);
                formVo.setFormValue(optionMap.get(dictTo.getValue()));
            } else {
                // 大部分情况下存的是id或者字典项，这种需要翻译出对应的值，也有可能以逗号分隔包含多个值
                List<String> values = new LinkedList<>();
                for (String id : value.split(",")) {
                    values.add(optionMap.getOrDefault(id, "未知"));
                }
                // 也有少数情况直接存的就是翻译后的值（不知道他们怎么配的...），这种直接赋值
                if (values.isEmpty()) {
                    values.add(value);
                }
                formVo.setFormValue(Joiner.on(",").join(values));
            }
        }
        if (type.contains("date") || type.contains("time")) {
            // 存在两种情况：单独存的时间，和以逗号分隔存的时间区间
            String[] split = value.split(",");
            if (split.length == 1) {
                // 有些时间直接存的就是字符串而不是时间戳，无语
                if (!StringUtils.isNumeric(split[0])) {
                    formVo.setFormValue(value);
                } else {
                    formVo.setFormValue(TimeUtils.formatAsDateTime(TimeTransUtils.transSecondTimestamp2Date(Long.parseLong(split[0]))));
                }
            }
            if (split.length == 2) {
                String formStartTime = TimeUtils.formatAsDateTime(TimeTransUtils.transSecondTimestamp2Date(Long.parseLong(split[0])));
                String formEndTime = TimeUtils.formatAsDateTime(TimeTransUtils.transSecondTimestamp2Date(Long.parseLong(split[1])));
                formVo.setFormValue(formStartTime + "-" + formEndTime);
            }
        }
        if (type.contains("text") || type.contains("number")) {
            String unit = "";
            if (CollectionUtils.isNotEmpty(formColumnTo.getUnitOptions())) {
                unit = formColumnTo.getUnitOptions().get(0);
            }
            formVo.setFormValue(value.replace("</p>", "").replace("<p>", "") + unit);
        }
        if (type.contains("file") || type.contains("image")) {
            // 图片有可能存的是对象数组，需要判断一下
            if (value.contains("[") && value.contains("{")) {
                Object fileObjList = MapUtils.getObject(formVariables, formColumnTo.getName());
                List<BpmProcessAttendanceFileVo> fileList = JsonUtils.covertObject(fileObjList,
                        new TypeReference<List<BpmProcessAttendanceFileVo>>() {
                        });
                List<String> fileUrlList = fileList
                        .stream()
                        .map(BpmProcessAttendanceFileVo::getUrl)
                        .collect(Collectors.toList());
                formVo.setFormValue(Joiner.on(",").join(fileUrlList));
            } else {
                formVo.setFormValue(value);
            }
        }
        if (type.equals("switch")) {
            boolean choose = Boolean.parseBoolean(value);
            if (StringUtils.isBlank(formColumnTo.getOnText())) {
                formColumnTo.setOnText("是");
            }
            if (StringUtils.isBlank(formColumnTo.getOffText())) {
                formColumnTo.setOffText("否");
            }
            formVo.setFormValue(choose ? formColumnTo.getOnText() : formColumnTo.getOffText());
        }
        // 如果有子表格，则重新组装一次
        if (type.equals("input-table") || type.equals("combo") || type.equals("collapse")) {
            Object tableValueObj = MapUtils.getObject(formVariables, formColumnTo.getName());
            List<Map<String, Object>> tableFormVariablesList = JsonUtils.covertObject(tableValueObj,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            // 不同配置的表单，表格输入值在不同的字段内
            List<BpmProcessDefinitionFromColumnTo> tableColumnList = new LinkedList<>();
            if (type.equals("input-table")) {
                tableColumnList.addAll(formColumnTo.getColumns());
            }
            if (type.equals("combo")) {
                tableColumnList.addAll(formColumnTo.getItems());
            }
            if (type.equals("collapse")) {
                List<BpmProcessDefinitionFromColumnTo> bodyList = JsonUtils.covertObject(formColumnTo.getBody(),
                        new TypeReference<List<BpmProcessDefinitionFromColumnTo>>() {
                        });
                if (CollectionUtils.isEmpty(bodyList)) {
                    return null;
                }
                List<BpmProcessDefinitionFromColumnTo> items = bodyList.get(0).getItems();
                if (CollectionUtils.isNotEmpty(items)) {
                    // 有些表格的字段配置，里面还会包一层group，group下面的body才是真正存字段配置的地方
                    if (items.size() == 1 && items.get(0).getType().equals("collapse-group")) {
                        List<BpmProcessDefinitionFromColumnTo> itemBodies = JsonUtils.covertObject(items.get(0).getBody(),
                                new TypeReference<List<BpmProcessDefinitionFromColumnTo>>() {
                                });
                        tableColumnList.addAll(JsonUtils.covertObject(itemBodies.get(0).getBody(),
                                new TypeReference<List<BpmProcessDefinitionFromColumnTo>>() {
                                }));
                    } else {
                        tableColumnList.addAll(items);
                    }
                }
            }
            List<String> tableStrList = new LinkedList<>();
            tableFormVariablesList.forEach(tableFormVariables -> {
                List<String> tableFormStrList = new LinkedList<>();
                tableColumnList.forEach(tableColumn -> {
                    if (tableColumn.getHidden()) {
                        return;
                    }
                    String tableColumnType = tableColumn.getType();
                    BpmProcessAttendanceFormVo tableColumnFormVo = this.buildProcessFromVo(tableColumn, tableFormVariables, optionTypeMap, tableColumnType);
                    if (Objects.isNull(tableColumnFormVo)) {
                        return;
                    }
                    tableFormStrList.add(tableColumnFormVo.getFormLabel() + ":" + tableColumnFormVo.getFormValue());
                });
                tableStrList.add("【" + Joiner.on(",").join(tableFormStrList) + "】");
            });
            if (!tableStrList.isEmpty()) {
                formVo.setFormValue("[" + Joiner.on(",").join(tableStrList) + "]");
            }
        }
        return formVo;
    }

    /**
     * 构建表单选择项翻译映射关系
     *
     * @param list            流程实例列表
     * @param processFormList 流程实例表单字段配置列表
     * @param bpmTaskExtList  节点表单实例列表
     * @param taskFormList    节点表单实例表单字段配置列表
     * @return 表单选择项翻译映射关系
     */
    private Map<String, Map<String, String>> getOptionTypeMap(List<BpmProcessInstanceExtDO> list, List<BpmProcessDefinitionFromColumnTo> processFormList,
                                                              List<BpmTaskExtDO> bpmTaskExtList, List<BpmProcessDefinitionFromColumnTo> taskFormList) {
        Map<String, Map<String, String>> result = new HashMap<>();
        // 现在考勤流程大概有几种需要翻译的：用户，部门，岗位，建能通项目，字典
        Set<Long> userIds = this.buildOptionFormTransIds(processFormList, list, taskFormList, bpmTaskExtList, "user");
        if (!userIds.isEmpty()) {
            Map<String, String> userMap = adminUserWrapper.getByIds(userIds)
                    .stream()
                    .collect(Collectors.toMap(user -> user.getId().toString(), AdminUserRespDTO::getNickname));
            result.put("userMap", userMap);
        }

        Set<Long> deptIds = this.buildOptionFormTransIds(processFormList, list, taskFormList, bpmTaskExtList, "dept");
        if (!deptIds.isEmpty()) {
            Map<String, String> deptMap = deptWrapper.getDeptList(deptIds)
                    .stream()
                    .collect(Collectors.toMap(dept -> dept.getId().toString(), DeptRespDTO::getName));
            result.put("deptMap", deptMap);
        }

        Set<Long> postIds = this.buildOptionFormTransIds(processFormList, list, taskFormList, bpmTaskExtList, "post");
        if (!deptIds.isEmpty()) {
            Map<String, String> postMap = postWrapper.getPostList(postIds)
                    .stream()
                    .collect(Collectors.toMap(post -> post.getId().toString(), PostTO::getName));
            result.put("postMap", postMap);
        }

        Set<Long> jntProjectIds = this.buildOptionFormTransIds(processFormList, list, taskFormList, bpmTaskExtList, "project");
        if (!jntProjectIds.isEmpty()) {
            Map<String, String> jntProjectMap = projectWrapper.getJntProjectMap(jntProjectIds);
            result.put("projectMap", jntProjectMap);
        }

        List<Map<String, String>> dictTypeList = new LinkedList<>();
        dictTypeList.addAll(processFormList
                .stream()
                .filter(column -> column.getSourceTo().getUrl().contains("dict-data"))
                .map(from -> {
                    Map<String, String> dictMap = new HashMap<>();
                    // 有两种情况，参数有可能是单独存的，也有可能是直接拼在url上的
                    if (MapUtils.isNotEmpty(from.getSourceTo().getData())) {
                        dictMap.putAll(from.getSourceTo().getData());
                    }
                    if (from.getSourceTo().getUrl().contains("?")) {
                        String url = from.getSourceTo().getUrl();
                        String dictType = url.split("=")[1];
                        dictMap.put("dictTypes", dictType);
                    }
                    return dictMap;
                })
                .collect(Collectors.toList()));
        dictTypeList.addAll(taskFormList
                .stream()
                .filter(column -> column.getSourceTo().getUrl().contains("dict-data"))
                .map(from -> {
                    Map<String, String> dictMap = new HashMap<>();
                    // 有两种情况，参数有可能是单独存的，也有可能是直接拼在url上的
                    if (MapUtils.isNotEmpty(from.getSourceTo().getData())) {
                        dictMap.putAll(from.getSourceTo().getData());
                    }
                    if (from.getSourceTo().getUrl().contains("?")) {
                        String url = from.getSourceTo().getUrl();
                        String dictType = url.split("=")[1];
                        dictMap.put("dictTypes", dictType);
                    }
                    return dictMap;
                })
                .collect(Collectors.toList()));
        Set<String> dictTypes = dictTypeList.stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toSet());
        Map<String, Map<String, String>> dictTypeMap = systemDictDataWrapper.getDictTypeMap(dictTypes);
        dictTypeMap.forEach((dictType, dictMap) -> result.put("dict" + dictType, dictMap));
        return result;
    }

    /**
     * 构建需要翻译的选择项类型的ids
     *
     * @param sourceList     实例表单字段配置列表
     * @param list           实例表单列表
     * @param taskFormList   节点表单字段配置列表
     * @param bpmTaskExtList 节点表单列表
     * @param type           当前翻译类型
     * @return 当前选择项类型的ids
     */
    private Set<Long> buildOptionFormTransIds(List<BpmProcessDefinitionFromColumnTo> sourceList, List<BpmProcessInstanceExtDO> list,
                                              List<BpmProcessDefinitionFromColumnTo> taskFormList, List<BpmTaskExtDO> bpmTaskExtList,
                                              String type) {
        Set<Long> ids = new HashSet<>();
        Set<String> processFormConfUserColumnNames = sourceList
                .stream()
                .filter(column -> column.getSourceTo().getUrl().contains(type))
                .map(BpmProcessDefinitionFromColumnTo::getName)
                .collect(Collectors.toSet());
        processFormConfUserColumnNames.forEach(columnName -> {
            // 有些配置的名字会带个.label之类的东西，要处理一下
            String finalColumnName = columnName.split("\\.")[0];
            list.forEach(instance -> {
                String valueStr = MapUtils.getString(instance.getFormVariables(), finalColumnName);
                if (StringUtils.isBlank(valueStr)) {
                    return;
                }
                // 表单配置的字段名称基本都不统一，所以需要限制类型，不然鬼知道会配个什么东西上去
                if (JsonUtils.isJson(valueStr)) {
                    // 有些貌似配置的是个map，字符串转不出来，统一用object处理
                    Object objDict = MapUtils.getObject(instance.getFormVariables(), finalColumnName);
                    DictDataRespDTO dictTo = JsonUtils.covertObject(objDict, DictDataRespDTO.class);
                    if (Objects.isNull(dictTo)) {
                        return;
                    }
                    ids.add(Long.parseLong(dictTo.getValue()));
                }
                else if (valueStr.contains(",")) {
                    ids.addAll(Arrays.stream(valueStr.split(",")).map(Long::parseLong).collect(Collectors.toSet()));
                }
                else if (StringUtils.isNumeric(valueStr)) {
                    ids.add(Long.parseLong(valueStr));
                }
            });
        });

        // 组装节点表单的数据
        Set<String> formExtConfUserColumnNames = taskFormList
                .stream()
                .filter(column -> column.getSourceTo().getUrl().contains(type))
                .map(BpmProcessDefinitionFromColumnTo::getName)
                .collect(Collectors.toSet());
        formExtConfUserColumnNames.forEach(columnName -> {
            String finalColumnName = columnName.split("\\.")[0];
            bpmTaskExtList.forEach(taskExt -> {
                String valueStr = MapUtils.getString(taskExt.getFormVariables(), finalColumnName);
                if (JsonUtils.isJson(valueStr)) {
                    Object objDict = MapUtils.getObject(taskExt.getFormVariables(), finalColumnName);
                    DictDataRespDTO dictTo = JsonUtils.covertObject(objDict, DictDataRespDTO.class);
                    if (Objects.isNull(dictTo)) {
                        return;
                    }
                    ids.add(Long.parseLong(dictTo.getValue()));
                }
                if (StringUtils.isNumeric(valueStr)) {
                    ids.add(Long.parseLong(valueStr));
                }
            });
        });
        return ids;
    }

    public static String toDateFormat(long time) {
        time = time * 1000;
        Date date = new Date(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime());
    }

    @Override
    public List<BpmProcessAttendanceExportVo> getExportProcessInstanceData(BpmProcessAttendanceQueryParam param) {
        String startTime = null;
        if (Objects.nonNull(param.getStartDate())) {
            startTime = TimeUtils.formatAsDateTime(TimeTransUtils.transSecondTimestamp2Date(param.getStartDate()));
        }
        String endTime = null;
        if (Objects.nonNull(param.getEndDate())) {
            endTime = TimeUtils.formatAsDateTime(TimeTransUtils.transSecondTimestamp2Date(param.getEndDate()));
        }
        Set<Long> userIds = adminUserWrapper.getContainUserList(param.getUserId(), param.getDeptId(), param.getProjectId());
        if ((Objects.nonNull(param.getUserId()) || Objects.nonNull(param.getDeptId()) || Objects.nonNull(param.getProjectId())) && userIds.isEmpty()) {
            return null;
        }
        LambdaQueryWrapperX<BpmProcessInstanceExtDO> lambdaQueryWrapper = new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .inIfPresent(BpmProcessInstanceExtDO::getStartUserId, userIds)
                .eqIfPresent(BpmProcessInstanceExtDO::getResult, BpmProcessInstanceResultEnum.APPROVE.getResult())
                .likeIfPresent(BpmProcessInstanceExtDO::getName, param.getProcessName())
                .betweenIfPresent(BpmProcessInstanceExtDO::getCreateTime, startTime, endTime)
                .orderByDesc(BpmProcessInstanceExtDO::getId);
        List<BpmProcessInstanceExtDO> instanceList = processInstanceExtMapper.selectList(lambdaQueryWrapper);
        if (instanceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<BpmProcessAttendanceVo> attendanceVos = this.transProcessInstance2AttendanceVo(instanceList);
        return JsonUtils.covertList(attendanceVos, BpmProcessAttendanceExportVo.class);
    }

    @Override
    public Set<String> getAllProcessInstanceIdsByUserId(Long userId) {
        List<BpmTaskExtDO> bpmTaskExtDOList = bpmTaskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>().eq(BpmTaskExtDO::getAssigneeUserId, userId));
        Set<String> processInstanceIdSet = bpmTaskExtDOList.stream().map(BpmTaskExtDO::getProcessInstanceId).collect(Collectors.toSet());
        return processInstanceIdSet;
    }


    private void getTaskFormData(Set<Integer> mergeIndexSet, Set<String> columns, List<List> objData, BpmProcessInstanceExtDO bpmProcessInstanceExtDO) {
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(bpmProcessInstanceExtDO.getProcessInstanceId())
                .orderByHistoricTaskInstanceStartTime()
                .desc()
                .list();
        Set<String> columnList = new LinkedHashSet<>();
        if (CollectionUtils.isNotEmpty(historicTaskInstanceList)) {
            //保留同类型的最晚一条
            List<HistoricTaskInstance> result = historicTaskInstanceList.stream()
                    .collect(Collectors.groupingBy(HistoricTaskInstance::getTaskDefinitionKey))
                    .entrySet().stream()
                    .map(entry -> {
                        List<HistoricTaskInstance> values = entry.getValue();
                        Collections.sort(values, Comparator.comparing(HistoricTaskInstance::getCreateTime).reversed());
                        return values.get(0);
                    })
                    .collect(Collectors.toList());
            Set<String> taskIds = result.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(taskIds)) {
                List<BpmTaskExtDO> bpmTaskExtDOS = bpmTaskExtMapper.selectListByTaskIds(taskIds);//节点数据
                if (CollectionUtils.isNotEmpty(bpmTaskExtDOS)) {
                    List<BpmTaskExtDO> formDataTaskExtDOS = bpmTaskExtDOS.stream().filter(bpmTaskExtDO -> ObjectUtils.isNotEmpty(bpmTaskExtDO.getFormKey())).collect(Collectors.toList());
                    //表格之外的数据
                    List<Object> data = new ArrayList<>();
                    //获取人员部门基本数据
                    CommonResult<List<AdminUserRespDTO>> users = adminUserApi.getAllUsers();
                    CommonResult<List<DeptRespDTO>> depts = deptApi.getAllDepts();
                    Map<Long, AdminUserRespDTO> userMapList = users.getData().stream().collect(Collectors.toMap(AdminUserRespDTO::getId, item -> item));
                    Map<Long, DeptRespDTO> deptMapList = depts.getData().stream().collect(Collectors.toMap(DeptRespDTO::getId, item -> item));
                    formDataTaskExtDOS.forEach(bpmTaskExtDO -> {
                        String name = bpmTaskExtDO.getName();
                        String formKey = bpmTaskExtDO.getFormKey();
                        log.info("formKey:{}", formKey);
                        List<BpmFormFieldExportDO> bpmTaskFormFieldExportDOS = bpmFormFieldExportMapper.selectListByFormId(Long.valueOf(formKey));
                        List<BpmProcessDefinitionRespVO> processDefinitionById = processDefinitionService.getProcessDefinitionById(bpmProcessInstanceExtDO.getProcessDefinitionId());
//                        Map<String, Object> formVariables = bpmProcessInstanceExtDO.getFormVariables();
//                        if (formVariables.isEmpty()) {
//                            return;
//                        }
                        if (processDefinitionById == null) {
                            return;
                        }
                        BpmProcessDefinitionRespVO bpmProcessDefinitionRespVO = processDefinitionById.get(0);
                        //获取表单配置
                        String formConf = bpmProcessDefinitionRespVO.getFormConf();
                        JSONObject formConfObj = JsonUtils.parseObject(formConf, JSONObject.class);
                        if (formConfObj == null) {
                            return;
                        }
                        JSONArray bodys = JsonUtils.parseObject(JsonUtils.toJsonString(formConfObj.get("body")), JSONArray.class);
                        if (bodys == null) {
                            return;
                        }
                        JSONObject o = JsonUtils.parseObject(JsonUtils.toJsonString(bodys.get(0)), JSONObject.class);
                        if (o == null) {
                            return;
                        }
                        //表单配置的具体信息
                        JSONArray formConfDetails = JsonUtils.parseObject(JsonUtils.toJsonString(o.get("body")), JSONArray.class);//表单组件配置项集合
                        assert formConfDetails != null;
                        LinkedHashMap<String, Object> formVariables = getDataFromBpmProcessInstance(bpmProcessInstanceExtDO, formConfDetails);
                        if (MapUtils.isEmpty(formVariables)) return;
                        if (ObjectUtils.isNotEmpty(formVariables)) {
                            LinkedHashMap<String, List<Map>> map = new LinkedHashMap<>();
                            bpmTaskFormFieldExportDOS.forEach(bpmFormFieldExportDO -> {
                                String field = bpmFormFieldExportDO.getField();
                                String fieldName = name + bpmFormFieldExportDO.getFieldName();
                                //表格字段
                                String tableName = "表格";
                                Boolean hasTableData = getTableData1(columnList, userMapList, deptMapList, formVariables, map, field, fieldName, tableName);
                                if (hasTableData) return;
                                //表格外数据
                                columns.add(fieldName);
                                Object value = formVariables.get(field);
                                if (ObjectUtils.isEmpty(value)) return;
                                data.add(value);
                            });
                            columns.addAll(columnList);
                            if (ObjectUtils.isNotEmpty(columnList) && columnList.size() > 0) {
                                for (int i = 0; i < columnList.size(); i++) {
                                    mergeIndexSet.add(columns.size() - 1 - i);
                                }
                            }
                            List<List<Map>> values = map.values().stream().sorted(Comparator.comparingInt(List::size)).collect(Collectors.toList());
                            int size = values.size() > 0 ? values.get(values.size() - 1).size() : 0;
                            Collection<List<Map>> values1 = map.values();
                            if (size == 0) {
                                List<Object> objects;
                                objects = new ArrayList<>(data);
                                objects.add(1);
                                objData.add(objects);
                            }
                            for (int i = 0; i < size; i++) {
                                List<Object> objects;
                                objects = new ArrayList<>(data);
                                int finalI = i;
                                values1.forEach(list -> {
                                    if (finalI < list.size()) {
                                        objects.add(list.get(finalI).toString());
                                    } else {
                                        objects.add("");
                                    }
                                });
                                if (i == 0) {//将表格编辑框数据条数设置到第一行 最后一位
                                    objects.add(size);
                                } else {//除第一行位 其他的设置为0
                                    objects.add(0);
                                }
                                objData.add(objects);
                            }
                        }
                    });
                }
            }
        }
    }

    private void getProcessInstanceFormData(Set<Integer> mergeIndexSet, Set<String> columns, List<List> objData, List<BpmFormFieldExportDO> bpmFormFieldExportDOS, BpmProcessInstanceExtDO bpmProcessInstanceExtDO, JSONArray formConfDetails) {
        Set<String> columnList = new LinkedHashSet<>();
        //表格之外的数据
        List<Object> data = new ArrayList<>();
        List<Object> fieldNameList = new ArrayList<>();
        LocalDateTime createTime = bpmProcessInstanceExtDO.getCreateTime();
        columns.add("发起时间");
        data.add(createTime);
        fieldNameList.add("发起时间");
        LocalDateTime endTime = bpmProcessInstanceExtDO.getEndTime();
        data.add(endTime);
        columns.add("结束时间");
        fieldNameList.add("结束时间");
        Long instanceExtDOStartUserId = bpmProcessInstanceExtDO.getStartUserId();
        CommonResult<List<AdminUserRespDTO>> users = adminUserApi.getAllUsers();
        CommonResult<List<DeptRespDTO>> depts = deptApi.getAllDepts();
        columns.add("流程");
        data.add(bpmProcessInstanceExtDO.getInstanceName());
        Map<Long, AdminUserRespDTO> userMapList = users.getData().stream().collect(Collectors.toMap(AdminUserRespDTO::getId, item -> item));
        Map<Long, DeptRespDTO> deptMapList = depts.getData().stream().collect(Collectors.toMap(DeptRespDTO::getId, item -> item));

        if (ObjectUtils.isNotEmpty(userMapList.get(instanceExtDOStartUserId))) {
            columns.add("发起人");
            data.add(userMapList.get(instanceExtDOStartUserId).getNickname());
        }
        Long deptId = bpmProcessInstanceExtDO.getDeptId();
        if (ObjectUtils.isNotEmpty(deptMapList.get(deptId))) {
            columns.add("部门");
            data.add(deptMapList.get(deptId).getName());
        }
        LinkedHashMap<String, Object> formVariables = getDataFromBpmProcessInstance(bpmProcessInstanceExtDO, formConfDetails);
        if (ObjectUtils.isNotEmpty(formVariables)) {
            LinkedHashMap<String, List<Map>> map = new LinkedHashMap<>();

            bpmFormFieldExportDOS.forEach(bpmFormFieldExportDO -> {
                String field = bpmFormFieldExportDO.getField();
                String fieldName = bpmFormFieldExportDO.getFieldName();
                //表格字段
                String tableName = "表格";
                Boolean hasTableData = getTableData1(columnList, userMapList, deptMapList, formVariables, map, field, fieldName, tableName);
                if (hasTableData) return;
                //表格外数据
                columns.add(fieldName);
                Object value = formVariables.get(field);
                if (ObjectUtils.isEmpty(value)) return;
                data.add(value);
            });
            columns.addAll(columnList);
            if (ObjectUtils.isNotEmpty(columnList) && columnList.size() > 0) {
                for (int i = 0; i < columnList.size(); i++) {
                    mergeIndexSet.add(columns.size() - 1 - i);
                }
            }
            List<List<Map>> values = map.values().stream().sorted(Comparator.comparingInt(List::size)).collect(Collectors.toList());
            int size = values.size() > 0 ? values.get(values.size() - 1).size() : 0;
            Collection<List<Map>> values1 = map.values();
            if (size == 0) {
                List<Object> objects;
                objects = new ArrayList<>(data);
                objects.add(1);
                objData.add(objects);
            }
            for (int i = 0; i < size; i++) {
                List<Object> objects;
                objects = new ArrayList<>(data);
                int finalI = i;
                values1.forEach(list -> {
                    if (finalI < list.size()) {
                        objects.add(list.get(finalI).toString());
                    } else {
                        objects.add("");
                    }
                });
                if (i == 0) {//将表格编辑框数据条数设置到第一行 最后一位
                    objects.add(size);
                } else {//除第一行位 其他的设置为0
                    objects.add(0);
                }
                objData.add(objects);
            }
        }
    }


    private static Boolean getTableData(Set<String> columnList, Map<Long, AdminUserRespDTO> userMapList, Map<Long, DeptRespDTO> deptMapList, Map<String, Object> formVariables, List<Map> fieldValueList, String field, String fieldName, String tableName) {
        if (field.contains("table")) {
            if (fieldName.contains(".")) {
                tableName = fieldName.split("\\.")[0];
            }
            columnList.add(tableName);
            String[] split = field.split("\\.");
            if (split.length > 1) {
                field = field.replace(split[0] + ".", "");
            }
            Object value = formVariables.get(split[0]);
            if (ObjectUtils.isEmpty(value)) return true;
            if (value instanceof List) {
                List<Map> valueList = JsonUtils.covertObject(value, List.class);
                Map<String, Object> tableValueMap = new LinkedHashMap<>();

                for (int i = 0; i < valueList.size(); i++) {
                    Map map1 = valueList.get(i);
                    Object tableValue = map1.get(field);
                    if (field.contains(".")) {
                        String[] split1 = field.split("\\.");
                        //表格中字段
                        for (String s : split1) {
                            tableValue = map1.get(s);
                            tableValue = changeUserAndDeptValue(userMapList, deptMapList, tableValue, s);
                            if (tableValue instanceof Map) {
                                map1 = (Map) tableValue;
                            }
                        }
                    } else {
                        tableValue = changeUserAndDeptValue(userMapList, deptMapList, tableValue, field);
                    }
                    if (fieldName.contains(".")) {
                        String[] split1 = fieldName.split("\\.");
                        fieldName = split1[split1.length - 1];
                        tableName = split1[0];
                    }
                    if (CollectionUtils.isNotEmpty(fieldValueList) && fieldValueList.size() > i) {
                        tableValueMap = fieldValueList.get(i);
                        tableValueMap.put(fieldName, tableValue);
                        fieldValueList.set(i, tableValueMap);

                    } else {
                        tableValueMap = new LinkedHashMap<>();
                        tableValueMap.put(fieldName, tableValue);
                        fieldValueList.add(tableValueMap);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private static Boolean getTableData1(Set<String> columnList, Map<Long, AdminUserRespDTO> userMapList, Map<Long, DeptRespDTO> deptMapList, Map<String, Object> formVariables, LinkedHashMap<String, List<Map>> fieldValueList, String field, String fieldName, String tableName) {
        if (field.contains("table")) {
            if (fieldName.contains(".")) {
                tableName = fieldName.split("\\.")[0];
            }
            columnList.add(tableName);
            String[] split = field.split("\\.");
            if (split.length > 1) {
                field = field.replace(split[0] + ".", "");
            }
            String key = split[0];
            Object value = formVariables.get(split[0]);

            if (fieldValueList.containsKey(key)) {
                List<Map> list = fieldValueList.get(key);
                if (ObjectUtils.isEmpty(value)) {
                    fieldValueList.put(key, list);
                    return true;
                }
                if (value instanceof List) {
                    List<Map> valueList = JsonUtils.covertObject(value, List.class);
                    Map<String, Object> tableValueMap = new LinkedHashMap<>();

                    for (int i = 0; i < valueList.size(); i++) {
                        Map map1 = valueList.get(i);
                        Object tableValue = map1.get(field);
                        if (field.contains(".")) {
                            String[] split1 = field.split("\\.");
                            //表格中字段
                            for (String s : split1) {
                                tableValue = map1.get(s);
                                tableValue = changeUserAndDeptValue(userMapList, deptMapList, tableValue, s);
                                if (tableValue instanceof Map) {
                                    map1 = (Map) tableValue;
                                }
                            }
                        } else {
                            tableValue = changeUserAndDeptValue(userMapList, deptMapList, tableValue, field);
                        }
                        if (fieldName.contains(".")) {
                            String[] split1 = fieldName.split("\\.");
                            fieldName = split1[split1.length - 1];
                            tableName = split1[0];
                        }
                        if (CollectionUtils.isNotEmpty(list) && list.size() > i) {
                            tableValueMap = list.get(i);
                            tableValueMap.put(fieldName, tableValue);
                            list.set(i, tableValueMap);

                        } else {
                            tableValueMap = new LinkedHashMap<>();
                            tableValueMap.put(fieldName, tableValue);
                            list.add(tableValueMap);
                        }
                    }
                    fieldValueList.put(key, list);
                    return true;
                }

            } else {
                List<Map> list = new ArrayList<>();
                if (ObjectUtils.isEmpty(value)) {
                    fieldValueList.put(key, list);
                    return true;
                }
                if (value instanceof List) {
                    List<Map> valueList = JsonUtils.covertObject(value, List.class);
                    Map<String, Object> tableValueMap = new LinkedHashMap<>();
                    for (int i = 0; i < valueList.size(); i++) {
                        Map map1 = valueList.get(i);
                        Object tableValue = map1.get(field);
                        if (field.contains(".")) {
                            String[] split1 = field.split("\\.");
                            //表格中字段
                            for (String s : split1) {
                                tableValue = map1.get(s);
                                tableValue = changeUserAndDeptValue(userMapList, deptMapList, tableValue, s);
                                if (tableValue instanceof Map) {
                                    map1 = (Map) tableValue;
                                }
                            }
                        } else {
                            tableValue = changeUserAndDeptValue(userMapList, deptMapList, tableValue, field);
                        }
                        if (fieldName.contains(".")) {
                            String[] split1 = fieldName.split("\\.");
                            fieldName = split1[split1.length - 1];
                            tableName = split1[0];
                        }
                        if (CollectionUtils.isNotEmpty(list) && list.size() > i) {
                            tableValueMap = list.get(i);
                            tableValueMap.put(fieldName, tableValue);
                            list.set(i, tableValueMap);

                        } else {
                            tableValueMap = new LinkedHashMap<>();
                            tableValueMap.put(fieldName, tableValue);
                            list.add(tableValueMap);
                        }
                    }
                    fieldValueList.put(key, list);
                    return true;
                }
            }
        }
        return false;
    }

    private static Object changeUserAndDeptValue(Map<Long, AdminUserRespDTO> userMapList, Map<Long, DeptRespDTO> deptMapList, Object tableValue, String field) {
        if (tableValue instanceof Number) {
            if (field.contains("dept")) {
                DeptRespDTO deptRespDTO = deptMapList.get(Long.valueOf(tableValue.toString()));
                if (ObjectUtils.isNotEmpty(deptRespDTO)) {
                    tableValue = deptRespDTO.getName();
                }
            }
            if (field.contains("user")) {
                AdminUserRespDTO adminUserRespDTO = userMapList.get(Long.valueOf(tableValue.toString()));
                if (ObjectUtils.isNotEmpty(adminUserRespDTO)) {
                    tableValue = adminUserRespDTO.getNickname();
                }
            }
        }
        return tableValue;
    }

    /**
     * 从流程实例中获取表单 解析表单 获取数据
     *
     * @param bpmProcessInstanceExtDO
     * @return
     */
    private LinkedHashMap<String, Object> getDataFromBpmProcessInstance(BpmProcessInstanceExtDO bpmProcessInstanceExtDO, JSONArray formConfDetails) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        Map<String, Object> formVariables = bpmProcessInstanceExtDO.getFormVariables();
        //根据填写的值获取展示的值 主要是根据下拉框的value去获取对应的label 便于展示在导出的表格中
        for (int i = 0; i < formConfDetails.size(); i++) {
            Object form = formConfDetails.get(i);
            JSONObject jsonObject = JsonUtils.parseObject(JsonUtils.toJsonString(form), JSONObject.class);
            assert jsonObject != null;
            String type = String.valueOf(jsonObject.get("type"));
            if (type.equals("table")) {
                //table后必然跟一个collapse
                Object nextForm = formConfDetails.get(i + 1);
                JSONObject nextFormJsonObject = JsonUtils.parseObject(JsonUtils.toJsonString(nextForm), JSONObject.class);
                assert nextFormJsonObject != null;
                jsonObject.put("config", nextFormJsonObject.get("body"));
            }
            getFormDetail(jsonObject, formVariables, resultMap);
        }
        return resultMap;
    }

    /**
     * 获取填写的数据 主要是下拉框匹配
     *
     * @param jsonObject    当前组件的定义信息
     * @param formVariables 填写的信息
     * @param resultMap     返回值
     */
    private void getFormDetail(JSONObject jsonObject, Map<String, Object> formVariables, LinkedHashMap<String, Object> resultMap) {
        String formName = String.valueOf(jsonObject.get("name"));
        String formValue = String.valueOf(formVariables.get(formName));//选择的值
        boolean contains = formVariables.containsKey(formName);
        if (contains) {
            String type = (String) jsonObject.get("type");
            if (FORM_TYPE_SELECT.equals(type) || FORM_TYPE_REDIOS.equals(type)) {
                //是下拉框则查询下拉选项并匹配
                //下拉的value
                JSONObject source = JsonUtils.parseObject(JsonUtils.toJsonString(jsonObject.get("source")), JSONObject.class);
                if (source != null) {//从接口获取数据
                    //接口地址
                    String url = (String) source.get("url");
                    //接口参数
                    StringBuilder stringBuilder = new StringBuilder("?");
                    Object data1 = source.get("data");
                    if (data1 != null) {
                        Map data = JsonUtils.covertObject(data1, Map.class);
                        data.forEach((key, value) -> {
                            String strKey = (String) key;
                            String strValue = (String) value;
                            if (strValue.contains("$")) {
                                //参数和表单数据绑定 一般为自定义接口
                                String param = (String) formVariables.get(strValue.replace("$", "").replace("{", "").replace("}", ""));
                                if (StringUtils.isNotBlank(param)) {
                                    stringBuilder.append("&").append(strKey).append("=").append(param);
                                }
                            } else {
                                //参数写死  一般为字典接口
                                stringBuilder.append("&").append(strKey).append("=").append(value);
                            }
                        });
                        url = url + stringBuilder;
                    }
                    List<Map<String, String>> sourceOptions = getSelectOption(url);
                    // 填充数据
                    sourceOptions.forEach(map -> {
                        String label = map.get("label");
                        String value = String.valueOf(map.get("value"));
                        if (value.equals(formValue)) {
                            resultMap.put(formName, label);
                        }
                    });
                } else {
                    //手动配置的数据
                    JSONArray options = JsonUtils.parseObject(JsonUtils.toJsonString(jsonObject.get("options")), JSONArray.class);
                    options.forEach(obj -> {
                        Map<String, String> map = JsonUtils.parseObject(JsonUtils.toJsonString(obj), Map.class);
                        String value = String.valueOf(map.get("value"));
                        if (value.equals(formValue)) {
                            resultMap.put(formName, map.get("label"));
                        }
                    });
                }
            } else {
                //不是下拉框直接将数据写入
                resultMap.put(formName, formVariables.get(formName));
            }
        }
    }

    public List<Map<String, String>> getSelectOption(String url) {
        List<Map<String, String>> resultList = new ArrayList<>();
        log.info("调用接口获取下拉框数据，url:{}", url);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(request);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getEntity().getContent());
            JSONArray list = JsonUtils.parseObject(JsonUtils.toJsonString(jsonNode.get("data")), JSONArray.class);
            log.info("调用结果:{}", JsonUtils.toJsonString(jsonNode));
            if (list != null) {
                list.forEach(obj -> {
                    Map map = JsonUtils.covertObject(obj, Map.class);
                    resultList.add(map);
                });
            }
        } catch (IOException e) {
            log.info("调用失败:{}", e.getMessage());
        }
        return resultList;
    }


}
