package com.lh.oa.module.system.service.projectrecord;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.*;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordCreateReqVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordPageReqVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.projectrecord.ProjectRecordDO;

public interface ProjectRecordService {

    Boolean createRecordProject(ProjectRecordCreateReqVO createReqVO);


    Boolean updateRecordProject(ProjectRecordUpdateReqVO updateReqVO);

    PageResult<ProjectRecordDO> getRecordProjectPage(ProjectRecordPageReqVO pageVO);
}
