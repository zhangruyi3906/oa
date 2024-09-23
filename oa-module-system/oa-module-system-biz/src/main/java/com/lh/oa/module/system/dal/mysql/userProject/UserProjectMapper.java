package com.lh.oa.module.system.dal.mysql.userProject;

import com.github.yulichang.query.MPJQueryWrapper;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.userProject.vo.UserProjectExportReqVO;
import com.lh.oa.module.system.controller.admin.userProject.vo.UserProjectPageReqVO;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

/**
 * 人员项目 Mapper
 *
 * @author
 */
@Mapper
public interface UserProjectMapper extends BaseMapperX<UserProjectDO> {

    default PageResult<UserProjectDO> selectPage(UserProjectPageReqVO reqVO) {
        MPJQueryWrapper<UserProjectDO> wrapper = new MPJQueryWrapper<UserProjectDO>()
                .selectAll(UserProjectDO.class)
                .select("su.email,su.mobile,su.sex,su.dept_id")
                .select("ui.info_type,ui.hire_date,ui.resign_time,ui.is_resigned")
                .innerJoin("system_users su on t.user_id = su.id and su.deleted = 0")
                .innerJoin("user_information ui on su.id = ui.user_id and ui.deleted = 0")
                .eq("t.project_id", reqVO.getProjectId());
        if (reqVO.getUserId() != null) {
            wrapper.eq("t.user_id", reqVO.getUserId());
        }
        if (reqVO.getIsResigned() != null) {
            wrapper.apply("ui.is_resigned = " + reqVO.getIsResigned());
        }
        if (reqVO.getType() != null) {
            wrapper.apply("t.type = " + reqVO.getType());
        }
        if (reqVO.getStatus() != null) {
            wrapper.apply("t.status = " + reqVO.getStatus());
        }
        if (reqVO.getInfoType() != null) {
            if (reqVO.getInfoType() == 3) {
                wrapper.ne("ui.info_type", 2);
            } else {
                wrapper.apply("ui.info_type = " + reqVO.getInfoType());
            }
        }
        if (reqVO.getInformationDO() != null) {
            if (reqVO.getInformationDO().getInfoType() != null) {
                wrapper.apply("ui.info_type = " + reqVO.getInformationDO().getInfoType());
            }
            if (reqVO.getInformationDO().getIsResigned() != null) {
                wrapper.apply("ui.is_resigned = " + reqVO.getInformationDO().getIsResigned());
            }
        }
        wrapper.orderByAsc("type").orderBy(true, true, "user_id");

        return selectPage(reqVO, wrapper);
    }

    default List<UserProjectDO> selectList(UserProjectExportReqVO reqVO) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (reqVO.getStartTime() != null) {
            Long startTime = reqVO.getStartTime();
            start = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime), ZoneId.systemDefault()).with(LocalTime.MIN);
        }
        if (reqVO.getEndTime() != null) {
            Long endTime = reqVO.getEndTime();
            end = LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime), ZoneId.systemDefault()).with(LocalTime.MIN);
        }
        return selectList(new LambdaQueryWrapperX<UserProjectDO>()
                .eqIfPresent(UserProjectDO::getUserId, reqVO.getUserId())
                .eqIfPresent(UserProjectDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(UserProjectDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(UserProjectDO::getCreateTime, start, end)
                .eqIfPresent(UserProjectDO::getType, reqVO.getType())
                .eqIfPresent(UserProjectDO::getIsRecord, reqVO.getIsRecord())
                .orderByDesc(UserProjectDO::getId));
    }
}
