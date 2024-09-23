package com.lh.oa.module.bpm.service.approvalprocess;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.*;
import com.lh.oa.module.bpm.dal.dataobject.approvalprocess.ApprovalProcessDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.ApprovalProcessCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.ApprovalProcessPageReqVO;

/**
 * 项目立项 Service 接口
 *
 * @author 狗蛋
 */
public interface ApprovalProcessService {

    /**
     * 创建项目立项
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createApprovalProcess(Long userId ,@Valid ApprovalProcessCreateReqVO createReqVO);

    /**
     * 更新项目立项
     *
     * @param updateReqVO 更新信息
     */
    void updateApprovalProcess(Long id, Integer result);

    /**
     * 删除项目立项
     *
     * @param id 编号
     */
    void deleteApprovalProcess(Long id);

    /**
     * 获得项目立项
     *
     * @param id 编号
     * @return 项目立项
     */
    ApprovalProcessDO getApprovalProcess(Long id);

    /**
     * 获得项目立项列表
     *
     * @param ids 编号
     * @return 项目立项列表
     */
    List<ApprovalProcessDO> getApprovalProcessList(Collection<Long> ids);

    /**
     * 获得项目立项分页
     *
     * @param pageReqVO 分页查询
     * @return 项目立项分页
     */
    PageResult<ApprovalProcessDO> getApprovalProcessPage(ApprovalProcessPageReqVO pageReqVO);

    /**
     * 获得项目立项列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 项目立项列表
     */
//    List<ApprovalProcessDO> getApprovalProcessList(ApprovalProcessExportReqVO exportReqVO);

}
