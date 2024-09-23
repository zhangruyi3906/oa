package com.lh.oa.module.system.dal.mysql.jobcommission;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.dal.dataobject.jobcommission.JobCommissionDO;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionExportReqVO;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionPageReqVO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.*;

/**
 * 项目工种提成 Mapper
 *
 * @author
 */
@Mapper
public interface JobCommissionMapper extends BaseMapperX<JobCommissionDO> {

    default PageResult<JobCommissionDO> selectPage(JobCommissionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<JobCommissionDO>()
                .eqIfPresent(JobCommissionDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(JobCommissionDO::getJobId, reqVO.getJobId())
                .eqIfPresent(JobCommissionDO::getBaseCommission, reqVO.getBaseCommission())
                .eqIfPresent(JobCommissionDO::getBonusCommission, reqVO.getBonusCommission())
                .orderByDesc(JobCommissionDO::getId));
    }

    default List<JobCommissionDO> selectList(JobCommissionExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<JobCommissionDO>()
                .eqIfPresent(JobCommissionDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(JobCommissionDO::getJobId, reqVO.getJobId())
                .eqIfPresent(JobCommissionDO::getBaseCommission, reqVO.getBaseCommission())
                .eqIfPresent(JobCommissionDO::getBonusCommission, reqVO.getBonusCommission())
                .orderByDesc(JobCommissionDO::getId));
    }

}
