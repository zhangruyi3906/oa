package com.lh.oa.module.bpm.dal.mysql.businessForm.borrowForm;

import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.borrowCar.BpmBorrowCarForm;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tanghanlin
 * @since 2023-10-24
 */
@Mapper
public interface BpmBorrowCarFormMapper extends BaseMapperX<BpmBorrowCarForm> {

    default PageResult<BpmBorrowCarForm> selectPage(PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<BpmBorrowCarForm>()
                .orderByDesc(BpmBorrowCarForm::getId));
    }

}
