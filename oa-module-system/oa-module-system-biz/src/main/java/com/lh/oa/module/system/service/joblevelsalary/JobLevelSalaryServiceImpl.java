package com.lh.oa.module.system.service.joblevelsalary;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryCreateReqVO;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryExportReqVO;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryPageReqVO;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryUpdateReqVO;
import com.lh.oa.module.system.convert.joblevelsalary.JobLevelSalaryConvert;
import com.lh.oa.module.system.dal.dataobject.joblevelsalary.JobLevelSalaryDO;
import com.lh.oa.module.system.dal.mysql.joblevelsalary.JobLevelSalaryMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.*;
import com.lh.oa.framework.common.pojo.PageResult;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 员工工种等级基础工资 Service 实现类
 *
 * @author
 */
@Service
@Validated
public class JobLevelSalaryServiceImpl implements JobLevelSalaryService {

    @Resource
    private JobLevelSalaryMapper jobLevelSalaryMapper;

    @Override
    public Long createJobLevelSalary(JobLevelSalaryCreateReqVO createReqVO) {
        // 插入
        JobLevelSalaryDO jobLevelSalary = JobLevelSalaryConvert.INSTANCE.convert(createReqVO);
        jobLevelSalaryMapper.insert(jobLevelSalary);
        // 返回
        return jobLevelSalary.getId();
    }

    @Override
    public void updateJobLevelSalary(JobLevelSalaryUpdateReqVO updateReqVO) {
        // 校验存在
        validateJobLevelSalaryExists(updateReqVO.getId());
        // 更新
        JobLevelSalaryDO updateObj = JobLevelSalaryConvert.INSTANCE.convert(updateReqVO);
        jobLevelSalaryMapper.updateById(updateObj);
    }

    @Override
    public void deleteJobLevelSalary(Long id) {
        // 校验存在
        validateJobLevelSalaryExists(id);
        // 删除
        jobLevelSalaryMapper.deleteById(id);
    }

    private void validateJobLevelSalaryExists(Long id) {
        if (jobLevelSalaryMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.JOB_LEVEL_SALARY_NOT_EXISTS);
        }
    }

    @Override
    public JobLevelSalaryDO getJobLevelSalary(Long id) {
        return jobLevelSalaryMapper.selectById(id);
    }

    @Override
    public List<JobLevelSalaryDO> getJobLevelSalaryList(Collection<Long> ids) {
        return jobLevelSalaryMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<JobLevelSalaryDO> getJobLevelSalaryPage(JobLevelSalaryPageReqVO pageReqVO) {
        return jobLevelSalaryMapper.selectPage(pageReqVO);
    }

    @Override
    public List<JobLevelSalaryDO> getJobLevelSalaryList(JobLevelSalaryExportReqVO exportReqVO) {
        return jobLevelSalaryMapper.selectList(exportReqVO);
    }

}
