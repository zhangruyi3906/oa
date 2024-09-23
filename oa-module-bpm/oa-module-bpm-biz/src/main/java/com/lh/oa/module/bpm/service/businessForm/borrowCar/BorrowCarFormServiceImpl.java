package com.lh.oa.module.bpm.service.businessForm.borrowCar;

import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.BeanCopyUtils;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceApi;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.param.BorrowCarFormParam;
import com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.param.BpmBorrowCarFormDetailParam;
import com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.vo.BorrowCarFormDetailVO;
import com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.vo.BorrowCarFormVo;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.borrowCar.BpmBorrowCarForm;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.borrowCar.BpmBorrowCarFormDetail;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.borrowCar.BpmBorrowCarSubsidy;
import com.lh.oa.module.bpm.dal.mysql.businessForm.borrowForm.BpmBorrowCarFormDetailMapper;
import com.lh.oa.module.bpm.dal.mysql.businessForm.borrowForm.BpmBorrowCarFormMapper;
import com.lh.oa.module.bpm.dal.mysql.businessForm.borrowForm.BpmBorrowCarSubsidyMapper;
import com.lh.oa.module.bpm.enums.ErrorCodeConstants;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.wrapper.SystemDictDataWrapper;
import com.lh.oa.module.system.api.dept.DeptApi;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import liquibase.pro.packaged.B;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tanghanlin
 * @since 2023/10/21
 */
@Service
@Slf4j
public class BorrowCarFormServiceImpl implements BorrowCarFormService {

    /**
     * 用车申请单流程标识，流程模型管理里的流程标识字段就是
     */
    public static final String PROCESS_KEY = "borrow-car-form";

    public static final String BORROW_DICT_TYPES = "sub_company,sub_area,borrow_car_type,car_displacement_type";

    @Resource
    private BpmBorrowCarFormMapper bpmBorrowCarFormMapper;

    @Resource
    private BpmBorrowCarFormDetailMapper bpmBorrowCarFormDetailMapper;

    @Resource
    private BpmBorrowCarSubsidyMapper bpmBorrowCarSubsidyMapper;

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
    public Long create(Long userId, BorrowCarFormParam param) {
        log.info("创建用车申请单，userId:{}, param:{}", userId, param);
        // 插入实体，获取id
        BpmBorrowCarForm entity = JsonUtils.covertObject(param, BpmBorrowCarForm.class);
        entity.setStartTime(TimeUtils.parseAsDateTime(param.getStartTimeStr()));
        entity.setEndTime(TimeUtils.parseAsDateTime(param.getEndTimeStr()));
        entity.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        bpmBorrowCarFormMapper.insert(entity);
        Long id = entity.getId();

        //保存用车明细表
        if (null != param.getDetailList()) {
            List<BpmBorrowCarFormDetailParam> paramList = param.getDetailList();
            List<BpmBorrowCarFormDetail> detailList = new ArrayList<>();
            for (BpmBorrowCarFormDetailParam vo : paramList) {
                BpmBorrowCarFormDetail detail = new BpmBorrowCarFormDetail();
                BeanCopyUtils.copy(vo, detail);
                detail.setBorrowCarId(id);
                detail.setBorrowCarDate(TimeUtils.parseAsDateTime(vo.getBorrowCarDate()));
                detailList.add(detail);
            }
            bpmBorrowCarFormDetailMapper.insertBatch(detailList);
        }

        // 更新流程信息
        BpmProcessInstanceCreateReqDTO processInstance = new BpmProcessInstanceCreateReqDTO(
                PROCESS_KEY, JsonUtils.covertObject2Map(param), id.toString());
        String processInstanceId = processInstanceApi.createProcessInstance(userId, processInstance);
        entity.setProcessInstanceId(processInstanceId);
        bpmBorrowCarFormMapper.updateById(entity);
        return id;
    }

    @Override
    public void updateResult(Long id, Integer result) {
        log.info("更新用车申请单流程执行结果，id:{}, result:{}", id, result);
        BpmBorrowCarForm applyForm = bpmBorrowCarFormMapper.selectById(id);
        ExceptionThrowUtils.throwIfNull(applyForm, ErrorCodeConstants.SUPPLY_NOT_EXISTS_ERROR);
        bpmBorrowCarFormMapper.updateById(new BpmBorrowCarForm().setId(id).setResult(result));
    }

    @Override
    public PageResult<BorrowCarFormVo> queryPageByParam(PageParam pageParam) {
        log.info("分页查询用车申请单，pageParam:{}", pageParam);
        PageResult<BpmBorrowCarForm> page = bpmBorrowCarFormMapper.selectPage(pageParam);
        if (page.getList().isEmpty()) {
            return PageResult.empty();
        }
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(page
                .getList()
                .stream()
                .map(BpmBorrowCarForm::getDeptId)
                .collect(Collectors.toSet()));
        Set<Long> userIds = new HashSet<>();
        userIds.addAll(page
                .getList()
                .stream()
                .map(BpmBorrowCarForm::getUserId)
                .collect(Collectors.toSet()));
        userIds.addAll(page
                .getList()
                .stream()
                .map(BpmBorrowCarForm::getDriverUserId)
                .collect(Collectors.toSet()));
        userIds.addAll(page
                .getList()
                .stream()
                .map(form -> Long.valueOf(form.getCreator()))
                .collect(Collectors.toSet()));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);
        Map<String, Map<String, String>> dictTypeMap = systemDictDataWrapper.getDictTypeMap(BORROW_DICT_TYPES);
        Map<String, String> companyMap = dictTypeMap.getOrDefault("sub_company", Collections.emptyMap());
        Map<String, String> areaMap = dictTypeMap.getOrDefault("sub_area", Collections.emptyMap());
        Map<String, String> carTypeMap = dictTypeMap.getOrDefault("borrow_car_type", Collections.emptyMap());
        Map<String, String> displacementTypeMap = dictTypeMap.getOrDefault("car_displacement_type", Collections.emptyMap());

        List<BorrowCarFormVo> result = new LinkedList<>();
        page.getList().forEach(form -> {
            DeptRespDTO dept = deptMap.get(form.getDeptId());
            if (Objects.isNull(dept)) {
                return;
            }
            AdminUserRespDTO user = userMap.get(form.getUserId());
            if (Objects.isNull(user)) {
                return;
            }
            AdminUserRespDTO driveUser = userMap.get(form.getDriverUserId());
            if (Objects.isNull(driveUser)) {
                return;
            }
            AdminUserRespDTO creator = userMap.get(Long.valueOf(form.getCreator()));
            if (Objects.isNull(creator)) {
                return;
            }

            BorrowCarFormVo formVo = JsonUtils.covertObject(form, BorrowCarFormVo.class);
            formVo.setUserNick(user.getNickname());
            formVo.setDeptName(dept.getName());
            formVo.setDriverUserNick(driveUser.getNickname());
            formVo.setBorrowCarType(carTypeMap.getOrDefault(form.getBorrowCarType().toString(), ""));
            formVo.setSubCompany(companyMap.getOrDefault(form.getSubCompany().toString(), ""));
            formVo.setSubArea(areaMap.getOrDefault(form.getSubArea().toString(), ""));
            formVo.setCarDisplacementType(displacementTypeMap.getOrDefault(form.getCarDisplacementType().toString(), ""));
            if (Objects.nonNull(form.getPayProject())) {
                formVo.setPayProject(companyMap.getOrDefault(form.getPayProject().toString(), ""));
            }
            formVo.setStartTime(TimeUtils.formatAsDateTime(form.getStartTime()));
            formVo.setEndTime(TimeUtils.formatAsDateTime(form.getEndTime()));
            formVo.setCreateTime(TimeUtils.formatAsDateTime(form.getCreateTime()));
            formVo.setCreatorName(creator.getNickname());
            result.add(formVo);
        });
        return new PageResult<>(result, page.getTotal());
    }

    @Override
    public BorrowCarFormVo queryDetail(Long id) {
        log.info("查询用车申请单详情，id:{}", id);
        BpmBorrowCarForm applyForm = bpmBorrowCarFormMapper.selectById(id);
        ExceptionThrowUtils.throwIfNull(applyForm, ErrorCodeConstants.SUPPLY_NOT_EXISTS_ERROR);

        DeptRespDTO dept = deptApi.getDeptById(applyForm.getDeptId());
        Set<Long> userIds = new HashSet<>();
        userIds.add(applyForm.getUserId());
        userIds.add(applyForm.getDriverUserId());
        userIds.add(Long.valueOf(applyForm.getCreator()));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);

        BorrowCarFormVo result = JsonUtils.covertObject(applyForm, BorrowCarFormVo.class);
        if (Objects.nonNull(dept)) {
            result.setDeptName(dept.getName());
        }
        AdminUserRespDTO user = userMap.get(applyForm.getUserId());
        if (Objects.nonNull(user)) {
            result.setUserNick(user.getNickname());
        }
        AdminUserRespDTO driveUser = userMap.get(applyForm.getDriverUserId());
        if (Objects.nonNull(driveUser)) {
            result.setDriverUserNick(driveUser.getNickname());
        }
        AdminUserRespDTO creator = userMap.get(Long.valueOf(applyForm.getCreator()));
        if (Objects.nonNull(creator)) {
            result.setCreatorName(creator.getNickname());
        }

        Map<String, Map<String, String>> dictTypeMap = systemDictDataWrapper.getDictTypeMap(BORROW_DICT_TYPES);
        result.setBorrowCarType(dictTypeMap
                .getOrDefault("borrow_car_type", Collections.emptyMap())
                .getOrDefault(applyForm.getBorrowCarType().toString(), ""));
        result.setSubCompany(dictTypeMap
                .getOrDefault("sub_company", Collections.emptyMap())
                .getOrDefault(applyForm.getSubCompany().toString(), ""));
        result.setSubArea(dictTypeMap
                .getOrDefault("sub_area", Collections.emptyMap())
                .getOrDefault(applyForm.getSubArea().toString(), ""));
        result.setCarDisplacementType(dictTypeMap.
                getOrDefault("car_displacement_type", Collections.emptyMap())
                .getOrDefault(applyForm.getCarDisplacementType().toString(), ""));
        result.setPayProject(dictTypeMap
                .getOrDefault("sub_company", Collections.emptyMap())
                .getOrDefault(applyForm.getPayProject().toString(), ""));
        result.setStartTime(TimeUtils.formatAsDateTime(applyForm.getStartTime()));
        result.setEndTime(TimeUtils.formatAsDateTime(applyForm.getEndTime()));
        result.setCreateTime(TimeUtils.formatAsDateTime(applyForm.getCreateTime()));
        result.setDriverLicensePhotoUrl(applyForm.getDriverLicensePhotoUrl());
        result.setReimbursementAmountTotal(applyForm.getReimbursementAmountTotal());
        result.setMileageTraveledTotal(applyForm.getMileageTraveledTotal());

        //查询用车明细
        List<BpmBorrowCarFormDetail> detailsList = bpmBorrowCarFormDetailMapper.selectList(BpmBorrowCarFormDetail::getBorrowCarId, id);
        if (null != detailsList && detailsList.size() > 0) {

            List<BorrowCarFormDetailVO> detailsVOList = new ArrayList<>();
            for (BpmBorrowCarFormDetail detail : detailsList) {
                BorrowCarFormDetailVO detailVO = new BorrowCarFormDetailVO();
                detailVO.setId(detail.getId());
                detailVO.setBorrowCarId(detail.getBorrowCarId());
                detailVO.setBorrowCarDate(TimeUtils.formatAsDate(detail.getBorrowCarDate()));
                detailVO.setBorrowCarReasons(detail.getBorrowCarReasons());
                detailVO.setAmountTotal(detail.getAmountTotal());
                detailVO.setEndMileage(detail.getEndMileage());
                detailVO.setEndPoint(detail.getEndPoint());
                detailVO.setStartMileage(detail.getStartMileage());
                detailVO.setStartPoint(detail.getStartPoint());
                detailVO.setConstructionSiteSubsidy(detail.getConstructionSiteSubsidy());
                detailVO.setTravelMileage(detail.getTravelMileage());
                detailVO.setTravelBusinessAmount(detail.getTravelBusinessAmount());
                detailVO.setPlateauAreas(detail.getPlateauAreas());
                detailVO.setOilFeeAmount(detail.getOilFeeAmount());
                detailsVOList.add(detailVO);
            }
            result.setDetailList(detailsVOList);
        }

        return result;
    }

    @Override
    public BpmBorrowCarSubsidy queryCarSubsidyDetail(Integer type) {
        return bpmBorrowCarSubsidyMapper.selectOne(BpmBorrowCarSubsidy::getDisplacementType, type);
    }

}
