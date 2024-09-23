package com.lh.oa.module.system.service.fundtransfer;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.*;
import com.lh.oa.module.system.dal.dataobject.fundtransfer.FundTransferDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferCreateReqVO;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferExportReqVO;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferPageReqVO;
import com.lh.oa.module.system.controller.admin.fundtransfer.vo.FundTransferUpdateReqVO;

/**
 * 资金划拨 Service 接口
 *
 * @author 管理员
 */
public interface FundTransferService {

    /**
     * 创建资金划拨
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFundTransfer(@Valid FundTransferCreateReqVO createReqVO);

    /**
     * 更新资金划拨
     *
     * @param updateReqVO 更新信息
     */
    void updateFundTransfer(@Valid FundTransferUpdateReqVO updateReqVO);

    /**
     * 删除资金划拨
     *
     * @param id 编号
     */
    void deleteFundTransfer(Long id);

    /**
     * 获得资金划拨
     *
     * @param id 编号
     * @return 资金划拨
     */
    FundTransferDO getFundTransfer(Long id);

    /**
     * 获得资金划拨列表
     *
     * @param ids 编号
     * @return 资金划拨列表
     */
    List<FundTransferDO> getFundTransferList(Collection<Long> ids);

    /**
     * 获得资金划拨分页
     *
     * @param pageReqVO 分页查询
     * @return 资金划拨分页
     */
    PageResult<FundTransferDO> getFundTransferPage(FundTransferPageReqVO pageReqVO);

    /**
     * 获得资金划拨列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 资金划拨列表
     */
    List<FundTransferDO> getFundTransferList(FundTransferExportReqVO exportReqVO);

}
