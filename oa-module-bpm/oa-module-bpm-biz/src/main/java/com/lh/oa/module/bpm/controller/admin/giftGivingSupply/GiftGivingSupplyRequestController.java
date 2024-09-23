package com.lh.oa.module.bpm.controller.admin.giftGivingSupply;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftGivingSupplyCreateVo;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftGivingSupplyPageReqVo;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftGivingSupplyRespVo;
import com.lh.oa.module.bpm.service.giftGivingSupply.GiftGivingSupplyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name =  "管理后台 - 礼品赠送申请")
@RestController
@RequestMapping("/bpm/giftGiving/supply")
@Validated
public class GiftGivingSupplyRequestController {

    @Resource
    private GiftGivingSupplyService service;

    @PostMapping("/create")
    @PermitAll
    public CommonResult<Long> createOfficeSu(@RequestBody GiftGivingSupplyCreateVo createReqVO) {
        return success(service.createGiftGivingSupply(createReqVO));
    }

    @GetMapping("/get")
    @PermitAll
    public CommonResult<GiftGivingSupplyRespVo> findGiftGivingSupplyById(@RequestParam("id") Long id) {
        return success(service.findGiftGivingSupplyById(id));
    }

    @PostMapping("/page")
    @PermitAll
    public CommonResult<PageResult<GiftGivingSupplyRespVo>> pageQueryGiftGivingSupply(@RequestBody GiftGivingSupplyPageReqVo pageVO) {
        return success(service.pageQueryGiftGivingSupply(pageVO));
    }

}