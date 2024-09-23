package com.lh.oa.module.bpm.dal.mysql.definition;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.group.BpmUserGroupPageReqVO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmUserGroupDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

/**
 * 用户组 Mapper
 *
 * @author
 */
@Mapper
public interface BpmUserGroupMapper extends BaseMapperX<BpmUserGroupDO> {

    default PageResult<BpmUserGroupDO> selectPage(BpmUserGroupPageReqVO reqVO) {
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
        return selectPage(reqVO, new LambdaQueryWrapperX<BpmUserGroupDO>()
                .likeIfPresent(BpmUserGroupDO::getName, reqVO.getName())
                .eqIfPresent(BpmUserGroupDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BaseDO::getCreateTime, start, end)
                .orderByDesc(BpmUserGroupDO::getId));
    }

    default List<BpmUserGroupDO> selectListByStatus(Integer status) {
        return selectList(BpmUserGroupDO::getStatus, status);
    }

}
