package com.lh.oa.module.bpm.convert.approvalprocess;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.ApprovalProcessCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.ApprovalProcessRespVO;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.ApprovalProcessUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.bpm.controller.admin.approvalprocess.vo.*;
import com.lh.oa.module.bpm.dal.dataobject.approvalprocess.ApprovalProcessDO;

/**
 * 项目立项 Convert
 *
 * @author 狗蛋
 */
@Mapper
public interface ApprovalProcessConvert {

    ApprovalProcessConvert INSTANCE = Mappers.getMapper(ApprovalProcessConvert.class);

    ApprovalProcessDO convert(ApprovalProcessCreateReqVO bean);

    ApprovalProcessDO convert(ApprovalProcessUpdateReqVO bean);

    ApprovalProcessRespVO convert(ApprovalProcessDO bean);

    List<ApprovalProcessRespVO> convertList(List<ApprovalProcessDO> list);

    PageResult<ApprovalProcessRespVO> convertPage(PageResult<ApprovalProcessDO> page);

//    List<ApprovalProcessExcelVO> convertList02(List<ApprovalProcessDO> list);

}
