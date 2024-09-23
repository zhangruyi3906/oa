package com.lh.oa.module.system.dal.mysql.file;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.file.vo.file.FilePageReqVO;
import com.lh.oa.module.system.dal.dataobject.file.FileDO;
import com.lh.oa.module.system.enums.file.FileEnum;
import org.apache.ibatis.annotations.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

/**
 * 文件操作 Mapper
 *
 * @author
 */
@Mapper
public interface FileMapper extends BaseMapperX<FileDO> {


    default PageResult<FileDO> selectPage(FilePageReqVO reqVO) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (reqVO.getStartTime() != null) {
            Long startTime = reqVO.getStartTime();
            start = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime), ZoneId.systemDefault()).with(LocalTime.MIN);
            ;
        }
        if (reqVO.getEndTime() != null) {
            Long endTime = reqVO.getEndTime();
            end = LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime), ZoneId.systemDefault()).with(LocalTime.MIN);
            ;
        }
        return selectPage(reqVO, new LambdaQueryWrapperX<FileDO>()
                .likeIfPresent(FileDO::getPath, reqVO.getPath())
                .likeIfPresent(FileDO::getType, reqVO.getType())
                .eqIfPresent(FileDO::getDeptId, reqVO.getDeptId())
                .likeIfPresent(FileDO::getTypeName, reqVO.getTypeName())
                .eqIfPresent(FileDO::getUserPublic, reqVO.getUserPublic())
                .eqIfPresent(FileDO::getDeptPublic, reqVO.getDeptPublic())
                .eqIfPresent(FileDO::getSource, reqVO.getSource())
                .betweenIfPresent(FileDO::getCreateTime, start, end)
                .orderByDesc(FileDO::getId));
    }

    default PageResult<FileDO> selectPublicPage(FilePageReqVO reqVO) {
        LambdaQueryWrapper<FileDO> fileDOLambdaQueryWrapper = new LambdaQueryWrapperX<FileDO>()
                .likeIfPresent(FileDO::getPath, reqVO.getPath())
                .likeIfPresent(FileDO::getType, reqVO.getType())
                .likeIfPresent(FileDO::getTypeName, reqVO.getTypeName())
                .eqIfPresent(FileDO::getSource, reqVO.getSource());

        if(reqVO.getUserPublic() != null && reqVO.getUserPublic() == true){
            fileDOLambdaQueryWrapper.eq(FileDO::getUserPublic, true).orderByDesc(FileDO::getId);
            return selectPage(reqVO, fileDOLambdaQueryWrapper);
        }

        if(reqVO.getDeptPublic() != null && reqVO.getDeptPublic() == true){
            fileDOLambdaQueryWrapper
                    .eq(FileDO::getDeptPublic, true)
                    .eq(FileDO::getDeptId, reqVO.getDeptId())
                    .orderByDesc(FileDO::getId);
            return selectPage(reqVO, fileDOLambdaQueryWrapper);
        }
        fileDOLambdaQueryWrapper.eq(FileDO::getUserPublic, true)
                .or()
                .eq(FileDO::getDeptPublic, true)
                .eq(FileDO::getDeptId, reqVO.getDeptId())
                .orderByDesc(FileDO::getId);
        return selectPage(reqVO, fileDOLambdaQueryWrapper);
    }


    default PageResult<FileDO> selectProcedurePage(FilePageReqVO reqVO, Long userId) {
        LambdaQueryWrapper<FileDO> fileDOLambdaQueryWrapper = new LambdaQueryWrapperX<FileDO>()
                .likeIfPresent(FileDO::getPath, reqVO.getPath())
                .likeIfPresent(FileDO::getType, reqVO.getType())
                .likeIfPresent(FileDO::getTypeName, reqVO.getTypeName())
                .eqIfPresent(FileDO::getSource, reqVO.getSource())
                .likeIfPresent(FileDO::getName, reqVO.getName())
                .likeIfPresent(FileDO::getInstanceName, reqVO.getInstanceName())
                .inIfPresent(FileDO::getProcessInstanceId, reqVO.getProcessInstanceIdSet())
                .or(lambdaQueryWrapper -> lambdaQueryWrapper.isNotNull(FileDO::getProcessInstanceId).eq(FileDO::getWindId, userId))
                .orderByDesc(FileDO::getId);
        return selectPage(reqVO, fileDOLambdaQueryWrapper);

    }


    default PageResult<FileDO> selectCustomerPage(FilePageReqVO reqVO) {
        LambdaQueryWrapper<FileDO> fileDOLambdaQueryWrapper = new LambdaQueryWrapperX<FileDO>()
                .likeIfPresent(FileDO::getPath, reqVO.getPath())
                .likeIfPresent(FileDO::getType, reqVO.getType())
                .likeIfPresent(FileDO::getTypeName, reqVO.getTypeName())
                .likeIfPresent(FileDO::getName, reqVO.getName())
                .eqIfPresent(FileDO::getSource, reqVO.getSource())
                .eqIfPresent(FileDO::getCustomerName, reqVO.getCustomerName())
                .orderByDesc(FileDO::getId);
        return selectPage(reqVO, fileDOLambdaQueryWrapper);

    }

    default List<FileDO> getCustomer() {
        return selectList(FileDO::getSource, FileEnum.CUSTOMER.getSource());
    }

    default List<FileDO> getContractByCustomerId(String customerName) {
        LambdaQueryWrapperX<FileDO> fileDOLambdaQueryWrapperX = new LambdaQueryWrapperX<FileDO>()
                .eqIfPresent(FileDO::getSource, FileEnum.CONTRACT.getSource())
                .eqIfPresent(FileDO::getCustomerName, customerName);
        return selectList(fileDOLambdaQueryWrapperX);
    }

}
