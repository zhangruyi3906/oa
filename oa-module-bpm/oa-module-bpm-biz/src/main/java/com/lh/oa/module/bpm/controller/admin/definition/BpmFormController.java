package com.lh.oa.module.bpm.controller.admin.definition;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.definition.vo.form.BpmFormCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.form.BpmFormFieldVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.form.BpmFormPageReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.form.BpmFormRespVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.form.BpmFormSimpleRespVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.form.BpmFormUpdateReqVO;
import com.lh.oa.module.bpm.convert.definition.BpmFormConvert;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormFieldExportDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormFieldShowDO;
import com.lh.oa.module.bpm.service.definition.BpmFormFieldExportService;
import com.lh.oa.module.bpm.service.definition.BpmFormFieldShowService;
import com.lh.oa.module.bpm.service.definition.BpmFormService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name =  "管理后台 - 动态表单")
@RestController
@RequestMapping("/bpm/form")
@Validated
public class BpmFormController {

    @Resource
    private BpmFormService formService;
    @Resource
    private BpmFormFieldShowService bpmFormFieldShowService;
    @Resource
    private BpmFormFieldExportService bpmFormFieldExportService;

    @PostMapping("/create")
    //@Operation(summary = "创建动态表单")
    @PreAuthorize("@ss.hasPermission('bpm:form:create')")
    public CommonResult<Long> createForm(@Valid @RequestBody BpmFormCreateReqVO createReqVO) {
        return success(formService.createForm(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新动态表单")
    @PreAuthorize("@ss.hasPermission('bpm:form:update')")
    public CommonResult<Boolean> updateForm(@Valid @RequestBody BpmFormUpdateReqVO updateReqVO) {
        formService.updateForm(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除动态表单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('bpm:form:delete')")
    public CommonResult<Boolean> deleteForm(@RequestParam("id") Long id) {
        formService.deleteForm(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得动态表单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:form:query')")
    public CommonResult<BpmFormRespVO> getForm(@RequestParam("id") Long id) {
        BpmFormDO form = formService.getForm(id);
        BpmFormRespVO bpmFormRespVO = BpmFormConvert.INSTANCE.convert(form);
        List<BpmFormFieldShowDO> showList = bpmFormFieldShowService.getShowList(id);
        List<BpmFormFieldExportDO> exportList = bpmFormFieldExportService.getExportList(id);
        if (ObjectUtils.isNotEmpty(showList)) {
            bpmFormRespVO.setBpmFormFieldShowList(showList);
        }
        if (ObjectUtils.isNotEmpty(exportList)) {
            bpmFormRespVO.setBpmFormFieldExportList(exportList);
        }
        return success(bpmFormRespVO);
    }

    @GetMapping("/list-all-simple")
    //@Operation(summary = "获得动态表单的精简列表", description = "用于表单下拉框")
    public CommonResult<List<BpmFormSimpleRespVO>> getSimpleForms() {
        List<BpmFormDO> list = formService.getFormList();
        return success(BpmFormConvert.INSTANCE.convertList2(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得动态表单分页")
    @PreAuthorize("@ss.hasPermission('bpm:form:query')")
    public CommonResult<PageResult<BpmFormRespVO>> getFormPage(@Valid BpmFormPageReqVO pageVO) {
        PageResult<BpmFormDO> pageResult = formService.getFormPage(pageVO);
        return success(BpmFormConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/form-fields")
    @PermitAll
    public CommonResult<List<BpmFormFieldVO>> getFormFields(@RequestParam("id") Long id) {
        return success(formService.getFormFields(id));
    }

}