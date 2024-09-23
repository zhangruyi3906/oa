package com.lh.oa.module.bpm.service.costReimburse;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.costReimburse.vo.CostReimburseCreateVo;
import com.lh.oa.module.bpm.controller.admin.costReimburse.vo.CostReimbursePageReqVo;
import com.lh.oa.module.bpm.controller.admin.costReimburse.vo.CostReimburseRespVo;

public interface CostReimburseService {

    Long createCostReimburse(CostReimburseCreateVo createReqVO);

    void updateResult(Long id, Integer result);

    CostReimburseRespVo findCostReimburseById(Long id);

    PageResult<CostReimburseRespVo> pageQueryCostReimburse(CostReimbursePageReqVo pageVO);
}
