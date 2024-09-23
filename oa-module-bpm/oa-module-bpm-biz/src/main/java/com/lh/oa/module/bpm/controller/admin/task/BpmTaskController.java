package com.lh.oa.module.bpm.controller.admin.task;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.task.vo.copy.BpmCopy;
import com.lh.oa.module.bpm.controller.admin.task.vo.copy.BpmCopyPageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRollbackReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskDoneExcVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskDonePageItemRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskDonePageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskRevokeReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskTodoPageItemRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskTodoPageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskUpdateAssigneeReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmUserTaskRespVO;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.service.task.BpmCopyService;
import com.lh.oa.module.bpm.service.task.BpmTaskService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.error;
import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name =  "管理后台 - 流程任务实例")
@RestController
@RequestMapping("/bpm/task")
@Validated
public class BpmTaskController {

    @Resource
    private BpmTaskService taskService;

    @Resource
    private BpmCopyService bpmCopyService;

    @GetMapping("todo-page")
    //@Operation(summary = "获取 Todo 待办任务分页")
//    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    @PermitAll
    public CommonResult<PageResult<BpmTaskTodoPageItemRespVO>> getTodoTaskPage(@Valid BpmTaskTodoPageReqVO pageVO) {
//        Long userId = pageVO.getUserId();
        Long userId = pageVO.getUserId() == null ? getLoginUserId() : pageVO.getUserId();
        return success(taskService.getNewTodoTaskPage(userId, pageVO));
    }

    @GetMapping("todo-count")
    //@Operation(summary = "获取 Todo 待办任务数量")
//    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    @PermitAll
    public CommonResult<Integer> getTodoTaskCount(@RequestParam(required = false) Long userId) {
        return success(taskService.getTodoTaskCount(ObjectUtils.isNotEmpty(userId) ? userId : getLoginUserId()));
    }

    @GetMapping("done-page")
    //@Operation(summary = "获取 Done 已办任务分页")
//    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    @PermitAll
    public CommonResult<PageResult<BpmTaskDonePageItemRespVO>> getDoneTaskPage(@Valid BpmTaskDonePageReqVO pageVO) {
        return success(taskService.getNewDoneTaskPage(getLoginUserId(), pageVO));
    }

    @GetMapping(value = "/copyList")
    public CommonResult<PageResult<BpmCopy>> copyProcessList(BpmCopyPageReqVO bpmCopyPageReqVO) {
        PageResult<BpmCopy> bpmCopyPage = bpmCopyService.selectPageList(bpmCopyPageReqVO);
        return success(bpmCopyPage);
    }

    @GetMapping("export")
    //@Operation(summary = "导出流程")
//    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    @PermitAll
    public void exportAllTask(@Valid BpmTaskDoneExcVo excVO, HttpServletRequest request, HttpServletResponse response) {
        taskService.exportAllTask(excVO, request, response);
    }

    @GetMapping("/list-by-process-instance-id")
    //@Operation(summary = "获得指定流程实例的任务列表", description = "包括完成的、未完成的")
    @Parameter(name = "processInstanceId", description = "流程实例的编号", required = true)
//    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    public CommonResult<List<BpmTaskRespVO>> getTaskListByProcessInstanceId(
        @RequestParam("processInstanceId") String processInstanceId) {
        List<BpmTaskRespVO> taskList = taskService.getTaskListByProcessInstanceId(processInstanceId);
        if (CollectionUtils.isEmpty(taskList)) {
            return error(500, "流程不存在或已被发起人收回");
        }
        return success(taskList);
    }

    @PutMapping("/approve")
    //@Operation(summary = "通过任务")
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    @PermitAll
    public CommonResult<Boolean> approveTask(@Valid @RequestBody BpmTaskVo bpmTaskVO) {
        if(bpmTaskVO.getUserId() !=null) {
            taskService.approveTask(Long.valueOf(bpmTaskVO.getUserId()), bpmTaskVO);
        }
        else {
            taskService.approveTask(getLoginUserId(), bpmTaskVO);
        }
        return success(true);
    }

    @PostMapping("/save/task")
    //@Operation(summary = "保存任务")
    @PermitAll
    public CommonResult<Boolean> saveTask(@Valid @RequestBody BpmTaskVo bpmTaskVO) {
        taskService.saveTask(bpmTaskVO);
        return success(true);
    }

    @PutMapping("/reject")
    //@Operation(summary = "不通过任务")
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    @PermitAll
    public CommonResult<Boolean> rejectTask(@Valid @RequestBody BpmTaskVo bpmTaskVO) {
        if(bpmTaskVO.getUserId() !=null) {
            taskService.rejectTask(Long.valueOf(bpmTaskVO.getUserId()), bpmTaskVO);
        }else {
            taskService.rejectTask(getLoginUserId(), bpmTaskVO);
        }
        return success(true);
    }

    @PutMapping("/update-assignee")
    //@Operation(summary = "更新任务的负责人")
    @PermitAll
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> updateTaskAssignee(@Valid @RequestBody BpmTaskUpdateAssigneeReqVO reqVO) {
        if(reqVO.getUserId() !=null) {
            taskService.updateTaskAssignee(reqVO.getUserId(), reqVO);
        }else {
            taskService.updateTaskAssignee(getLoginUserId(), reqVO);
        }
        return success(true);
    }

    @PutMapping("/read")
    @PermitAll
    public CommonResult<Boolean> readTodoTask(@RequestParam String taskId) {
        taskService.readTodoTask(taskId);
        return success(true);
    }

    @GetMapping("/unread/count")
    @PermitAll
    public CommonResult<Integer> unreadTodoTaskCount(@RequestParam Long userId) {
        return success(taskService.unreadTodoTaskCount(userId));
    }

    @GetMapping("/return/list")
    @PermitAll
    public CommonResult<List<BpmUserTaskRespVO>> findReturnTaskList(@RequestParam String taskId) {
        try {
            return success(taskService.findReturnTaskList(taskId));
        }catch (Exception e) {
            return success();
        }
    }

    @PostMapping("/rollback")
    @PermitAll
    public CommonResult<Boolean> rollbackProcessInstance(@Valid @RequestBody BpmProcessInstanceRollbackReqVO createReqVO){
        taskService.taskReturn(createReqVO.setResult(BpmProcessInstanceResultEnum.BACK.getResult()));
        return success(true);
    }

    @GetMapping("/revoke")
    @PermitAll
    public CommonResult<Boolean> findReturnTaskList(@RequestParam String processInstanceId, @RequestParam String message) {
        taskService.revokeProcess(processInstanceId, message);
        return success(true);
    }
    /**
     * 查看已办任务是否可撤回
     */
    @GetMapping("/getRevocable")
    @PermitAll
    public CommonResult<Boolean> getRevocable(@RequestParam String processInstanceId, @RequestParam String taskId) {
        Boolean revocable = taskService.getRevocable(processInstanceId, taskId);
        return success(revocable);
    }

    /**
     * 撤回任务
     */
    @PostMapping("/revokeTask")
    @PermitAll
    public CommonResult<Boolean> revokeTask(@Valid @RequestBody BpmTaskRevokeReqVO bpmTaskRevokeReqVO) {
        taskService.revokeTask(bpmTaskRevokeReqVO);
        return success(true);
    }

}