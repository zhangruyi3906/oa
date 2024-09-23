package com.lh.oa.module.system.service.worklog;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogCreateReqVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogExportReqVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogMonthTotalVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogPageReqVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogPersonVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogUpdateReqVO;
import com.lh.oa.module.system.convert.worklog.WorkLogConvert;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.dataobject.worklog.WorkLogDO;
import com.lh.oa.module.system.dal.mysql.dept.DeptMapper;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.dal.mysql.worklog.WorkLogMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.service.dept.DeptService;
import com.lh.oa.module.system.util.roleScope.RoleScopeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

/**
 * 员工工作日志 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
@Slf4j
public class WorkLogServiceImpl implements WorkLogService {
    @Value(value = "${work-log.editable.days}")
    public Long editableDays;
    @Resource
    private WorkLogMapper workLogMapper;

    @Resource
    private AdminUserMapper userMapper;

    @Resource
    private RoleScopeUtils utils;

    @Resource
    private DeptService deptService;
    @Resource
    private DeptMapper deptMapper;

    @Override
    public Long createWorkLog(Long userId, WorkLogCreateReqVO createReqVO) {
        long time = createReqVO.getLogDate().getTime();
        Date start = Date.from(LocalDateTime.ofInstant(Instant.ofEpochSecond(time / 1000), ZoneId.systemDefault()).with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
        List<WorkLogDO> workLogDO = workLogMapper.selectList(new LambdaQueryWrapperX<WorkLogDO>().eqIfPresent(WorkLogDO::getUserId, userId).eqIfPresent(WorkLogDO::getLogDate, start));
        if (!CollUtil.isEmpty(workLogDO)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WORK_LOG_IS_EXISTS);
        }
        AdminUserDO adminUserDO = userMapper.selectOne(new LambdaQueryWrapperX<AdminUserDO>().eq(AdminUserDO::getId, userId));
        WorkLogDO workLog = WorkLogConvert.INSTANCE.convert(createReqVO).setUserId(userId).setUserName(adminUserDO.getNickname()).setDeptId(adminUserDO.getDeptId());
        workLogMapper.insert(workLog);
        return workLog.getId();
    }

    @Override
    public void updateWorkLog(WorkLogUpdateReqVO updateReqVO) {
        WorkLogDO workLogDO = workLogMapper.selectById(updateReqVO.getId());
        if (workLogDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WORK_LOG_NOT_EXISTS);
        }
        updateReqVO.setSubmitTime(DateUtil.date().toString());
        WorkLogDO updateObj = WorkLogConvert.INSTANCE.convert(updateReqVO);
        workLogMapper.updateById(updateObj);
    }
    @Override
    public void deleteWorkLog(Long id) {
        validateWorkLogExists(id);
        workLogMapper.deleteById(id);
    }

    private void validateWorkLogExists(Long id) {
        if (workLogMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WORK_LOG_NOT_EXISTS);
        }
    }

    @Override
    public WorkLogDO getWorkLog(Long id) {
        return workLogMapper.selectById(id);
    }

    @Override
    public List<WorkLogDO> getWorkLogList(Collection<Long> ids) {
        return workLogMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<WorkLogDO> getWorkLogPage(WorkLogPageReqVO pageReqVO) {
        //获取登录人数据权限中的部门ids
        Set<Long> deptIdSet = new HashSet<>(utils.isPage(getLoginUserId()));
        if (pageReqVO.getDeptId() != null) {
            List<Long> deptIds = deptService.getDeptAndAllChildDeptList(pageReqVO.getDeptId());
            deptIds.retainAll(deptIdSet);
            if (ObjectUtils.isEmpty(deptIds)) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.WORK_LOG_is_ROLE);
            }
            Set<Long> realDeptSet = deptIds.stream().collect(Collectors.toSet());
            PageResult<WorkLogDO> workLogDOPageResult = workLogMapper.selectPage(realDeptSet, pageReqVO);
            assignmentEditAble(workLogDOPageResult);
            return workLogDOPageResult;
        }
        if (deptIdSet.isEmpty()) {
            if (pageReqVO.getUserId() != null && !getLoginUserId().equals(pageReqVO.getUserId())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.WORK_LOG_is_ROLE);
            }
            pageReqVO.setUserId(getLoginUserId());
        }
        PageResult<WorkLogDO> workLogDOPageResult = workLogMapper.selectPage(deptIdSet, pageReqVO);
        assignmentEditAble(workLogDOPageResult);
        return workLogDOPageResult;
    }

    private void assignmentEditAble(PageResult<WorkLogDO> workLogDOPageResult) {
        if (workLogDOPageResult.getTotal() > 0) {
            List<WorkLogDO> workLogDOList = workLogDOPageResult.getList();
            workLogDOList.stream().filter(workLogDO -> this.isBeforeEightDays(workLogDO.getLogDate())).forEach(workLogDO -> workLogDO.setIsEditable(true));
        }
    }
    public Boolean isBeforeEightDays(Date logDate) {
        LocalDate logLocalDate = logDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return LocalDate.now().isBefore(logLocalDate.plusDays(editableDays));
    }

    @Override
    public List<WorkLogDO> getWorkLogList(WorkLogExportReqVO exportReqVO) {
        return workLogMapper.selectList(exportReqVO);
    }

    @Override
    public Long repairCreateWorkLog(Long userId, WorkLogCreateReqVO createReqVO) {
        long time = createReqVO.getLogDate().getTime();
        Date start = Date.from(LocalDateTime.ofInstant(Instant.ofEpochSecond(time / 1000), ZoneId.systemDefault()).with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
        List<WorkLogDO> workLogDO = workLogMapper.selectList(new LambdaQueryWrapperX<WorkLogDO>().eqIfPresent(WorkLogDO::getUserId, userId).eqIfPresent(WorkLogDO::getLogDate, start));
        if (!CollUtil.isEmpty(workLogDO)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WORK_LOG_IS_EXISTS);
        }
        //判断是否与当前时间相差两天内
        boolean after = createReqVO.getLogDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(8).isAfter(LocalDateTime.now().toLocalDate());
        if (!after) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WORK_LOG_NOT_REPAIR);
        }

        AdminUserDO adminUserDO = userMapper.selectOne(new LambdaQueryWrapperX<AdminUserDO>().eq(AdminUserDO::getId, userId));
        WorkLogDO workLog = WorkLogConvert.INSTANCE.convert(createReqVO).setUserId(userId).setUserName(adminUserDO.getNickname()).setDeptId(adminUserDO.getDeptId());
        workLogMapper.insert(workLog);
        return workLog.getId();
    }
    @Override
    public List<Map<String, Object>> getWorkLogMonthTotalList(String month, Long deptId, Long workLogUserId) throws java.text.ParseException {
        Long loginUserId = getLoginUserId();
        log.info("微博统计 month:{}, deptId: {}, workLogUserId: {}, loginUserId: {}", month, deptId, workLogUserId, loginUserId);
        //登录人数据权限中的部门ids
        Set<Long> deptIdSet = new HashSet<>(utils.isPage(loginUserId));
        if (!deptIdSet.contains(deptId)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WORK_LOG_is_ROLE);
        }
        List<Integer> daysOfMonth = new ArrayList<>();
        List<Date> everyDayOfMonth = new LinkedList<>();
        getDaysOfMonth(month, daysOfMonth, everyDayOfMonth);

        List<WorkLogMonthTotalVO> workLogMonthTotalVOList = new ArrayList<>();
        WorkLogMonthTotalVO workLogMonthTotalVO = new WorkLogMonthTotalVO();
        workLogMonthTotalVO.setDateList(daysOfMonth);
//        if (ObjectUtils.isEmpty(deptId)) {
//            deptId = userMapper.selectById(loginUserId).getDeptId();;
//        }
        if (ObjectUtils.isNotEmpty(workLogUserId)) {
            AdminUserDO adminUserDO = userMapper.selectById(workLogUserId);
            if (ObjectUtils.isEmpty(adminUserDO)) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
            }
            deptId = adminUserDO.getDeptId();
        }
        List<Long> deptAndAllChildDeptList = deptService.getDeptAndAllChildDeptList(deptId);
        if (CollUtil.isEmpty(deptAndAllChildDeptList)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_FOUND);
        }
        //初始赋值
        DeptDO dept = deptService.getDept(deptId);

        WorkLogPersonVO workLogPersonVOFirst = new WorkLogPersonVO();
        Map<String, Object> firstValueMap = new LinkedHashMap<>();
        firstValueMap.put("name", dept.getName());
        workLogPersonVOFirst.setName(dept.getName());
        List<WorkLogPersonVO> workLogPersonVOList = new ArrayList<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        resultList.add(firstValueMap);
        workLogPersonVOList.add(workLogPersonVOFirst);
        List<AdminUserDO> adminUserDOS = userMapper.selectList(new LambdaQueryWrapperX<AdminUserDO>().in(AdminUserDO::getDeptId, deptAndAllChildDeptList));
        Set<Long> userIdSet = adminUserDOS.stream().map(AdminUserDO::getId).collect(Collectors.toSet());
        if (ObjectUtils.isNotEmpty(workLogUserId)) {
            userIdSet = new HashSet<>();
            userIdSet.add(workLogUserId);
        }

        if (CollUtil.isEmpty(userIdSet)) {

            workLogMonthTotalVO.setPersonList(workLogPersonVOList);
            workLogMonthTotalVOList.add(workLogMonthTotalVO);
//            return workLogMonthTotalVOList;
            return resultList;
        }

        List<WorkLogDO> workLogDOS = workLogMapper.selectList(new LambdaQueryWrapperX<WorkLogDO>()
                .likeIfPresent(WorkLogDO::getLogDate, month)
                .in(WorkLogDO::getUserId, userIdSet)
                .orderByAsc(WorkLogDO::getUserId)
                .orderByDesc(WorkLogDO::getLogDate));
        //过滤重复脏数据
        //人-对应日志
        Map<Long, List<WorkLogDO>> workLogMap = workLogDOS.stream()
                .collect(Collectors.groupingBy(WorkLogDO::getUserId,
                        Collectors.collectingAndThen(Collectors.toCollection(() ->
                                        new TreeSet<>(Comparator.comparing(WorkLogDO::getLogDate))),
                                ArrayList::new)
                ));
        if (MapUtils.isEmpty(workLogMap)) {
            workLogMonthTotalVOList.add(workLogMonthTotalVO);
//            return workLogMonthTotalVOList;
            return resultList;
        }
        //无脏数据的所有日志
        List<WorkLogDO> allWorkLogs = new ArrayList<>();
        for (List<WorkLogDO> workLogList : workLogMap.values()) {
            allWorkLogs.addAll(workLogList);
        }
        //部门每天的微博
        Map<Date, List<WorkLogDO>> dateListMap = allWorkLogs.stream().collect(Collectors.groupingBy(WorkLogDO::getLogDate, Collectors.toList()));
        Map<Date, Integer> dayTotalMap = new HashMap<>();
        Map<Date, Integer> dayNoSubmitMap = new HashMap<>();
        dateListMap.forEach((date, workLogList) -> {
            List<WorkLogDO> dayNoSubmitList = workLogList.stream().filter(item -> ObjectUtils.isEmpty(item.getLogContent())).collect(Collectors.toList());
            dayTotalMap.put(date, workLogList.size());
            dayNoSubmitMap.put(date, dayNoSubmitList.size());
        });
        //用户每日已填的微博
        Map<Long, Map<Date, WorkLogDO>> submitMapForUserIdMap = allWorkLogs.stream()
                .filter(item -> ObjectUtils.isNotEmpty(item.getLogContent()))
                .collect(Collectors.groupingBy(WorkLogDO::getUserId, Collectors.toMap(WorkLogDO::getLogDate, item -> item)));

        List<AdminUserDO> adminUserDOList = userMapper.selectList();
        Map<Long, String> userIdToNicknameMap = adminUserDOList.stream().collect(Collectors.toMap(AdminUserDO::getId, AdminUserDO::getNickname));
        List<Integer> emptyList = new ArrayList<>();
        List<Integer> fullList = new ArrayList<>();
        LinkedHashMap<String, Object> emptyMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> fullMap = new LinkedHashMap<>();
        for (int i = 1; i <= daysOfMonth.size(); i++) {
            emptyList.add(0);
            emptyMap.put("day" + i, 0);
        }
        for (int i = 1; i <= daysOfMonth.size(); i++) {
            fullList.add(1);
            fullMap.put("day" + i, 1);
        }
        userIdSet.forEach(userId -> {
            WorkLogPersonVO workLogPersonVO = new WorkLogPersonVO();
            Map<String, Object> valueMap = new LinkedHashMap<>();
            valueMap.put("name", userIdToNicknameMap.get(userId));
            workLogPersonVO.setName(userIdToNicknameMap.get(userId));
            workLogPersonVO.setTotalList(fullList);
            Map<Date, WorkLogDO> dateSubmitWorkLogDOMap = submitMapForUserIdMap.get(userId);
            workLogPersonVO.setTotal(daysOfMonth.size());
            if (MapUtils.isEmpty(dateSubmitWorkLogDOMap)) {
                workLogPersonVO.setDataList(emptyList);
                valueMap.putAll(emptyMap);
                valueMap.put("total", daysOfMonth.size());
                valueMap.put("totalList", fullList);
                resultList.add(valueMap);
                workLogPersonVOList.add(workLogPersonVO);
                return;
            }
            List<Integer> dataList = new ArrayList<>();
            int i = 1;
            for (Date date : everyDayOfMonth) {
                WorkLogDO workLogDO = dateSubmitWorkLogDOMap.get(date);
                if (ObjectUtils.isEmpty(workLogDO)) {
                    dataList.add(0);
                    valueMap.put("day" + i, 0);
                } else {
                    valueMap.put("day" + i, 1);
                    dataList.add(1);
                }
                i++;
            }
//            everyDayOfMonth.forEach(date -> {
//                WorkLogDO workLogDO = dateSubmitWorkLogDOMap.get(date);
//                if (ObjectUtils.isEmpty(workLogDO)) {
//
//                    dataList.add(0);
//                } else {
//                    dataList.add(1);
//                }
//            });

            valueMap.put("total", daysOfMonth.size());
            valueMap.put("totalList", fullList);
            resultList.add(valueMap);

            workLogPersonVO.setDataList(dataList);
            workLogPersonVOList.add(workLogPersonVO);
        });

        assignmentToFirstPersonData(daysOfMonth, workLogPersonVOFirst, workLogPersonVOList);
        Map<String, Object> firstMap = resultList.get(0);
        WorkLogPersonVO workLogPersonVO = workLogPersonVOList.get(0);
        List<Integer> dataList = workLogPersonVO.getDataList();
        for (int i = 1; i <= dataList.size(); i++) {
            firstMap.put("day" + i, dataList.get(i - 1));
        }
        resultList.set(0, firstMap);
        workLogMonthTotalVO.setPersonList(workLogPersonVOList);
        workLogMonthTotalVOList.add(workLogMonthTotalVO);
//        return workLogMonthTotalVOList;
        return resultList;
    }


    private static void assignmentToFirstPersonData(List<Integer> daysOfMonth, WorkLogPersonVO workLogPersonVOFirst, List<WorkLogPersonVO> workLogPersonVOList) {
        List<Integer> dataFirstList = new ArrayList<>();
        List<Integer> totalFirstList = new ArrayList<>();
        List<WorkLogPersonVO> realWorkLogPersonVOList = workLogPersonVOList.subList(1, workLogPersonVOList.size());
        List<List<Integer>> noSubmitList = realWorkLogPersonVOList.stream().map(WorkLogPersonVO::getDataList).collect(Collectors.toList());
        for (int i = 0; i < daysOfMonth.size(); i++) {
            int finalI = i;
            AtomicInteger noSubmitTimes = new AtomicInteger();
            AtomicInteger totalTimes = new AtomicInteger();
            noSubmitList.forEach(list -> {
                Integer noSubmit = 1;
                if (ObjectUtils.isNotEmpty(list.get(finalI)) && list.get(finalI) == 1) {
                    noSubmit = 0;
                }
                noSubmitTimes.addAndGet(noSubmit);
                totalTimes.addAndGet(1);
            });
            dataFirstList.add(noSubmitTimes.get());
            totalFirstList.add(totalTimes.get());
        }
        workLogPersonVOFirst.setTotalList(totalFirstList);
        workLogPersonVOFirst.setDataList(dataFirstList);
        workLogPersonVOList.set(0, workLogPersonVOFirst);
    }

    private static void getDaysOfMonth(String month, List<Integer> daysOfMonth, List<Date> everyDayOfMonth) throws ParseException {
        log.info("month:{}", month);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Date everyDayToMonth = new Date();
        Date date = formatter.parse(month);
        LocalDate startDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate endDate = startDate.plusMonths(1); // 下个月的第一天
        int daysInMonth = (int) ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 1; i <= daysInMonth; i++) {
            daysOfMonth.add(i);
            if (i != 1) {
                startDate = startDate.plusDays(1);
            }
            everyDayToMonth = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            everyDayOfMonth.add(everyDayToMonth);
        }
        log.info("daysOfMonth:{}, everyDayOfMonth", daysOfMonth, everyDayOfMonth);
    }

}
