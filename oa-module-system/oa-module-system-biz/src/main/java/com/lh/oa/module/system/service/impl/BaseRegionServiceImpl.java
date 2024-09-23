package com.lh.oa.module.system.service.impl;

import com.lh.oa.module.system.dal.dataobject.baseRegion.BaseRegion;
import com.lh.oa.module.system.mapper.BaseRegionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 区域信息表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-13
 */
@Service
public class BaseRegionServiceImpl implements IBaseRegionService {

    @Resource
    private BaseRegionMapper baseRegionMapper;
    @Override
    public List<BaseRegion> selectList() {
        return baseRegionMapper.selectList();
    }


    @Override
    public List<BaseRegion> treeData() {
        List<BaseRegion> baseRegions = baseRegionMapper.selectList();
        Map<Integer, BaseRegion> collect =
                baseRegions.stream().collect(Collectors.toMap(BaseRegion::getId, item -> item));
        List<BaseRegion> result = new ArrayList<>();
        for (BaseRegion baseRegion : baseRegions) {
            Integer pid = baseRegion.getParentId();
            if (pid == null || pid.equals(0)){
                result.add(baseRegion);
            }else {
                BaseRegion parent = collect.get(pid);
                if (parent != null){
                    parent.getChildren().add(baseRegion);
                }
            }
        }
        return result;
    }
}
