package com.lh.oa.module.system.dal.mysql.project;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.project.vo.ProjectExportReqVO;
import com.lh.oa.module.system.controller.admin.project.vo.ProjectPageReqVO;
import com.lh.oa.module.system.dal.dataobject.project.ProjectDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

/**
 * 项目 Mapper
 *
 * @author 狗蛋
 */
@Mapper
public interface ProjectMapper extends BaseMapperX<ProjectDO> {

    default PageResult<ProjectDO> selectPage(List<Long> collect, ProjectPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProjectDO>()
                .in(ProjectDO::getId,collect)
                .eqIfPresent(ProjectDO::getOrgId, reqVO.getOrgId())
                .likeIfPresent(ProjectDO::getName, reqVO.getName())
                .likeIfPresent(ProjectDO::getSimpleName, reqVO.getSimpleName())
                .eqIfPresent(ProjectDO::getSimpleDescription, reqVO.getSimpleDescription())
                .eqIfPresent(ProjectDO::getType, reqVO.getType())
                .eqIfPresent(ProjectDO::getEffectGraphFileId, reqVO.getEffectGraphFileId())
                .eqIfPresent(ProjectDO::getEffectGraphFileUrl, reqVO.getEffectGraphFileUrl())
                .eqIfPresent(ProjectDO::getPlanarGraphFileId, reqVO.getPlanarGraphFileId())
                .eqIfPresent(ProjectDO::getPlanarGraphFileUrl, reqVO.getPlanarGraphFileUrl())
                .betweenIfPresent(ProjectDO::getPlanStartTime, reqVO.getPlanStartTime())
                .betweenIfPresent(ProjectDO::getPlanEndTime, reqVO.getPlanEndTime())
                .likeIfPresent(ProjectDO::getDirectorName, reqVO.getDirectorName())
                .eqIfPresent(ProjectDO::getDirectorMobile, reqVO.getDirectorMobile())
                .eqIfPresent(ProjectDO::getAddress, reqVO.getAddress())
                .eqIfPresent(ProjectDO::getLongitude, reqVO.getLongitude())
                .eqIfPresent(ProjectDO::getLatitude, reqVO.getLatitude())
                .eqIfPresent(ProjectDO::getConstructUnit, reqVO.getConstructUnit())
                .eqIfPresent(ProjectDO::getWorkUnit, reqVO.getWorkUnit())
                .eqIfPresent(ProjectDO::getSuperviseUnit, reqVO.getSuperviseUnit())
                .eqIfPresent(ProjectDO::getDesignUnit, reqVO.getDesignUnit())
                .eqIfPresent(ProjectDO::getSurveyUnit, reqVO.getSurveyUnit())
                .eqIfPresent(ProjectDO::getMicroWebsiteId, reqVO.getMicroWebsiteId())
                .eqIfPresent(ProjectDO::getOrderNumber, reqVO.getOrderNumber())
                .betweenIfPresent(ProjectDO::getModifiedTime, reqVO.getModifiedTime())
                .eqIfPresent(ProjectDO::getModifiedBy, reqVO.getModifiedBy())
                .eqIfPresent(ProjectDO::getFlag, reqVO.getFlag())
                .or(projectDO -> projectDO.eq(ProjectDO::getCreator, getLoginUserId().toString()))
                .orderByDesc(ProjectDO::getId));
    }

    default PageResult<ProjectDO> selectPage(ProjectPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProjectDO>()
                .eqIfPresent(ProjectDO::getOrgId, reqVO.getOrgId())
                .eqIfPresent(ProjectDO::getTypeVal, reqVO.getTypeVal())
                .eqIfPresent(ProjectDO::getType, reqVO.getType())
                .eqIfPresent(ProjectDO::getIsDisabled, reqVO.getIsDisabled())
                .eqIfPresent(ProjectDO::getStatus,reqVO.getStatus())
                .likeIfPresent(ProjectDO::getDirectorName, reqVO.getDirectorName())
                .likeIfPresent(ProjectDO::getConstructUnit, reqVO.getConstructUnit())
                .likeIfPresent(ProjectDO::getName, reqVO.getName())
                .inIfPresent(ProjectDO::getId, reqVO.getProjectIds())
                .orderByDesc(ProjectDO::getId));
    }

    default List<ProjectDO> selectList(ProjectExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProjectDO>()
                .eqIfPresent(ProjectDO::getOrgId, reqVO.getOrgId())
                .likeIfPresent(ProjectDO::getName, reqVO.getName())
                .likeIfPresent(ProjectDO::getSimpleName, reqVO.getSimpleName())
                .eqIfPresent(ProjectDO::getSimpleDescription, reqVO.getSimpleDescription())
                .eqIfPresent(ProjectDO::getType, reqVO.getType())
                .eqIfPresent(ProjectDO::getEffectGraphFileId, reqVO.getEffectGraphFileId())
                .eqIfPresent(ProjectDO::getEffectGraphFileUrl, reqVO.getEffectGraphFileUrl())
                .eqIfPresent(ProjectDO::getPlanarGraphFileId, reqVO.getPlanarGraphFileId())
                .eqIfPresent(ProjectDO::getPlanarGraphFileUrl, reqVO.getPlanarGraphFileUrl())
                .betweenIfPresent(ProjectDO::getPlanStartTime, reqVO.getPlanStartTime())
                .betweenIfPresent(ProjectDO::getPlanEndTime, reqVO.getPlanEndTime())
                .likeIfPresent(ProjectDO::getDirectorName, reqVO.getDirectorName())
                .eqIfPresent(ProjectDO::getDirectorMobile, reqVO.getDirectorMobile())
                .eqIfPresent(ProjectDO::getAddress, reqVO.getAddress())
                .eqIfPresent(ProjectDO::getLongitude, reqVO.getLongitude())
                .eqIfPresent(ProjectDO::getLatitude, reqVO.getLatitude())
                .eqIfPresent(ProjectDO::getConstructUnit, reqVO.getConstructUnit())
                .eqIfPresent(ProjectDO::getWorkUnit, reqVO.getWorkUnit())
                .eqIfPresent(ProjectDO::getSuperviseUnit, reqVO.getSuperviseUnit())
                .eqIfPresent(ProjectDO::getDesignUnit, reqVO.getDesignUnit())
                .eqIfPresent(ProjectDO::getSurveyUnit, reqVO.getSurveyUnit())
                .eqIfPresent(ProjectDO::getMicroWebsiteId, reqVO.getMicroWebsiteId())
                .eqIfPresent(ProjectDO::getOrderNumber, reqVO.getOrderNumber())
                .betweenIfPresent(ProjectDO::getModifiedTime, reqVO.getModifiedTime())
                .eqIfPresent(ProjectDO::getModifiedBy, reqVO.getModifiedBy())
                .eqIfPresent(ProjectDO::getFlag, reqVO.getFlag())
                .or(projectDO -> projectDO.eq(ProjectDO::getCreator, getLoginUserId().toString()))
                .orderByDesc(ProjectDO::getId));
    }

}
