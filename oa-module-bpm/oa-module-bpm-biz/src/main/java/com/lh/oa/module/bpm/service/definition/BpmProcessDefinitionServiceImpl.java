package com.lh.oa.module.bpm.service.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.object.PageUtils;
import com.lh.oa.framework.flowable.core.util.FlowableUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionListReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionPageItemRespVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionPageReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionRespVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessTypeGroupRespVO;
import com.lh.oa.module.bpm.convert.definition.BpmProcessDefinitionConvert;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmModelDeptDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmModelExtDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmModelDeptMapper;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmModelExtMapper;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmProcessDefinitionExtMapper;
import com.lh.oa.module.bpm.enums.ErrorCodeConstants;
import com.lh.oa.module.bpm.service.definition.dto.BpmProcessDefinitionCreateReqDTO;
import com.lh.oa.module.bpm.wrapper.SystemDictDataWrapper;
import com.lh.oa.module.system.api.permission.PermissionApi;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.common.engine.impl.util.io.BytesStreamSource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.addIfNotNull;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertList;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertMap;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertSet;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static java.util.Collections.emptyList;

/**
 * @author
 */
@Service
@Validated
@Slf4j
public class BpmProcessDefinitionServiceImpl implements BpmProcessDefinitionService {

    private static final String BPMN_FILE_SUFFIX = ".bpmn";

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private BpmProcessDefinitionExtMapper processDefinitionMapper;

    @Resource
    private BpmFormService formService;

    @Resource
    private BpmModelExtMapper bpmModelExtMapper;

    @Resource
    private SystemDictDataWrapper systemDictDataWrapper;
    @Resource
    private PermissionApi permissionApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private BpmModelDeptMapper bpmModelDeptMapper;

    @Override
    public ProcessDefinition getProcessDefinition(String id) {
        return repositoryService.getProcessDefinition(id);
    }

    @Override
    public ProcessDefinition getProcessDefinition2(String id) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
    }

    @Override
    public ProcessDefinition getProcessDefinitionByDeploymentId(String deploymentId) {
        if (CharSequenceUtil.isEmpty(deploymentId)) {
            return null;
        }
        return repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
    }

    @Override
    public List<ProcessDefinition> getProcessDefinitionListByDeploymentIds(Set<String> deploymentIds) {
        if (CollUtil.isEmpty(deploymentIds)) {
            return emptyList();
        }
        return repositoryService.createProcessDefinitionQuery().deploymentIds(deploymentIds).list();
    }

    @Override
    public ProcessDefinition getActiveProcessDefinition(String key) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).active().singleResult();
    }

    @Override
    public List<Deployment> getDeployments(Set<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return emptyList();
        }
        List<Deployment> list = new ArrayList<>(ids.size());
        for (String id : ids) {
            addIfNotNull(list, getDeployment(id));
        }
        return list;
    }

    @Override
    public Deployment getDeployment(String id) {
        if (CharSequenceUtil.isEmpty(id)) {
            return null;
        }
        return repositoryService.createDeploymentQuery().deploymentId(id).singleResult();
    }

    @Override
    public BpmnModel getBpmnModel(String processDefinitionId) {
        return repositoryService.getBpmnModel(processDefinitionId);
    }

    @Override
    public String createProcessDefinition(@Valid BpmProcessDefinitionCreateReqDTO createReqDTO) {
        Deployment deploy = repositoryService.createDeployment()
                .key(createReqDTO.getKey())
                .name(createReqDTO.getName())
                .category(createReqDTO.getCategory())
                .addBytes(createReqDTO.getKey() + BPMN_FILE_SUFFIX, createReqDTO.getBpmnBytes())
                .deploy();

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploy.getId()).singleResult();
        repositoryService.setProcessDefinitionCategory(definition.getId(), createReqDTO.getCategory());

        if (!Objects.equals(definition.getKey(), createReqDTO.getKey())) {
            throw exception(ErrorCodeConstants.PROCESS_DEFINITION_KEY_NOT_MATCH, createReqDTO.getKey(), definition.getKey());
        }
        if (!Objects.equals(definition.getName(), createReqDTO.getName())) {
            throw exception(ErrorCodeConstants.PROCESS_DEFINITION_NAME_NOT_MATCH, createReqDTO.getName(), definition.getName());
        }

        BpmProcessDefinitionExtDO definitionDO = BpmProcessDefinitionConvert.INSTANCE.convert2(createReqDTO)
                .setProcessDefinitionId(definition.getId());
        processDefinitionMapper.insert(definitionDO);
        return definition.getId();
    }

    @Override
    public void updateProcessDefinitionState(String id, Integer state) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        if (processDefinition == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PROCESS_DEFINITION_NOT_EXISTS);
        }
        // 当前流程是否未激活
        boolean isSuspended = processDefinition.isSuspended();
        if (Objects.equals(SuspensionState.ACTIVE.getStateCode(), state)) {
            if (!isSuspended) {
                // 流程激活状态，再次调用下面方法会报错，测试要求这种情况不提示，直接返回操作成功
                return;
            }
            repositoryService.activateProcessDefinitionById(id, false, null);
            return;
        }
        if (Objects.equals(SuspensionState.SUSPENDED.getStateCode(), state)) {
            if (isSuspended) {
                // 流程处于未激活状态，再次调用下面方法会报错
                return;
            }
            repositoryService.suspendProcessDefinitionById(id, false, null);
            return;
        }
        log.error("[updateProcessDefinitionState][流程定义({}) 修改未知状态({})]", id, state);
    }

    @Override
    public String getProcessDefinitionBpmnXML(String id) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(id);
        if (bpmnModel == null) {
            return null;
        }
        BpmnXMLConverter converter = new BpmnXMLConverter();
        return StrUtil.utf8Str(converter.convertToXML(bpmnModel));
    }

    @Override
    public boolean isProcessDefinitionEquals(@Valid BpmProcessDefinitionCreateReqDTO createReqDTO) {
        ProcessDefinition oldProcessDefinition = getActiveProcessDefinition(createReqDTO.getKey());
        if (oldProcessDefinition == null) {
            return false;
        }
        BpmProcessDefinitionExtDO oldProcessDefinitionExt = getProcessDefinitionExt(oldProcessDefinition.getId());
        if (!CharSequenceUtil.equals(createReqDTO.getName(), oldProcessDefinition.getName())
                || !CharSequenceUtil.equals(createReqDTO.getDescription(), oldProcessDefinitionExt.getDescription())
                || !CharSequenceUtil.equals(createReqDTO.getCategory(), oldProcessDefinition.getCategory())) {
            return false;
        }
        if (!ObjectUtil.equal(createReqDTO.getFormType(), oldProcessDefinitionExt.getFormType())
                || !ObjectUtil.equal(createReqDTO.getFormId(), oldProcessDefinitionExt.getFormId())
                || !ObjectUtil.equal(createReqDTO.getFormConf(), oldProcessDefinitionExt.getFormConf())
                || !ObjectUtil.equal(createReqDTO.getFormFields(), oldProcessDefinitionExt.getFormFields())
                || !ObjectUtil.equal(createReqDTO.getFormCustomCreatePath(), oldProcessDefinitionExt.getFormCustomCreatePath())
                || !ObjectUtil.equal(createReqDTO.getFormCustomViewPath(), oldProcessDefinitionExt.getFormCustomViewPath())) {
            return false;
        }
        BpmnModel newModel = buildBpmnModel(createReqDTO.getBpmnBytes());
        BpmnModel oldModel = getBpmnModel(oldProcessDefinition.getId());
        if (FlowableUtils.equals(oldModel, newModel)) {
            return false;
        }
        return true;
    }

    /**
     * 构建对应的 BPMN Model
     *
     * @param bpmnBytes 原始的 BPMN XML 字节数组
     * @return BPMN Model
     */
    private BpmnModel buildBpmnModel(byte[] bpmnBytes) {
        // 转换成 BpmnModel 对象
        BpmnXMLConverter converter = new BpmnXMLConverter();
        return converter.convertToBpmnModel(new BytesStreamSource(bpmnBytes), true, true);
    }

    @Override
    public BpmProcessDefinitionExtDO getProcessDefinitionExt(String id) {
        return processDefinitionMapper.selectByProcessDefinitionId(id);
    }

    @Override
    public List<BpmProcessTypeGroupRespVO> getProcessDefinitionList(BpmProcessDefinitionListReqVO listReqVO) {
        log.info("查询流程定义列表:{}", JsonUtils.toJsonString(listReqVO));

        List<BpmProcessDefinitionExtDO> processDefinitionDOs;
        Map<String, BpmProcessDefinitionExtDO> processDefinitionDOMap;
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();

        if (StringUtils.isNotBlank(listReqVO.getName())) {
            definitionQuery.processDefinitionNameLike("%" + listReqVO.getName() + "%");
        }

        if (Objects.equals(SuspensionState.SUSPENDED.getStateCode(), listReqVO.getSuspensionState())) {
            definitionQuery.suspended();
        } else if (Objects.equals(SuspensionState.ACTIVE.getStateCode(), listReqVO.getSuspensionState())) {
            definitionQuery.active();
        }

        List<ProcessDefinition> processDefinitions = definitionQuery.list();

        if (CollUtil.isEmpty(processDefinitions)) {
            return Collections.emptyList();
        }
        //查询登录人的部门权限
        Long loginUserId = getLoginUserId();
        log.info("登录用户loginUserId:{}", loginUserId);
        LambdaQueryWrapperX<BpmProcessDefinitionExtDO> bpmProcessDefinitionExtDOLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        if (ObjectUtils.isNotEmpty(loginUserId)) {
            CommonResult<AdminUserRespDTO> user = adminUserApi.getUser(loginUserId);
            if (ObjectUtils.isNotEmpty(user.getData())) {
                Long deptId = user.getData().getDeptId();
                log.info("登录用户部门id:{}", deptId);
                if (ObjectUtils.isNotEmpty(deptId)) {
                    List<BpmModelDeptDO> bpmModelDeptDOS = bpmModelDeptMapper.selectList(new LambdaQueryWrapperX<BpmModelDeptDO>().eqIfPresent(BpmModelDeptDO::getDeptId, deptId));
                    Set<String> modelIds = new HashSet<>();
                    if (CollUtil.isNotEmpty(bpmModelDeptDOS)) {
                        modelIds = bpmModelDeptDOS.stream().map(BpmModelDeptDO::getModelId).collect(Collectors.toSet());
                    }
                    if (CollUtil.isEmpty(modelIds)) {
                        return new ArrayList<>();
                    }
                    bpmProcessDefinitionExtDOLambdaQueryWrapperX.in(BpmProcessDefinitionExtDO::getModelId, modelIds);
                }
            }
        }
        if (listReqVO.getId() == null) {
            Set<String> processDefinitionIds = processDefinitions.stream().map(ProcessDefinition::getId).collect(Collectors.toSet());

            processDefinitionDOs = processDefinitionMapper.selectList(bpmProcessDefinitionExtDOLambdaQueryWrapperX
                    .inIfPresent(BpmProcessDefinitionExtDO::getProcessDefinitionId, processDefinitionIds));
        } else {
            processDefinitions = processDefinitions.stream().filter(s -> s.getId().equals(listReqVO.getId())).collect(Collectors.toList());
            processDefinitionDOs = processDefinitionMapper.selectList(bpmProcessDefinitionExtDOLambdaQueryWrapperX.eqIfPresent(BpmProcessDefinitionExtDO::getProcessDefinitionId, listReqVO.getId()));
        }
        processDefinitionDOMap = convertMap(processDefinitionDOs,
                BpmProcessDefinitionExtDO::getProcessDefinitionId);

        Set<String> modelIds = processDefinitionDOs.stream().map(BpmProcessDefinitionExtDO::getModelId).collect(Collectors.toSet());
        Map<String, BpmModelExtDO> modelExtDOMap = bpmModelExtMapper.selectList(new LambdaQueryWrapperX<BpmModelExtDO>().inIfPresent(BpmModelExtDO::getModelId, modelIds))
                .stream().collect(Collectors.toMap(BpmModelExtDO::getModelId, Function.identity()));

        Map<String, List<BpmProcessDefinitionRespVO>> processTypeMap = BpmProcessDefinitionConvert.INSTANCE.convertList3(processDefinitions, processDefinitionDOMap, modelExtDOMap);
        //根据流程类型筛选
        if (StringUtils.isNotBlank(listReqVO.getProcessTypeId())) {
            List<BpmProcessDefinitionRespVO> bpmProcessDefinitionRespVOS = processTypeMap.get(listReqVO.getProcessTypeId());
            processTypeMap.clear();
            processTypeMap.put(listReqVO.getProcessTypeId(), bpmProcessDefinitionRespVOS);
        }

        Map<String, Map<String, String>> processTypeDict = systemDictDataWrapper.getDictTypeMap("process_type");
        Map<String, String> processTypeDictMap = processTypeDict.get("process_type");

        List<BpmProcessTypeGroupRespVO> processTypeGroupRespVOList = new ArrayList<>();
        processTypeMap.forEach((processType, processList) -> {
            BpmProcessTypeGroupRespVO processTypeGroupRespVO = new BpmProcessTypeGroupRespVO();
            processTypeGroupRespVO.setProcessList(processList);

            String typeName = processTypeDictMap.get(processType);
            processTypeGroupRespVO.setProcessTypeId(processType);
            processTypeGroupRespVO.setProcessTypeName(typeName);
            processTypeGroupRespVOList.add(processTypeGroupRespVO);
        });
        return processTypeGroupRespVOList;
    }

    @Override
    public List<BpmProcessDefinitionRespVO> getProcessDefinitionById(String definitionId) {
        log.info("查询流程定义详情:{}", definitionId);

        List<BpmProcessDefinitionExtDO> processDefinitionDOs;
        Map<String, BpmProcessDefinitionExtDO> processDefinitionDOMap;
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery().processDefinitionId(definitionId);

        List<ProcessDefinition> processDefinitions = definitionQuery.list();

        if (CollUtil.isEmpty(processDefinitions)) {
            throw new BusinessException("未查询到流程定义");
        }

        Set<String> processDefinitionIds = processDefinitions.stream().map(ProcessDefinition::getId).collect(Collectors.toSet());

        processDefinitionDOs = processDefinitionMapper.selectList(new LambdaQueryWrapperX<BpmProcessDefinitionExtDO>()
                .in(BpmProcessDefinitionExtDO::getProcessDefinitionId, processDefinitionIds));

        processDefinitionDOMap = convertMap(processDefinitionDOs,
                BpmProcessDefinitionExtDO::getProcessDefinitionId);

        Set<String> modelIds = processDefinitionDOs.stream().map(BpmProcessDefinitionExtDO::getModelId).collect(Collectors.toSet());
        Map<String, BpmModelExtDO> modelExtDOMap = bpmModelExtMapper.selectList(new LambdaQueryWrapperX<BpmModelExtDO>().in(BpmModelExtDO::getModelId, modelIds))
                .stream().collect(Collectors.toMap(BpmModelExtDO::getModelId, Function.identity()));

        return BpmProcessDefinitionConvert.INSTANCE.convertList3(processDefinitions, processDefinitionDOMap, modelExtDOMap)
                .values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public PageResult<BpmProcessDefinitionPageItemRespVO> getProcessDefinitionPage(BpmProcessDefinitionPageReqVO pageVO) {
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        if (CharSequenceUtil.isNotBlank(pageVO.getKey())) {
            definitionQuery.processDefinitionKey(pageVO.getKey());
        }

        List<ProcessDefinition> processDefinitions = definitionQuery.orderByProcessDefinitionVersion().desc()
                .listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());

        if (CollUtil.isEmpty(processDefinitions)) {
            return new PageResult<>(emptyList(), definitionQuery.count());
        }
        Set<String> deploymentIds = new HashSet<>();
        processDefinitions.forEach(definition -> addIfNotNull(deploymentIds, definition.getDeploymentId()));
        Map<String, Deployment> deploymentMap = getDeploymentMap(deploymentIds);

        List<BpmProcessDefinitionExtDO> processDefinitionDOs = processDefinitionMapper.selectListByProcessDefinitionIds(
                convertList(processDefinitions, ProcessDefinition::getId));
        Map<String, BpmProcessDefinitionExtDO> processDefinitionDOMap = convertMap(processDefinitionDOs,
                BpmProcessDefinitionExtDO::getProcessDefinitionId);

        Set<Long> formIds = convertSet(processDefinitionDOs, BpmProcessDefinitionExtDO::getFormId);
        Map<Long, BpmFormDO> formMap = formService.getFormMap(formIds);

        long definitionCount = definitionQuery.count();
        return new PageResult<>(BpmProcessDefinitionConvert.INSTANCE.convertList(processDefinitions, deploymentMap,
                processDefinitionDOMap, formMap), definitionCount);
    }

    @Override
    public List<BpmProcessDefinitionExtDO> getByDefinitionIds(Set<String> procDefinitionIds) {
        return processDefinitionMapper.selectListByProcessDefinitionIds(procDefinitionIds)
                .stream()
                .filter(process -> StringUtils.isNotBlank(process.getFormConf()))
                .collect(Collectors.toList());
    }

}
