package com.lh.oa.module.bpm.service.businessForm.performanceExamine;

import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.param.InvoicingApplyFormCreateParam;
import com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.vo.InvoicingApplyFormVo;
import com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.param.PerformanceExamineFormParam;
import com.lh.oa.module.bpm.controller.admin.businessForm.performanceExamine.vo.PerformanceExamineFormVo;

/**
 * @author tanghanlin
 * @since 2023/10/21
 */
public interface PerformanceExamineFormService {

    /**
     * 创建新的绩效考评申请单
     *
     * @param param 创建参数
     * @return 新的绩效考评申请单id
     */
    Long create(Long userId, PerformanceExamineFormParam param);

    /**
     * 更新流程执行结果，由监听器触发
     *
     * @param id        绩效考评申请单id
     * @param result    流程执行结果
     */
    void updateResult(Long id, Integer result);

    /**
     * 分页查询绩效考评申请单
     *
     * @param pageParam 分页参数
     * @return 绩效考评申请单分页列表
     */
    PageResult<PerformanceExamineFormVo> queryPageByParam(PageParam pageParam);

    /**
     * 查询绩效考评申请单详情
     *
     * @param id 绩效考评申请单id
     * @return 绩效考评申请单详情信息
     */
    PerformanceExamineFormVo queryDetail(Long id);

}
