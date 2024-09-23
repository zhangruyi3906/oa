package com.lh.oa.module.system.service.salarysettlement;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.*;
import com.lh.oa.module.system.convert.salarysettlement.SalarySettlementConvert;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.salarysettlement.SalarySettlementDO;
import com.lh.oa.module.system.dal.dataobject.systemDeptPunish.SystemDeptPunish;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.mysql.information.InformationMapper;
import com.lh.oa.module.system.dal.mysql.jobcommission.JobCommissionMapper;
import com.lh.oa.module.system.dal.mysql.joblevelsalary.JobLevelSalaryMapper;
import com.lh.oa.module.system.dal.mysql.monthstatistics.MonthStatisticsMapper;
import com.lh.oa.module.system.dal.mysql.salarysettlement.SalarySettlementMapper;
import com.lh.oa.module.system.dal.mysql.userProject.UserProjectMapper;
import com.lh.oa.module.system.dal.mysql.volumestatistics.VolumeStatisticsMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.full.entity.attandance.vo.AttendanceStatisticInfoVo;
import com.lh.oa.module.system.full.entity.attandance.vo.AttendanceStatisticProjectInfoVo;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRecordService;
import com.lh.oa.module.system.mapper.SystemDeptPunishMapper;
import com.lh.oa.module.system.service.information.InformationService;
import com.lh.oa.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author
 */
@Slf4j
@Service
@Validated
public class SalarySettlementServiceImpl implements SalarySettlementService {

    @Resource
    private SalarySettlementMapper salarySettlementMapper;

    @Resource
    private MonthStatisticsMapper monthStatisticsMapper;

    @Resource
    private InformationMapper informationMapper;

    @Resource
    private JobLevelSalaryMapper jobLevelSalaryMapper;

    @Resource
    private UserProjectMapper userProjectMapper;

    @Resource
    private JobCommissionMapper jobCommissionMapper;

    @Resource
    private VolumeStatisticsMapper volumeStatisticsMapper;

    @Resource
    private SysAttendanceRecordService sysAttendanceRecordService;

    @Resource
    private InformationService informationService;

    @Resource
    private SystemDeptPunishMapper systemDeptPunishMapper;

    @Resource
    private AdminUserService adminUserService;

    @Override
    public Long createSalarySettlement(MonthAttendance createReqVO) {
        List<SalarySettlementCreateReqVO> list = new ArrayList<>();
        if (createReqVO.getReType() == 0) {  //按部门
            List<InformationDO> informationDOS = informationService.selectListInDeptIdsOrUserIds(createReqVO);
            List<InformationDO> collectIn = informationDOS.stream().filter(in -> in.getIsResigned() == false).collect(Collectors.toList());

            for (InformationDO in : collectIn) {
                BigDecimal baseSalary = in.getBaseSalary();
                BigDecimal salary;
                if (in.getHasProbation()) {
                    salary = baseSalary.multiply(in.getProbationSalaryRatio());
                } else {
                    salary = baseSalary;
                }

                List<AttendanceStatisticInfoVo> attendanceStatisticPageList = sysAttendanceRecordService.getAttendanceStatisticPageList(in.getDeptId().intValue(), 0, in.getName(), createReqVO.getAttendanceMonth(), in.getUserId().intValue());
                AttendanceStatisticInfoVo attendanceStatisticInfoVo = attendanceStatisticPageList.get(0);
                //获取月考勤
                Integer attendanceNormalCount = attendanceStatisticInfoVo.getAttendanceNormalCount(); //出勤（天）
                Double attendanceLeaveCount = attendanceStatisticInfoVo.getAttendanceLeaveCount().doubleValue(); //请假（小时）
                Integer attendanceEarlyCount = attendanceStatisticInfoVo.getAttendanceEarlyCount(); //早退（次）
                Integer attendanceLateCount = attendanceStatisticInfoVo.getAttendanceLateCount(); //迟到（次）
                Integer attendanceMissCount = attendanceStatisticInfoVo.getAttendanceMissCount(); //漏签（次）
//                旷工
                //判断非空
                attendanceNormalCount = ValidatedNull(attendanceNormalCount);
                attendanceLeaveCount = attendanceLeaveCount == null ? 0.0 : attendanceLeaveCount;
                attendanceEarlyCount = ValidatedNull(attendanceEarlyCount);
                attendanceLateCount = ValidatedNull(attendanceLateCount);
                attendanceMissCount = ValidatedNull(attendanceMissCount);
                //获取部门考勤处理规则
                SystemDeptPunish deptPunish = systemDeptPunishMapper.selectOne("dept_id", in.getDeptId());
                if (deptPunish == null) {
                    throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_SALARY_NOT_EXISTS);
                }

                //单次罚款金额
                BigDecimal latePenalty = deptPunish.getLatePenalty(); //迟到罚款
//                BigDecimal excusedPenalty = deptPunish.getExcusedPenalty(); //请假罚款
                BigDecimal leaveEarlyPenalty = deptPunish.getLeaveEarlyPenalty(); //早退罚款
                BigDecimal missSignaturePenalty = deptPunish.getMissSignaturePenalty(); //漏签罚款
                //罚款金额
                BigDecimal lateReal = latePenalty.multiply(BigDecimal.valueOf(attendanceLateCount));
                BigDecimal earlyReal = leaveEarlyPenalty.multiply(BigDecimal.valueOf(attendanceEarlyCount));
                BigDecimal missReal = missSignaturePenalty.multiply(BigDecimal.valueOf(attendanceMissCount));
                //考勤扣款
                BigDecimal attendanceDeduction = lateReal.add(earlyReal).add(missReal);
                //社保缴纳基数
                BigDecimal retirementScale = deptPunish.getRetirementScale(); //养老保险缴纳比例
                BigDecimal birthScale = deptPunish.getBirthScale(); //生育保险缴纳比例
                BigDecimal hurtScale = deptPunish.getHurtScale(); //工伤保险缴纳比例
                BigDecimal treatScale = deptPunish.getTreatScale(); //医疗保险缴纳比例
                BigDecimal unemploymentScale = deptPunish.getUnemploymentScale(); //失业保险缴纳比例
                BigDecimal fundsScale = deptPunish.getFundsScale(); //公积金缴纳比例
                //外聘人员每日结算工资
                if (in.getInfoType() == 2) {
                    salary = salary.multiply(BigDecimal.valueOf(attendanceNormalCount));
                }
                BigDecimal retirement = salary.multiply(retirementScale);
                BigDecimal birth = salary.multiply(birthScale);
                BigDecimal hurt = salary.multiply(hurtScale);
                BigDecimal treat = salary.multiply(treatScale);
                BigDecimal unemployment = salary.multiply(unemploymentScale);
                BigDecimal funds = salary.multiply(fundsScale);

                StringJoiner deductionDetails = getDeductionDetails(attendanceEarlyCount, attendanceLateCount, attendanceMissCount, lateReal, earlyReal, missReal, retirement, birth, hurt, treat, unemployment, funds);
                //法定扣款
                BigDecimal statutoryDeduction = retirement.add(birth).add(hurt).add(treat).add(unemployment).add(funds);
                String formattedDate = getSettlementDate(createReqVO);
                SalarySettlementCreateReqVO salarySettlementCreateReqVO = getSalarySettlementCreateReqVO(in, salary, attendanceNormalCount, attendanceDeduction, statutoryDeduction, formattedDate, deductionDetails);
                list.add(salarySettlementCreateReqVO);
            }
        } else if (createReqVO.getReType() == 1) {
            Long projectId = createReqVO.getProjectId();
            Integer attendanceMonth = createReqVO.getAttendanceMonth();
            List<AttendanceStatisticProjectInfoVo> attendanceRecordStatisticProject = sysAttendanceRecordService.getAttendanceRecordStatisticProject(Math.toIntExact(projectId), attendanceMonth);
            for (AttendanceStatisticProjectInfoVo statisticProjectInfoVo : attendanceRecordStatisticProject) {
                int userId = statisticProjectInfoVo.getUserId();
                AdminUserDO user = adminUserService.getUser((long) userId);
                Long deptId = user.getDeptId();

                //获取月考勤
                Integer attendanceNormalCount = statisticProjectInfoVo.getAttendanceNormalCount(); //出勤（天）
                Integer attendanceLeaveCount = statisticProjectInfoVo.getAttendanceLeaveCount(); //请假（小时）
                Integer attendanceEarlyCount = statisticProjectInfoVo.getAttendanceEarlyCount(); //早退（次）
                Integer attendanceLateCount = statisticProjectInfoVo.getAttendanceLateCount(); //迟到（次）
                Integer attendanceMissCount = statisticProjectInfoVo.getAttendanceMissCount(); //漏签（次）
                //判断非空
                attendanceNormalCount = ValidatedNull(attendanceNormalCount);
                attendanceLeaveCount = ValidatedNull(attendanceLeaveCount);
                attendanceEarlyCount = ValidatedNull(attendanceEarlyCount);
                attendanceLateCount = ValidatedNull(attendanceLateCount);
                attendanceMissCount = ValidatedNull(attendanceMissCount);

                //获取部门考勤处理规则
                SystemDeptPunish deptPunish = systemDeptPunishMapper.selectOne("dept_id", deptId);
                if (deptPunish == null) {
                    throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_SALARY_NOT_EXISTS);
                }
//                Integer allowLateCount = deptPunish.getAllowLateCount(); //允许迟到次数
//                Integer allowExcusedCount = deptPunish.getAllowExcusedCount(); //允许请假次数
//                Integer allowLeaveEarlyCount = deptPunish.getAllowLeaveEarlyCount(); //允许早退次数
//                Integer allowMissSignatureCount = deptPunish.getAllowMissSignatureCount(); //允许漏签次数

                //惩罚的次数
//                int late = 0;
//                int excused = 0;
//                int early = 0;
//                int miss = 0;
//                if (attendanceLateCount > allowLateCount)
//                    late = attendanceLateCount - allowLateCount; //惩罚迟到的次数
//                if (attendanceLeaveCount > allowExcusedCount)
//                    excused = attendanceLeaveCount - allowExcusedCount; //惩罚请假的次数
//                if (attendanceEarlyCount > allowLeaveEarlyCount)
//                    early = attendanceEarlyCount - allowLeaveEarlyCount; //惩罚 早退的次数
//                if (attendanceMissCount > allowMissSignatureCount) {
//                    miss = attendanceMissCount - allowMissSignatureCount; //惩罚漏签的次数
//                }
                //单次罚款金额
                BigDecimal latePenalty = deptPunish.getLatePenalty(); //迟到罚款
//                BigDecimal excusedPenalty = deptPunish.getExcusedPenalty(); //请假罚款
                BigDecimal leaveEarlyPenalty = deptPunish.getLeaveEarlyPenalty(); //早退罚款
                BigDecimal missSignaturePenalty = deptPunish.getMissSignaturePenalty(); //漏签罚款
                //罚款金额
                BigDecimal lateReal = latePenalty.multiply(BigDecimal.valueOf(attendanceLateCount));
                BigDecimal earlyReal = leaveEarlyPenalty.multiply(BigDecimal.valueOf(attendanceEarlyCount));
                BigDecimal missReal = missSignaturePenalty.multiply(BigDecimal.valueOf(attendanceMissCount));
                //考勤扣款
                BigDecimal attendanceDeduction = lateReal.add(earlyReal).add(missReal);
                //社保缴纳基数
                BigDecimal retirementScale = deptPunish.getRetirementScale(); //养老保险缴纳比例
                BigDecimal birthScale = deptPunish.getBirthScale(); //生育保险缴纳比例
                BigDecimal hurtScale = deptPunish.getHurtScale(); //工伤保险缴纳比例
                BigDecimal treatScale = deptPunish.getTreatScale(); //医疗保险缴纳比例
                BigDecimal unemploymentScale = deptPunish.getUnemploymentScale(); //失业保险缴纳比例
                BigDecimal fundsScale = deptPunish.getFundsScale(); //公积金缴纳比例

                InformationDO information = informationService.getInformationByUserId((long) userId);
                if (information != null) {
                    BigDecimal baseSalary = information.getBaseSalary();
                    BigDecimal salary;
                    if (information.getHasProbation()) {
                        salary = baseSalary.multiply(information.getProbationSalaryRatio());
                    } else {
                        salary = baseSalary;
                    }
                    //外聘人员每日结算工资
                    if (information.getInfoType() == 2) {
                        salary = salary.multiply(BigDecimal.valueOf(attendanceNormalCount));
                    }
                    BigDecimal retirement = salary.multiply(retirementScale);
                    BigDecimal birth = salary.multiply(birthScale);
                    BigDecimal hurt = salary.multiply(hurtScale);
                    BigDecimal treat = salary.multiply(treatScale);
                    BigDecimal unemployment = salary.multiply(unemploymentScale);
                    BigDecimal funds = salary.multiply(fundsScale);

                    StringJoiner deductionDetails = getProjectDeductionDetails(attendanceEarlyCount, attendanceLateCount, attendanceMissCount, lateReal, earlyReal, missReal);
                    //法定扣款
                    BigDecimal statutoryDeduction = retirement.add(birth).add(hurt).add(treat).add(unemployment).add(funds);
                    String formattedDate = getSettlementDate(createReqVO);
                    SalarySettlementCreateReqVO salarySettlementCreateReqVO = getSalarySettlementCreateReqVO(information, salary, attendanceNormalCount, attendanceDeduction, statutoryDeduction, formattedDate, deductionDetails);
                    list.add(salarySettlementCreateReqVO);
                }
            }
        }
        List<SalarySettlementDO> salarySettlements = SalarySettlementConvert.INSTANCE.convertList03(list);
        salarySettlementMapper.insertBatch(salarySettlements);
        return (long) salarySettlements.size();
    }

    @NotNull
    private static Integer ValidatedNull(Integer num) {
        num = num == null ? 0 : num;
        return num;
    }

    @NotNull
    private static StringJoiner getDeductionDetails(Integer attendanceEarlyCount, Integer attendanceLateCount, Integer attendanceMissCount, BigDecimal lateReal, BigDecimal earlyReal, BigDecimal missReal, BigDecimal retirement, BigDecimal birth, BigDecimal hurt, BigDecimal treat, BigDecimal unemployment, BigDecimal funds) {
        List<String> details = new ArrayList<>();
        String lateStr = String.format("本月迟到%d次，迟到罚款%.2f", attendanceLateCount, lateReal);
        String earlyStr = String.format("本月早退%d次，早退罚款%.2f", attendanceEarlyCount, earlyReal);
        String missStr = String.format("本月漏签%d次，漏签罚款%.2f", attendanceMissCount, missReal);
        details.add(lateStr);
        details.add(earlyStr);
        details.add(missStr);

        String retirementStr = String.format("养老保险扣除%.2f", retirement);
        String birthStr = String.format("生育保险扣除%.2f", birth);
        String hurtStr = String.format("工伤保险扣除%.2f", hurt);
        String treatStr = String.format("医疗保险扣除%.2f", treat);
        String unemploymentStr = String.format("失业保险扣除%.2f", unemployment);
        String fundsStr = String.format("公积金扣除%.2f", funds);
        details.add(retirementStr);
        details.add(birthStr);
        details.add(hurtStr);
        details.add(treatStr);
        details.add(unemploymentStr);
        details.add(fundsStr);
        StringJoiner stringJoiner = new StringJoiner(",", "", "");
        for (String deductionDetail : details) {
            stringJoiner.add(deductionDetail);
        }
        return stringJoiner;
    }

    @NotNull
    private static StringJoiner getProjectDeductionDetails(Integer attendanceEarlyCount, Integer attendanceLateCount, Integer attendanceMissCount, BigDecimal lateReal, BigDecimal earlyReal, BigDecimal missReal) {
        List<String> details = new ArrayList<>();
        String lateStr = String.format("本月迟到%d次，迟到罚款%.2f", attendanceLateCount, lateReal);
        String earlyStr = String.format("本月早退%d次，早退罚款%.2f", attendanceEarlyCount, earlyReal);
        String missStr = String.format("本月漏签%d次，漏签罚款%.2f", attendanceMissCount, missReal);
        details.add(lateStr);
        details.add(earlyStr);
        details.add(missStr);

        StringJoiner stringJoiner = new StringJoiner(",", "", "");
        for (String deductionDetail : details) {
            stringJoiner.add(deductionDetail);
        }
        return stringJoiner;
    }

    @NotNull
    private static SalarySettlementCreateReqVO getSalarySettlementCreateReqVO(InformationDO in, BigDecimal salary, Integer attendanceNormalCount, BigDecimal attendanceDeduction, BigDecimal statutoryDeduction, String formattedDate, StringJoiner stringJoiner) {
        SalarySettlementCreateReqVO salarySettlementCreateReqVO = new SalarySettlementCreateReqVO();
        salarySettlementCreateReqVO.setUserId(in.getUserId());
        salarySettlementCreateReqVO.setUserName(in.getName());
        salarySettlementCreateReqVO.setDeptId(in.getDeptId());
        salarySettlementCreateReqVO.setDeptName(in.getDeptName());
        salarySettlementCreateReqVO.setBaseSalary(salary);
        salarySettlementCreateReqVO.setStatutoryDeduction(statutoryDeduction);
        salarySettlementCreateReqVO.setAttendanceDeduction(attendanceDeduction);
        salarySettlementCreateReqVO.setDeductionDetails(stringJoiner.toString());
        salarySettlementCreateReqVO.setAttendanceDays(attendanceNormalCount);
        salarySettlementCreateReqVO.setSettlementDate(formattedDate);
        return salarySettlementCreateReqVO;
    }

    @NotNull
    private static String getSettlementDate(MonthAttendance createReqVO) {
        LocalDateTime dateTime = LocalDateTimeUtil.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedDate = null;
        if (createReqVO.getAttendanceMonth() == null || createReqVO.getAttendanceMonth() == 0) {
            formattedDate = dateTime.format(formatter);
        } else {
            Instant instant = Instant.ofEpochSecond(createReqVO.getAttendanceMonth());
            dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            formattedDate = dateTime.format(formatter);
        }
        return formattedDate;
    }

    @Override
    public void updateSalarySettlement(SalarySettlementUpdateReqVO updateReqVO) {
        validateSalarySettlementExists(updateReqVO.getId());
        SalarySettlementDO updateObj = SalarySettlementConvert.INSTANCE.convert(updateReqVO);
        salarySettlementMapper.updateById(updateObj);
    }

    @Override
    public void deleteSalarySettlement(Long id) {
        validateSalarySettlementExists(id);
        salarySettlementMapper.deleteById(id);
    }

    private void validateSalarySettlementExists(Long id) {
        if (salarySettlementMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SALARY_SETTLEMENT_NOT_EXISTS);
        }
    }

    @Override
    public SalarySettlementDO getSalarySettlement(Long id) {
        return salarySettlementMapper.selectById(id);
    }

    @Override
    public List<SalarySettlementDO> getSalarySettlementList(Collection<Long> ids) {
        return salarySettlementMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<SalarySettlementDO> getSalarySettlementPage(SalarySettlementPageReqVO pageReqVO) {
        return salarySettlementMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SalarySettlementDO> getSalarySettlementList(SalarySettlementExportReqVO exportReqVO) {
        return salarySettlementMapper.selectList(exportReqVO);
    }

    private Integer days(LocalDateTime date) {
        LocalDate localDate = date.toLocalDate();
        YearMonth currentYearMonth = YearMonth.now();
        int currentYear = currentYearMonth.getYear();
        int currentMonth = currentYearMonth.getMonthValue();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int dayOfMonth = localDate.getDayOfMonth();
        if (year == currentYear && month == currentMonth) {

            return dayOfMonth;
        } else {

            return 0;
        }
    }


    @Override
    public void export(List<SalarySettlementExcelVO> data, HttpServletRequest req, HttpServletResponse resp) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("userId", "员工id");
        writer.addHeaderAlias("userName", "员工姓名");
        writer.addHeaderAlias("deptName", "部门");
        writer.addHeaderAlias("baseSalary", "基础工资");
        writer.addHeaderAlias("statutoryDeduction", "法定扣款");
        writer.addHeaderAlias("attendanceDeduction", "考勤扣款");
        writer.addHeaderAlias("realSalary", "实发工资");
        writer.addHeaderAlias("deductionDetails", "扣款明细");
        writer.addHeaderAlias("attendanceDays", "考勤天数");
        writer.addHeaderAlias("settlementDate", "结算日期");

        for (int i = 0; i < SalarySettlementExcelVO.class.getDeclaredFields().length; i++) {
            writer.setColumnWidth(i, 13);
        }
        writer.setColumnWidth(8, 50);
        writer.setFreezePane(1);
        writer.write(data);
        String filename = "员工工资结算.xls";
        this.setFilenameHeader(req, resp, filename);
        try {
            OutputStream os = resp.getOutputStream();
            writer.flush(os, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.close();
        log.info("工资结算导出成功：{}", filename);
    }


    private static void setFilenameHeader(HttpServletRequest req, HttpServletResponse resp, String filename) {
        try {
            String encodedFilename = filename;
            String userAgent = req.getHeader("User-Agent");

            // 根据不同的浏览器设置不同的文件名编码
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                if (userAgent.contains("msie") || userAgent.contains("trident")) {
                    // IE浏览器
                    encodedFilename = URLEncoder.encode(filename, "UTF-8");
                    encodedFilename = encodedFilename.replace("+", "%20");  // 替换空格为%20
                } else if (userAgent.contains("firefox")) {
                    // 火狐浏览器
                    encodedFilename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
                } else if (userAgent.contains("chrome")) {
                    // Chrome浏览器
                    encodedFilename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
                } else {
                    // 其他浏览器
                    encodedFilename = URLEncoder.encode(filename, "UTF-8");
                }
            }

            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFilename + "\"");
        } catch (Exception e) {
            log.error("文件名编码出错", e);
            throw new BusinessException("文件编码出错");
        }
    }
}
