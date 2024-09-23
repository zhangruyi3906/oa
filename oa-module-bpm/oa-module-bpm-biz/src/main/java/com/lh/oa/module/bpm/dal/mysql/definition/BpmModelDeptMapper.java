package com.lh.oa.module.bpm.dal.mysql.definition;

import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmModelDeptDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface BpmModelDeptMapper extends BaseMapperX<BpmModelDeptDO> {
    default List<BpmModelDeptDO> selectListByModelId(String modelId) {
        return selectList(new LambdaQueryWrapperX<BpmModelDeptDO>().eqIfPresent(BpmModelDeptDO::getModelId, modelId));
    }

    default void deleteByModelIdAndDeptId(String modelId, Long deptId) {
        delete(new LambdaQueryWrapperX<BpmModelDeptDO>().eq(BpmModelDeptDO::getModelId, modelId)
                .eq(BpmModelDeptDO::getDeptId, deptId));
    }

    default void createdByModelIdAndDeptIds(String modelId, List<Long> deptIds) {
        insertBatch(deptIds.stream().map(deptId -> new BpmModelDeptDO().setModelId(modelId).setDeptId(deptId)).collect(Collectors.toList()));
    }
}
