package com.lh.oa.module.bpm.service.giftGivingSupply;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceApi;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftDetailVo;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftGivingSupplyCreateVo;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftGivingSupplyPageReqVo;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftGivingSupplyRespVo;
import com.lh.oa.module.bpm.dal.dataobject.giftGivingSupply.GiftDetailDO;
import com.lh.oa.module.bpm.dal.dataobject.giftGivingSupply.GiftGivingSupplyDO;
import com.lh.oa.module.bpm.dal.mysql.giftGivingSupply.GiftDetailMapper;
import com.lh.oa.module.bpm.dal.mysql.giftGivingSupply.GiftGivingSupplyMapper;
import com.lh.oa.module.bpm.wrapper.SystemDictDataWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.SUPPLY_NOT_EXISTS_ERROR;

@Service
@Validated
@Slf4j
public class GiftGivingSupplyServiceImpl implements GiftGivingSupplyService {

    public static final String PROCESS_KEY = "gift-giving-supply";

    @Resource
    private GiftGivingSupplyMapper mapper;
    @Resource
    private GiftDetailMapper giftDetailMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private SystemDictDataWrapper systemDictDataWrapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createGiftGivingSupply(GiftGivingSupplyCreateVo createReqVo) {
        log.info("创建礼物赠送申请, createReqVo:{}", JsonUtils.toJsonString(createReqVo));

        GiftGivingSupplyDO giftGivingSupply = JsonUtils.covertObject(createReqVo, GiftGivingSupplyDO.class);
        mapper.insert(giftGivingSupply);

        Map<String, Object> variables = JsonUtils.covertObject2Map(createReqVo);
        variables.put("party", createReqVo.getUserId());

        if (!CollUtil.isEmpty(createReqVo.getGiftDetailVoList())) {
            List<GiftDetailDO> giftDetailList = JsonUtils.covertList(createReqVo.getGiftDetailVoList(), GiftDetailDO.class);
            giftDetailList.forEach(s ->s.setGiftGivingSupplyId(giftGivingSupply.getId()));
            giftDetailMapper.insertBatch(giftDetailList);

            BigDecimal totalPrice = giftDetailList.stream().map(GiftDetailDO::getTotalPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            variables.put("totalPrice", totalPrice);
        }

        String processInstanceId = processInstanceApi.createProcessInstance(createReqVo.getUserId(),
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY).setVariables(variables).setBusinessKey(String.valueOf(giftGivingSupply.getId())));

        mapper.updateById(new GiftGivingSupplyDO().setId(giftGivingSupply.getId()).setProcessInstanceId(processInstanceId));
        return giftGivingSupply.getId();
    }

    @Override
    public void updateGiftGivingSupply(Long id, Integer result) {
        log.info("更新礼物赠送申请, id:{}, result:{}", id, result);

        validateDataExists(id);
        mapper.updateById(new GiftGivingSupplyDO().setId(id).setResult(result));
    }

    @Override
    public GiftGivingSupplyRespVo findGiftGivingSupplyById(Long id) {
        log.info("根据id查询礼物赠送申请, id:{}", id);

        GiftGivingSupplyDO giftGivingSupply = mapper.selectById(id);
        if (Objects.isNull(giftGivingSupply)) {
            throw exception(SUPPLY_NOT_EXISTS_ERROR);
        }

        GiftGivingSupplyRespVo respVo = JsonUtils.covertObject(giftGivingSupply, GiftGivingSupplyRespVo.class);

        List<GiftDetailDO> giftDetailList = giftDetailMapper.selectList(new LambdaQueryWrapperX<GiftDetailDO>().eq(GiftDetailDO::getGiftGivingSupplyId, id));

        if(!CollUtil.isEmpty(giftDetailList)) {
            List<GiftDetailVo> giftDetailVoList = JsonUtils.covertList(giftDetailList, GiftDetailVo.class);
            respVo.setGiftDetailVoList(giftDetailVoList);
        }
        return respVo;
    }

    @Override
    public PageResult<GiftGivingSupplyRespVo> pageQueryGiftGivingSupply(GiftGivingSupplyPageReqVo pageVo) {
        log.info("分页查询礼物赠送申请, pageVo:{}", JsonUtils.toJsonString(pageVo));

        PageResult<GiftGivingSupplyDO> supplyDOPageResult = mapper.selectPage(pageVo);

        List<GiftGivingSupplyRespVo> respVoList = JsonUtils.covertList(supplyDOPageResult.getList(), GiftGivingSupplyRespVo.class);

        Map<String, Map<String, String>> businessPlaceTypeMap = systemDictDataWrapper.getDictTypeMap("business_place");
        Map<String, String> businessPlaces = businessPlaceTypeMap.get("business_place");
        respVoList.forEach(respVo -> {
            String placeName = businessPlaces.get(respVo.getPlace());
            respVo.setPlaceName(placeName);
        });

        return new PageResult<GiftGivingSupplyRespVo>()
                .setList(respVoList)
                .setTotal(supplyDOPageResult.getTotal());
    }

    private void validateDataExists(Long id){
        if(mapper.selectById(id) == null){
            throw exception(SUPPLY_NOT_EXISTS_ERROR);
        }
    }
}
