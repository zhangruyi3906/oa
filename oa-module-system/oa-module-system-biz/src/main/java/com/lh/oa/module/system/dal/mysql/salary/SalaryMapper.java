package com.lh.oa.module.system.dal.mysql.salary;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.dal.dataobject.salary.SalaryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SalaryMapper extends BaseMapperX<SalaryDO> {
    default List<SalaryDO> getSalaryListByMonth(String month) {
        return selectList(new QueryWrapper<SalaryDO>().eq("month", month));
    }
}
