package com.lh.oa.module.bpm.dal.mysql.attendancecorrection;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionPageReqVO;
import com.lh.oa.module.bpm.dal.dataobject.attendancecorrection.CorrectionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 补卡流程 Mapper
 *
 * @author 狗蛋
 */
@Mapper
public interface CorrectionMapper extends BaseMapperX<CorrectionDO> {

    default PageResult<CorrectionDO> selectPage(CorrectionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CorrectionDO>()
                .eqIfPresent(CorrectionDO::getUserId, reqVO.getUserId())
                .likeIfPresent(CorrectionDO::getUserName, reqVO.getUserName())
                .eqIfPresent(CorrectionDO::getReason, reqVO.getReason())
                .betweenIfPresent(CorrectionDO::getCorrectionTime, reqVO.getCorrectionTime())
                .eqIfPresent(CorrectionDO::getMonth, reqVO.getMonth())
                .betweenIfPresent(CorrectionDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CorrectionDO::getType, reqVO.getType())
                .eqIfPresent(CorrectionDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .eqIfPresent(CorrectionDO::getApprovalStatus, reqVO.getApprovalStatus())
                .orderByDesc(CorrectionDO::getId));
    }

//    default List<CorrectionDO> selectList(CorrectionExportReqVO reqVO) {
//        return selectList(new LambdaQueryWrapperX<CorrectionDO>()
//                .eqIfPresent(CorrectionDO::getUserId, reqVO.getUserId())
//                .likeIfPresent(CorrectionDO::getUserName, reqVO.getUserName())
//                .eqIfPresent(CorrectionDO::getReason, reqVO.getReason())
//                .betweenIfPresent(CorrectionDO::getCorrectionTime, reqVO.getCorrectionTime())
//                .eqIfPresent(CorrectionDO::getMonth, reqVO.getMonth())
//                .betweenIfPresent(CorrectionDO::getCreateTime, reqVO.getCreateTime())
//                .eqIfPresent(CorrectionDO::getType, reqVO.getType())
//                .eqIfPresent(CorrectionDO::getProcessInstanceId, reqVO.getProcessInstanceId())
//                .eqIfPresent(CorrectionDO::getApprovalStatus, reqVO.getApprovalStatus())
//                .orderByDesc(CorrectionDO::getId));
//    }

}
