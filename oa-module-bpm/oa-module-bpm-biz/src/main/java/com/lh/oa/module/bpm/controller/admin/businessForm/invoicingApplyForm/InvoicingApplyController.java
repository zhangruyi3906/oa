package com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.param.InvoicingApplyFormCreateParam;
import com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.vo.InvoicingApplyFormVo;
import com.lh.oa.module.bpm.service.businessForm.invoicingApply.InvoicingApplyFormService;
import io.swagger.v3.oas.annotations.Operation;
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
import javax.validation.Valid;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;


/**
 * @author tanghanlin
 * @since 2023/10/21
 */
@Tag(name = "管理后台 - 开票申请单")
@RestController
@RequestMapping("/bpm/invoicingApply/form")
@Validated
public class InvoicingApplyController {

    @Resource
    private InvoicingApplyFormService invoicingApplyFormService;

    /**
     * 查询开票申请单详情
     */
    @PostMapping("/create")
    public CommonResult<Long> create(@Valid @RequestBody InvoicingApplyFormCreateParam param) {
        Long id = invoicingApplyFormService.create(getLoginUserId(), param);
        return success(id);
    }

    @GetMapping("/queryDetail")
    @Parameter(name = "id", description = "开票申请单id", required = true)
    public CommonResult<InvoicingApplyFormVo> queryDetail(@RequestParam("id") Long id) {
        InvoicingApplyFormVo detail = invoicingApplyFormService.queryDetail(id);
        return success(detail);
    }

    /**
     * 分页查询开票申请单
     */
    @PostMapping("/queryPage")
    public CommonResult<PageResult<InvoicingApplyFormVo>> queryPage(@Valid @RequestBody PageParam pageParam) {
        PageResult<InvoicingApplyFormVo> pageResult = invoicingApplyFormService.queryPageByParam(pageParam);
        return success(pageResult);
    }

}