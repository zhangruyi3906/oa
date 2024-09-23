package com.lh.oa.module.bpm.dal.mysql.definition;

import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormFieldExportDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BpmFormFieldExportMapper extends BaseMapperX<BpmFormFieldExportDO> {
    default List<BpmFormFieldExportDO> selectListByFormId(Long formId) {
        return selectList(new LambdaQueryWrapperX<BpmFormFieldExportDO>().eqIfPresent(BpmFormFieldExportDO::getFormId, formId));
    }

    default void deleteByFormId(Long formId) {
        delete(new LambdaQueryWrapperX<BpmFormFieldExportDO>().eq(BpmFormFieldExportDO::getFormId, formId));
    }

    default void createdByBpmFormFieldExportExportList(List<BpmFormFieldExportDO> BpmFormFieldExportDOList) {
        insertBatch(BpmFormFieldExportDOList);
    }
}
