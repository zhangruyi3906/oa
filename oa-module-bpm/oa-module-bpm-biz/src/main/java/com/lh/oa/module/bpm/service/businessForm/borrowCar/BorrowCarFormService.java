package com.lh.oa.module.bpm.service.businessForm.borrowCar;

import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.param.BorrowCarFormParam;
import com.lh.oa.module.bpm.controller.admin.businessForm.borrowCar.vo.BorrowCarFormVo;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.borrowCar.BpmBorrowCarSubsidy;

/**
 * @author tanghanlin
 * @since 2023/10/21
 */
public interface BorrowCarFormService {

    /**
     * 创建新的用车申请流程
     *
     * @param param 创建参数
     * @return 新的绩效考评申请单id
     */
    Long create(Long userId, BorrowCarFormParam param);

    /**
     * 更新流程执行结果，由监听器触发
     *
     * @param id     绩效考评申请单id
     * @param result 流程执行结果
     */
    void updateResult(Long id, Integer result);

    /**
     * 分页查询绩效考评申请单
     *
     * @param pageParam 分页参数
     * @return 绩效考评申请单分页列表
     */
    PageResult<BorrowCarFormVo> queryPageByParam(PageParam pageParam);

    /**
     * 查询绩效考评申请单详情
     *
     * @param id 绩效考评申请单id
     * @return 绩效考评申请单详情信息
     */
    BorrowCarFormVo queryDetail(Long id);

    /**
     * 根据汽车排量类型查询汽车补贴详情
     * @param type 汽车排量类型
     * @return BpmBorrowCarSubsidy 汽车补贴详情
     */
    BpmBorrowCarSubsidy queryCarSubsidyDetail(Integer type);
}
