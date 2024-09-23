package com.lh.oa.module.system.full.service.attendance;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.api.sysAttendanceRule.to.SysAttendanceRecordQueryParam;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordCreateReqVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordPageReqVO;
import com.lh.oa.module.system.dal.dataobject.projectrecord.ProjectRecordDO;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRecordEntity;
import com.lh.oa.module.system.full.entity.attandance.dto.AttendanceRecordDTO;
import com.lh.oa.module.system.full.entity.attandance.vo.AttendanceStatisticInfoVo;
import com.lh.oa.module.system.full.entity.attandance.vo.AttendanceStatisticProjectInfoVo;
import com.lh.oa.module.system.full.entity.base.InfoEntity;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SysAttendanceRecordService {

    String saveAttendanceRecord(AttendanceRecordDTO recordDTO, int userId);

    List<SysAttendanceRecordEntity> getDefaultAttendanceRecordList(Long deptId, Long projectId, Long userId, Integer unixStartTime, Integer unixEndTime, Pageable pageable);

    int getDefaultAttendanceRecordCount(Long deptId, Long projectId, Long userId, Integer unixStartTime, Integer unixEndTime);

    SysAttendanceRecordEntity getAttendanceRecordByUserIdAndUnixDate(int userId, int attendanceDate, int projectId);

    InfoEntity getAttendanceStatisticPageV2(Long deptId, Long projectId, Long userId, Integer attendanceMonth, Integer pageNo, Integer pageSize);

    void exportAttendanceRecordList(Long deptId, Long projectId, Long userId, Integer unixStartTime, Integer unixEndTime,
                                    HttpServletRequest req, HttpServletResponse resp);

    void exportAttendanceRecordStatisticV2(Long deptId, Long projectId, Long userId, Integer attendanceMonth,
                                           HttpServletRequest req, HttpServletResponse resp);

    List<AttendanceStatisticInfoVo> getAttendanceStatisticPageList(int deptId, int projectId, String userName, Integer attendanceMonth, int userId);

    List<AttendanceStatisticProjectInfoVo> getAttendanceRecordStatisticProject(int projectId, Integer attendanceMonth);

    /**
     * 建能通新增打卡记录，该方法主要做参数转换
     *
     * @param createReqVO 建能通打卡参数
     * @return 打卡结果提示信息
     */
    Boolean savePmsAttendanceRecord(ProjectRecordCreateReqVO createReqVO);

    /**
     * 建能通查询打卡记录，该方法主要做参数转换
     *
     * @param pageVo 建能通分页参数
     * @return 打卡结果提示信息
     */
    PageResult<ProjectRecordDO> queryPmsAttendanceRecordPage(ProjectRecordPageReqVO pageVo);

    /**
     * 查询最近几个月的打卡数据
     *
     * @param param 查询参数
     * @return 最近几个月的打卡数据
     */
    Map<Integer, Map<Integer, List<Long>>> getRecentlyMonthAttendanceDateList(SysAttendanceRecordQueryParam param);
}
