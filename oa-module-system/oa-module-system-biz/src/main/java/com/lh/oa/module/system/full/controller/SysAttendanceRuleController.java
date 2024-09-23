package com.lh.oa.module.system.full.controller;

import cn.hutool.json.JSONUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleEntity;
import com.lh.oa.module.system.full.entity.base.InfoEntity;
import com.lh.oa.module.system.full.enums.attendance.AttendanceTypeEnum;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRulePositionService;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRuleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.common.pojo.CommonResult.successMsg;
import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.loginUserId;

@AllArgsConstructor
@RestController
@RequestMapping("attendance/rule")
public class SysAttendanceRuleController extends BaseController {

    private final SysAttendanceRuleService service;
    private final SysAttendanceRulePositionService attendanceRulePositionService;

    @PostMapping
    public CommonResult addAttendanceRule(@RequestBody @Valid SysAttendanceRuleEntity AttendanceRule) {
        log.info("新增考勤规则，param:{}", JSONUtil.toJsonStr(AttendanceRule));
        service.saveAttendanceRule(AttendanceRule, loginUserId());
        return successMsg("考勤配置创建成功");
    }

    @PutMapping
    public CommonResult updateAttendanceRule(@RequestBody @Valid SysAttendanceRuleEntity AttendanceRule) {
        log.info("修改考勤规则，param:{}", JSONUtil.toJsonStr(AttendanceRule));
        service.saveAttendanceRule(AttendanceRule, loginUserId());
        return successMsg("考勤配置修改成功");
    }

    @GetMapping("list")
    public CommonResult getAttendanceRuleList(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) String deptName,
                                              @RequestParam(required = false) String projectName,
                                              @RequestParam(required = false) AttendanceTypeEnum attendanceType,
                                              @RequestParam(required = false) Integer pageNo,
                                              @RequestParam(required = false) Integer pageSize) {
        log.info("name: {}, deptName: {}, projectName: {}, attendanceType: {}, pageNo: {}, pageSize: {}", name, deptName, projectName, attendanceType, pageNo, pageSize);
        Pageable pageable = pageNo != null ? PageRequest.of(pageNo - 1, pageSize, Sort.unsorted()) : null;
        List<SysAttendanceRuleEntity> list = service.getAttendanceRuleList(name, deptName, projectName, attendanceType, pageable);
        int count = service.getAttendanceRuleCount(name, deptName, projectName, attendanceType);
        return success(new InfoEntity(pageNo == null ? 0 : pageNo, pageSize == null ? 0 : pageSize, count, list));
    }

    @GetMapping
    public CommonResult getAttendanceRule(@RequestParam("id") Integer attendanceRuleId) {
        log.info("attendanceRuleId: {}", attendanceRuleId);
        return success(service.getAttendanceRuleById(attendanceRuleId));
    }

    @DeleteMapping()
    public CommonResult deleteAttendanceRule(@RequestParam("ids") String attendanceRuleIds) {
        log.info("attendanceRuleIds: {}", attendanceRuleIds);
        service.deleteAttendanceRule(attendanceRuleIds, loginUserId());
        return successMsg("考勤配置删除成功");
    }

    /**
     * 打卡页面-获取该用户、该项目/部门的打卡记录
     * 目前用于判断是否
     */
    @GetMapping("record")
    public CommonResult saveAttendanceRecord(@RequestParam(required = false) Integer deptId,
                                             @RequestParam(required = false) Integer projectId,
                                             @RequestParam(required = false) Integer userId,
                                             @RequestParam(required = false) Integer attendanceDate,
                                             @RequestParam(required = false) BigDecimal longitude,
                                             @RequestParam(required = false) BigDecimal latitude) {
        log.info("deptId: {}, projectId: {}, userId: {}, attendanceDate: {}, longitude: {}, latitude: {}", deptId, projectId, userId, attendanceDate, longitude, latitude);
        return success(service.getAttendanceRuleAndRecord(deptId == null ? 0 : deptId, projectId == null ? 0 : projectId, userId != null && userId > 0 ? userId : loginUserId(), attendanceDate == null ? 0 : attendanceDate, longitude, latitude));
    }

    @PostMapping("/getAttendanceRuleByProjectIds")
    public CommonResult<List<SysAttendanceRuleEntity>> getAttendanceRuleByProjectIds(@RequestBody Set<Integer> projectIds) {
        log.info("根据项目id获取项目打卡规则，projectIds: {}", projectIds);
        return success(service.getAttendanceRuleByProjectIds(projectIds));
    }

}
