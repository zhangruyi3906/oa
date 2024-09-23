package com.lh.oa.module.bpm.controller.admin.task;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceParam;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceTo;
import com.lh.oa.module.bpm.controller.admin.task.vo.BpmProcessAttendanceExportVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.BpmProcessAttendanceQueryParam;
import com.lh.oa.module.bpm.controller.admin.task.vo.BpmProcessAttendanceVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceCancelReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceFormVo;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceMyPageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstancePageItemRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRespVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRetractResVO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.enums.definition.AttendanceProcessNameEnum;
import com.lh.oa.module.bpm.service.task.BpmProcessInstanceService;
import com.lh.oa.module.bpm.utils.export.ExportEntry;
import com.lh.oa.module.system.api.dict.dto.DictDataRespDTO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.lh.oa.framework.common.pojo.CommonResult.error;
import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;


@Tag(name = "管理后台 - 流程实例") // 流程实例，通过流程定义创建的一次“申请”
@RestController
@RequestMapping("/bpm/process-instance")
@Validated
@Slf4j
public class BpmProcessInstanceController {

    @Resource
    private BpmProcessInstanceService processInstanceService;

    @GetMapping("/my-page")
    @PermitAll
    //@Operation(summary = "获得我的实例分页列表", description = "在【我的流程】菜单中，进行调用")
//    @OperateLog(enable = false)
//    @PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    public CommonResult<PageResult<BpmProcessInstancePageItemRespVO>> getMyProcessInstancePage(
            @Valid BpmProcessInstanceMyPageReqVO pageReqVO) {
        return success(processInstanceService.getMyProcessInstancePage(getLoginUserId(), pageReqVO));
    }

    @GetMapping("/my-list")
    @PermitAll
    //@Operation(summary = "获得我的实例列表")
//    @OperateLog(enable = false)
//    @PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    public CommonResult<List<BpmProcessInstanceFormVo>> getMyProcessInstanceList(
            @Valid BpmProcessInstanceExtDO bpmProcessInstanceExtDO) {
        return success(processInstanceService.getMyProcessInstanceList(getLoginUserId(), bpmProcessInstanceExtDO));
    }

    @GetMapping("/all-page")
    @PermitAll
//    @OperateLog(enable = false)
    public CommonResult<PageResult<BpmProcessInstancePageItemRespVO>> getAllProcessInstancePage(
            @Valid BpmProcessInstanceMyPageReqVO pageReqVO) {
        return success(processInstanceService.getMyProcessInstancePage(null, pageReqVO));
    }

    @PostMapping("/create")
    //@Operation(summary = "新建流程实例")
//    @OperateLog(enable = false)
    @PermitAll
//    @PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    public CommonResult<String> createProcessInstance(@RequestBody Map<String, Object> variables) {
        return success(processInstanceService.createProcessInstance(variables));
    }


    @PostMapping("/save")
    //@Operation(summary = "保存流程")
//    @OperateLog(enable = false)
    public CommonResult<Boolean> saveProcess(@RequestBody Map<String, Object> variables) {
        processInstanceService.saveProcess(variables);
        return success(true);
    }

    @GetMapping("/getSaveProcess")
    //@Operation(summary = "读取保存的数据")
//    @OperateLog(enable = false)
    public CommonResult<Map<String, Object>> getSaveProcess(@RequestParam("processDefinitionId") String processDefinitionId) {
        Map<String, Object> variables = processInstanceService.getSaveProcess(processDefinitionId);
        return success(variables);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得指定流程实例", description = "在【流程详细】界面中，进行调用")
    @Parameter(name = "id", description = "流程实例的编号", required = true)
    @PermitAll
//    @OperateLog(enable = false)
//    @PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    public CommonResult<BpmProcessInstanceRespVO> getProcessInstance(@RequestParam("id") String id) {
        try {
            BpmProcessInstanceRespVO bpmProcessInstanceRespVO = processInstanceService.getProcessInstanceVO(id);
            return success(bpmProcessInstanceRespVO);
        } catch (Exception e) {
            log.error("获取流程实例失败，id：{}", id);
            return error(500, "流程不存在或已被发起人收回");
        }
    }

    @DeleteMapping("/cancel")
    //@Operation(summary = "取消流程实例", description = "撤回发起的流程")
    @PermitAll
    public CommonResult<Boolean> cancelProcessInstance(@Valid @RequestBody BpmProcessInstanceCancelReqVO cancelReqVO) {
        processInstanceService.cancelProcessInstance(getLoginUserId(), cancelReqVO);
        return success(true);
    }

    /**
     * 撤回流程
     */
    @GetMapping("/retract")
    //@Operation(summary = "撤回流程实例", description = "撤回发起的流程")
    @PermitAll
    public CommonResult<BpmProcessInstanceRetractResVO> retractProcessInstance(@RequestParam("processInstanceId") String processInstanceId) {
        log.info("撤回流程实例，processInstanceId：{}", Arrays.asList(processInstanceId));
        BpmProcessInstanceRetractResVO bpmProcessInstanceRetractResVO = processInstanceService.retractProcessInstance(getLoginUserId(), processInstanceId);
        return success(bpmProcessInstanceRetractResVO);
    }

    /**
     * 获得请假流程
     */
    @GetMapping("/getAttendanceLeaveCount")
    //@Operation(summary = "获得指定流程实例", description = "在【流程详细】界面中，进行调用")
    @Parameter(name = "id", description = "流程实例的编号", required = true)
    @PermitAll
    public Double getAttendanceLeaveCount(@RequestParam("useId") Long userId, @RequestParam("attendMonth") Integer attendMonth) {
        return processInstanceService.getAttendanceLeaveCount(userId, attendMonth);
    }

    /**
     * 获得加班流程
     */
    @GetMapping("/getAttendanceAddCount")
    //@Operation(summary = "获得流程实例", description = "在【流程详细】界面中，进行调用")
    @Parameter(name = "id", description = "流程实例的编号", required = true)
    @PermitAll
    public Integer getAttendanceAddCount(@RequestParam("useId") Long userId, @RequestParam("attendMonth") Integer attendMonth) {
        return processInstanceService.getAttendanceAddCount(userId, attendMonth);
    }

    /**
     * 获得出差流程
     */
    @GetMapping("/getAttendanceTravelCount")
    //@Operation(summary = "获得指定流程实例", description = "在【流程详细】界面中，进行调用")
    @Parameter(name = "id", description = "流程实例的编号", required = true)
    @PermitAll
    public Integer getAttendanceTravelCount(@RequestParam("useId") Long userId, @RequestParam("attendMonth") Integer attendMonth) {
        return processInstanceService.getAttendanceTravelCount(userId, attendMonth);
    }

    @PostMapping("/getProcessByUserIdAndMonth")
    public CommonResult<List<BpmProcessInstanceTo>> getProcessByUserIdAndMonth(@RequestBody BpmProcessInstanceParam param) {
        log.info("获取用户发起的流程列表, param:{}", JsonUtils.toJsonString(param));
        List<BpmProcessInstanceTo> result = processInstanceService.getProcessByUserIdAndMonth(param);
        return success(result);
    }

    @PostMapping("/exportProcessInstance")
    public CommonResult<String> getExportProcessInstanceData(@RequestBody BpmProcessAttendanceQueryParam param,
                                             HttpServletResponse response) throws IOException {
        log.info("流程列表导出, param:{}", JsonUtils.toJsonString(param));
        List<BpmProcessAttendanceExportVo> list = processInstanceService.getExportProcessInstanceData(param);
        // 输出
        ExcelUtils.write(response, "流程列表数据.xls",
                "流程列表",
                BpmProcessAttendanceExportVo.class,
                list);
        return success();
    }


    @GetMapping("/getAttendanceProcessNameList")
    public CommonResult<List<DictDataRespDTO>> getAttendanceProcessNameList() {
        log.info("获取考勤相关流程名称列表");
        List<DictDataRespDTO> result = new LinkedList<>();
        for (AttendanceProcessNameEnum value : AttendanceProcessNameEnum.values()) {
            DictDataRespDTO to = new DictDataRespDTO();
            to.setLabel(value.getLabel());
            to.setValue(value.getValue());
            result.add(to);
        }
        // 前端组件写死要对象数组格式不然解析有问题
        return success(result);
    }

    @PostMapping("/getAttendanceProcessByParamPage")
    public CommonResult<PageResult<BpmProcessAttendanceVo>> getAttendanceProcessByParamPage(@RequestBody BpmProcessAttendanceQueryParam param) {
        log.info("获取考勤相关流程列表, param:{}", JsonUtils.toJsonString(param));
        PageResult<BpmProcessAttendanceVo> result = processInstanceService.getAttendanceProcessByParamPage(param);
        return success(result);
    }

    @PostMapping("/exportAttendanceProcess")
    public CommonResult<String> exportAttendanceProcess(@RequestBody BpmProcessAttendanceQueryParam param,
                                                                HttpServletResponse response) throws IOException {
        log.info("考勤相关流程列表导出, param:{}", JsonUtils.toJsonString(param));
        List<BpmProcessAttendanceExportVo> list = processInstanceService.getAttendanceProcessExportListByParam(param);
        // 输出
        ExcelUtils.write(response, "考勤" + param.getProcessName() + "流程数据.xls",
                param.getProcessName() + "流程列表",
                BpmProcessAttendanceExportVo.class,
                list);
        return success();
    }

    @GetMapping("/getAllProcessInstanceIdsByUserId")
    CommonResult<Set<String>> getAllProcessInstanceIdsByUserId(Long userId) {
        Set<String> processInstanceIdSet = processInstanceService.getAllProcessInstanceIdsByUserId(userId);
        return success(processInstanceIdSet);
    }
}