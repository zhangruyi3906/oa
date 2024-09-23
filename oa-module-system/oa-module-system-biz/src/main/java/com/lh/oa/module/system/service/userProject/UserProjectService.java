package com.lh.oa.module.system.service.userProject;

import java.util.*;
import javax.validation.*;

import com.lh.oa.module.system.controller.admin.userProject.vo.*;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.userProject.vo.UserProjectCreateReqVO;
import com.lh.oa.module.system.controller.admin.userProject.vo.UserProjectExportReqVO;
import com.lh.oa.module.system.controller.admin.userProject.vo.UserProjectPageReqVO;
import com.lh.oa.module.system.controller.admin.userProject.vo.UserProjectUpdateReqVO;

/**
 * 人员项目 Service 接口
 *
 * @author
 */
public interface UserProjectService {

    /**
     * 创建人员项目
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUserProject(@Valid UserProjectCreateReqVO createReqVO);

    /**
     * 更新人员项目
     *
     * @param updateReqVO 更新信息
     */
    void updateUserProject(@Valid UserProjectUpdateReqVO updateReqVO);

    /**
     * 删除人员项目
     *
     * @param id 编号
     */
    void deleteUserProject(Long id);

    void deleteUserProject(Long id, boolean sync);

    /**
     * 获得人员项目
     *
     * @param id 编号
     * @return 人员项目
     */
    UserProjectDO getUserProject(Long id);

    /**
     * 获得人员项目列表
     *
     * @param ids 编号
     * @return 人员项目列表
     */
    List<UserProjectDO> getUserProjectListByUserIds(Collection<Long> ids);

    /**
     * 获得人员项目分页
     *
     * @param pageReqVO 分页查询
     * @return 人员项目分页
     */
    PageResult<UserProjectDO> getUserProjectPage(UserProjectPageReqVO pageReqVO);

    /**
     * 获得人员项目列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 人员项目列表
     */
    List<UserProjectDO> getUserProjectListByUserIds(UserProjectExportReqVO exportReqVO);


    Boolean getProject(Long id);

    List<UserProjectUserVO> getProjectUser(Long projectId);

    /**
     * 项目管理平台编辑同步至oa
     *
     * @param baseVO
     */
    void updateUserProjectFromPMS(UserProjectBaseVO baseVO);

    /**
     * 项目管理平台删除同步至oa
     */
    void deleteUserProjectFromPMS(Long userId, Long projectId);

    /**
     * 项目管理平台批量同步人员项目
     */
    Boolean syncBatchFromPMS(List<UserProjectCreateReqVO> list);

    List<UserProjectDO> getAllUserProjectSimpleList(UserProjectPageReqVO userProjectPageReqVO);

//    PageResult<UserProjectDO> filter(PageResult<UserProjectDO> pageResult);
}
