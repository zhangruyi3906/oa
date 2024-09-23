package com.lh.oa.module.bpm.service.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.object.PageUtils;
import com.lh.oa.framework.common.util.validation.ValidationUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.controller.admin.definition.vo.model.BpmModelCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.model.BpmModelPageItemRespVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.model.BpmModelPageReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.model.BpmModelRespVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.model.BpmModelUpdateReqVO;
import com.lh.oa.module.bpm.convert.definition.BpmModelConvert;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmModelDeptDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmModelExtDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmModelDeptMapper;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmModelExtMapper;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmProcessDefinitionExtMapper;
import com.lh.oa.module.bpm.enums.ErrorCodeConstants;
import com.lh.oa.module.bpm.enums.definition.BpmModelFormTypeEnum;
import com.lh.oa.module.bpm.service.definition.dto.BpmModelMetaInfoRespDTO;
import com.lh.oa.module.bpm.service.definition.dto.BpmProcessDefinitionCreateReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.common.engine.impl.util.io.BytesStreamSource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertMap;

/**
 *
 */
@Service
@Validated
@Slf4j
public class BpmModelServiceImpl implements BpmModelService {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private BpmFormService bpmFormService;
    @Resource
    private BpmTaskAssignRuleService taskAssignRuleService;
    @Resource
    private BpmProcessDefinitionExtMapper mapper;
    @Resource
    private BpmModelExtMapper bpmModelExtMapper;
    @Resource
    private BpmModelDeptService bpmModelDeptService;
    @Resource
    private BpmModelDeptMapper bpmModelDeptMapper;

    @Override
    public PageResult<BpmModelPageItemRespVO> getModelPage(BpmModelPageReqVO pageVO) {
        log.info("params:{}", JsonUtils.toJsonString(pageVO));
        ModelQuery modelQuery = repositoryService.createModelQuery();
        if (CharSequenceUtil.isNotBlank(pageVO.getKey())) {
            modelQuery.modelKey(pageVO.getKey());
        }
        if (CharSequenceUtil.isNotBlank(pageVO.getName())) {
            modelQuery.modelNameLike("%" + pageVO.getName() + "%");
        }
        if (CharSequenceUtil.isNotBlank(pageVO.getCategory())) {
            modelQuery.modelCategory(pageVO.getCategory());
        }
        List<Model> models = modelQuery.orderByCreateTime().desc()
                .listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());

        Set<Long> formIds = CollectionUtils.convertSet(models, model -> {
            BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(model.getMetaInfo(), BpmModelMetaInfoRespDTO.class);
            return metaInfo != null ? metaInfo.getFormId() : null;
        });
        Map<Long, BpmFormDO> formMap = bpmFormService.getFormMap(formIds);

        Set<String> deploymentIds = new HashSet<>();
        models.forEach(model -> CollectionUtils.addIfNotNull(deploymentIds, model.getDeploymentId()));
        Map<String, Deployment> deploymentMap = processDefinitionService.getDeploymentMap(deploymentIds);
        List<ProcessDefinition> processDefinitions = processDefinitionService.getProcessDefinitionListByDeploymentIds(deploymentIds);
        Map<String, ProcessDefinition> processDefinitionMap = convertMap(processDefinitions, ProcessDefinition::getDeploymentId);
        List<String> collect1 = processDefinitions.stream().map(ProcessDefinition::getId).collect(Collectors.toList());
        List<BpmProcessDefinitionExtDO> bpmProcessDefinitionExtDOS = mapper.selectList(new LambdaQueryWrapperX<BpmProcessDefinitionExtDO>()
                .inIfPresent(BpmProcessDefinitionExtDO::getProcessDefinitionId, collect1));
        Map<String, Integer> collect = bpmProcessDefinitionExtDOS
                .stream()
                .collect(Collectors.toMap(BpmProcessDefinitionExtDO::getProcessDefinitionId, BpmProcessDefinitionExtDO::getStatus));
        long modelCount = modelQuery.count();
        PageResult<BpmModelPageItemRespVO> page = new PageResult<>(BpmModelConvert.INSTANCE.convertList(models, formMap, deploymentMap, processDefinitionMap), modelCount);

        page.getList().forEach(s -> {
            BpmModelExtDO bpmModelExtDO = bpmModelExtMapper.selectByModelId(s.getId());
            List<BpmModelDeptDO> bpmModelDeptDOS = bpmModelDeptMapper.selectListByModelId(s.getId());
            s.setDeptIds(CollectionUtils.convertList(bpmModelDeptDOS, BpmModelDeptDO::getDeptId));
            s.setType(bpmModelExtDO.getType());
            if (s.getProcessDefinition() == null) {
                return;
            }
            s.setStatus(collect.get(s.getProcessDefinition().getId()));
        });
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createModel(@Valid BpmModelCreateReqVO createReqVO, String bpmnXml) {
        checkKeyNCName(createReqVO.getKey());
        if (repositoryService.createModelQuery().modelName(createReqVO.getName()).singleResult() != null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_NAME_EXISTS, createReqVO.getName());
        }
        Model keyModel = getModelByKey(createReqVO.getKey());
        if (keyModel != null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_KEY_EXISTS, createReqVO.getKey());
        }

        Model model = repositoryService.newModel();
        BpmModelConvert.INSTANCE.copy(model, createReqVO);
        repositoryService.saveModel(model);
        saveModelBpmnXml(model, bpmnXml);

        List<Long> deptIds = createReqVO.getDeptIds();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(deptIds)) {
            bpmModelDeptService.saveOrUpdate(model.getId(), deptIds);
        }
        bpmModelExtMapper.insert(new BpmModelExtDO()
                .setModelId(model.getId())
                .setName(model.getName())
                .setModelKey(model.getKey())
                .setRev(String.valueOf(model.getVersion()))
                .setDeploymentId(model.getDeploymentId())
                .setType(createReqVO.getType())
        );
        return model.getId();
    }

    private Model getModelByKey(String key) {
        return repositoryService.createModelQuery().modelKey(key).singleResult();
    }

    @Override
    public BpmModelRespVO getModel(String id) {
        Model model = repositoryService.getModel(id);
        if (model == null) {
            return null;
        }
        BpmModelRespVO modelRespVO = BpmModelConvert.INSTANCE.convert(model);
        BpmModelExtDO bpmModelExtDO = bpmModelExtMapper.selectByModelId(id);
        byte[] bpmnBytes = repositoryService.getModelEditorSource(id);
        modelRespVO.setBpmnXml(StrUtil.utf8Str(bpmnBytes));
        modelRespVO.setType(bpmModelExtDO.getType());
        return modelRespVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModel(@Valid BpmModelUpdateReqVO updateReqVO) {
        Model model = repositoryService.getModel(updateReqVO.getId());
        if (model == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_NOT_EXISTS);
        }
        BpmModelConvert.INSTANCE.copy(model, updateReqVO);
        repositoryService.saveModel(model);
        saveModelBpmnXml(model, updateReqVO.getBpmnXml());
        BpmModelExtDO bpmModelExtDO = new BpmModelExtDO();
        bpmModelExtDO.setModelId(model.getId())
                .setName(model.getName())
                .setModelKey(model.getKey())
                .setRev(String.valueOf(model.getVersion()))
                .setDeploymentId(model.getDeploymentId())
                .setType(updateReqVO.getType());
        BpmModelExtDO bpmModelExtDO1 = bpmModelExtMapper.selectByModelId(updateReqVO.getId());
        if (ObjectUtils.isNotEmpty(bpmModelExtDO1)) {
            bpmModelExtMapper.updateByModelId(bpmModelExtDO);
        } else {
            bpmModelExtMapper.insert(bpmModelExtDO);
        }
        List<Long> deptIds = updateReqVO.getDeptIds();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(deptIds)) {
            bpmModelDeptService.saveOrUpdate(updateReqVO.getId(), deptIds);
        }
        List<BpmProcessDefinitionExtDO> bpmProcessDefinitionExtDOS = mapper.selectList(new LambdaQueryWrapperX<BpmProcessDefinitionExtDO>().eq(BpmProcessDefinitionExtDO::getModelId, updateReqVO.getId()).orderByDesc(BpmProcessDefinitionExtDO::getId));
        if (CollUtil.isNotEmpty(bpmProcessDefinitionExtDOS)) {
            BpmProcessDefinitionExtDO bpmProcessDefinitionExtDO = bpmProcessDefinitionExtDOS.get(0);
            bpmProcessDefinitionExtDO.setStatus(bpmProcessDefinitionExtDO.getStatus() + 1);
            bpmProcessDefinitionExtDO.setType(updateReqVO.getType());
            mapper.updateById(bpmProcessDefinitionExtDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deployModel(String id) {
        Model model = repositoryService.getModel(id);
        if (ObjectUtils.isEmpty(model)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_NOT_EXISTS);
        }
        byte[] bpmnBytes = repositoryService.getModelEditorSource(model.getId());
        if (bpmnBytes == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_NOT_EXISTS);
        }
        BpmFormDO form = checkFormConfig(model.getMetaInfo());
        taskAssignRuleService.checkTaskAssignRuleAllConfig(id);
        BpmModelExtDO bpmModelExtDO = new BpmModelExtDO();
        bpmModelExtDO.setModelId(model.getId())
                .setName(model.getName())
                .setModelKey(model.getKey())
                .setRev(String.valueOf(model.getVersion()))
                .setDeploymentId(model.getDeploymentId());

        BpmProcessDefinitionCreateReqDTO definitionCreateReqDTO = BpmModelConvert.INSTANCE.convert2(model, form).setBpmnBytes(bpmnBytes);
        BpmModelExtDO bpmModelExtDO1 = bpmModelExtMapper.selectByModelId(id);
        if (ObjectUtils.isNotEmpty(bpmModelExtDO1)) {
            definitionCreateReqDTO.setType(bpmModelExtDO1.getType());
            bpmModelExtDO.setType(bpmModelExtDO1.getType());
            bpmModelExtMapper.updateByModelId(bpmModelExtDO);
        } else {
            bpmModelExtMapper.insert(bpmModelExtDO);
        }
        if (processDefinitionService.isProcessDefinitionEquals(definitionCreateReqDTO)) {
            ProcessDefinition oldProcessDefinition = processDefinitionService.getProcessDefinitionByDeploymentId(model.getDeploymentId());
            Integer status = mapper.selectOne(new LambdaQueryWrapperX<BpmProcessDefinitionExtDO>().eqIfPresent(BpmProcessDefinitionExtDO::getProcessDefinitionId, oldProcessDefinition.getId())).getStatus();
            if (status == null || status == 0) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_DEPLOY_FAIL_TASK_INFO_EQUALS);
            }
//            if (oldProcessDefinition != null && taskAssignRuleService.isTaskAssignRulesEquals(model.getId(), oldProcessDefinition.getId())) {
//                throw exception(MODEL_DEPLOY_FAIL_TASK_INFO_EQUALS);
//            }
        }

        String definitionId = processDefinitionService.createProcessDefinition(definitionCreateReqDTO);

        updateProcessDefinitionSuspended(model.getDeploymentId());

        ProcessDefinition definition = processDefinitionService.getProcessDefinition(definitionId);
        model.setDeploymentId(definition.getDeploymentId());
        repositoryService.saveModel(model);

        taskAssignRuleService.copyTaskAssignRules(id, definition.getId());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteModel(String id) {
        Model model = repositoryService.getModel(id);
        if (model == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_NOT_EXISTS);
        }
        repositoryService.deleteModel(id);
        bpmModelExtMapper.deleteByModelId(id);
        updateProcessDefinitionSuspended(model.getDeploymentId());
    }

    @Override
    public void updateModelState(String id, Integer state) {
        Model model = repositoryService.getModel(id);
        if (model == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_NOT_EXISTS);
        }
        ProcessDefinition definition = processDefinitionService.getProcessDefinitionByDeploymentId(model.getDeploymentId());
        if (definition == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PROCESS_DEFINITION_NOT_EXISTS);
        }

        processDefinitionService.updateProcessDefinitionState(definition.getId(), state);
    }

    @Override
    public BpmnModel getBpmnModel(String id) {
        byte[] bpmnBytes = repositoryService.getModelEditorSource(id);
        if (ArrayUtil.isEmpty(bpmnBytes)) {
            return null;
        }
        BpmnXMLConverter converter = new BpmnXMLConverter();
        return converter.convertToBpmnModel(new BytesStreamSource(bpmnBytes), true, true);
    }

    private void checkKeyNCName(String key) {
        if (!ValidationUtils.isXmlNCName(key)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_KEY_VALID);
        }
    }

    /**
     * 校验流程表单已配置
     */
    private BpmFormDO checkFormConfig(String metaInfoStr) {
        BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(metaInfoStr, BpmModelMetaInfoRespDTO.class);
        if (metaInfo == null || metaInfo.getFormType() == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_DEPLOY_FAIL_FORM_NOT_CONFIG);
        }
        if (Objects.equals(metaInfo.getFormType(), BpmModelFormTypeEnum.NORMAL.getType())) {
            BpmFormDO form = bpmFormService.getForm(metaInfo.getFormId());
            if (form == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.FORM_NOT_EXISTS);
            }
            return form;
        }
        return null;
    }

    private void saveModelBpmnXml(Model model, String bpmnXml) {
        if (StrUtil.isEmpty(bpmnXml)) {
            return;
        }
        repositoryService.addModelEditorSource(model.getId(), StrUtil.utf8Bytes(bpmnXml));
    }

    /**
     *
     */
    private void updateProcessDefinitionSuspended(String deploymentId) {
        if (StrUtil.isEmpty(deploymentId)) {
            return;
        }
        ProcessDefinition oldDefinition = processDefinitionService.getProcessDefinitionByDeploymentId(deploymentId);
        if (oldDefinition == null) {
            return;
        }
        if (oldDefinition.isSuspended()) {
            return;
        }
        processDefinitionService.updateProcessDefinitionState(oldDefinition.getId(), SuspensionState.SUSPENDED.getStateCode());
    }


}
