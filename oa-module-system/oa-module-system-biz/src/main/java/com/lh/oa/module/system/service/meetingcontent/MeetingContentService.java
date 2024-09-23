package com.lh.oa.module.system.service.meetingcontent;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentCreateReqVO;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentExportReqVO;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentPageReqVO;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentUpdateReqVO;
import com.lh.oa.module.system.dal.dataobject.meetingcontent.MeetingContentDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 会议记录 Service 接口
 *
 * @author didida
 */
public interface MeetingContentService {

    /**
     * 创建会议记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMeetingContent(@Valid MeetingContentCreateReqVO createReqVO);

    /**
     * 更新会议记录
     *
     * @param updateReqVO 更新信息
     */
    void updateMeetingContent(@Valid MeetingContentUpdateReqVO updateReqVO);

    /**
     * 删除会议记录
     *
     * @param id 编号
     */
    void deleteMeetingContent(Long id);

    /**
     * 获得会议记录
     *
     * @param id 编号
     * @return 会议记录
     */
    MeetingContentDO getMeetingContent(Long id);

    /**
     * 获得会议记录列表
     *
     * @param ids 编号
     * @return 会议记录列表
     */
    List<MeetingContentDO> getMeetingContentList(Collection<Long> ids);

    /**
     * 获得会议记录分页
     *
     * @param pageReqVO 分页查询
     * @return 会议记录分页
     */
    PageResult<MeetingContentDO> getMeetingContentPage(MeetingContentPageReqVO pageReqVO);

    /**
     * 获得会议记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 会议记录列表
     */
    List<MeetingContentDO> getMeetingContentList(MeetingContentExportReqVO exportReqVO);


    MeetingContentDO getMeetingContentByMeetId(Long MeetId);

}
