package com.lh.oa.module.bpm.controller.admin.officesuppliesrequest;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestCreateVo;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestPageReqVo;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestRespVo;
import com.lh.oa.module.bpm.convert.officesuppliesrequest.OfficeSuppliesRequestConvert;
import com.lh.oa.module.bpm.dal.dataobject.officesuppliesrequest.OfficeSuppliesRequestDO;
import com.lh.oa.module.bpm.service.oa.BpmOALeaveService;
import com.lh.oa.module.bpm.service.officesuppliesrequest.OfficeSuppliesRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name =  "管理后台 - 办公物品申请")
@RestController
@RequestMapping("/bpm/office/supplies")
@Validated
public class OfficeSuppliesRequestController {

    @Resource
    private BpmOALeaveService leaveService;

    @Resource
    private OfficeSuppliesRequestService service;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('bpm:0ffice-supplies-request:create')")
    //@Operation(summary = "创建请求申请")
    @PermitAll
    public CommonResult<Long> createOfficeSu(@Valid @RequestBody OfficeSuppliesRequestCreateVo createReqVO) {
        return success(service.createOfficeSu(createReqVO));
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('bpm:0ffice-supplies-request:query')")
//    @Operation(summary = "获得请假申请")
    @PermitAll
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OfficeSuppliesRequestRespVo> getOfficeSu(@RequestParam("id") Long id) {
        OfficeSuppliesRequestRespVo officeSu = service.getOfficeSu(id);
        return success(officeSu);
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('bpm:0ffice-supplies-request:query')")
//    @Operation(summary = "获得请假申请分页")
    @PermitAll
    public CommonResult<PageResult<OfficeSuppliesRequestRespVo>> getOfficeSuPage(@Valid OfficeSuppliesRequestPageReqVo pageVO) {
        PageResult<OfficeSuppliesRequestDO> pageResult = service.getOfficeSuPage(getLoginUserId(), pageVO);
        return success(OfficeSuppliesRequestConvert.INSTANCE.convertPage(pageResult));
    }

}