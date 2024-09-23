package com.lh.oa.module.system.convert.salarysettlement;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.salarysettlement.vo.SalarySettlementCreateReqVO;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.SalarySettlementExcelVO;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.SalarySettlementRespVO;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.SalarySettlementUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.salarysettlement.SalarySettlementDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.*;

/**
 * 员工工资结算 Convert
 *
 * @author
 */
@Mapper
public interface SalarySettlementConvert {

    SalarySettlementConvert INSTANCE = Mappers.getMapper(SalarySettlementConvert.class);

    SalarySettlementDO convert(SalarySettlementCreateReqVO bean);

    SalarySettlementDO convert(SalarySettlementUpdateReqVO bean);

    SalarySettlementRespVO convert(SalarySettlementDO bean);

    List<SalarySettlementRespVO> convertList(List<SalarySettlementDO> list);

    PageResult<SalarySettlementRespVO> convertPage(PageResult<SalarySettlementDO> page);

    List<SalarySettlementExcelVO> convertList02(List<SalarySettlementDO> list);

    List<SalarySettlementDO> convertList03(List<SalarySettlementCreateReqVO> list);

}
