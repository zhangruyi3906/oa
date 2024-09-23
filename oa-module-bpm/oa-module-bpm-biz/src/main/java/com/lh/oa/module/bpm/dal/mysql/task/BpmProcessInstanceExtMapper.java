package com.lh.oa.module.bpm.dal.mysql.task;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceMyPageReqVO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.annotations.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@Mapper
public interface BpmProcessInstanceExtMapper extends BaseMapperX<BpmProcessInstanceExtDO> {

    default PageResult<BpmProcessInstanceExtDO> selectPage(Long userId, BpmProcessInstanceMyPageReqVO reqVO) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (reqVO.getStartTime() != null) {
            Long startTime = reqVO.getStartTime();
            start = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime), ZoneId.systemDefault());
        }
        if (reqVO.getEndTime() != null) {
            Long endTime = reqVO.getEndTime();
            end = LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime), ZoneId.systemDefault());
        }

        return selectPage(reqVO, new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .eqIfPresent(BpmProcessInstanceExtDO::getStartUserId, userId)
                .likeIfPresent(BpmProcessInstanceExtDO::getName, reqVO.getName())
                .eqIfPresent(BpmProcessInstanceExtDO::getProcessDefinitionId, reqVO.getProcessDefinitionId())
                .eqIfPresent(BpmProcessInstanceExtDO::getCategory, reqVO.getCategory())
                .eqIfPresent(BpmProcessInstanceExtDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BpmProcessInstanceExtDO::getResult, reqVO.getResult())
                .likeIfPresent(BpmProcessInstanceExtDO::getInstanceName, reqVO.getInstanceName())
                .betweenIfPresent(BpmProcessInstanceExtDO::getCreateTime, start, end)
                .orderByDesc(BpmProcessInstanceExtDO::getId));
    }

    default List<BpmProcessInstanceExtDO> selectList(Long userId, BpmProcessInstanceExtDO reqVO) {

        return selectList(new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .eqIfPresent(BpmProcessInstanceExtDO::getStartUserId, userId)
                .likeIfPresent(BpmProcessInstanceExtDO::getName, reqVO.getName())
                .eqIfPresent(BpmProcessInstanceExtDO::getResult, BpmProcessInstanceResultEnum.APPROVE.getResult()));
    }

    default BpmProcessInstanceExtDO selectByProcessInstanceId(String processInstanceId) {
        return selectOne(BpmProcessInstanceExtDO::getProcessInstanceId, processInstanceId);
    }

    default void updateByProcessInstanceId(BpmProcessInstanceExtDO updateObj) {
        update(updateObj, new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .eq(BpmProcessInstanceExtDO::getProcessInstanceId, updateObj.getProcessInstanceId()));
    }

    default List<BpmProcessInstanceExtDO> selectListByProcessInstanceIds(List<String> processInstanceIds) {
        return selectList(BpmProcessInstanceExtDO::getProcessInstanceId, processInstanceIds);
    }

    default PageResult<BpmProcessInstanceExtDO> selectPage(Long userId, BpmProcessInstanceMyPageReqVO reqVO, List<Long> ids, Set<Long> deptIds) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (reqVO.getStartTime() != null) {
            Long startTime = reqVO.getStartTime();
            start = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime), ZoneId.systemDefault());
        }
        if (reqVO.getEndTime() != null) {
            Long endTime = reqVO.getEndTime();
            end = LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime), ZoneId.systemDefault());
        }
        LambdaQueryWrapperX<BpmProcessInstanceExtDO> queryWrapper = new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .likeIfPresent(BpmProcessInstanceExtDO::getName, reqVO.getName())
                .eqIfPresent(BpmProcessInstanceExtDO::getProcessDefinitionId, reqVO.getProcessDefinitionId())
                .eqIfPresent(BpmProcessInstanceExtDO::getCategory, reqVO.getCategory())
                .eqIfPresent(BpmProcessInstanceExtDO::getStatus, reqVO.getStatus())
                .likeIfPresent(BpmProcessInstanceExtDO::getInstanceName, reqVO.getInstanceName())
                .eqIfPresent(BpmProcessInstanceExtDO::getResult, reqVO.getResult())
                .inIfPresent(BpmProcessInstanceExtDO::getDeptId, deptIds)
                .betweenIfPresent(BpmProcessInstanceExtDO::getCreateTime, start, end)
                .orderByDesc(BpmProcessInstanceExtDO::getId);
        if (ObjectUtils.isNotEmpty(userId)) {
            queryWrapper.eq(BpmProcessInstanceExtDO::getStartUserId, userId).and(q -> q.in(BpmProcessInstanceExtDO::getStartUserId, ids));
        }else {
            queryWrapper.in(BpmProcessInstanceExtDO::getStartUserId, ids);
        }
        return selectPage(reqVO, queryWrapper);
    }

    default PageResult<BpmProcessInstanceExtDO> selectPage(Long userId, BpmProcessInstanceMyPageReqVO reqVO, Set<Long> deptIds) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (reqVO.getStartTime() != null) {
            Long startTime = reqVO.getStartTime();
            start = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime), ZoneId.systemDefault());
        }
        if (reqVO.getEndTime() != null) {
            Long endTime = reqVO.getEndTime();
            end = LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime), ZoneId.systemDefault());
        }
        LambdaQueryWrapperX<BpmProcessInstanceExtDO> queryWrapper = new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                .likeIfPresent(BpmProcessInstanceExtDO::getName, reqVO.getName())
                .eqIfPresent(BpmProcessInstanceExtDO::getProcessDefinitionId, reqVO.getProcessDefinitionId())
                .eqIfPresent(BpmProcessInstanceExtDO::getCategory, reqVO.getCategory())
                .eqIfPresent(BpmProcessInstanceExtDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BpmProcessInstanceExtDO::getResult, reqVO.getResult())
                .likeIfPresent(BpmProcessInstanceExtDO::getInstanceName, reqVO.getInstanceName())
                .inIfPresent(BpmProcessInstanceExtDO::getDeptId, deptIds)
                .betweenIfPresent(BpmProcessInstanceExtDO::getCreateTime, start, end)
                .orderByDesc(BpmProcessInstanceExtDO::getId);
        queryWrapper.eqIfPresent(BpmProcessInstanceExtDO::getStartUserId, userId);
        return selectPage(reqVO, queryWrapper);
    }
}
