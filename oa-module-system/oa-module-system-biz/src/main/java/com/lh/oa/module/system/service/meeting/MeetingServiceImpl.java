package com.lh.oa.module.system.service.meeting;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingCreateReqVO;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingExportReqVO;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingPageReqVO;
import com.lh.oa.module.system.controller.admin.meeting.vo.MeetingUpdateReqVO;
import com.lh.oa.module.system.convert.meeting.MeetingConvert;
import com.lh.oa.module.system.dal.dataobject.meeting.MeetingDO;
import com.lh.oa.module.system.dal.mysql.meeting.MeetingMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 会议组织 Service 实现类
 *
 * @author didida
 */
@Service
@Validated
public class MeetingServiceImpl implements MeetingService {

    @Resource
    private MeetingMapper meetingMapper;

    @Override
    public Long createMeeting(MeetingCreateReqVO createReqVO) {
        // 插入
        MeetingDO meeting = MeetingConvert.INSTANCE.convert(createReqVO);
        meetingMapper.insert(meeting);
        // 返回
        return meeting.getId();
    }

    @Override
    public void updateMeeting(MeetingUpdateReqVO updateReqVO) {
        // 校验存在
        validateMeetingExists(updateReqVO.getId());
        // 更新
        MeetingDO updateObj = MeetingConvert.INSTANCE.convert(updateReqVO);
        meetingMapper.updateById(updateObj);
    }

    @Override
    public void deleteMeeting(Long id) {
        // 校验存在
        validateMeetingExists(id);
        // 删除
        meetingMapper.deleteById(id);
    }

    private void validateMeetingExists(Long id) {
        if (meetingMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MEETING_NOT_EXISTS);
        }
    }

    @Override
    public MeetingDO getMeeting(Long id) {
        return meetingMapper.selectById(id);
    }

    @Override
    public List<MeetingDO> getMeetingList(Collection<Long> ids) {
        return meetingMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<MeetingDO> getMeetingPage(MeetingPageReqVO pageReqVO) {
        return meetingMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MeetingDO> getMeetingList(MeetingExportReqVO exportReqVO) {
        return meetingMapper.selectList(exportReqVO);
    }

    @Override
    public List<MeetingDO> getMeetingList() {
        return meetingMapper.selectList();
    }

}
