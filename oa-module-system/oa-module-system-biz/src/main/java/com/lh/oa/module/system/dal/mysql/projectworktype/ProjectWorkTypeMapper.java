package com.lh.oa.module.system.dal.mysql.projectworktype;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypeExportReqVO;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypePageReqVO;
import com.lh.oa.module.system.dal.dataobject.projectworktype.ProjectWorkTypeDO;
import org.apache.ibatis.annotations.Mapper;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.*;

/**
 * 项目工种 Mapper
 *
 * @author
 */
@Mapper
public interface ProjectWorkTypeMapper extends BaseMapperX<ProjectWorkTypeDO> {

    default PageResult<ProjectWorkTypeDO> selectPage(ProjectWorkTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProjectWorkTypeDO>()
                .eqIfPresent(ProjectWorkTypeDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(ProjectWorkTypeDO::getCode, reqVO.getCode())
                .likeIfPresent(ProjectWorkTypeDO::getName, reqVO.getName())
                .eqIfPresent(ProjectWorkTypeDO::getDataSource, reqVO.getDataSource())
                .eqIfPresent(ProjectWorkTypeDO::getOrderNumber, reqVO.getOrderNumber())
                .betweenIfPresent(ProjectWorkTypeDO::getCreatedTime, reqVO.getCreatedTime())
                .eqIfPresent(ProjectWorkTypeDO::getCreatedBy, reqVO.getCreatedBy())
                .betweenIfPresent(ProjectWorkTypeDO::getModifiedTime, reqVO.getModifiedTime())
                .eqIfPresent(ProjectWorkTypeDO::getModifiedBy, reqVO.getModifiedBy())
                .eqIfPresent(ProjectWorkTypeDO::getFlag, reqVO.getFlag())
                .orderByDesc(ProjectWorkTypeDO::getId));
    }

    default List<ProjectWorkTypeDO> selectList(ProjectWorkTypeExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProjectWorkTypeDO>()
                .eqIfPresent(ProjectWorkTypeDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(ProjectWorkTypeDO::getCode, reqVO.getCode())
                .likeIfPresent(ProjectWorkTypeDO::getName, reqVO.getName())
                .eqIfPresent(ProjectWorkTypeDO::getDataSource, reqVO.getDataSource())
                .eqIfPresent(ProjectWorkTypeDO::getOrderNumber, reqVO.getOrderNumber())
                .betweenIfPresent(ProjectWorkTypeDO::getCreatedTime, reqVO.getCreatedTime())
                .eqIfPresent(ProjectWorkTypeDO::getCreatedBy, reqVO.getCreatedBy())
                .betweenIfPresent(ProjectWorkTypeDO::getModifiedTime, reqVO.getModifiedTime())
                .eqIfPresent(ProjectWorkTypeDO::getModifiedBy, reqVO.getModifiedBy())
                .eqIfPresent(ProjectWorkTypeDO::getFlag, reqVO.getFlag())
                .orderByDesc(ProjectWorkTypeDO::getId));
    }

}
