package com.lh.oa.module.system.dal.mysql.joblevelsalary;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryExportReqVO;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryPageReqVO;
import com.lh.oa.module.system.dal.dataobject.joblevelsalary.JobLevelSalaryDO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.*;

/**
 * 员工工种等级基础工资 Mapper
 *
 * @author
 */
@Mapper
public interface JobLevelSalaryMapper extends BaseMapperX<JobLevelSalaryDO> {

    default PageResult<JobLevelSalaryDO> selectPage(JobLevelSalaryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<JobLevelSalaryDO>()
                .eqIfPresent(JobLevelSalaryDO::getJobCode, reqVO.getJobCode())
                .eqIfPresent(JobLevelSalaryDO::getJobLevel, reqVO.getJobLevel())
                .eqIfPresent(JobLevelSalaryDO::getUserId, reqVO.getUserId())
                .eqIfPresent(JobLevelSalaryDO::getBaseSalary, reqVO.getBaseSalary())
                .orderByDesc(JobLevelSalaryDO::getId));
    }

    default List<JobLevelSalaryDO> selectList(JobLevelSalaryExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<JobLevelSalaryDO>()
                .eqIfPresent(JobLevelSalaryDO::getJobCode, reqVO.getJobCode())
                .eqIfPresent(JobLevelSalaryDO::getJobLevel, reqVO.getJobLevel())
                .eqIfPresent(JobLevelSalaryDO::getUserId, reqVO.getUserId())
                .eqIfPresent(JobLevelSalaryDO::getBaseSalary, reqVO.getBaseSalary())
                .orderByDesc(JobLevelSalaryDO::getId));
    }

}
