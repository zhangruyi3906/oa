package com.lh.oa.module.bpm.service.task;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceParam;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceTo;
import com.lh.oa.module.bpm.controller.admin.task.vo.BpmProcessAttendanceExportVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.BpmProcessAttendanceQueryParam;
import com.lh.oa.module.bpm.controller.admin.task.vo.BpmProcessAttendanceVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceCancelReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceFormVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceMyPageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstancePageItemRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRetractResVO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.utils.export.ExportEntry;
import org.flowable.engine.delegate.event.FlowableCancelledEvent;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 流程实例 Service 接口
 *
 * @author
 */
public interface BpmProcessInstanceService {

    /**
     * 获得流程实例
     *
     * @param id 流程实例的编号
     * @return 流程实例
     */
    ProcessInstance getProcessInstance(String id);

    /**
     * 获得流程实例列表
     *
     * @param ids 流程实例的编号集合
     * @return 流程实例列表
     */
    List<ProcessInstance> getProcessInstances(Set<String> ids);

    /**
     * 获得流程实例 Map
     *
     * @param ids 流程实例的编号集合
     * @return 流程实例列表 Map
     */
    default Map<String, ProcessInstance> getProcessInstanceMap(Set<String> ids) {
        return CollectionUtils.convertMap(getProcessInstances(ids), ProcessInstance::getProcessInstanceId);
    }

    /**
     * 获得流程实例的分页
     *
     * @param userId    用户编号
     * @param pageReqVO 分页请求
     * @return 流程实例的分页
     */
    PageResult<BpmProcessInstancePageItemRespVO> getMyProcessInstancePage(Long userId, @Valid BpmProcessInstanceMyPageReqVO pageReqVO);

    /**
     * 创建流程实例（提供给前端）
     *
     * @param variables 创建信息
     * @return 实例的编号
     */
    String createProcessInstance(Map<String, Object> variables);

    /**
     * 创建流程实例（提供给内部）
     *
     * @param userId       用户编号
     * @param createReqDTO 创建信息
     * @return 实例的编号
     */
    String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqDTO createReqDTO);


    /**
     * 建流程实例
     *
     * @param userId
     * @param createReqDTO
     * @return
     */
    String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqVO createReqDTO);

    /**
     * 获得流程实例 VO 信息
     *
     * @param id 流程实例的编号
     * @return 流程实例
     */
    BpmProcessInstanceRespVO getProcessInstanceVO(String id);

    /**
     * 取消流程实例
     *
     * @param userId      用户编号
     * @param cancelReqVO 取消信息
     */
    void cancelProcessInstance(Long userId, @Valid BpmProcessInstanceCancelReqVO cancelReqVO);

    /**
     * 获得历史的流程实例
     *
     * @param id 流程实例的编号
     * @return 历史的流程实例
     */
    HistoricProcessInstance getHistoricProcessInstance(String id);

    /**
     * 获得历史的流程实例列表
     *
     * @param ids 流程实例的编号集合
     * @return 历史的流程实例列表
     */
    List<HistoricProcessInstance> getHistoricProcessInstances(Set<String> ids);

    /**
     * 获得历史的流程实例 Map
     *
     * @param ids 流程实例的编号集合
     * @return 历史的流程实例列表 Map
     */
    default Map<String, HistoricProcessInstance> getHistoricProcessInstanceMap(Set<String> ids) {
        return CollectionUtils.convertMap(getHistoricProcessInstances(ids), HistoricProcessInstance::getId);
    }

    /**
     * 创建 ProcessInstance 拓展记录
     *
     * @param instance 流程任务
     */
    void createProcessInstanceExt(ProcessInstance instance);

    /**
     * 更新 ProcessInstance 拓展记录为取消
     *
     * @param event 流程取消事件
     */
    void updateProcessInstanceExtCancel(FlowableCancelledEvent event);

    /**
     * 更新 ProcessInstance 拓展记录为完成
     *
     * @param instance 流程任务
     */
    void updateProcessInstanceExtComplete(ProcessInstance instance);

    /**
     * 更新 ProcessInstance 拓展记录为不通过
     *
     * @param id     流程编号
     * @param reason 理由。例如说，审批不通过时，需要传递该值
     */
    void updateProcessInstanceExtReject(String id, String reason);


    void saveProcess(Map<String, Object> variables);

    Double getAttendanceLeaveCount(Long userId, Integer attendanceMonth);

    Integer getAttendanceAddCount(Long userId, Integer attendanceMonth);

    Integer getAttendanceTravelCount(Long userId, Integer attendanceMonth);

    BpmProcessInstanceRetractResVO retractProcessInstance(Long loginUserId, String processInstanceId);

    List<BpmProcessInstanceFormVo> getMyProcessInstanceList(Long loginUserId, BpmProcessInstanceExtDO bpmProcessInstanceExtDO);

    Map<String, Object> getSaveProcess(String processDefinitionId);

    List<BpmProcessInstanceTo> getProcessByUserIdAndMonth(BpmProcessInstanceParam param);

    PageResult<BpmProcessAttendanceVo> getAttendanceProcessByParamPage(BpmProcessAttendanceQueryParam param);

    List<BpmProcessAttendanceExportVo> getAttendanceProcessExportListByParam(BpmProcessAttendanceQueryParam param);

    List<BpmProcessAttendanceExportVo> getExportProcessInstanceData(BpmProcessAttendanceQueryParam param);

    Set<String> getAllProcessInstanceIdsByUserId(Long userId);
}
