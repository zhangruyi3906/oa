package com.lh.oa.module.system.service.joblevelsalary;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.*;
import com.lh.oa.module.system.dal.dataobject.joblevelsalary.JobLevelSalaryDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryCreateReqVO;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryExportReqVO;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryPageReqVO;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryUpdateReqVO;

/**
 * 员工工种等级基础工资 Service 接口
 *
 * @author
 */
public interface JobLevelSalaryService {

    /**
     * 创建员工工种等级基础工资
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createJobLevelSalary(@Valid JobLevelSalaryCreateReqVO createReqVO);

    /**
     * 更新员工工种等级基础工资
     *
     * @param updateReqVO 更新信息
     */
    void updateJobLevelSalary(@Valid JobLevelSalaryUpdateReqVO updateReqVO);

    /**
     * 删除员工工种等级基础工资
     *
     * @param id 编号
     */
    void deleteJobLevelSalary(Long id);

    /**
     * 获得员工工种等级基础工资
     *
     * @param id 编号
     * @return 员工工种等级基础工资
     */
    JobLevelSalaryDO getJobLevelSalary(Long id);

    /**
     * 获得员工工种等级基础工资列表
     *
     * @param ids 编号
     * @return 员工工种等级基础工资列表
     */
    List<JobLevelSalaryDO> getJobLevelSalaryList(Collection<Long> ids);

    /**
     * 获得员工工种等级基础工资分页
     *
     * @param pageReqVO 分页查询
     * @return 员工工种等级基础工资分页
     */
    PageResult<JobLevelSalaryDO> getJobLevelSalaryPage(JobLevelSalaryPageReqVO pageReqVO);

    /**
     * 获得员工工种等级基础工资列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 员工工种等级基础工资列表
     */
    List<JobLevelSalaryDO> getJobLevelSalaryList(JobLevelSalaryExportReqVO exportReqVO);

}
