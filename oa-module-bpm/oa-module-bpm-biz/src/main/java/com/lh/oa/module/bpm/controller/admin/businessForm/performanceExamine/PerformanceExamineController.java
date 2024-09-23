package com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.param.PerformanceExamineFormParam;
import com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.vo.PerformanceExamineFormVo;
import com.lh.oa.module.bpm.service.businessForm.performanceExamine.PerformanceExamineFormService;
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
 * @since 2023/10/24
 */
@Tag(name = "管理后台 - 绩效考评申请单")
@RestController
@RequestMapping("/bpm/performanceExamine/form")
@Validated
public class PerformanceExamineController {

    @Resource
    private PerformanceExamineFormService performanceExamineFormService;

    /**
     * 创建绩效考评申请单
     */
    @PostMapping("/create")
    public CommonResult<Long> create(@Valid @RequestBody PerformanceExamineFormParam param) {
        Long id = performanceExamineFormService.create(getLoginUserId(), param);
        return success(id);
    }

    /**
     * 查询绩效考评申请单详情
     */
    @GetMapping("/queryDetail")
    @Parameter(name = "id", description = "开票申请单id", required = true)
    public CommonResult<PerformanceExamineFormVo> queryDetail(@RequestParam("id") Long id) {
        PerformanceExamineFormVo detail = performanceExamineFormService.queryDetail(id);
        return success(detail);
    }

    /**
     * 分页绩效考评申请单
     */
    @PostMapping("/queryPage")
    public CommonResult<PageResult<PerformanceExamineFormVo>> queryPage(@Valid @RequestBody PageParam pageParam) {
        PageResult<PerformanceExamineFormVo> pageResult = performanceExamineFormService.queryPageByParam(pageParam);
        return success(pageResult);
    }

}