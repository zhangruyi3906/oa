package com.lh.oa.module.system.service.meeting;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingCreateReqVO;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingExportReqVO;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingPageReqVO;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.meeting.MeetingDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 会议组织 Service 接口
 *
 * @author didida
 */
public interface MeetingService {

    /**
     * 创建会议组织
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMeeting(@Valid MeetingCreateReqVO createReqVO);

    /**
     * 更新会议组织
     *
     * @param updateReqVO 更新信息
     */
    void updateMeeting(@Valid MeetingUpdateReqVO updateReqVO);

    /**
     * 删除会议组织
     *
     * @param id 编号
     */
    void deleteMeeting(Long id);

    /**
     * 获得会议组织
     *
     * @param id 编号
     * @return 会议组织
     */
    MeetingDO getMeeting(Long id);

    /**
     * 获得会议组织列表
     *
     * @param ids 编号
     * @return 会议组织列表
     */
    List<MeetingDO> getMeetingList(Collection<Long> ids);

    /**
     * 获得会议组织分页
     *
     * @param pageReqVO 分页查询
     * @return 会议组织分页
     */
    PageResult<MeetingDO> getMeetingPage(MeetingPageReqVO pageReqVO);

    /**
     * 获得会议组织列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 会议组织列表
     */
    List<MeetingDO> getMeetingList(MeetingExportReqVO exportReqVO);

    List<MeetingDO> getMeetingList();
}
