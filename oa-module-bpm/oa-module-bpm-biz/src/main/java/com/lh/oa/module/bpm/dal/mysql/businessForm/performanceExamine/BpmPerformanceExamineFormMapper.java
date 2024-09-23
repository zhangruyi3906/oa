package com.lh.oa.module.bpm.dal.mysql.businessForm.performanceExamine;

import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.invoicingApply.BpmInvoicingApplyForm;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.performanceExamine.BpmPerformanceExamineForm;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author tanghanlin
 * @since 2023-10-24
 */
@Mapper
public interface BpmPerformanceExamineFormMapper extends BaseMapperX<BpmPerformanceExamineForm> {

    default PageResult<BpmPerformanceExamineForm> selectPage(PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<BpmPerformanceExamineForm>()
                .orderByDesc(BpmPerformanceExamineForm::getId));
    }

}
