package com.lh.oa.module.system.service.jobcommission;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.*;
import com.lh.oa.module.system.dal.dataobject.jobcommission.JobCommissionDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionCreateReqVO;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionExportReqVO;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionPageReqVO;
import com.lh.oa.module.system.controller.admin.jobcommission.vo.JobCommissionUpdateReqVO;

/**
 * 项目工种提成 Service 接口
 *
 * @author
 */
public interface JobCommissionService {

    /**
     * 创建项目工种提成
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createJobCommission(@Valid JobCommissionCreateReqVO createReqVO);

    /**
     * 更新项目工种提成
     *
     * @param updateReqVO 更新信息
     */
    void updateJobCommission(@Valid JobCommissionUpdateReqVO updateReqVO);

    /**
     * 删除项目工种提成
     *
     * @param id 编号
     */
    void deleteJobCommission(Long id);

    /**
     * 获得项目工种提成
     *
     * @param id 编号
     * @return 项目工种提成
     */
    JobCommissionDO getJobCommission(Long id);

    /**
     * 获得项目工种提成列表
     *
     * @param ids 编号
     * @return 项目工种提成列表
     */
    List<JobCommissionDO> getJobCommissionList(Collection<Long> ids);

    /**
     * 获得项目工种提成分页
     *
     * @param pageReqVO 分页查询
     * @return 项目工种提成分页
     */
    PageResult<JobCommissionDO> getJobCommissionPage(JobCommissionPageReqVO pageReqVO);

    /**
     * 获得项目工种提成列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 项目工种提成列表
     */
    List<JobCommissionDO> getJobCommissionList(JobCommissionExportReqVO exportReqVO);

}
