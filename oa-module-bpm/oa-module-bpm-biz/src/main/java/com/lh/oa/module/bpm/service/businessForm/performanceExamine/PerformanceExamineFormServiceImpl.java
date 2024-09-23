package com.lh.oa.module.bpm.service.businessForm.performanceExamine;

import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceApi;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.param.PerformanceExamineFormParam;
import com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.vo.PerformanceExamineDetailVo;
import com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.vo.PerformanceExamineFormVo;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.performanceExamine.BpmPerformanceExamineDetail;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.performanceExamine.BpmPerformanceExamineForm;
import com.lh.oa.module.bpm.dal.mysql.businessForm.performanceExamine.BpmPerformanceExamineDetailMapper;
import com.lh.oa.module.bpm.dal.mysql.businessForm.performanceExamine.BpmPerformanceExamineFormMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.wrapper.SystemDictDataWrapper;
import com.lh.oa.module.system.api.dept.DeptApi;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.SUPPLY_NOT_EXISTS_ERROR;

/**
 * @author tanghanlin
 * @since 2023/10/21
 */
@Service
@Slf4j
public class PerformanceExamineFormServiceImpl implements PerformanceExamineFormService {

    /**
     * 绩效考评申请单流程标识，流程模型管理里的流程标识字段就是
     */
    public static final String PROCESS_KEY = "performance-examine-form";

    public static final String PERFORMANCE_DICT_TYPES = "performance_applicable_file_type,performance_examine_situation_type";

    @Resource
    private BpmPerformanceExamineFormMapper bpmPerformanceExamineFormMapper;

    @Resource
    private BpmPerformanceExamineDetailMapper bpmPerformanceExamineDetailMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Resource
    private SystemDictDataWrapper systemDictDataWrapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private DeptApi deptApi;

    @Override
    @Transactional
    public Long create(Long userId, PerformanceExamineFormParam param) {
        log.info("创建绩效考评申请单，userId:{}, param:{}", userId, param);
        // 插入实体，获取id
        BpmPerformanceExamineForm entity = JsonUtils.covertObject(param, BpmPerformanceExamineForm.class);
        entity.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        bpmPerformanceExamineFormMapper.insert(entity);
        Long id = entity.getId();

        // 维护详情信息
        List<BpmPerformanceExamineDetail> detailList = JsonUtils.covertList(param.getDetailList(), BpmPerformanceExamineDetail.class);
        detailList.forEach(detail -> detail.setPerformanceExamineFormId(id));
        bpmPerformanceExamineDetailMapper.insertBatch(detailList);

        Map<String, Object> variables = JsonUtils.covertObject2Map(param);
        List<Integer> party = detailList.stream().map(d -> d.getUserId().intValue()).collect(Collectors.toList());
        variables.put("party", party);

        // 更新流程信息
        BpmProcessInstanceCreateReqDTO processInstance = new BpmProcessInstanceCreateReqDTO(
                PROCESS_KEY, variables, id.toString());
        String processInstanceId = processInstanceApi.createProcessInstance(userId, processInstance);
        entity.setProcessInstanceId(processInstanceId);
        bpmPerformanceExamineFormMapper.updateById(entity);
        return id;
    }

    @Override
    public void updateResult(Long id, Integer result) {
        log.info("更新绩效考评申请单流程执行结果，id:{}, result:{}", id, result);
        BpmPerformanceExamineForm applyForm = bpmPerformanceExamineFormMapper.selectById(id);
        ExceptionThrowUtils.throwIfNull(applyForm, SUPPLY_NOT_EXISTS_ERROR);
        bpmPerformanceExamineFormMapper.updateById(new BpmPerformanceExamineForm().setId(id).setResult(result));
    }

    @Override
    public PageResult<PerformanceExamineFormVo> queryPageByParam(PageParam pageParam) {
        log.info("分页查询绩效考评申请单, pageParam:{}", pageParam);
        PageResult<BpmPerformanceExamineForm> page = bpmPerformanceExamineFormMapper.selectPage(pageParam);
        if (page.getList().isEmpty()) {
            return PageResult.empty();
        }
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(page
                .getList()
                .stream()
                .map(form -> Long.valueOf(form.getCreator()))
                .collect(Collectors.toSet()));
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(page
                .getList()
                .stream()
                .map(BpmPerformanceExamineForm::getDeptId)
                .collect(Collectors.toSet()));
        List<PerformanceExamineFormVo> result = new LinkedList<>();
        page.getList().forEach(form -> {
            DeptRespDTO dept = deptMap.get(form.getDeptId());
            if (Objects.isNull(dept)) {
                return;
            }
            AdminUserRespDTO creator = userMap.get(Long.valueOf(form.getCreator()));
            if (Objects.isNull(creator)) {
                return;
            }

            PerformanceExamineFormVo formVo = JsonUtils.covertObject(form, PerformanceExamineFormVo.class);
            formVo.setDeptName(dept.getName());
            formVo.setCreateTime(TimeUtils.formatAsDateTime(form.getCreateTime()));
            formVo.setCreatorName(creator.getNickname());
            result.add(formVo);
        });
        return new PageResult<>(result, page.getTotal());
    }

    @Override
    public PerformanceExamineFormVo queryDetail(Long id) {
        log.info("查询绩效考评申请单详情，id:{}", id);
        BpmPerformanceExamineForm applyForm = bpmPerformanceExamineFormMapper.selectById(id);
        ExceptionThrowUtils.throwIfNull(applyForm, SUPPLY_NOT_EXISTS_ERROR);
        List<BpmPerformanceExamineDetail> detailList = bpmPerformanceExamineDetailMapper.findByFormId(id);

        Set<Long> deptIds = new HashSet<>();
        deptIds.add(applyForm.getDeptId());
        deptIds.addAll(detailList
                .stream()
                .map(BpmPerformanceExamineDetail::getDeptId)
                .collect(Collectors.toSet()));
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(deptIds);
        Set<Long> userIds = new HashSet<>();
        userIds.add(Long.valueOf(applyForm.getCreator()));
        userIds.addAll(detailList
                .stream()
                .map(BpmPerformanceExamineDetail::getUserId)
                .collect(Collectors.toSet()));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);
        Map<String, Map<String, String>> dictTypeMap = systemDictDataWrapper.getDictTypeMap(PERFORMANCE_DICT_TYPES);
        Map<String, String> applicableMap = dictTypeMap.getOrDefault("performance_applicable_file_type", Collections.emptyMap());
        Map<String, String> situationTypeMap = dictTypeMap.getOrDefault("performance_examine_situation_type", Collections.emptyMap());

        PerformanceExamineFormVo result = JsonUtils.covertObject(applyForm, PerformanceExamineFormVo.class);
        DeptRespDTO formDept = deptMap.get(applyForm.getDeptId());
        if (Objects.nonNull(formDept)) {
            result.setDeptName(formDept.getName());
        }
        AdminUserRespDTO creator = userMap.get(Long.valueOf(applyForm.getCreator()));
        if (Objects.nonNull(creator)) {
            result.setCreatorName(creator.getNickname());
        }
        result.setCreateTime(TimeUtils.formatAsDateTime(applyForm.getCreateTime()));

        List<PerformanceExamineDetailVo> detailVos = new LinkedList<>();
        detailList.forEach(detail -> {
            AdminUserRespDTO user = userMap.get(detail.getUserId());
            if (Objects.isNull(user)) {
                return;
            }
            DeptRespDTO dept = deptMap.get(detail.getDeptId());
            if (Objects.isNull(dept)) {
                return;
            }

            PerformanceExamineDetailVo detailVo = JsonUtils.covertObject(detail, PerformanceExamineDetailVo.class);
            detailVo.setName(user.getNickname());
            detailVo.setDeptName(dept.getName());
            detailVo.setApplicableFile(applicableMap.getOrDefault(detail.getApplicableFile().toString(), ""));
            detailVo.setExamineSituation(situationTypeMap.getOrDefault(detail.getExamineSituation().toString(), ""));
            detailVos.add(detailVo);
        });
        result.setDetailVoList(detailVos);
        return result;
    }

}
