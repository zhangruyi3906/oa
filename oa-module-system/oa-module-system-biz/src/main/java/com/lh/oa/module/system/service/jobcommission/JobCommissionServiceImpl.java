package com.lh.oa.module.system.service.jobcommission;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionCreateReqVO;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionExportReqVO;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionPageReqVO;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionUpdateReqVO;
import com.lh.oa.module.system.convert.jobcommission.JobCommissionConvert;
import com.lh.oa.module.system.dal.dataobject.jobcommission.JobCommissionDO;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.*;
import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.dal.mysql.jobcommission.JobCommissionMapper;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 项目工种提成 Service 实现类
 *
 * @author
 */
@Service
@Validated
public class JobCommissionServiceImpl implements JobCommissionService {

    @Resource
    private JobCommissionMapper jobCommissionMapper;

    @Override
    public Long createJobCommission(JobCommissionCreateReqVO createReqVO) {
        // 插入
        JobCommissionDO jobCommission = JobCommissionConvert.INSTANCE.convert(createReqVO);
        jobCommissionMapper.insert(jobCommission);
        // 返回
        return jobCommission.getId();
    }

    @Override
    public void updateJobCommission(JobCommissionUpdateReqVO updateReqVO) {
        // 校验存在
        validateJobCommissionExists(updateReqVO.getId());
        // 更新
        JobCommissionDO updateObj = JobCommissionConvert.INSTANCE.convert(updateReqVO);
        jobCommissionMapper.updateById(updateObj);
    }

    @Override
    public void deleteJobCommission(Long id) {
        // 校验存在
        validateJobCommissionExists(id);
        // 删除
        jobCommissionMapper.deleteById(id);
    }

    private void validateJobCommissionExists(Long id) {
        if (jobCommissionMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.JOB_COMMISSION_NOT_EXISTS);
        }
    }

    @Override
    public JobCommissionDO getJobCommission(Long id) {
        return jobCommissionMapper.selectById(id);
    }

    @Override
    public List<JobCommissionDO> getJobCommissionList(Collection<Long> ids) {
        return jobCommissionMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<JobCommissionDO> getJobCommissionPage(JobCommissionPageReqVO pageReqVO) {
        return jobCommissionMapper.selectPage(pageReqVO);
    }

    @Override
    public List<JobCommissionDO> getJobCommissionList(JobCommissionExportReqVO exportReqVO) {
        return jobCommissionMapper.selectList(exportReqVO);
    }

}
