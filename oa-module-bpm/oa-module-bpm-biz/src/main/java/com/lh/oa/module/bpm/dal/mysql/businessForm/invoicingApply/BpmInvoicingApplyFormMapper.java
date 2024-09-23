package com.lh.oa.module.bpm.dal.mysql.businessForm.invoicingApply;

import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.invoicingApply.BpmInvoicingApplyForm;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tanghanlin
 * @since 2023/10/23
 */
@Mapper
public interface BpmInvoicingApplyFormMapper extends BaseMapperX<BpmInvoicingApplyForm> {

    default PageResult<BpmInvoicingApplyForm> selectPage(PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<BpmInvoicingApplyForm>()
                .orderByDesc(BpmInvoicingApplyForm::getId));
    }

}
