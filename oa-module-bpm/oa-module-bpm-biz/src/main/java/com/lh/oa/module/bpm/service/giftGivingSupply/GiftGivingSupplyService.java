package com.lh.oa.module.bpm.service.giftGivingSupply;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftGivingSupplyBaseVo;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftGivingSupplyCreateVo;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftGivingSupplyPageReqVo;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftGivingSupplyRespVo;
import com.lh.oa.module.bpm.dal.dataobject.giftGivingSupply.GiftGivingSupplyDO;


public interface GiftGivingSupplyService {

    Long createGiftGivingSupply(GiftGivingSupplyCreateVo createReqVO);

    void updateGiftGivingSupply(Long id, Integer result);

    GiftGivingSupplyRespVo findGiftGivingSupplyById(Long id);

    PageResult<GiftGivingSupplyRespVo> pageQueryGiftGivingSupply(GiftGivingSupplyPageReqVo pageVO);

}
