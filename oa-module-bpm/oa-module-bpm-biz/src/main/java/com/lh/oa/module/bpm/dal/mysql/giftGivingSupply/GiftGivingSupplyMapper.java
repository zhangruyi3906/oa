package com.lh.oa.module.bpm.dal.mysql.giftGivingSupply;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo.GiftGivingSupplyPageReqVo;
import com.lh.oa.module.bpm.dal.dataobject.giftGivingSupply.GiftGivingSupplyDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GiftGivingSupplyMapper extends BaseMapperX<GiftGivingSupplyDO> {

    default PageResult<GiftGivingSupplyDO> selectPage(GiftGivingSupplyPageReqVo pageVO) {
        return selectPage(pageVO, new LambdaQueryWrapperX<GiftGivingSupplyDO>()
                .orderByDesc(GiftGivingSupplyDO::getId));
    }
}
