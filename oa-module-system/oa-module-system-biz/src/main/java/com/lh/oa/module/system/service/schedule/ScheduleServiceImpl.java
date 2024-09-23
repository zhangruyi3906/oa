package com.lh.oa.module.system.service.schedule;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.schedule.vo.*;
import com.lh.oa.module.system.convert.schedule.ScheduleConvert;
import com.lh.oa.module.system.dal.dataobject.schedule.ScheduleDO;
import com.lh.oa.module.system.dal.mysql.schedule.ScheduleMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 日程管理 Service 实现类
 *
 * @author
 */
@Service
@Validated
public class
ScheduleServiceImpl implements ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;



    @Override
    public Long createSchedule(ScheduleCreateReqVO createReqVO) {
        Date now = new Date();

        if(createReqVO.getScheStartTime() == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SCHEDULE_START_TIME_NOT_EXIST);
        }
        if(createReqVO.getScheEndTime() == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SCHEDULE_END_TIME_NOT_EXIST);
        }
        if (createReqVO.getScheStartTime().before(now)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SCHEDULE_NOT_BEFORE_NOW);
        }
        if (createReqVO.getScheEndTime().before(createReqVO.getScheStartTime())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SCHEDULE_END_NOT_BEFORE_START);
        }
        Date scheStartTime = createReqVO.getScheStartTime();
        // 插入
        Date expireDateDay = getExpireDateDay(scheStartTime);
        createReqVO.setExpireDateDay(expireDateDay);
        //判断日程是否冲突
        List<ScheduleDO> scheduleDOS = scheduleMapper.selectByTime(createReqVO.getScheStartTime(), createReqVO.getScheEndTime(), getLoginUserId());
        if (!scheduleDOS.isEmpty()) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SCHEDULE_IS_EXISTS);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String expireDate = dateFormat.format(scheStartTime);
        createReqVO.setExpireDate(expireDate);
        createReqVO.setStatus(0);
        createReqVO.setUserId(getLoginUserId());
        ScheduleDO schedule = ScheduleConvert.INSTANCE.convert(createReqVO);
        scheduleMapper.insert(schedule);

        // 返回
        return schedule.getId();
    }

    /**
     * 获取日程日期
     * @param scheStartTime
     * @return
     */
    @NotNull
    private static Date getExpireDateDay(Date scheStartTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(scheStartTime);
        // 将时、分、秒和毫秒部分设置为零
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 获取转换后的日期
        Date expireDateDay = calendar.getTime();
        return expireDateDay;
    }

    @Override
    public void updateSchedule(ScheduleUpdateReqVO updateReqVO) {
        // 校验存在
        validateScheduleExists(updateReqVO.getId());
        Date now = new Date();
        if (updateReqVO.getScheStartTime().before(now)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SCHEDULE_NOT_BEFORE_NOW);
        }
        if (updateReqVO.getScheEndTime().before(updateReqVO.getScheStartTime())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SCHEDULE_END_NOT_BEFORE_START);
        }
        if (updateReqVO.getScheEndTime().before(updateReqVO.getScheStartTime())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SCHEDULE_END_NOT_BEFORE_START);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String expireDate = dateFormat.format(updateReqVO.getScheStartTime());
        updateReqVO.setExpireDate(expireDate);
        // 更新
        ScheduleDO updateObj = ScheduleConvert.INSTANCE.convert(updateReqVO);
        scheduleMapper.updateById(updateObj);
    }

    @Override
    public void deleteSchedule(Long id) {
        // 校验存在
        validateScheduleExists(id);
        // 删除
        scheduleMapper.deleteById(id);
    }

    private void validateScheduleExists(Long id) {
        if (scheduleMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SCHEDULE_NOT_EXISTS);
        }
    }

    @Override
    public ScheduleDO getSchedule(Long id) {
        return scheduleMapper.selectById(id);
    }

    @Override
    public List<ScheduleDO> getScheduleList(ScheduleRespVO respVo) {
        respVo.setUserId(getLoginUserId());
        return scheduleMapper.selectListByMonth(respVo);
    }

    @Override
    public PageResult<ScheduleDO> getSchedulePage(SchedulePageReqVO pageReqVO) {
        if (pageReqVO.getUserId() == null || "".equals(pageReqVO.getUserId())) {
            pageReqVO.setUserId(getLoginUserId());
        }
        return scheduleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScheduleDO> getScheduleList(ScheduleExportReqVO exportReqVO) {
        return scheduleMapper.selectList(exportReqVO);
    }

    @Override
    public List<ScheduleDO> selectByTime(Date start, Date end, Long userId) {
        return scheduleMapper.selectByTime(start, end, userId);
    }


}
