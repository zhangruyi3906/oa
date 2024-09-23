package com.lh.oa.module.bpm.dal.mysql.businessForm.performanceExamine;

import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.performanceExamine.BpmPerformanceExamineDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author tanghanlin
 * @since 2023-10-24
 */
@Mapper
public interface BpmPerformanceExamineDetailMapper extends BaseMapperX<BpmPerformanceExamineDetail> {

    default List<BpmPerformanceExamineDetail> findByFormId(Long examineFormId) {
        return selectList(new LambdaQueryWrapperX<BpmPerformanceExamineDetail>()
                .eqIfPresent(BpmPerformanceExamineDetail::getPerformanceExamineFormId, examineFormId));
    }

}
