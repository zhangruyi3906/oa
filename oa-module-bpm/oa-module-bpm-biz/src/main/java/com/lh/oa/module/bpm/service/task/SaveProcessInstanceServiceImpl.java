package com.lh.oa.module.bpm.service.task;

import cn.hutool.core.bean.BeanUtil;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.controller.admin.task.vo.save.BpmSaveProcessInstancePageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.save.BpmSaveProcessInstanceResVO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmSaveInstanceDO;
import com.lh.oa.module.bpm.dal.mysql.task.BpmSaveInstanceMapper;
import com.lh.oa.module.bpm.enums.ErrorCodeConstants;
import com.lh.oa.module.bpm.service.definition.BpmProcessDefinitionService;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Service
@Validated
@Slf4j
public class SaveProcessInstanceServiceImpl implements SaveProcessInstanceService {
    @Resource
    private BpmSaveInstanceMapper bpmSaveInstanceMapper;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private BpmProcessDefinitionService bpmProcessDefinitionService;
    @Resource
    private AdminUserApi adminUserApi;

    @Override
    public PageResult<BpmSaveProcessInstanceResVO> getMySaveProcessInstancePage(Long loginUserId, BpmSaveProcessInstancePageReqVO pageReqVO) {
        CommonResult<List<AdminUserRespDTO>> allUsers = adminUserApi.getAllUsers();
        if (CollectionUtils.isEmpty(allUsers.getData())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_GROUP_NOT_EXISTS);
        }
        Map<Long, String> userIdToUsernameMap = allUsers.getData().stream().collect(Collectors.toMap(AdminUserRespDTO::getId, AdminUserRespDTO::getNickname));
        PageResult<BpmSaveInstanceDO> bpmSaveInstanceDOPageResult = bpmSaveInstanceMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<BpmSaveInstanceDO>()
                .likeIfPresent(BpmSaveInstanceDO::getName, pageReqVO.getName())
                .eqIfPresent(BpmSaveInstanceDO::getType, pageReqVO.getType())
                .eqIfPresent(BpmSaveInstanceDO::getUserId, loginUserId)
                .orderByDesc(BpmSaveInstanceDO::getSaveId));
        List<BpmSaveInstanceDO> list = bpmSaveInstanceDOPageResult.getList();
        List<BpmSaveProcessInstanceResVO> bpmSaveProcessInstanceResVOList = list.stream()
                .map(item -> {
                    BpmSaveProcessInstanceResVO bpmSaveProcessInstanceResVO = new BpmSaveProcessInstanceResVO();
                    BeanUtil.copyProperties(item, bpmSaveProcessInstanceResVO);
                    Model model = repositoryService.getModel(item.getModelId());
                    //防止保存后流程模型被删
                    if (ObjectUtils.isNotEmpty(model)) {
                        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(model.getKey()).active().singleResult();
                        if (ObjectUtils.isNotEmpty(processDefinition)) {
                            BpmProcessDefinitionExtDO processDefinitionExt = bpmProcessDefinitionService.getProcessDefinitionExt(processDefinition.getId());
                            if (ObjectUtils.isNotEmpty(processDefinitionExt)) {
                                bpmSaveProcessInstanceResVO.setProcessDefinitionId(processDefinitionExt.getProcessDefinitionId());
                            }
                        }
                    }
                    Long userId = bpmSaveProcessInstanceResVO.getUserId();
                    String username = userIdToUsernameMap.get(userId);
                    if (StringUtils.isNotBlank(username))
                        bpmSaveProcessInstanceResVO.setUsername(username);
                    return bpmSaveProcessInstanceResVO;
                })
                .collect(Collectors.toList());
        return new PageResult<BpmSaveProcessInstanceResVO>().setList(bpmSaveProcessInstanceResVOList).setTotal(bpmSaveInstanceDOPageResult.getTotal());
    }

    @Override
    public BpmSaveInstanceDO getSaveProcessInstance(Long saveId) {
        BpmSaveInstanceDO bpmSaveInstanceDO = bpmSaveInstanceMapper.selectBySaveId(saveId);
        Model model = repositoryService.getModel(bpmSaveInstanceDO.getModelId());
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(model.getKey()).active().singleResult();
        if (ObjectUtils.isNotEmpty(processDefinition)) {
            BpmProcessDefinitionExtDO processDefinitionExt = bpmProcessDefinitionService.getProcessDefinitionExt(processDefinition.getId());
            if (ObjectUtils.isNotEmpty(processDefinitionExt)) {
                bpmSaveInstanceDO.setFormConf(processDefinitionExt.getFormConf());
            }
        }
        return bpmSaveInstanceDO;
    }

    @Override
    public void deleteSaveProcessInstance(Long saveId) {
        log.info("删除草稿流程 deleteSaveProcessInstance saveId={}", saveId);
        BpmSaveInstanceDO bpmSaveInstanceDO = bpmSaveInstanceMapper.selectBySaveId(saveId);
        if (ObjectUtils.isEmpty(bpmSaveInstanceDO)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SAVE_PROCESS_INSTANCE_NOT_EXIST);
        }
        Long loginUserId = getLoginUserId();
        log.info("登录人Id loginUserId={}，bpmSaveInstanceDO.getUserId()={}", loginUserId, bpmSaveInstanceDO.getUserId());
        if (!loginUserId.equals(bpmSaveInstanceDO.getUserId())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SAVE_PROCESS_INSTANCE_DELETE_NOT_AUTHORIZED);
        }
        bpmSaveInstanceMapper.deleteBySaveId(String.valueOf(saveId));
    }
}
