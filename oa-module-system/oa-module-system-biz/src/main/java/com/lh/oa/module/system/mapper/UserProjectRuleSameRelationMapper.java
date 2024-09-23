package com.lh.oa.module.system.mapper;

import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.module.system.full.entity.attandance.UserProjectRuleSameRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


/**
 * @author tanghanlin
 * @since 2023-12-08
 */
@Mapper
public interface UserProjectRuleSameRelationMapper extends BaseMapperX<UserProjectRuleSameRelation> {

    void batchDeleteByIds(@Param("ids") Set<Integer> ids);

    List<UserProjectRuleSameRelation> selectHireUserPage(@Param("userIds") Set<Long> userIds,
                                                         @Param("hireDate") String hireDate,
                                                         @Param("page") Integer page,
                                                         @Param("pageSize") Integer pageSize);

    Integer selectHireUserCount(@Param("userIds") Set<Long> userIds,
                                @Param("hireDate") String hireDate);

}
