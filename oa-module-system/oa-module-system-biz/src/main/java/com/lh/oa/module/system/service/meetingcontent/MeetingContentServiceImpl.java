package com.lh.oa.module.system.service.meetingcontent;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentCreateReqVO;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentExportReqVO;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentPageReqVO;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.MeetingContentUpdateReqVO;
import com.lh.oa.module.system.convert.meetingcontent.MeetingContentConvert;
import com.lh.oa.module.system.dal.dataobject.meetingcontent.MeetingContentDO;
import com.lh.oa.module.system.dal.mysql.meetingcontent.MeetingContentMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 会议记录 Service 实现类
 *
 * @author didida
 */
@Service
@Validated
public class MeetingContentServiceImpl implements MeetingContentService {

    @Resource
    private MeetingContentMapper meetingContentMapper;

    @Override
    public Long createMeetingContent(MeetingContentCreateReqVO createReqVO) {
        // 插入
        MeetingContentDO meetingContent = MeetingContentConvert.INSTANCE.convert(createReqVO);
        meetingContentMapper.insert(meetingContent);
        // 返回
        return meetingContent.getId();
    }

    @Override
    public void updateMeetingContent(MeetingContentUpdateReqVO updateReqVO) {
        // 校验存在
        validateMeetingContentExists(updateReqVO.getId());
        // 更新
        MeetingContentDO updateObj = MeetingContentConvert.INSTANCE.convert(updateReqVO);
        meetingContentMapper.updateById(updateObj);
    }

    @Override
    public void deleteMeetingContent(Long id) {
        // 校验存在
        validateMeetingContentExists(id);
        // 删除
        meetingContentMapper.deleteById(id);
    }

    private void validateMeetingContentExists(Long id) {
        if (meetingContentMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MEETING_CONTENT_NOT_EXISTS);
        }
    }

    @Override
    public MeetingContentDO getMeetingContent(Long id) {
        return meetingContentMapper.selectById(id);
    }

    @Override
    public List<MeetingContentDO> getMeetingContentList(Collection<Long> ids) {
        return meetingContentMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<MeetingContentDO> getMeetingContentPage(MeetingContentPageReqVO pageReqVO) {
        return meetingContentMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MeetingContentDO> getMeetingContentList(MeetingContentExportReqVO exportReqVO) {
        return meetingContentMapper.selectList(exportReqVO);
    }

    @Override
    public MeetingContentDO getMeetingContentByMeetId(Long MeetId) {
        MeetingContentDO meetingContentByMeetId = meetingContentMapper.getMeetingContentByMeetId(MeetId);
        return meetingContentByMeetId == null ? null : meetingContentByMeetId;
    }

}
