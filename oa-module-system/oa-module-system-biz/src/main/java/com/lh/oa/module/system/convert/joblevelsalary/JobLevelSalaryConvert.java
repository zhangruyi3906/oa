package com.lh.oa.module.system.convert.joblevelsalary;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryCreateReqVO;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryExcelVO;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryRespVO;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.JobLevelSalaryUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.joblevelsalary.JobLevelSalaryDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.joblevelsalary.vo.*;

/**
 * 员工工种等级基础工资 Convert
 *
 * @author
 */
@Mapper
public interface JobLevelSalaryConvert {

    JobLevelSalaryConvert INSTANCE = Mappers.getMapper(JobLevelSalaryConvert.class);

    JobLevelSalaryDO convert(JobLevelSalaryCreateReqVO bean);

    JobLevelSalaryDO convert(JobLevelSalaryUpdateReqVO bean);

    JobLevelSalaryRespVO convert(JobLevelSalaryDO bean);

    List<JobLevelSalaryRespVO> convertList(List<JobLevelSalaryDO> list);

    PageResult<JobLevelSalaryRespVO> convertPage(PageResult<JobLevelSalaryDO> page);

    List<JobLevelSalaryExcelVO> convertList02(List<JobLevelSalaryDO> list);

}
