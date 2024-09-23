package com.lh.oa.module.system.service.impl;


import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.systemDeptPunish.SystemDeptPunish;
import com.lh.oa.module.system.dal.dataobject.systemDeptPunish.SystemDeptPunishQuery;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.mapper.SystemDeptPunishMapper;
import com.lh.oa.module.system.service.dept.DeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 部门考勤处罚表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2023-09-14
 */
@Service
public class SystemDeptPunishServiceImpl implements ISystemDeptPunishService {

    @Resource
    private SystemDeptPunishMapper systemDeptPunishMapper;

    @Resource
    private DeptService deptService;

    @Override
    public void updateById(SystemDeptPunish systemDeptPunish) {
        Long deptId = systemDeptPunish.getDeptId();
        DeptDO dept = deptService.getDept(deptId);
        systemDeptPunish.setDeptName(dept.getName());
        SystemDeptPunish deptPunish = systemDeptPunishMapper.selectById(systemDeptPunish.getId());
        if(deptPunish == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_SALARY_NOT_EXISTS);
        }
        systemDeptPunishMapper.updateById(systemDeptPunish);
    }

    @Override
    public void insert(SystemDeptPunish systemDeptPunish) {
        Long deptId = systemDeptPunish.getDeptId();
        DeptDO dept = deptService.getDept(deptId);
        systemDeptPunish.setDeptName(dept.getName());
        SystemDeptPunish deptPunish = systemDeptPunishMapper.selectOne("dept_id", systemDeptPunish.getDeptId());
        if(deptPunish != null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_SALARY_IS_EXISTS);
        }
        systemDeptPunishMapper.insert(systemDeptPunish);
    }

    @Override
    public void deleteById(Long id) {
        systemDeptPunishMapper.deleteById(id);
    }

    @Override
    public SystemDeptPunish selectById(Long id) {
        return systemDeptPunishMapper.selectById(id);
    }

    @Override
    public List<SystemDeptPunish> selectList() {
        return systemDeptPunishMapper.selectList();
    }

    @Override
    public PageResult selectPage(SystemDeptPunishQuery query) {
        return systemDeptPunishMapper.selectPage(query);
    }

}
