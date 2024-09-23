package com.lh.oa.module.system.convert.budgetcapital;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalCreateReqVO;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalExcelVO;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalRespVO;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.BudgetCapitalUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.budgetcapital.BudgetCapitalDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.budgetcapital.vo.*;

/**
 * 资金预算 Convert
 *
 * @author 管理员
 */
@Mapper
public interface BudgetCapitalConvert {

    BudgetCapitalConvert INSTANCE = Mappers.getMapper(BudgetCapitalConvert.class);

    BudgetCapitalDO convert(BudgetCapitalCreateReqVO bean);

    BudgetCapitalDO convert(BudgetCapitalUpdateReqVO bean);

    BudgetCapitalRespVO convert(BudgetCapitalDO bean);

    List<BudgetCapitalRespVO> convertList(List<BudgetCapitalDO> list);

    PageResult<BudgetCapitalRespVO> convertPage(PageResult<BudgetCapitalDO> page);

    List<BudgetCapitalExcelVO> convertList02(List<BudgetCapitalDO> list);

}
