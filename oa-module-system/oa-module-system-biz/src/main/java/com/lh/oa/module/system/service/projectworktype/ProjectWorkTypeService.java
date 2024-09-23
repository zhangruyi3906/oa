package com.lh.oa.module.system.service.projectworktype;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.*;
import com.lh.oa.module.system.dal.dataobject.projectworktype.ProjectWorkTypeDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypeCreateReqVO;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypeExportReqVO;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypePageReqVO;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypeUpdateReqVO;

/**
 * 项目工种 Service 接口
 *
 * @author
 */
public interface ProjectWorkTypeService {

    /**
     * 创建项目工种
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createProjectWorkType(@Valid ProjectWorkTypeCreateReqVO createReqVO);

    /**
     * 更新项目工种
     *
     * @param updateReqVO 更新信息
     */
    void updateProjectWorkType(@Valid ProjectWorkTypeUpdateReqVO updateReqVO);

    /**
     * 删除项目工种
     *
     * @param id 编号
     */
    void deleteProjectWorkType(Integer id);

    /**
     * 获得项目工种
     *
     * @param id 编号
     * @return 项目工种
     */
    ProjectWorkTypeDO getProjectWorkType(Integer id);

    /**
     * 获得项目工种列表
     *
     * @param ids 编号
     * @return 项目工种列表
     */
    List<ProjectWorkTypeDO> getProjectWorkTypeList(Collection<Integer> ids);

    /**
     * 获得项目工种分页
     *
     * @param pageReqVO 分页查询
     * @return 项目工种分页
     */
    PageResult<ProjectWorkTypeDO> getProjectWorkTypePage(ProjectWorkTypePageReqVO pageReqVO);

    /**
     * 获得项目工种列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 项目工种列表
     */
    List<ProjectWorkTypeDO> getProjectWorkTypeList(ProjectWorkTypeExportReqVO exportReqVO);

}
