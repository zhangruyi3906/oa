package com.lh.oa.module.bpm.service.fixAssetPurchase;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceApi;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo.FixAssetPurchaseCreateVo;
import com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo.FixAssetPurchasePageReqVo;
import com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo.FixAssetPurchaseRespVo;
import com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo.PurchaseDetailVo;
import com.lh.oa.module.bpm.dal.dataobject.fixAssetPurchase.FixAssetPurchaseDO;
import com.lh.oa.module.bpm.dal.dataobject.fixAssetPurchase.PurchaseDetailDO;
import com.lh.oa.module.bpm.dal.mysql.fixAssetPurchase.FixAssetPurchaseMapper;
import com.lh.oa.module.bpm.dal.mysql.fixAssetPurchase.PurchaseDetailMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.SUPPLY_NOT_EXISTS_ERROR;

@Service
@Validated
@Slf4j
public class FixAssetPurchaseServiceImpl implements FixAssetPurchaseService {
    public static final String PROCESS_KEY = "fix-asset-purchase";
    @Resource
    private FixAssetPurchaseMapper mapper;
    @Resource
    private PurchaseDetailMapper purchaseDetailMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFixAssetPurchase(FixAssetPurchaseCreateVo createReqVO) {
        log.info("创建固定资产申购, createReqVo:{}", JsonUtils.toJsonString(createReqVO));

        FixAssetPurchaseDO fixAssetPurchaseDO = JsonUtils.parseObject(JsonUtils.toJsonString(createReqVO), FixAssetPurchaseDO.class);
        fixAssetPurchaseDO.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        mapper.insert(fixAssetPurchaseDO);

        if (!CollUtil.isEmpty(createReqVO.getPurchaseDetailVoList())){
            List<PurchaseDetailDO> purchaseDetailList = JsonUtils.parseArray(JsonUtils.toJsonString(createReqVO.getPurchaseDetailVoList()), PurchaseDetailDO.class);
            purchaseDetailList.forEach(s ->s.setFixAssetPurchaseId(fixAssetPurchaseDO.getId()));
            purchaseDetailMapper.insertBatch(purchaseDetailList);
        }

        String processInstanceId = processInstanceApi.createProcessInstance(createReqVO.getUserId(),
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY).setVariables(JsonUtils.covertObject2Map(createReqVO)).setBusinessKey(String.valueOf(fixAssetPurchaseDO.getId())));

        mapper.updateById(new FixAssetPurchaseDO().setId(fixAssetPurchaseDO.getId()).setProcessInstanceId(processInstanceId));
        return fixAssetPurchaseDO.getId();
    }

    @Override
    public void updateResult(Long id, Integer result) {
        log.info("更新固定资产申购流程执行结果，id:{}, result:{}", id, result);
        FixAssetPurchaseDO fixAssetPurchaseDO = mapper.selectById(id);
        ExceptionThrowUtils.throwIfNull(fixAssetPurchaseDO, SUPPLY_NOT_EXISTS_ERROR);
        mapper.updateById(new FixAssetPurchaseDO().setId(id).setResult(result));
    }

    @Override
    public FixAssetPurchaseRespVo findFixAssetPurchaseById(Long id) {
        log.info("根据id查询固定资产申购, id:{}", id);

        FixAssetPurchaseDO fixAssetPurchaseDO = mapper.selectById(id);
        if (Objects.isNull(fixAssetPurchaseDO)) {
            throw exception(SUPPLY_NOT_EXISTS_ERROR);
        }
        FixAssetPurchaseRespVo respVo = JsonUtils.parseObject(JsonUtils.toJsonString(fixAssetPurchaseDO), FixAssetPurchaseRespVo.class);
        List<PurchaseDetailDO> purchaseDetailList = purchaseDetailMapper.selectList(
                new LambdaQueryWrapperX<PurchaseDetailDO>().eq(PurchaseDetailDO::getFixAssetPurchaseId, id));
        if(!CollUtil.isEmpty(purchaseDetailList)) {
            List<PurchaseDetailVo> purchaseDetailVoS = JsonUtils.parseArray(JsonUtils.toJsonString(purchaseDetailList), PurchaseDetailVo.class);
            respVo.setPurchaseDetailVoList(purchaseDetailVoS);
        }
        return respVo;
    }

    @Override
    public PageResult<FixAssetPurchaseRespVo> pageQueryFixAssetPurchase(FixAssetPurchasePageReqVo pageVO) {
        log.info("分页查询固定资产申购, pageVo:{}", JsonUtils.toJsonString(pageVO));
        PageResult<FixAssetPurchaseDO> purchaseDOPageResult = mapper.selectPage(pageVO);
        List<FixAssetPurchaseRespVo> respVoList = JsonUtils.parseArray(JsonUtils.toJsonString(purchaseDOPageResult.getList()), FixAssetPurchaseRespVo.class);
        respVoList.forEach(res -> {
            List<PurchaseDetailDO> purchaseDetailDOS = purchaseDetailMapper.selectList(new LambdaQueryWrapperX<PurchaseDetailDO>().eqIfPresent(PurchaseDetailDO::getFixAssetPurchaseId, res.getId()));
            List<PurchaseDetailVo> purchaseDetailVos = JsonUtils.parseArray(JsonUtils.toJsonString(purchaseDetailDOS), PurchaseDetailVo.class);
            res.setPurchaseDetailVoList(purchaseDetailVos);
        });
        return new PageResult<FixAssetPurchaseRespVo>()
                .setList(respVoList)
                .setTotal(purchaseDOPageResult.getTotal());
    }
}
