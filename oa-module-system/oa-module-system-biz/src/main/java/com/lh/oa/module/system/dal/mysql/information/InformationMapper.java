package com.lh.oa.module.system.dal.mysql.information;

import com.github.yulichang.query.MPJQueryWrapper;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.information.vo.InformationExportReqVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationPageReqVO;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.MonthAttendance;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static cn.hutool.core.util.ObjectUtil.isAllNotEmpty;
import static cn.hutool.core.util.ObjectUtil.isNotEmpty;

/**
 * 员工信息 Mapper
 *
 * @author
 */
@Mapper
public interface InformationMapper extends BaseMapperX<InformationDO> {

    default PageResult<InformationDO> selectPage(InformationPageReqVO reqVO) {
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
        return selectPage(reqVO, new MPJQueryWrapper<InformationDO>()
                .selectAll(InformationDO.class)
                .select("sd.id deptId, sd.name deptName")
                .leftJoin("system_users su on su.id = t.user_id")
                .leftJoin("system_dept sd on sd.id = su.dept_id")
                .eq(isNotEmpty(reqVO.getUserId()), "t.user_id", reqVO.getUserId())
                .like(isNotEmpty(reqVO.getName()), "t.name", reqVO.getName())
                .between(isAllNotEmpty(start, end), "hire_date", start, end)
                .eq(isNotEmpty(reqVO.getDeptId()), "su.dept_id", reqVO.getDeptId())
                .eq(isNotEmpty(reqVO.getDeptName()), "sd.name", reqVO.getDeptName())
                .eq(isNotEmpty(reqVO.getInfoType()), "t.info_type", reqVO.getInfoType())
                .eq("su.deleted", 0)
                .orderByDesc("id"));
    }

    default List<InformationDO> selectList(InformationExportReqVO reqVO) {
        MPJQueryWrapper<InformationDO> wrapper = new MPJQueryWrapper<InformationDO>()
                .selectAll(InformationDO.class)
                .select("su.dept_id")
                .select("sd.id as deptId, sd.name as deptName")
                .leftJoin("system_users su on t.user_id = su.id")
                .leftJoin("system_dept sd on su.dept_id = sd.id");
        if (reqVO.getUserId() != null) {
            wrapper.eq("t.user_id", reqVO.getUserId());
        }
        if (reqVO.getName() != null) {
            wrapper.like("t.name", reqVO.getName());
        }
        if (reqVO.getInfoType() != null) {
            wrapper.eq("t.info_type", reqVO.getInfoType());
        }
        if (reqVO.getHasProbation() != null) {
            wrapper.eq("t.has_probation", reqVO.getHasProbation());
        }
        if (reqVO.getDeptId() != null) {
            wrapper.eq("sd.id", reqVO.getDeptId());
        }
        if (reqVO.getDeptName() != null) {
            wrapper.eq("sd.name", reqVO.getDeptName());
        }
        wrapper.orderByDesc("t.id");
        return selectList(wrapper);
    }


    default List<InformationDO> selectListInDeptIdsOrUserIds(MonthAttendance createReqVO){
        MPJQueryWrapper<InformationDO> wrapper = new MPJQueryWrapper<InformationDO>()
                .selectAll(InformationDO.class)
                .select("su.dept_id")
                .select("sd.id as deptId, sd.name as deptName")
                .leftJoin("system_users su on t.user_id = su.id")
                .leftJoin("system_dept sd on su.dept_id = sd.id");
        if (createReqVO.getDeptIds() != null) {
            wrapper.in("sd.id", createReqVO.getDeptIds());
        }
        if (createReqVO.getUserIds() != null) {
            wrapper.in("su.id", createReqVO.getUserIds());
        }
        return selectList(wrapper);
    }


    default InformationDO getByUserId(Long userId) {
        LambdaQueryWrapperX<InformationDO> wrapper = new LambdaQueryWrapperX<InformationDO>().eqIfPresent(InformationDO::getUserId, userId);
        return selectOne(wrapper);
    }

    default List<InformationDO> getByUserIds(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<InformationDO>()
                .inIfPresent(InformationDO::getUserId, userIds));
    }

}
