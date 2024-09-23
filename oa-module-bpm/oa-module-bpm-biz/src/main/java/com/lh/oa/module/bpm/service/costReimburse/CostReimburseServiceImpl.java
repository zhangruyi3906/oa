package com.lh.oa.module.bpm.service.costReimburse;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceApi;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.controller.admin.costReimburse.vo.CostDetailVo;
import com.lh.oa.module.bpm.controller.admin.costReimburse.vo.CostReimburseCreateVo;
import com.lh.oa.module.bpm.controller.admin.costReimburse.vo.CostReimbursePageReqVo;
import com.lh.oa.module.bpm.controller.admin.costReimburse.vo.CostReimburseRespVo;
import com.lh.oa.module.bpm.dal.dataobject.costReimburse.CostDetailDO;
import com.lh.oa.module.bpm.dal.dataobject.costReimburse.CostReimburseDO;
import com.lh.oa.module.bpm.dal.mysql.costReimburse.CostDetailMapper;
import com.lh.oa.module.bpm.dal.mysql.costReimburse.CostReimburseMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.SUPPLY_NOT_EXISTS_ERROR;

@Service
@Validated
@Slf4j
public class CostReimburseServiceImpl implements CostReimburseService {

    public static final String PROCESS_KEY = "cost-reimburse";

    @Resource
    private CostReimburseMapper mapper;
    @Resource
    private CostDetailMapper costDetailMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCostReimburse(CostReimburseCreateVo createReqVO) {
        log.info("创建费用报销申请, createReqVo:{}", JsonUtils.toJsonString(createReqVO));
        List<CostDetailVo> costDetailVoList = createReqVO.getCostDetailVoList();
        BigDecimal totalPrice = new BigDecimal(0.00);
        for (CostDetailVo costDetailVo : costDetailVoList) {
            BigDecimal reimburse = costDetailVo.getReimburse();
            if (reimburse != null)
                totalPrice.add(reimburse);
        }
        createReqVO.setTotalPrice(totalPrice);
        CostReimburseDO costReimburseDO = JsonUtils.parseObject(JsonUtils.toJsonString(createReqVO), CostReimburseDO.class);
        costReimburseDO.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        mapper.insert(costReimburseDO);
        Map<String, Object> variables = JsonUtils.covertObject2Map(createReqVO);
        variables.put("party", createReqVO.getUserId());
        variables.put("deptName", createReqVO.getDeptName());
        if (!CollUtil.isEmpty(createReqVO.getCostDetailVoList())) {
            List<CostDetailDO> costDetailList = JsonUtils.parseArray(JsonUtils.toJsonString(createReqVO.getCostDetailVoList()), CostDetailDO.class);
            costDetailList.forEach(s -> s.setCostReimburseId(costReimburseDO.getId()));
            costDetailMapper.insertBatch(costDetailList);
            variables.put("totalPrice", createReqVO.getTotalPrice());
        }
        String processInstanceId = processInstanceApi.createProcessInstance(createReqVO.getUserId(),
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY).setVariables(variables).setBusinessKey(String.valueOf(costReimburseDO.getId())));
        mapper.updateById(new CostReimburseDO().setId(costReimburseDO.getId()).setProcessInstanceId(processInstanceId));
        return costReimburseDO.getId();
    }

    @Override
    public void updateResult(Long id, Integer result) {
        log.info("更新费用报销申请流程执行结果，id:{}, result:{}", id, result);
        CostReimburseDO costReimburseDO = mapper.selectById(id);
        ExceptionThrowUtils.throwIfNull(costReimburseDO, SUPPLY_NOT_EXISTS_ERROR);
        mapper.updateById(new CostReimburseDO().setId(id).setResult(result));
    }

    @Override
    public CostReimburseRespVo findCostReimburseById(Long id) {
        log.info("根据id查询费用报销申请, id:{}", id);

        CostReimburseDO costReimburse = mapper.selectById(id);
        if (Objects.isNull(costReimburse)) {
            throw exception(SUPPLY_NOT_EXISTS_ERROR);
        }
        CostReimburseRespVo respVo = JsonUtils.parseObject(JsonUtils.toJsonString(costReimburse), CostReimburseRespVo.class);
        List<CostDetailDO> costDetailList = costDetailMapper.selectList(new LambdaQueryWrapperX<CostDetailDO>().eq(CostDetailDO::getCostReimburseId, id));
        if (!CollUtil.isEmpty(costDetailList)) {
            List<CostDetailVo> costDetailVoList = JsonUtils.parseArray(JsonUtils.toJsonString(costDetailList), CostDetailVo.class);
            respVo.setCostDetailVoList(costDetailVoList);
        }
        return respVo;
    }

    @Override
    public PageResult<CostReimburseRespVo> pageQueryCostReimburse(CostReimbursePageReqVo pageVO) {
        log.info("分页查询费用报销申请, pageVo:{}", JsonUtils.toJsonString(pageVO));
        PageResult<CostReimburseDO> costReimburseDOPageResult = mapper.selectPage(pageVO);
        List<CostReimburseRespVo> respVoList = JsonUtils.parseArray(JsonUtils.toJsonString(costReimburseDOPageResult.getList()), CostReimburseRespVo.class);
        return new PageResult<CostReimburseRespVo>()
                .setList(respVoList)
                .setTotal(costReimburseDOPageResult.getTotal());
    }
}
