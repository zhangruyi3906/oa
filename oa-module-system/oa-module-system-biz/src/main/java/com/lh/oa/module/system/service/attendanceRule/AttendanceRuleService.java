package com.lh.oa.module.system.service.attendanceRule;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.*;
import com.lh.oa.module.system.dal.dataobject.attendanceRule.AttendanceRuleDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.AttendanceRuleExportReqVO;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.DeptRuleListVO;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.DeptRulePageBaseVO;

/**
 * 打卡规则（部门） Service 接口
 *
 * @author
 */
public interface AttendanceRuleService {

    /**
     * 创建打卡规则（部门）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAttendanceRule(@Valid DeptRuleListVO createReqVO);

    /**
     * 更新打卡规则（部门）
     *
     * @param updateReqVO 更新信息
     */
    void updateAttendanceRule(@Valid DeptRuleListVO updateReqVO);

    /**
     * 删除打卡规则（部门）
     *
     * @param id 编号
     */
    void deleteAttendanceRule(Long id);

    /**
     * 获得打卡规则（部门）
     *
     * @param id 编号
     * @return 打卡规则（部门）
     */
    DeptRuleListVO getAttendanceRule(Long id);

    /**
     * 获得打卡规则（部门）列表
     *
     * @param ids 编号
     * @return 打卡规则（部门）列表
     */
    List<AttendanceRuleDO> getAttendanceRuleList(Collection<Long> ids);

    /**
     * 获得打卡规则（部门）分页
     *
     * @param pageReqVO 分页查询
     * @return 打卡规则（部门）分页
     */
    PageResult<DeptRuleListVO> getAttendanceRulePage(DeptRulePageBaseVO pageReqVO);

    /**
     * 获得打卡规则（部门）列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 打卡规则（部门）列表
     */
    List<AttendanceRuleDO> getAttendanceRuleList(AttendanceRuleExportReqVO exportReqVO);

}
