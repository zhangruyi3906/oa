package com.lh.oa.module.system.service.salary;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.ServiceException;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryCreateVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryExportExcelVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryExportVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryImportExcelVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryMonthSimpleVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryPageReqVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryResVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryYearResVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserAndInformation;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.salary.SalaryDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.mysql.dept.DeptMapper;
import com.lh.oa.module.system.dal.mysql.salary.SalaryMapper;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.service.user.AdminUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.reflections.Reflections.log;

@Service
public class SalaryServiceImpl implements SalaryService {
    @Resource
    private SalaryMapper salaryMapper;
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private DeptMapper deptMapper;


    @Override
    public PageResult<SalaryDO> getSalaryPage(SalaryPageReqVO salaryPageReqVO) {
        PageResult<SalaryDO> salaryDOPageResult = salaryMapper.selectPage(salaryPageReqVO, new LambdaQueryWrapperX<SalaryDO>()
                .likeIfPresent(SalaryDO::getUsername, salaryPageReqVO.getUsername())
                .eqIfPresent(SalaryDO::getMobile, salaryPageReqVO.getMobile())
                .eqIfPresent(SalaryDO::getMonth, salaryPageReqVO.getMonth())
                .orderByDesc(SalaryDO::getMonth));
        return salaryDOPageResult;
    }

    @Override
    public void createSalaryList(List<SalaryCreateVO> salaryCreateVOList) {
        List<SalaryDO> salaryDOList = JsonUtils.covertList(salaryCreateVOList, SalaryDO.class);
        salaryMapper.insertAll(salaryDOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SalaryImportExcelVO> importSalaryList(String month, List<SalaryImportExcelVO> importSalaryList, Boolean updateSupport) {
        log.info("导入工资条数据,month: {}，importSalaryList:{}", month, JsonUtils.toJsonString(importSalaryList));
        if (CollUtil.isEmpty(importSalaryList)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SALARY_IMPORT_LIST_IS_EMPTY);
        }
        importSalaryList = importSalaryList.stream().filter(SalaryImportExcelVO::isNotEmpty).collect(Collectors.toList());
        if (CollUtil.isEmpty(importSalaryList)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SALARY_IMPORT_LIST_IS_EMPTY);
        }


        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = inputFormat.parse(month);
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SALARY_IMPORT_MONTH_IS_ERROR);
        }
        month = outputFormat.format(date);

        List<SalaryDO> existSalaryDOList = salaryMapper.getSalaryListByMonth(month);
//        if (CollUtil.isNotEmpty(existSalaryDOList)) {
//            throw ServiceExceptionUtil.exception(ErrorCodeConstants.THIS_MONTH_SALARY_LIST_IS_EXIST);
//        }
        Map<String, SalaryDO> mobileToSalaryMap = existSalaryDOList.stream().collect(Collectors.toMap(SalaryDO::getMobile, salaryDO -> salaryDO));

        List<DeptDO> deptDOList = deptMapper.selectList();
        Map<Long, String> deptIdToDeptNameMap = deptDOList.stream().collect(Collectors.toMap(DeptDO::getId, DeptDO::getName));
        List<AdminUserDO> adminUserDOS = adminUserMapper.selectList();
        Map<String, AdminUserDO> mobileToUserMap = adminUserDOS.stream().filter(adminUserDO -> StringUtils.isNotEmpty(adminUserDO.getMobile())).collect(Collectors.toMap(AdminUserDO::getMobile, adminUserDO -> adminUserDO));
        List<SalaryImportExcelVO> errList = new LinkedList<>();
        List<SalaryCreateVO> salaryCreateVOList = new ArrayList<>();
        for (SalaryImportExcelVO salaryImportExcelVO : importSalaryList) {
            // 避免数据行为空的情况
            if (StringUtils.isBlank(salaryImportExcelVO.getUsername()) || StringUtils.isBlank(salaryImportExcelVO.getMobile())) {
                salaryImportExcelVO.setMsg("缺少必填信息");
                errList.add(salaryImportExcelVO);
                continue;
            }
            String mobile = salaryImportExcelVO.getMobile();
            boolean matches = mobile.matches("^1[3-9]\\d{9}$");
            if (!matches) {
                salaryImportExcelVO.setMsg("手机号格式不正确");
                errList.add(salaryImportExcelVO);
                continue;
            }
            //如果重复则覆盖之前数据
            SalaryDO salaryDO = mobileToSalaryMap.get(mobile);
            if (ObjectUtils.isNotEmpty(salaryDO)) {
                salaryMapper.deleteById(salaryDO);
            }

            AdminUserDO adminUserDO = mobileToUserMap.get(mobile);
            if (ObjectUtils.isEmpty(adminUserDO)) {
                salaryImportExcelVO.setMsg("手机号不存在");
                errList.add(salaryImportExcelVO);
                continue;
            }
            if (!salaryImportExcelVO.getUsername().equals(adminUserDO.getNickname())) {
                salaryImportExcelVO.setMsg("手机号与用户名不匹配");
                errList.add(salaryImportExcelVO);
                continue;
            }
            String deptName = deptIdToDeptNameMap.get(adminUserDO.getDeptId());
            if (StringUtils.isBlank(deptName)) {
                salaryImportExcelVO.setMsg("该用户部门信息有误，请到用户管理界面进行修改");
                errList.add(salaryImportExcelVO);
                continue;
            }
            if (!salaryImportExcelVO.getDeptName().equals(deptName)) {
                salaryImportExcelVO.setMsg("该用户部门信息与实际部门不符，请确认后提交");
                errList.add(salaryImportExcelVO);
                continue;
            }
            SalaryCreateVO salaryCreateVO = JsonUtils.covertObject(salaryImportExcelVO, SalaryCreateVO.class);
            if (StringUtils.isNotBlank(month)) {
                salaryCreateVO.setMonth(month);
                String[] split = month.split("-");
                salaryCreateVO.setYear(split[0]);
            }
            salaryCreateVOList.add(salaryCreateVO);
        }
        this.createSalaryList(salaryCreateVOList);

        return errList;
    }

    @Override
    public List<SalaryExportExcelVO> exportSalary(SalaryExportVO salaryExportVO) {
        log.info("导出工资条数据，salaryExportVO:{}", JsonUtils.toJsonString(salaryExportVO));
        List<SalaryDO> salaryDOS = salaryMapper.selectList(new LambdaQueryWrapperX<SalaryDO>()
                .eqIfPresent(SalaryDO::getMonth, salaryExportVO.getMonth())
                .likeIfPresent(SalaryDO::getUsername, salaryExportVO.getUsername())
                .eqIfPresent(SalaryDO::getMobile, salaryExportVO.getMobile()));
        List<SalaryExportExcelVO> salaryExportExcelVOS = JsonUtils.covertList(salaryDOS, SalaryExportExcelVO.class);
        return salaryExportExcelVOS;
    }

    @Override
    public List<SalaryResVO> getSalaryListByMobileAndMonth(Long userId, String mobile, String month) {
        log.info("根据手机号获取工资条数据，userId:{}，mobile:{}，month: {}", userId, mobile, month);
        if (ObjectUtils.isEmpty(userId) || userId == 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_LOGIN);
        }
        AdminUserDO user = adminUserService.getUser(userId);
        if (ObjectUtils.isEmpty(user)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
        }
        if (ObjectUtils.notEqual(user.getMobile(), mobile)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CANNOT_LOOK_OTHER_SALARY_LIST);
        }
        LambdaQueryWrapperX<SalaryDO> queryWrapperX = new LambdaQueryWrapperX<SalaryDO>()
                .eq(SalaryDO::getMobile, mobile)
                .eq(SalaryDO::getUsername, user.getNickname());

        if (StringUtils.isNotBlank(month) && ObjectUtils.notEqual(month, "null")) {
            queryWrapperX.eqIfPresent(SalaryDO::getMonth, month);
        }
        List<SalaryDO> salaryDOS = salaryMapper.selectList(queryWrapperX.orderByDesc(SalaryDO::getMonth));
        List<SalaryResVO> salaryResVOList = JsonUtils.covertList(salaryDOS, SalaryResVO.class);
        return salaryResVOList;
    }

    @Override
    public SalaryYearResVO getSalaryListByMobileAndYear(Long userId, String mobile, String year) {
        log.info("根据手机号获取年工资条数据，userId:{}，mobile:{}，year: {}", userId, mobile, year);
        if (ObjectUtils.isEmpty(userId) || userId == 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_LOGIN);
        }
        AdminUserDO user = adminUserService.getUser(userId);
        if (ObjectUtils.isEmpty(user)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
        }
        try {
            if (ObjectUtils.notEqual(user.getMobile(), mobile)) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.CANNOT_LOOK_OTHER_SALARY_LIST);
            }
        } catch (Exception e) {
            throw new BusinessException("无权限查看数据");
        }
        SalaryYearResVO salaryYearResVO = new SalaryYearResVO();
        //获取历史工资条
        List<SalaryDO> existSalaryDOList = salaryMapper.selectList(new LambdaQueryWrapperX<SalaryDO>()
                .eq(SalaryDO::getMobile, mobile)
                .eqIfPresent(SalaryDO::getUsername, user.getNickname())
                .orderByDesc(SalaryDO::getMonth));
        if (CollUtil.isNotEmpty(existSalaryDOList) && existSalaryDOList.size() > 0) {
            salaryYearResVO.setNewSalary(existSalaryDOList.get(0).getRealSalary());
        }
        //获取年工资
        List<SalaryDO> salaryDOS = salaryMapper.selectList(new LambdaQueryWrapperX<SalaryDO>()
                .eq(SalaryDO::getMobile, mobile)
                .eqIfPresent(SalaryDO::getYear, year)
                .eq(SalaryDO::getUsername, user.getNickname())
                .orderByDesc(SalaryDO::getMonth));

        if (CollUtil.isNotEmpty(salaryDOS) && salaryDOS.size() > 0) {
            List<String> monthList = salaryDOS.stream().map(SalaryDO::getMonth).sorted().collect(Collectors.toList());
            Map<String, BigDecimal> yearSalaryMap = salaryDOS.stream().collect(Collectors.toMap(SalaryDO::getMonth, SalaryDO::getRealSalary));
            List<BigDecimal> salaryList = new LinkedList<>();
            monthList.forEach(month -> {
                BigDecimal salary = yearSalaryMap.get(month);
                salaryList.add(salary);
            });
            salaryYearResVO.setMonthList(monthList);
            salaryYearResVO.setSalaryList(salaryList);
            BigDecimal yearSalary = salaryList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            log.info("yearSalary: {}", yearSalary);
            log.info("monthList.size(): {}", monthList.size());
            BigDecimal avgSalary = yearSalary.divide(BigDecimal.valueOf(monthList.size()), 2, RoundingMode.HALF_UP);
            salaryYearResVO.setYearSalary(yearSalary);
            salaryYearResVO.setAvgSalary(avgSalary);
        }
        return salaryYearResVO;
    }

    @Override
    public List<SalaryMonthSimpleVO> getSalaryMonthList(Long userId) {
        UserAndInformation userAndInformation = adminUserService.getUserAndInformation(userId);
        if (ObjectUtils.isEmpty(userAndInformation)) {
            throw new ServiceException(ErrorCodeConstants.USER_NOT_EXISTS);
        }
        LocalDateTime hireDate = userAndInformation.getHireDate();
        if (ObjectUtils.isEmpty(hireDate)) {
            throw new ServiceException(ErrorCodeConstants.USER_HIRE_DATE_NOT_EXIST);
        }
        LocalDateTime oneMonthBefore = hireDate.minusMonths(1);
        String beforeMonth = DateUtil.format(oneMonthBefore, "yyyy-MM");
        List<SalaryMonthSimpleVO> monthList = new ArrayList<>();
        this.getMonthBetween(beforeMonth, monthList);

        return monthList;
    }

    @Override
    public void deleteByMonth(String month) {
        salaryMapper.delete(new LambdaQueryWrapperX<SalaryDO>().eq(SalaryDO::getMonth, month));
    }

    private static List<SalaryMonthSimpleVO> getMonthBetween(String beforeMonth, List<SalaryMonthSimpleVO> monthList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); // 格式化为年月
        String nowDate = DateUtil.format(TimeUtils.now().toLocalDateTime(), "yyyy-MM");
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(beforeMonth));
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        try {
            max.setTime(sdf.parse(nowDate));
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        Calendar curr = min;
        while (curr.before(max)) {
            SalaryMonthSimpleVO salaryMonthSimpleVO = new SalaryMonthSimpleVO();
            salaryMonthSimpleVO.setValue(sdf.format(curr.getTime()));
            salaryMonthSimpleVO.setLabel(sdf.format(curr.getTime()) + "月薪资");
            monthList.add(salaryMonthSimpleVO);
            curr.add(Calendar.MONTH, 1);
        }
        return monthList;
    }

}
