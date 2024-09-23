package com.lh.oa.module.bpm.dal.mysql.fixAssetPurchase;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo.FixAssetPurchasePageReqVo;
import com.lh.oa.module.bpm.dal.dataobject.fixAssetPurchase.FixAssetPurchaseDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FixAssetPurchaseMapper extends BaseMapperX<FixAssetPurchaseDO> {
    default PageResult<FixAssetPurchaseDO> selectPage(FixAssetPurchasePageReqVo pageVO) {
        return selectPage(pageVO, new LambdaQueryWrapperX<FixAssetPurchaseDO>()
                .orderByDesc(FixAssetPurchaseDO::getId));
    }
}
