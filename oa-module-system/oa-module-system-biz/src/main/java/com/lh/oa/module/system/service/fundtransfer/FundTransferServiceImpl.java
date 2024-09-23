package com.lh.oa.module.system.service.fundtransfer;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferCreateReqVO;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferExportReqVO;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferPageReqVO;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferUpdateReqVO;
import com.lh.oa.module.system.convert.fundtransfer.FundTransferConvert;
import com.lh.oa.module.system.dal.dataobject.fundtransfer.FundTransferDO;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.*;
import com.lh.oa.framework.common.pojo.PageResult;

import com.lh.oa.module.system.dal.mysql.fundtransfer.FundTransferMapper;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 资金划拨 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class FundTransferServiceImpl implements FundTransferService {

    @Resource
    private FundTransferMapper fundTransferMapper;

    @Override
    public Long createFundTransfer(FundTransferCreateReqVO createReqVO) {
        // 插入
        FundTransferDO fundTransfer = FundTransferConvert.INSTANCE.convert(createReqVO);
        fundTransferMapper.insert(fundTransfer);
        // 返回
        return fundTransfer.getId();
    }

    @Override
    public void updateFundTransfer(FundTransferUpdateReqVO updateReqVO) {
        // 校验存在
        validateFundTransferExists(updateReqVO.getId());
        // 更新
        FundTransferDO updateObj = FundTransferConvert.INSTANCE.convert(updateReqVO);
        fundTransferMapper.updateById(updateObj);
    }

    @Override
    public void deleteFundTransfer(Long id) {
        // 校验存在
        validateFundTransferExists(id);
        // 删除
        fundTransferMapper.deleteById(id);
    }

    private void validateFundTransferExists(Long id) {
        if (fundTransferMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FUND_TRANSFER_NOT_EXISTS);
        }
    }

    @Override
    public FundTransferDO getFundTransfer(Long id) {
        return fundTransferMapper.selectById(id);
    }

    @Override
    public List<FundTransferDO> getFundTransferList(Collection<Long> ids) {
        return fundTransferMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<FundTransferDO> getFundTransferPage(FundTransferPageReqVO pageReqVO) {
        return fundTransferMapper.selectPage(pageReqVO);
    }

    @Override
    public List<FundTransferDO> getFundTransferList(FundTransferExportReqVO exportReqVO) {
        return fundTransferMapper.selectList(exportReqVO);
    }

}
