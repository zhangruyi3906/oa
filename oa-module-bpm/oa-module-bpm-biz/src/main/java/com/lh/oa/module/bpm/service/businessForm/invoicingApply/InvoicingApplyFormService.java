package com.lh.oa.module.bpm.service.businessForm.invoicingApply;

import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.param.InvoicingApplyFormCreateParam;
import com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.vo.InvoicingApplyFormVo;

/**
 * @author tanghanlin
 * @since 2023/10/21
 */
public interface InvoicingApplyFormService {

    /**
     * 创建新的开票申请单
     *
     * @param param 创建参数
     * @return 新开票申请单id
     */
    Long create(Long userId, InvoicingApplyFormCreateParam param);

    /**
     * 更新流程执行结果，由监听器触发
     *
     * @param id        开票申请单id
     * @param result    流程执行结果
     */
    void updateResult(Long id, Integer result);

    /**
     * 分页查询开票申请单
     *
     * @param pageParam 分页参数
     * @return 开票申请单分页列表
     */
    PageResult<InvoicingApplyFormVo> queryPageByParam(PageParam pageParam);

    /**
     * 查询开票申请单详情
     *
     * @param id 开票申请单id
     * @return 开票申请单详情信息
     */
    InvoicingApplyFormVo queryDetail(Long id);

}
