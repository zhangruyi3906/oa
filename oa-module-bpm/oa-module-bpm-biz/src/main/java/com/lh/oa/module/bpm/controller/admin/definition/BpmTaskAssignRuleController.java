package com.lh.oa.module.bpm.controller.admin.definition;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleRespVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleUpdateReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.rule.BpmTaskKeyVO;
import com.lh.oa.module.bpm.service.definition.BpmTaskAssignRuleService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name =  "管理后台 - 任务分配规则")
@RestController
@RequestMapping("/bpm/task-assign-rule")
@Validated
public class BpmTaskAssignRuleController {

    @Resource
    private BpmTaskAssignRuleService taskAssignRuleService;

    @GetMapping("/list")
    //@Operation(summary = "获得任务分配规则列表")
    @Parameters({
            @Parameter(name = "modelId", description = "模型编号", example = "1024"),
            @Parameter(name = "processDefinitionId", description = "流程定义的编号", example = "2048")
    })
    @PreAuthorize("@ss.hasPermission('bpm:task-assign-rule:query')")
    public CommonResult<List<BpmTaskAssignRuleRespVO>> getTaskAssignRuleList(
            @RequestParam(value = "modelId", required = false) String modelId,
            @RequestParam(value = "processDefinitionId", required = false) String processDefinitionId) {
        return success(taskAssignRuleService.getTaskAssignRuleList(modelId, processDefinitionId));
    }

    @GetMapping("/allTaskKey")
    //@Operation(summary = "获得流程所有节点")
    @Parameters({
            @Parameter(name = "modelId", description = "模型编号", example = "1024"),
            @Parameter(name = "processDefinitionId", description = "流程定义的编号", example = "2048")
    })
    @PreAuthorize("@ss.hasPermission('bpm:task-assign-rule:query')")
    public CommonResult<List<BpmTaskKeyVO>> getAllTaskKey(
            @RequestParam(value = "modelId", required = false) String modelId,
            @RequestParam(value = "processDefinitionId", required = false) String processDefinitionId) {
        return success(taskAssignRuleService.getAllTaskKey(modelId, processDefinitionId));
    }

    @PostMapping("/create")
    //@Operation(summary = "创建任务分配规则")
    @PreAuthorize("@ss.hasPermission('bpm:task-assign-rule:create')")
    public CommonResult<Long> createTaskAssignRule(@Valid @RequestBody BpmTaskAssignRuleCreateReqVO reqVO) {
        return success(taskAssignRuleService.createTaskAssignRule(reqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新任务分配规则")
    @PreAuthorize("@ss.hasPermission('bpm:task-assign-rule:update')")
    public CommonResult<Boolean> updateTaskAssignRule(@Valid @RequestBody BpmTaskAssignRuleUpdateReqVO reqVO) {
        taskAssignRuleService.updateTaskAssignRule(reqVO);
        return success(true);
    }
}