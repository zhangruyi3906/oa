package com.lh.oa.module.bpm.service.approvalprocess;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceApi;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.ApprovalProcessCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.ApprovalProcessPageReqVO;
import com.lh.oa.module.bpm.convert.approvalprocess.ApprovalProcessConvert;
import com.lh.oa.module.bpm.dal.dataobject.approvalprocess.ApprovalProcessDO;
import com.lh.oa.module.bpm.dal.mysql.approvalprocess.ApprovalProcessMapper;
import com.lh.oa.module.bpm.enums.ErrorCodeConstants;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 项目立项 Service 实现类
 *
 * @author 狗蛋
 */
@Service
@Validated
public class ApprovalProcessServiceImpl implements ApprovalProcessService {

    public static final String PROCESS_KEY = "approval_process";

    @Resource
    private ApprovalProcessMapper approvalProcessMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    public Long createApprovalProcess(Long userId, ApprovalProcessCreateReqVO createReqVO) {
        // 插入
        ApprovalProcessDO approvalProcess =  ApprovalProcessConvert.INSTANCE.convert(createReqVO).setApprovalStatus(BpmProcessInstanceResultEnum.PROCESS.getResult()).setUserId(userId);
        approvalProcessMapper.insert(approvalProcess);

        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("approval_process", createReqVO.getProjectName());
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(approvalProcess.getId())));

        approvalProcessMapper.updateById(new ApprovalProcessDO().setId(approvalProcess.getId()).setProcessInstanceId(processInstanceId));
        // 返回
        return approvalProcess.getId();

    }

    @Override
    public void updateApprovalProcess(Long id, Integer result) {
        // 校验存在
        validateApprovalProcessExists(id);
        // 更新
        approvalProcessMapper.updateById(new ApprovalProcessDO().setId(id).setApprovalStatus(result));

    }

    @Override
    public void deleteApprovalProcess(Long id) {
        // 校验存在
        validateApprovalProcessExists(id);
        // 删除
        approvalProcessMapper.deleteById(id);
    }

    private void validateApprovalProcessExists(Long id) {
        if (approvalProcessMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.APPROVAL_PROCESS_NOT_EXISTS);
        }
    }

    @Override
    public ApprovalProcessDO getApprovalProcess(Long id) {
        return approvalProcessMapper.selectById(id);
    }

    @Override
    public List<ApprovalProcessDO> getApprovalProcessList(Collection<Long> ids) {
        return approvalProcessMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ApprovalProcessDO> getApprovalProcessPage(ApprovalProcessPageReqVO pageReqVO) {
        return approvalProcessMapper.selectPage(pageReqVO);
    }

//    @Override
//    public List<ApprovalProcessDO> getApprovalProcessList(ApprovalProcessExportReqVO exportReqVO) {
//        return approvalProcessMapper.selectList(exportReqVO);
//    }

}
