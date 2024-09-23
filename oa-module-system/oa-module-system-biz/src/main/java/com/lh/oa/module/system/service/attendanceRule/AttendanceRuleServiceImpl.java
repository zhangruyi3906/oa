package com.lh.oa.module.system.service.attendanceRule;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.*;
import com.lh.oa.module.system.convert.attendanceRule.AttendanceRuleConvert;
import com.lh.oa.module.system.dal.dataobject.attendanceRule.AttendanceRuleDO;
import com.lh.oa.module.system.dal.mysql.attendanceRule.AttendanceRuleMapper;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.*;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 *
 */
@Service
@Validated
public class AttendanceRuleServiceImpl implements AttendanceRuleService {

    @Resource
    private AttendanceRuleMapper attendanceRuleMapper;



    @Override
    public Long createAttendanceRule(DeptRuleListVO createReqVO) {
        List<AttendanceRuleDO> list = attendanceRuleMapper.selectList(new LambdaQueryWrapperX<AttendanceRuleDO>().eq(AttendanceRuleDO::getDeptId, createReqVO.getDeptId()));
        if (CollUtil.isNotEmpty(list)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ATTENDANCE_RULE_IS_EXISTS);
        }
        List<AttendanceRuleDO> exchange = exchange(createReqVO);
        attendanceRuleMapper.insertBatch(exchange);
        return (long) exchange.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttendanceRule(DeptRuleListVO updateReqVO) {
        validateAttendanceRuleExists(updateReqVO.getDeptId());
        List<AttendanceRuleDO> exchange = exchange(updateReqVO);
        attendanceRuleMapper.delete(new LambdaQueryWrapperX<AttendanceRuleDO>().eq(AttendanceRuleDO::getDeptId,updateReqVO.getDeptId()));
        attendanceRuleMapper.insertBatch(exchange);
    }

    @Override
    public void deleteAttendanceRule(Long id) {
        validateAttendanceRuleExists(id);
        attendanceRuleMapper.delete(new LambdaQueryWrapperX<AttendanceRuleDO>().eq(AttendanceRuleDO::getDeptId,id));
    }

    private void validateAttendanceRuleExists(Long id) {
        List<AttendanceRuleDO> list = attendanceRuleMapper.selectList(new LambdaQueryWrapperX<AttendanceRuleDO>().eq(AttendanceRuleDO::getDeptId, id));
        if (list == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ATTENDANCE_RULE_NOT_EXISTS);
        }
    }

    @Override
    public DeptRuleListVO getAttendanceRule(Long id) {
        ArrayList<DeptRuleVO> listVOS = new ArrayList<>();
        List<AttendanceRuleDO> list = attendanceRuleMapper.selectList(new LambdaQueryWrapperX<AttendanceRuleDO>().eq(AttendanceRuleDO::getDeptId, id));
        List<AttendanceRuleRespVO> vos = AttendanceRuleConvert.INSTANCE.convertList(list);
        vos.forEach(v ->{
            DeptRuleVO ruleVO = new DeptRuleVO();
            ruleVO.setDescription(v.getDescription());
            ruleVO.setLatiLong(v.getLatiLong());
            ruleVO.setPunchRadius(v.getPunchRadius());
            listVOS.add(ruleVO);
        });
        DeptRuleListVO deptRuleListVO = new DeptRuleListVO();
        deptRuleListVO.setList(listVOS);
        deptRuleListVO.setDeptId(id);
        deptRuleListVO.setDeptName(list.get(0).getDeptName());
        deptRuleListVO.setSyncHolidays(list.get(0).getSyncHolidays());
        deptRuleListVO.setWorkDays(list.get(0).getWorkDays());
        deptRuleListVO.setPunchTypeName(list.get(0).getPunchTypeName());
        deptRuleListVO.setFlexibleCheckInEnd(list.get(0).getFlexibleCheckInEnd());
        deptRuleListVO.setFlexibleCheckInStart(list.get(0).getFlexibleCheckInStart());
        return deptRuleListVO;
    }

    @Override
    public List<AttendanceRuleDO> getAttendanceRuleList(Collection<Long> ids) {
        return attendanceRuleMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DeptRuleListVO> getAttendanceRulePage(DeptRulePageBaseVO pageReqVO) {
        AttendanceRuleExportReqVO reqVO = new AttendanceRuleExportReqVO();
        reqVO.setDepartmentId(pageReqVO.getDepartmentId());
        List<AttendanceRuleDO> ruleDOS = attendanceRuleMapper.selectList(reqVO);
        List<AttendanceRuleRespVO> list = AttendanceRuleConvert.INSTANCE.convertList(ruleDOS);
        Map<Long, List<AttendanceRuleRespVO>> collect = list.stream()
                .collect(Collectors.groupingBy(AttendanceRuleRespVO::getDeptId));

        List<DeptRuleListVO> baseVOS = new ArrayList<>();
        for (Map.Entry<Long, List<AttendanceRuleRespVO>> entry : collect.entrySet()) {
            Long key = entry.getKey();
            List<AttendanceRuleRespVO> value = entry.getValue();

            DeptRuleListVO deptRuleListVO = new DeptRuleListVO();
            deptRuleListVO.setDeptId(key);
            deptRuleListVO.setDeptName(value.get(0).getDeptName());
            deptRuleListVO.setSyncHolidays(list.get(0).getSyncHolidays());
            deptRuleListVO.setWorkDays(list.get(0).getWorkDays());
            deptRuleListVO.setPunchTypeName(list.get(0).getPunchTypeName());
            deptRuleListVO.setFlexibleCheckInEnd(list.get(0).getFlexibleCheckInEnd());
            deptRuleListVO.setFlexibleCheckInStart(list.get(0).getFlexibleCheckInStart());
            List<DeptRuleVO> ruleVOS = new ArrayList<>();
            for (AttendanceRuleRespVO n : value) {
                DeptRuleVO ruleVO = new DeptRuleVO();
                ruleVO.setPunchRadius(n.getPunchRadius());
                ruleVO.setDescription(n.getDescription());
                ruleVO.setLatiLong(n.getLatiLong());
                ruleVOS.add(ruleVO);
            }

            deptRuleListVO.setList(ruleVOS);
            baseVOS.add(deptRuleListVO);
        }

        int fromIndex = (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize();
        int toIndex = Math.min(fromIndex + pageReqVO.getPageSize(), baseVOS.size());
        List<DeptRuleListVO> vos = baseVOS.subList(fromIndex, toIndex);
        return new PageResult<>(vos, (long) baseVOS.size());
    }

    @Override
    public List<AttendanceRuleDO> getAttendanceRuleList(AttendanceRuleExportReqVO exportReqVO) {
        return attendanceRuleMapper.selectList(exportReqVO);
    }

    private List<AttendanceRuleDO> exchange(DeptRuleListVO createReqVO){
        List<AttendanceRuleDO> list = new ArrayList<>();
        createReqVO.getList().forEach(s ->{
            AttendanceRuleDO attendanceRuleDO = new AttendanceRuleDO();
            attendanceRuleDO.setDeptId(createReqVO.getDeptId());
            attendanceRuleDO.setDeptName(createReqVO.getDeptName());
            attendanceRuleDO.setWorkDays(createReqVO.getWorkDays());
            attendanceRuleDO.setSyncHolidays(createReqVO.getSyncHolidays());
            attendanceRuleDO.setPunchTypeName(createReqVO.getPunchTypeName());
            attendanceRuleDO.setLatiLong(s.getLatiLong());
            attendanceRuleDO.setFlexibleCheckInEnd(createReqVO.getFlexibleCheckInEnd());
            attendanceRuleDO.setFlexibleCheckInStart(createReqVO.getFlexibleCheckInStart());
            attendanceRuleDO.setPunchRadius(s.getPunchRadius());
            attendanceRuleDO.setDescription(s.getDescription());
            list.add(attendanceRuleDO);
        });
        return list;
    }

}
