package com.lh.oa.module.bpm.service.attendancecorrection;

import java.util.*;
import javax.validation.*;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.*;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionPageReqVO;
import com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo.CorrectionUpdateReqVO;
import com.lh.oa.module.bpm.dal.dataobject.attendancecorrection.CorrectionDO;
import com.lh.oa.framework.common.pojo.PageResult;

/**
 * 补卡流程 Service 接口
 *
 * @author 狗蛋
 */
public interface CorrectionService {

    /**
     * 创建补卡流程
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCorrection(Long userId,@Valid CorrectionCreateReqVO createReqVO);

    /**
     * 更新补卡流程
     *
     * @param updateReqVO 更新信息
     */
    void updateCorrection(@Valid CorrectionUpdateReqVO updateReqVO);

    /**
     * 删除补卡流程
     *
     * @param id 编号
     */
    void deleteCorrection(Long id);

    /**
     * 获得补卡流程
     *
     * @param id 编号
     * @return 补卡流程
     */
    CorrectionDO getCorrection(Long id);

    /**
     * 获得补卡流程列表
     *
     * @param ids 编号
     * @return 补卡流程列表
     */
    List<CorrectionDO> getCorrectionList(Collection<Long> ids);

    /**
     * 获得补卡流程分页
     *
     * @param pageReqVO 分页查询
     * @return 补卡流程分页
     */
    PageResult<CorrectionDO> getCorrectionPage(CorrectionPageReqVO pageReqVO);

    /**
     * 获得补卡流程列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 补卡流程列表
     */
//    List<CorrectionDO> getCorrectionList(CorrectionExportReqVO exportReqVO);

}
