package com.lh.oa.module.system.mapper;

import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.dal.dataobject.baseRegion.BaseRegion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 区域信息表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2023-09-13
 */
@Mapper
public interface BaseRegionMapper extends BaseMapperX<BaseRegion> {
    List<BaseRegion> selectAll();
}
