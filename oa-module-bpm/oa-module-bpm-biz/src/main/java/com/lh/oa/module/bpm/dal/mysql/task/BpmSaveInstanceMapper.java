package com.lh.oa.module.bpm.dal.mysql.task;

import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.QueryWrapperX;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmSaveInstanceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BpmSaveInstanceMapper extends BaseMapperX<BpmSaveInstanceDO> {

    default BpmSaveInstanceDO selectBySaveId(Long saveId) {
        return selectOne("save_id", saveId);
    }

    default void deleteBySaveId(String saveId){
        delete(new QueryWrapperX<BpmSaveInstanceDO>().eq("save_id", saveId));
    }
}
