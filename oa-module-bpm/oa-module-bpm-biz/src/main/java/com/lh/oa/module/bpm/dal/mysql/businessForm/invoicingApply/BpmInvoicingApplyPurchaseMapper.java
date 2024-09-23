package com.lh.oa.module.bpm.dal.mysql.businessForm.invoicingApply;

import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.invoicingApply.BpmInvoicingApplyPurchase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author tanghanlin
 * @since 2023/10/21
 */
@Mapper
public interface BpmInvoicingApplyPurchaseMapper extends BaseMapperX<BpmInvoicingApplyPurchase> {

    default List<BpmInvoicingApplyPurchase> findByFormId(Long invoicingApplyFormId) {
        return selectList(new LambdaQueryWrapperX<BpmInvoicingApplyPurchase>()
                .eqIfPresent(BpmInvoicingApplyPurchase::getBpmInvoicingApplyFormId, invoicingApplyFormId));
    }

}
