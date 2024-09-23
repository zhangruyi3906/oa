package com.lh.oa.module.bpm.service.attendancecorrection;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceApi;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionPageReqVO;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionUpdateReqVO;
import com.lh.oa.module.bpm.convert.attendancecorrection.CorrectionConvert;
import com.lh.oa.module.bpm.dal.dataobject.attendancecorrection.CorrectionDO;
import com.lh.oa.module.bpm.dal.mysql.attendancecorrection.CorrectionMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.CORRECTION_NOT_EXISTS;

/**
 * 补卡流程 Service 实现类
 *
 * @author 狗蛋
 */
@Service
@Validated
public class CorrectionServiceImpl implements CorrectionService {

    /**
     * OA 补卡对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "oa_correction";

    @Resource
    private CorrectionMapper correctionMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCorrection(Long userId, CorrectionCreateReqVO createReqVO) {
        CorrectionDO correctionDO = CorrectionConvert.INSTANCE.convert(createReqVO).setUserId(userId).setApprovalStatus(BpmProcessInstanceResultEnum.PROCESS.getResult());
        // 发起 BPM 流程
        // 插入
        correctionMapper.insert(correctionDO);
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("correction", createReqVO.getMonth());
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(correctionDO.getId())));
        // 将工作流的编号，更新到 OA 请假单中
        correctionMapper.updateById(new CorrectionDO().setId(correctionDO.getId()).setProcessInstanceId(processInstanceId));
        // 返回
        return correctionDO.getId();
    }

    @Override
    public void updateCorrection(CorrectionUpdateReqVO updateReqVO) {
        // 校验存在
        validateCorrectionExists(updateReqVO.getId());
        // 更新
        CorrectionDO updateObj = CorrectionConvert.INSTANCE.convert(updateReqVO);
        correctionMapper.updateById(updateObj);
    }

    @Override
    public void deleteCorrection(Long id) {
        // 校验存在
        validateCorrectionExists(id);
        // 删除
        correctionMapper.deleteById(id);
    }

    private void validateCorrectionExists(Long id) {
        if (correctionMapper.selectById(id) == null) {
            throw exception(CORRECTION_NOT_EXISTS);
        }
    }

    @Override
    public CorrectionDO getCorrection(Long id) {
        return correctionMapper.selectById(id);
    }

    @Override
    public List<CorrectionDO> getCorrectionList(Collection<Long> ids) {
        return correctionMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CorrectionDO> getCorrectionPage(CorrectionPageReqVO pageReqVO) {
        return correctionMapper.selectPage(pageReqVO);
    }

//    @Override
//    public List<CorrectionDO> getCorrectionList(CorrectionExportReqVO exportReqVO) {
//        return correctionMapper.selectList(exportReqVO);
//    }

}
