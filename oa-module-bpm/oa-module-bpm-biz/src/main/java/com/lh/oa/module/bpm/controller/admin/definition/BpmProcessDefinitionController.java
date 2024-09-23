package com.lh.oa.module.bpm.controller.admin.definition;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionListReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionPageItemRespVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionPageReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionRespVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.process.BpmProcessTypeGroupRespVO;
import com.lh.oa.module.bpm.service.definition.BpmProcessDefinitionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 流程定义")
@RestController
@RequestMapping("/bpm/process-definition")
@Validated
public class BpmProcessDefinitionController {

    @Resource
    private BpmProcessDefinitionService bpmDefinitionService;

    @GetMapping("/page")
    //@Operation(summary = "获得流程定义分页")
    @PreAuthorize("@ss.hasPermission('bpm:process-definition:query')")
    public CommonResult<PageResult<BpmProcessDefinitionPageItemRespVO>> getProcessDefinitionPage(
            BpmProcessDefinitionPageReqVO pageReqVO) {
        return success(bpmDefinitionService.getProcessDefinitionPage(pageReqVO));
    }

    @GetMapping("/list")
//    @Operation(summary = "获得流程定义列表")
//    @PreAuthorize("@ss.hasPermission('bpm:process-definition:query')")
//    @PermitAll
    public CommonResult<List<BpmProcessTypeGroupRespVO>> getProcessDefinitionList(
            BpmProcessDefinitionListReqVO listReqVO) {
        return success(bpmDefinitionService.getProcessDefinitionList(listReqVO));
    }

    @GetMapping("/info")
    @PermitAll
    public CommonResult<List<BpmProcessDefinitionRespVO>> getProcessDefinitionById(String id) {
        return success(bpmDefinitionService.getProcessDefinitionById(id));
    }

    @GetMapping("/get-bpmn-xml")
    @PermitAll
//    @Operation(summary = "获得流程定义的 BPMN XML")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('bpm:process-definition:query')")
    public CommonResult<String> getProcessDefinitionBpmnXML(@RequestParam("id") String id) {
        String bpmnXML = bpmDefinitionService.getProcessDefinitionBpmnXML(id);
        return success(bpmnXML);
    }
}