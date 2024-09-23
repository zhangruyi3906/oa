package com.lh.oa.module.bpm.dal.mysql.definition;

import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormFieldShowDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BpmFormFieldShowMapper extends BaseMapperX<BpmFormFieldShowDO> {
    default List<BpmFormFieldShowDO> selectListByFormId(Long formId) {
        return selectList(new LambdaQueryWrapperX<BpmFormFieldShowDO>().eqIfPresent(BpmFormFieldShowDO::getFormId, formId));
    }

    default void deleteByFormId(Long formId) {
        delete(new LambdaQueryWrapperX<BpmFormFieldShowDO>().eq(BpmFormFieldShowDO::getFormId, formId));
    }

    default void createdByBpmFormFieldShowDOList(List<BpmFormFieldShowDO> bpmFormFieldShowDOList) {
        insertBatch(bpmFormFieldShowDOList);
    }
}
