package com.lh.oa.module.system.service.impl;

import com.lh.oa.module.system.dal.dataobject.baseRegion.BaseRegion;

import java.util.List;

/**
 * <p>
 * 区域信息表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-13
 */
public interface IBaseRegionService {

    List<BaseRegion> selectList();

    List<BaseRegion> treeData();
}
