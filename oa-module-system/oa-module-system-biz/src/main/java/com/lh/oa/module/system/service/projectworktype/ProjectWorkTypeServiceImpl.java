package com.lh.oa.module.system.service.projectworktype;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypeCreateReqVO;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypeExportReqVO;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypePageReqVO;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.ProjectWorkTypeUpdateReqVO;
import com.lh.oa.module.system.convert.projectworktype.ProjectWorkTypeConvert;
import com.lh.oa.module.system.dal.dataobject.projectworktype.ProjectWorkTypeDO;
import com.lh.oa.module.system.dal.mysql.projectworktype.ProjectWorkTypeMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.lh.oa.module.system.controller.admin.projectworktype.vo.*;
import com.lh.oa.framework.common.pojo.PageResult;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 项目工种 Service 实现类
 *
 * @author
 */
@Service
@Validated
public class ProjectWorkTypeServiceImpl implements ProjectWorkTypeService {

    @Resource
    private ProjectWorkTypeMapper projectWorkTypeMapper;

    @Override
    public Integer createProjectWorkType(ProjectWorkTypeCreateReqVO createReqVO) {
        // 插入
        ProjectWorkTypeDO projectWorkType = ProjectWorkTypeConvert.INSTANCE.convert(createReqVO);
        projectWorkTypeMapper.insert(projectWorkType);
        // 返回
        return projectWorkType.getId();
    }

    @Override
    public void updateProjectWorkType(ProjectWorkTypeUpdateReqVO updateReqVO) {
        // 校验存在
        validateProjectWorkTypeExists(updateReqVO.getId());
        // 更新
        ProjectWorkTypeDO updateObj = ProjectWorkTypeConvert.INSTANCE.convert(updateReqVO);
        projectWorkTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteProjectWorkType(Integer id) {
        // 校验存在
        validateProjectWorkTypeExists(id);
        // 删除
        projectWorkTypeMapper.deleteById(id);
    }

    private void validateProjectWorkTypeExists(Integer id) {
        if (projectWorkTypeMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PROJECT_WORK_TYPE_NOT_EXISTS);
        }
    }

    @Override
    public ProjectWorkTypeDO getProjectWorkType(Integer id) {
        return projectWorkTypeMapper.selectById(id);
    }

    @Override
    public List<ProjectWorkTypeDO> getProjectWorkTypeList(Collection<Integer> ids) {
        return projectWorkTypeMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ProjectWorkTypeDO> getProjectWorkTypePage(ProjectWorkTypePageReqVO pageReqVO) {
        return projectWorkTypeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProjectWorkTypeDO> getProjectWorkTypeList(ProjectWorkTypeExportReqVO exportReqVO) {
        return projectWorkTypeMapper.selectList(exportReqVO);
    }

}
