package com.lh.oa.module.bpm.service.task;


import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.task.vo.copy.BpmCopy;
import com.lh.oa.module.bpm.controller.admin.task.vo.copy.BpmCopyPageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskVo;

import javax.validation.Valid;
import java.util.List;

/**
 * 流程抄送Service接口
 *
 * @author KonBAI
 * @date 2022-05-19
 */
public interface BpmCopyService {

    /**
     * 查询流程抄送
     *
     * @param copyId 流程抄送主键
     * @return 流程抄送
     */
    BpmCopy queryById(Long copyId);

    /**
     * 查询流程抄送列表
     *
     * @param bpmCopyPageReqVO 流程抄送
     * @return 流程抄送集合
     */
    PageResult<BpmCopy> selectPageList(BpmCopyPageReqVO bpmCopyPageReqVO);

    /**
     * 查询流程抄送列表
     *
     * @param bpmCopy 流程抄送
     * @return 流程抄送集合
     */
    List<BpmCopy> selectList(BpmCopy bpmCopy);

    /**
     * 抄送
     * @param bpmTaskVo
     * @return
     */
    void makeCopy(@Valid BpmTaskVo bpmTaskVo);

    void readCopy(Long copyId);

    Integer unreadCount(Long userId);
}
