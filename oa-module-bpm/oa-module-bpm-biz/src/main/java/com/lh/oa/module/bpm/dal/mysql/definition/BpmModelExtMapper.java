package com.lh.oa.module.bpm.dal.mysql.definition;

import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmModelExtDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BpmModelExtMapper extends BaseMapperX<BpmModelExtDO> {
    default void updateByModelId(BpmModelExtDO updateObj) {
        update(updateObj, new LambdaQueryWrapperX<BpmModelExtDO>()
                .eq(BpmModelExtDO::getModelId, updateObj.getModelId()));
    }

    default BpmModelExtDO selectByModelId(String modelId) {
        return selectOne(new LambdaQueryWrapperX<BpmModelExtDO>().eqIfPresent(BpmModelExtDO::getModelId, modelId));
    }

    default void deleteByModelId(String modelId){
        delete(new LambdaQueryWrapperX<BpmModelExtDO>().eqIfPresent(BpmModelExtDO::getModelId, modelId));
    }
}
