package com.lh.oa.module.bpm.controller.admin.costReimburse;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.costReimburse.vo.CostReimburseCreateVo;
import com.lh.oa.module.bpm.controller.admin.costReimburse.vo.CostReimbursePageReqVo;
import com.lh.oa.module.bpm.controller.admin.costReimburse.vo.CostReimburseRespVo;
import com.lh.oa.module.bpm.service.costReimburse.CostReimburseService;
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

@Tag(name =  "管理后台 - 费用报销申请")
@RestController
@RequestMapping("/bpm/cost/reimburse")
@Validated
public class CostReimburseRequestController {
    @Resource
    private CostReimburseService service;

    @PostMapping("/create")
    @PermitAll
    public CommonResult<Long> createCostReimburse(@Valid @RequestBody CostReimburseCreateVo createReqVO) {
        return success(service.createCostReimburse(createReqVO));
    }

    @GetMapping("/get")
    @PermitAll
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<CostReimburseRespVo> findCostReimburseById(@RequestParam("id") Long id) {
        return success(service.findCostReimburseById(id));
    }

    @GetMapping("/page")
    @PermitAll
    public CommonResult<PageResult<CostReimburseRespVo>> pageQueryCostReimburse(@Valid CostReimbursePageReqVo pageVO) {
        return success(service.pageQueryCostReimburse(pageVO));
    }
}
