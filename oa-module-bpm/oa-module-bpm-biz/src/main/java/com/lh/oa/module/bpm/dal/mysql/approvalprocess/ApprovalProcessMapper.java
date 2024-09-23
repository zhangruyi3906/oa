package com.lh.oa.module.bpm.dal.mysql.approvalprocess;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.ApprovalProcessPageReqVO;
import com.lh.oa.module.bpm.dal.dataobject.approvalprocess.ApprovalProcessDO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.*;

/**
 * 项目立项 Mapper
 *
 * @author 狗蛋
 */
@Mapper
public interface ApprovalProcessMapper extends BaseMapperX<ApprovalProcessDO> {

    default PageResult<ApprovalProcessDO> selectPage(ApprovalProcessPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ApprovalProcessDO>()
                .likeIfPresent(ApprovalProcessDO::getProjectName, reqVO.getProjectName())
                .betweenIfPresent(ApprovalProcessDO::getStartDate, reqVO.getStartDate())
                .betweenIfPresent(ApprovalProcessDO::getEndDate, reqVO.getEndDate())
                .eqIfPresent(ApprovalProcessDO::getProjectManagerId, reqVO.getProjectManagerId())
                .eqIfPresent(ApprovalProcessDO::getApprovalStatus, reqVO.getApprovalStatus())
                .eqIfPresent(ApprovalProcessDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .betweenIfPresent(ApprovalProcessDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ApprovalProcessDO::getId));
    }

//    default List<ApprovalProcessDO> selectList(ApprovalProcessExportReqVO reqVO) {
//        return selectList(new LambdaQueryWrapperX<ApprovalProcessDO>()
//                .likeIfPresent(ApprovalProcessDO::getProjectName, reqVO.getProjectName())
//                .betweenIfPresent(ApprovalProcessDO::getStartDate, reqVO.getStartDate())
//                .betweenIfPresent(ApprovalProcessDO::getEndDate, reqVO.getEndDate())
//                .eqIfPresent(ApprovalProcessDO::getProjectManagerId, reqVO.getProjectManagerId())
//                .eqIfPresent(ApprovalProcessDO::getApprovalStatus, reqVO.getApprovalStatus())
//                .eqIfPresent(ApprovalProcessDO::getProcessInstanceId, reqVO.getProcessInstanceId())
//                .betweenIfPresent(ApprovalProcessDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(ApprovalProcessDO::getId));
//    }

}
