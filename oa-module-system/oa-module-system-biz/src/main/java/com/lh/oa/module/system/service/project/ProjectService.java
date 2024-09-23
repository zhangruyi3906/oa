package com.lh.oa.module.system.service.project;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.project.vo.*;
import com.lh.oa.module.system.dal.dataobject.project.ProjectDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 项目 Service 接口
 *
 * @author 狗蛋
 */
public interface ProjectService {

    /**
     * 创建项目
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createProject(@Valid ProjectCreateReqVO createReqVO);

    /**
     * 更新项目
     *
     * @param updateReqVO 更新信息
     */
    void updateProject(@Valid ProjectUpdateReqVO updateReqVO);

    /**
     * 删除项目
     *
     * @param id 编号
     */
    void deleteProject(Integer id,Boolean notSyncJnt);

    /**
     * 获得项目
     *
     * @param id 编号
     * @return 项目
     */
    ProjectDO getProject(Integer id);

    /**
     * 获得项目列表
     *
     * @param ids 编号
     * @return 项目列表
     */
    List<ProjectDO> getProjectList(Collection<Integer> ids);

    /**
     * 获得项目分页
     *
     * @param pageReqVO 分页查询
     * @return 项目分页
     */
    PageResult<ProjectDO> getProjectPage(ProjectPageReqVO pageReqVO);

    /**
     * 获得项目列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 项目列表
     */
    List<ProjectDO> getProjectList(ProjectExportReqVO exportReqVO);

    List<ProjectDO> getProjectList();

    List<ProjectDO> getProjectAll();

    void isTopProject(Integer id);

    void disabledProject(Integer id);

    List<ProjectDO> getProjectByOrgId(Long orgId);

    Map<Long, String> getProjectsByUsrId(Long userId);

    Map<Long, ProjectDO> getProjectMap(Set<Long> projectIds);

    List<JntWarehouseProjectVO> getWarehouseProject();

    List<JntMaterialVO> getWarehouseMaterialByProject(Long projectId);

    /**
     * 根据项目id获取项目仓库
     */
    List<JntWarehouseVO> getWarehouseByProjectId(Long projectId);
}
