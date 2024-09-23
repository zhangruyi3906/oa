package com.lh.oa.module.bpm.convert.definition;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionPageItemRespVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionRespVO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmModelExtDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import com.lh.oa.module.bpm.service.definition.dto.BpmProcessDefinitionCreateReqDTO;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Bpm 流程定义的 Convert
 *
 * @author yunlong.li
 */
@Mapper
public interface BpmProcessDefinitionConvert {

    BpmProcessDefinitionConvert INSTANCE = Mappers.getMapper(BpmProcessDefinitionConvert.class);

    BpmProcessDefinitionPageItemRespVO convert(ProcessDefinition bean);

    BpmProcessDefinitionExtDO convert2(BpmProcessDefinitionCreateReqDTO bean);

    default List<BpmProcessDefinitionPageItemRespVO> convertList(List<ProcessDefinition> list, Map<String, Deployment> deploymentMap,
                                                                 Map<String, BpmProcessDefinitionExtDO> processDefinitionDOMap, Map<Long, BpmFormDO> formMap) {
        return CollectionUtils.convertList(list, definition -> {
            Deployment deployment = definition.getDeploymentId() != null ? deploymentMap.get(definition.getDeploymentId()) : null;
            BpmProcessDefinitionExtDO definitionDO = processDefinitionDOMap.get(definition.getId());
            BpmFormDO form = definitionDO != null ? formMap.get(definitionDO.getFormId()) : null;
            return convert(definition, deployment, definitionDO, form);
        });
    }

    default Map<String, List<BpmProcessDefinitionRespVO>> convertList3(List<ProcessDefinition> list,
                                                         Map<String, BpmProcessDefinitionExtDO> processDefinitionDOMap,
                                                         Map<String, BpmModelExtDO> modelExtDOMap) {
        Map<String, List<BpmProcessDefinitionRespVO>> processTypeGroup = new HashMap<>();
        list.forEach(processDefinition -> {
            BpmProcessDefinitionRespVO respVO = convert3(processDefinition);
            BpmProcessDefinitionExtDO processDefinitionExtDO = processDefinitionDOMap.get(processDefinition.getId());
            if (Objects.isNull(processDefinitionExtDO)) {
                return;
            }
            // 复制通用属性
            copyTo(processDefinitionExtDO, respVO);
            BpmModelExtDO modelExtDO = modelExtDOMap.get(processDefinitionExtDO.getModelId());
            if (Objects.isNull(modelExtDO)) {
                return;
            }

            if (processTypeGroup.containsKey(modelExtDO.getType())) {
                List<BpmProcessDefinitionRespVO> respVOList = processTypeGroup.get(modelExtDO.getType());
                respVOList.add(respVO);
            } else {
                List<BpmProcessDefinitionRespVO> respVOList = new ArrayList<>();
                respVOList.add(respVO);
                processTypeGroup.put(modelExtDO.getType(), respVOList);
            }
        });

        return processTypeGroup;
    }

    @Mapping(source = "suspended", target = "suspensionState", qualifiedByName = "convertSuspendedToSuspensionState")
    BpmProcessDefinitionRespVO convert3(ProcessDefinition bean);

    @Named("convertSuspendedToSuspensionState")
    default Integer convertSuspendedToSuspensionState(boolean suspended) {
        return suspended ? SuspensionState.SUSPENDED.getStateCode() :
                SuspensionState.ACTIVE.getStateCode();
    }

    default BpmProcessDefinitionPageItemRespVO convert(ProcessDefinition bean, Deployment deployment,
                                                       BpmProcessDefinitionExtDO processDefinitionExtDO, BpmFormDO form) {
        BpmProcessDefinitionPageItemRespVO respVO = convert(bean);
        respVO.setSuspensionState(bean.isSuspended() ? SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode());
        if (deployment != null) {
            respVO.setDeploymentTime(LocalDateTimeUtil.of(deployment.getDeploymentTime()));
        }
        if (form != null) {
            respVO.setFormName(form.getName());
        }
        // 复制通用属性
        copyTo(processDefinitionExtDO, respVO);
        return respVO;
    }

    @Mapping(source = "from.id", target = "to.id", ignore = true)
    void copyTo(BpmProcessDefinitionExtDO from, @MappingTarget BpmProcessDefinitionRespVO to);
}
