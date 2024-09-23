package com.lh.oa.module.system.service.worklog;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogCreateReqVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogExportReqVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogPageReqVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.worklog.WorkLogDO;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 员工工作日志 Service 接口
 *
 * @author 管理员
 */
public interface WorkLogService {

    /**
     * 创建员工工作日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWorkLog(Long userId ,@Valid WorkLogCreateReqVO createReqVO);

    /**
     * 更新员工工作日志
     *
     * @param updateReqVO 更新信息
     */
    void updateWorkLog(@Valid WorkLogUpdateReqVO updateReqVO);

    /**
     * 删除员工工作日志
     *
     * @param id 编号
     */
    void deleteWorkLog(Long id);

    /**
     * 获得员工工作日志
     *
     * @param id 编号
     * @return 员工工作日志
     */
    WorkLogDO getWorkLog(Long id);

    /**
     * 获得员工工作日志列表
     *
     * @param ids 编号
     * @return 员工工作日志列表
     */
    List<WorkLogDO> getWorkLogList(Collection<Long> ids);

    /**
     * 获得员工工作日志分页
     *
     * @param pageReqVO 分页查询
     * @return 员工工作日志分页
     */
    PageResult<WorkLogDO> getWorkLogPage(WorkLogPageReqVO pageReqVO);

    /**
     * 获得员工工作日志列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 员工工作日志列表
     */
    List<WorkLogDO> getWorkLogList(WorkLogExportReqVO exportReqVO);

    /**
     * 补交日志
     * @param userId
     * @param createReqVO
     * @return
     */
    Long repairCreateWorkLog(Long userId ,@Valid WorkLogCreateReqVO createReqVO);

List<Map<String, Object>> getWorkLogMonthTotalList(String month, Long deptId, Long userId) throws ParseException;
}
