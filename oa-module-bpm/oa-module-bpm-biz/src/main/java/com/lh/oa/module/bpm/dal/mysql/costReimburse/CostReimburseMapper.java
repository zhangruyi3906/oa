package com.lh.oa.module.bpm.dal.mysql.costReimburse;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.controller.admin.costReimburse.vo.CostReimbursePageReqVo;
import com.lh.oa.module.bpm.dal.dataobject.costReimburse.CostReimburseDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CostReimburseMapper extends BaseMapperX<CostReimburseDO> {

    default PageResult<CostReimburseDO> selectPage(CostReimbursePageReqVo pageVO){
        return selectPage(pageVO, new LambdaQueryWrapperX<CostReimburseDO>()
                .orderByDesc(CostReimburseDO::getId));
    }
}
