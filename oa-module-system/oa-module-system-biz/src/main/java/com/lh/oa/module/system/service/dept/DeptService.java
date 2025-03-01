package com.lh.oa.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 部门 Service 接口
 *
 * @author
 */
public interface DeptService {

    /**
     * 初始化部门的本地缓存
     */
    void initLocalCache();

    /**
     * 创建部门
     *
     * @param reqVO 部门信息
     * @return 部门编号
     */
    Long createDept(DeptCreateReqVO reqVO);

    /**
     * 更新部门
     *
     * @param reqVO 部门信息
     */
    void updateDept(DeptUpdateReqVO reqVO);

    /**
     * 删除部门
     *
     * @param id 部门编号
     */
    void deleteDept(Long id,Boolean notSyncJnt);

    /**
     * 同步部门
     *
     * @param id 部门id
     */
    void handSyncDept(Long id);

    /**
     * 筛选部门列表
     *
     * @param reqVO 筛选条件请求 VO
     * @return 部门列表
     */
    List<DeptDO> getDeptList(DeptListReqVO reqVO);

    List<DeptDO> getDeptByDeptName(String deptName);

    List<DeptDO> getDeptByDeptNames(Set<String> deptNames);

    /**
     * 获得所有子部门，从缓存中
     *
     * @param parentId 部门编号
     * @param recursive 是否递归获取所有
     * @return 子部门列表
     */
    List<DeptDO> getDeptListByParentIdFromCache(Long parentId, boolean recursive);

    /**
     * 获得部门信息数组
     *
     * @param ids 部门编号数组
     * @return 部门信息数组
     */
    List<DeptDO> getDeptList(Collection<Long> ids);

    /**
     * 获得指定编号的部门 Map
     *
     * @param ids 部门编号数组
     * @return 部门 Map
     */
    default Map<Long, DeptDO> getDeptMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<DeptDO> list = getDeptList(ids);
        return CollectionUtils.convertMap(list, DeptDO::getId);
    }

    /**
     * 获得部门信息
     *
     * @param id 部门编号
     * @return 部门信息
     */
    DeptDO getDept(Long id);

    /**
     * 校验部门们是否有效。如下情况，视为无效：
     * 1. 部门编号不存在
     * 2. 部门被禁用
     *
     * @param ids 角色编号数组
     */
    void validateDeptList(Collection<Long> ids);

    void validateDeptListByDeptName(String deptNames);

    /**
     * 获取上级公司
     */
    DeptDO getParentOrgOfCompany(Long id);

    /**
     * 获取上级部门
     */
    DeptDO getParentDeptOfDept(Long id);
    /**
     * 获取上级部门
     */
    List<DeptDO> getDeptAndChildList(Long deptId);

    /**
     * 获取全部一级部门
     *
     * @return
     */
    List<DeptDO> getAllParentDeptList();

    List<DeptRespDTO> getAllDepts();

    List<Long> getDeptAndAllChildDeptList(Long deptId);

    Set<Long> getOrganizationAndChildDeptList(Long deptId);

    Long getOfficeDeptLeaderUserIdByUserId(Long userId);

    List<DeptDO> roleListDepts();

    Set<Long> getOfficeDeptLeaderUserIdByUserIds(Set<Long> userIds);
}
