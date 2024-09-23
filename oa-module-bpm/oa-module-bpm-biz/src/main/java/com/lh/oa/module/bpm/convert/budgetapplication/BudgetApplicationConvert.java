package com.lh.oa.module.bpm.convert.budgetapplication;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.BudgetApplicationCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.BudgetApplicationRespVO;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.BudgetApplicationUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.bpm.controller.admin.budgetapplication.vo.*;
import com.lh.oa.module.bpm.dal.dataobject.budgetapplication.BudgetApplicationDO;

/**
 * 资金预算申请 Convert
 *
 * @author 管理员
 */
@Mapper
public interface BudgetApplicationConvert {

    BudgetApplicationConvert INSTANCE = Mappers.getMapper(BudgetApplicationConvert.class);

    BudgetApplicationDO convert(BudgetApplicationCreateReqVO bean);

    BudgetApplicationDO convert(BudgetApplicationUpdateReqVO bean);

    BudgetApplicationRespVO convert(BudgetApplicationDO bean);

    List<BudgetApplicationRespVO> convertList(List<BudgetApplicationDO> list);

    PageResult<BudgetApplicationRespVO> convertPage(PageResult<BudgetApplicationDO> page);

//    List<BudgetApplicationExcelVO> convertList02(List<budgetApplicationDO> list);

}
