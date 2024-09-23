package com.lh.oa.module.system.mapper;


import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.api.companyCar.CompanyCar;
import com.lh.oa.module.system.dal.dataobject.companyCar.CompanyCarQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2023-09-05
 */
@Mapper
public interface CompanyCarMapper extends BaseMapperX<CompanyCar> {

    default PageResult<CompanyCar> selectPage(CompanyCarQuery query){
        PageResult<CompanyCar> companyCarPageResult = selectPage(query, new LambdaQueryWrapperX<CompanyCar>()
                .eqIfPresent(CompanyCar::getType, query.getType())
                .eqIfPresent(CompanyCar::getModel, query.getModel())
                .eqIfPresent(CompanyCar::getColor, query.getColor())
                .eqIfPresent(CompanyCar::getStatus, query.getStatus())
                .eqIfPresent(CompanyCar::getDeptId, query.getDeptId())
                .eqIfPresent(CompanyCar::getProjectId, query.getProjectId())
                .orderByDesc(CompanyCar::getId)
        );
        return companyCarPageResult;
    }

    List<CompanyCar> free();

    default CompanyCar getByPlateNumber(String plateNumber){
        return selectOne("plate_number", plateNumber);
    }
}
