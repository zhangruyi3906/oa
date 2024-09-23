package com.lh.oa.module.system.full.controller;

import cn.hutool.json.JSONUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.system.api.sysAttendanceRule.to.SysAttendanceRecordQueryParam;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRecordEntity;
import com.lh.oa.module.system.full.entity.attandance.dto.AttendanceRecordDTO;
import com.lh.oa.module.system.full.entity.base.InfoEntity;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRecordService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.loginUserId;

@AllArgsConstructor
@RestController
@RequestMapping("attendance/record")
public class SysAttendanceRecordController extends BaseController {

    private final SysAttendanceRecordService service;

    /**
     * 打卡
     */
    @PostMapping
    public CommonResult saveAttendanceRecord(@RequestBody AttendanceRecordDTO recordDTO) {
        log.info(JSONUtil.toJsonStr(recordDTO));
        return success(service.saveAttendanceRecord(recordDTO, recordDTO.getUserId() > 0 ? recordDTO.getUserId() : loginUserId()));
    }

    /**
     * 打卡记录
     */
    @GetMapping("list")
    public CommonResult getAttendanceRecordList(@RequestParam(required = false) Long deptId,
                                                @RequestParam(required = false) Long projectId,
                                                @RequestParam(required = false) Long userId,
                                                @RequestParam(required = false) Integer startTime,
                                                @RequestParam(required = false) Integer endTime,
                                                @RequestParam(required = false) Integer attendanceMonth,
                                                @RequestParam(required = false) Integer pageNo,
                                                @RequestParam(required = false) Integer pageSize) {
        log.info("deptId: {}, projectId: {}, userId: {}, startTime: {}, endTime: {}, attendanceMonth: {}, pageNo: {}, pageSize: {}", deptId, projectId, userId, startTime, endTime, attendanceMonth, pageNo, pageSize);
        Pageable pageable = pageNo != null ? PageRequest.of(pageNo - 1, pageSize, Sort.unsorted()) : null;
        List<SysAttendanceRecordEntity> list = service.getDefaultAttendanceRecordList(deptId, projectId, userId, startTime == null ? 0 : startTime, endTime == null ? 0 : endTime, pageable);
        int count = service.getDefaultAttendanceRecordCount(deptId, projectId, userId, startTime == null ? 0 : startTime, endTime == null ? 0 : endTime);
        return success(new InfoEntity(pageNo == null ? 0 : pageNo, pageSize == null ? 0 : pageSize, count, list));
    }

    /*
     * 考勤统计V2
     */
    @GetMapping("statistic/listV2")
    public CommonResult<InfoEntity> getAttendanceRecordStatisticV2(@RequestParam(required = false) Long deptId,
                                                     @RequestParam(required = false) Long projectId,
                                                     @RequestParam(required = false) Long userId,
                                                     @RequestParam(required = false) Integer attendanceMonth,
                                                     @RequestParam(required = false) Integer pageNo,
                                                     @RequestParam(required = false) Integer pageSize) {
        log.info("考勤统计查询v2，deptId:{}, projectId:{}, userId:{}, attendanceMonth:{}, pageNo:{}, pageSize:{}", deptId, projectId, userId, attendanceMonth, pageNo, pageSize);
        return success(service.getAttendanceStatisticPageV2(deptId, projectId, userId, attendanceMonth, pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize));
    }

    @Deprecated
    @GetMapping("statistic/project")
    public CommonResult getAttendanceRecordStatisticProject( @RequestParam(required = false) Integer projectId,
                                                     @RequestParam(required = false) Integer attendanceMonth,
                                                     @RequestParam(required = false) Integer pageNo,
                                                     @RequestParam(required = false) Integer pageSize) {
        log.info("projectId: {}, attendanceMonth: {}, pageNo: {}, pageSize: {}", projectId, attendanceMonth, pageNo, pageSize);
        return success(service.getAttendanceRecordStatisticProject(projectId == null ? 0 : projectId, attendanceMonth));
    }

    /*
     * 导出打卡记录
     */
    @GetMapping("list/export")
    public void exportAttendanceRecordList(@RequestParam(required = false) Long deptId,
                                           @RequestParam(required = false) Long projectId,
                                           @RequestParam(required = false) Long userId,
                                           @RequestParam(required = false) Integer startTime,
                                           @RequestParam(required = false) Integer endTime,
                                           HttpServletRequest req, HttpServletResponse resp) {
        log.info("导出考勤记录, deptId:{}, projectId:{}, userId:{}, startTime:{}, endTime:{}", deptId, projectId, userId, startTime, endTime);
        service.exportAttendanceRecordList(deptId, projectId, userId, startTime, endTime, req, resp);
    }

    /*
     * 导出考勤统计记录
     */
    @GetMapping("statistic/list/exportV2")
    public void exportAttendanceRecordStatisticV2(@RequestParam(required = false) Long deptId,
                                                @RequestParam(required = false) Long projectId,
                                                @RequestParam(required = false) Long userId,
                                                @RequestParam(required = false) Integer attendanceMonth,
                                                HttpServletRequest req, HttpServletResponse resp) {
        log.info("导出考勤统计记录，deptId:{}, projectId:{}, userId:{}, attendanceMonth:{}", deptId, projectId, userId, attendanceMonth);
        service.exportAttendanceRecordStatisticV2(deptId, projectId, userId, attendanceMonth, req, resp);
    }

    @Deprecated
    @GetMapping("statistic/list/list")
    public CommonResult getAttendanceRecordStatisticList(@RequestParam(required = false) Integer deptId,
                                                     @RequestParam(required = false) Integer projectId,
                                                     @RequestParam(required = false) String userName,
                                                     @RequestParam(required = false) Integer attendanceMonth,
                                                     @RequestParam(required = false) Integer userId) {
        log.info("deptId: {}, projectId: {}, userName: {}, attendanceMonth: {}, userId: {}", deptId, projectId, userName, attendanceMonth, userId);
        return success(service.getAttendanceStatisticPageList(deptId == null ? 0 : deptId, projectId == null ? 0 : projectId, userName, attendanceMonth,userId == null ? 0 : userId));
    }

    @PostMapping("/getRecentlyMonthAttendanceDateList")
    public CommonResult<Map<Integer, Map<Integer, List<Long>>>> getRecentlyMonthAttendanceDateList(@RequestBody SysAttendanceRecordQueryParam param) {
        log.info("根据参数查询打卡天数列表，param: {}", JsonUtils.toJsonString(param));
        return success(service.getRecentlyMonthAttendanceDateList(param));
    }

}
