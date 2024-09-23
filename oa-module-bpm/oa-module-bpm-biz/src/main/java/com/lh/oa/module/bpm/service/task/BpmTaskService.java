package com.lh.oa.module.bpm.service.task;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
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
import org.flowable.task.api.Task;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 流程任务实例 Service 接口
 *
 * @author jason
 * @author
 */
public interface BpmTaskService {

    /**
     * 获得待办的流程任务分页
     *
     * @param userId    用户编号
     * @param pageReqVO 分页请求
     *
     * @return 流程任务分页
     */
    PageResult<BpmTaskTodoPageItemRespVO> getTodoTaskPage(Long userId, BpmTaskTodoPageReqVO pageReqVO);
    PageResult<BpmTaskTodoPageItemRespVO> getNewTodoTaskPage(Long userId, BpmTaskTodoPageReqVO pageVO);

    /**
     * 获得已办的流程任务分页
     *
     * @param userId    用户编号
     * @param pageReqVO 分页请求
     *
     * @return 流程任务分页
     */
    PageResult<BpmTaskDonePageItemRespVO> getDoneTaskPage(Long userId, BpmTaskDonePageReqVO pageReqVO);
    PageResult<BpmTaskDonePageItemRespVO> getNewDoneTaskPage(Long userId, BpmTaskDonePageReqVO pageVO);
    /**
     * 获得流程任务 Map
     *
     * @param processInstanceIds 流程实例的编号数组
     *
     * @return 流程任务 Map
     */
    default Map<String, List<Task>> getTaskMapByProcessInstanceIds(List<String> processInstanceIds) {
        return CollectionUtils.convertMultiMap(getTasksByProcessInstanceIds(processInstanceIds),
            Task::getProcessInstanceId);
    }

    /**
     * 获得流程任务列表
     *
     * @param processInstanceIds 流程实例的编号数组
     *
     * @return 流程任务列表
     */
    List<Task> getTasksByProcessInstanceIds(List<String> processInstanceIds);

    /**
     * 获得指令流程实例的流程任务列表，包括所有状态的
     *
     * @param processInstanceId 流程实例的编号
     *
     * @return 流程任务列表
     */
    List<BpmTaskRespVO> getTaskListByProcessInstanceId(String processInstanceId);

    /**
     * 通过任务
     *
     * @param userId 用户编号
     * @param bpmTaskVO  通过请求
     */
    void approveTask(Long userId, @Valid BpmTaskVo bpmTaskVO);

    /**
     * 不通过任务
     *
     * @param userId 用户编号
     * @param bpmTaskVO  不通过请求
     */
    void rejectTask(Long userId, @Valid BpmTaskVo bpmTaskVO);

    /**
     * 将流程任务分配给指定用户
     *
     * @param userId 用户编号
     * @param reqVO  分配请求
     */
    void updateTaskAssignee(Long userId, BpmTaskUpdateAssigneeReqVO reqVO);

    /**
     * 将流程任务分配给指定用户
     *
     * @param id     流程任务编号
     * @param userId 用户编号
     */
    void updateTaskAssignee(String id, Long userId);

    /**
     * 创建 Task 拓展记录
     *
     * @param task 任务实体
     */
    void createTaskExt(Task task);

    /**
     * 更新 Task 拓展记录为完成
     *
     * @param task 任务实体
     */
    void updateTaskExtComplete(Task task);

    /**
     * 更新 Task 拓展记录为已取消
     *
     * @param taskId 任务的编号
     */
    void updateTaskExtCancel(String taskId);

    /**
     * 更新 Task 拓展记录，并发送通知
     *
     * @param task 任务实体
     */
    void updateTaskExtAssign(Task task);

    void readTodoTask(String taskId);

    Integer unreadTodoTaskCount(Long userId);

    List<BpmUserTaskRespVO> findReturnTaskList(String taskId);

    void taskReturn(BpmProcessInstanceRollbackReqVO rollbackReqVO);

    void  exportAllTask(BpmTaskDoneExcVo excVO, HttpServletRequest request, HttpServletResponse response);

    void revokeProcess(String processInstanceId, String message);

    Integer getTodoTaskCount(Long userId);

    void saveTask(BpmTaskVo bpmTaskVO);

    Boolean getRevocable(String processInstanceId, String taskId);

    void revokeTask(BpmTaskRevokeReqVO bpmTaskRevokeReqVO);
}
