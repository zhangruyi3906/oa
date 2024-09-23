package com.lh.oa.module.system.convert.fundtransfer;

import java.util.*;

import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferCreateReqVO;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferExcelVO;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferRespVO;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.fundtransfer.FundTransferDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.*;

/**
 * 资金划拨 Convert
 *
 * @author 管理员
 */
@Mapper
public interface FundTransferConvert {

    FundTransferConvert INSTANCE = Mappers.getMapper(FundTransferConvert.class);

    FundTransferDO convert(FundTransferCreateReqVO bean);

    FundTransferDO convert(FundTransferUpdateReqVO bean);

    FundTransferRespVO convert(FundTransferDO bean);

    List<FundTransferRespVO> convertList(List<FundTransferDO> list);

    PageResult<FundTransferRespVO> convertPage(PageResult<FundTransferDO> page);

    List<FundTransferExcelVO> convertList02(List<FundTransferDO> list);

}
