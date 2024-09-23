package com.lh.oa.module.system.service.impl;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.dal.dataobject.systemDeptPunish.SystemDeptPunish;
import com.lh.oa.module.system.dal.dataobject.systemDeptPunish.SystemDeptPunishQuery;

import java.util.List;

/**
 * <p>
 * 部门考勤处罚表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-14
 */
public interface ISystemDeptPunishService {

    void updateById(SystemDeptPunish systemDeptPunish);

    void insert(SystemDeptPunish systemDeptPunish);

    void deleteById(Long id);

    SystemDeptPunish selectById(Long id);

    List<SystemDeptPunish> selectList();

    PageResult selectPage(SystemDeptPunishQuery query);
}
