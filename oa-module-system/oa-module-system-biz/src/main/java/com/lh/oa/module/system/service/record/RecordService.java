package com.lh.oa.module.system.service.record;

import java.util.*;
import javax.validation.*;

import com.lh.oa.module.system.controller.admin.record.vo.*;
import com.lh.oa.module.system.dal.dataobject.record.RecordDO;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.record.vo.RecordCreateReqVO;
import com.lh.oa.module.system.controller.admin.record.vo.RecordExportReqVO;
import com.lh.oa.module.system.controller.admin.record.vo.RecordPageReqVO;
import com.lh.oa.module.system.controller.admin.record.vo.RecordUpdateReqVO;

/**
 * 打卡记录 Service 接口
 *
 * @author
 */
public interface RecordService {

    /**
     * 创建打卡记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Boolean createRecord(@Valid RecordCreateReqVO createReqVO);




    /**
     * 更新打卡记录
     *
     * @param updateReqVO 更新信息
     */
    void updateRecord(@Valid RecordUpdateReqVO updateReqVO);

    /**
     * 删除打卡记录
     *
     * @param id 编号
     */
    void deleteRecord(Long id);

    /**
     * 获得打卡记录
     *
     * @param id 编号
     * @return 打卡记录
     */
    RecordDO getRecord(Long id);

    /**
     * 获得打卡记录列表
     *
     * @param ids 编号
     * @return 打卡记录列表
     */
    List<RecordDO> getRecordList(Collection<Long> ids);

    /**
     * 获得打卡记录分页
     *
     * @param pageReqVO 分页查询
     * @return 打卡记录分页
     */
    PageResult<RecordDO> getRecordPage(RecordPageReqVO pageReqVO);

    /**
     * 获得打卡记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 打卡记录列表
     */
    List<RecordDO> getRecordList(RecordExportReqVO exportReqVO);

}
