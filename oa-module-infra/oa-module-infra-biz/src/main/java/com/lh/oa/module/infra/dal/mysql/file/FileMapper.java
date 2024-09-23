package com.lh.oa.module.infra.dal.mysql.file;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import com.lh.oa.module.infra.dal.dataobject.file.FileDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

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
            start = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime), ZoneId.systemDefault()).with(LocalTime.MIN);;
        }
        if (reqVO.getEndTime() != null) {
            Long endTime = reqVO.getEndTime();
            end = LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime), ZoneId.systemDefault()).with(LocalTime.MIN);;
        }
        return selectPage(reqVO, new LambdaQueryWrapperX<FileDO>()
                .likeIfPresent(FileDO::getPath, reqVO.getPath())
                .likeIfPresent(FileDO::getType, reqVO.getType())
                .eqIfPresent(FileDO::getDeptId, reqVO.getDeptId())
                .likeIfPresent(FileDO::getTypeName, reqVO.getTypeName())
                .eqIfPresent(FileDO::getUserPublic, reqVO.getUserPublic())
                .eqIfPresent(FileDO::getDeptPublic, reqVO.getDeptPublic())
                .betweenIfPresent(FileDO::getCreateTime, start,end)
                .orderByDesc(FileDO::getId));
    }
//
//    default List<FileDO> selectList(FileExportReqVO reqVO) {
//        return selectList(new LambdaQueryWrapperX<FileDO>()
//                .likeIfPresent(FileDO::getPath, reqVO.getPath())
//                .likeIfPresent(FileDO::getType, reqVO.getType())
//                .eqIfPresent(FileDO::getDeptId, reqVO.getDeptId())
//                .likeIfPresent(FileDO::getTypeName, reqVO.getTypeName())
//                .eqIfPresent(FileDO::getUserPublic, reqVO.getUserPublic())
//                .eqIfPresent(FileDO::getDeptPublic, reqVO.getDeptPublic())
//                .betweenIfPresent(FileDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(FileDO::getId));
//    }

}
