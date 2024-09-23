package com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.param.BorrowCarFormParam;
import com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.vo.BorrowCarFormVo;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.borrowCar.BpmBorrowCarSubsidy;
import com.lh.oa.module.bpm.service.businessForm.borrowCar.BorrowCarFormService;
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
@Tag(name = "管理后台 - 用车申请单")
@RestController
@RequestMapping("/bpm/borrowCar/form")
@Validated
public class BorrowCarController {

    @Resource
    private BorrowCarFormService performanceExamineFormService;

    /**
     * 创建用车申请单
     */
    @PostMapping("/create")
    public CommonResult<Long> create(@Valid @RequestBody BorrowCarFormParam param) {
        Long id = performanceExamineFormService.create(getLoginUserId(), param);
        return success(id);
    }

    /**
     * 查询用车申请单详情
     */
    @GetMapping("/queryDetail")
    @Parameter(name = "id", description = "开票申请单id", required = true)
    public CommonResult<BorrowCarFormVo> queryDetail(@RequestParam("id") Long id) {
        BorrowCarFormVo detail = performanceExamineFormService.queryDetail(id);
        return success(detail);
    }

    /**
     * 分页用车申请单
     */
    @PostMapping("/queryPage")
    public CommonResult<PageResult<BorrowCarFormVo>> queryPage(@Valid @RequestBody PageParam pageParam) {
        PageResult<BorrowCarFormVo> pageResult = performanceExamineFormService.queryPageByParam(pageParam);
        return success(pageResult);
    }
    /**
     * 查询用车补贴详情
     */
    @GetMapping("/queryCarSubsidyDetail")
    @Parameter(name = "displacementType", description = "汽车排量类型(数据字典中排量值)", required = true)
    public CommonResult<BpmBorrowCarSubsidy> queryCarSubsidyDetail(@RequestParam("displacementType") Integer id) {
        BpmBorrowCarSubsidy detail = performanceExamineFormService.queryCarSubsidyDetail(id);
        return success(detail);
    }


}