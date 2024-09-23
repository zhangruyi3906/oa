package com.lh.oa.module.system.service.projectAttendanceRule;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.*;
import com.lh.oa.module.system.dal.dataobject.projectAttendanceRule.ProjectAttendanceRuleDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.ProjectAttendanceRuleExportReqVO;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.ProjectAttendanceRuleListBaseVO;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.ProjectAttendanceRuleListPageBaseVO;

/**
 * 打卡规则（项目） Service 接口
 *
 * @author
 */
public interface ProjectAttendanceRuleService {

    /**
     * 创建打卡规则（项目）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProjectAttendanceRule(@Valid ProjectAttendanceRuleListBaseVO createReqVO);

    /**
     * 更新打卡规则（项目）
     *
     * @param updateReqVO 更新信息
     */
    void updateProjectAttendanceRule(@Valid ProjectAttendanceRuleListBaseVO updateReqVO);

    /**
     * 删除打卡规则（项目）
     *
     * @param id 编号
     */
    void deleteProjectAttendanceRule(Long id);

    /**
     * 获得打卡规则（项目）
     *
     * @param id 编号
     * @return 打卡规则（项目）
     */
    ProjectAttendanceRuleListBaseVO getProjectAttendanceRule(Long id);

    /**
     * 获得打卡规则（项目）列表
     *
     * @param ids 编号
     * @return 打卡规则（项目）列表
     */
    List<ProjectAttendanceRuleDO> getProjectAttendanceRuleList(Collection<Long> ids);

    /**
     * 获得打卡规则（项目）分页
     *
     * @param pageReqVO 分页查询
     * @return 打卡规则（项目）分页
     */
    PageResult<ProjectAttendanceRuleListBaseVO> getProjectAttendanceRulePage(ProjectAttendanceRuleListPageBaseVO pageReqVO);

    /**
     * 获得打卡规则（项目）列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 打卡规则（项目）列表
     */
    List<ProjectAttendanceRuleDO> getProjectAttendanceRuleList(ProjectAttendanceRuleExportReqVO exportReqVO);

}
