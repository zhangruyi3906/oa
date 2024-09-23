package com.lh.oa.module.bpm.controller.admin.fixAssetPurchase;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo.FixAssetPurchaseCreateVo;
import com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo.FixAssetPurchasePageReqVo;
import com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo.FixAssetPurchaseRespVo;
import com.lh.oa.module.bpm.service.fixAssetPurchase.FixAssetPurchaseService;
import io.swagger.v3.oas.annotations.Parameter;
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
import javax.validation.Valid;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
@Tag(name =  "管理后台 - 固定资产申购")
@RestController
@RequestMapping("/bpm/fixAsset/purchase")
@Validated
public class FixAssetPurchaseRequestController {
    @Resource
    private FixAssetPurchaseService service;

    @PostMapping("/create")
    @PermitAll
    public CommonResult<Long> createFixAssetPurchase(@Valid @RequestBody FixAssetPurchaseCreateVo createReqVO) {
        return success(service.createFixAssetPurchase(createReqVO));
    }

    @GetMapping("/get")
    @PermitAll
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<FixAssetPurchaseRespVo> findFixAssetPurchaseById(@RequestParam("id") Long id) {
        return success(service.findFixAssetPurchaseById(id));
    }

    @GetMapping("/page")
    @PermitAll
    public CommonResult<PageResult<FixAssetPurchaseRespVo>> pageQueryFixAssetPurchase(@Valid FixAssetPurchasePageReqVo pageVO) {
        return success(service.pageQueryFixAssetPurchase(pageVO));
    }
}
