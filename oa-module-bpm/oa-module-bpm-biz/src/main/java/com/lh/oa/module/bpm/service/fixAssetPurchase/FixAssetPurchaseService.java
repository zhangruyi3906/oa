package com.lh.oa.module.bpm.service.fixAssetPurchase;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo.FixAssetPurchaseCreateVo;
import com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo.FixAssetPurchasePageReqVo;
import com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo.FixAssetPurchaseRespVo;

public interface FixAssetPurchaseService {

    Long createFixAssetPurchase(FixAssetPurchaseCreateVo createReqVo);

    void updateResult(Long id, Integer result);

    FixAssetPurchaseRespVo findFixAssetPurchaseById(Long id);

    PageResult<FixAssetPurchaseRespVo> pageQueryFixAssetPurchase(FixAssetPurchasePageReqVo pageVO);
}
