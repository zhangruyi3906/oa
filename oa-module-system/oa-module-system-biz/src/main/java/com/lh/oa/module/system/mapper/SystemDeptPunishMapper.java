package com.lh.oa.module.system.mapper;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.dal.dataobject.systemDeptPunish.SystemDeptPunish;
import com.lh.oa.module.system.dal.dataobject.systemDeptPunish.SystemDeptPunishQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 部门考勤处罚表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2023-09-14
 */
@Mapper
public interface SystemDeptPunishMapper extends BaseMapperX<SystemDeptPunish> {

    default PageResult<SystemDeptPunish> selectPage(SystemDeptPunishQuery query){
        PageResult<SystemDeptPunish> orderMeetPageResult = selectPage(query, new LambdaQueryWrapperX<SystemDeptPunish>()
                .eqIfPresent(SystemDeptPunish::getDeptId, query.getDeptId())
                .orderByDesc(SystemDeptPunish::getId));
        return orderMeetPageResult;
    }
}
