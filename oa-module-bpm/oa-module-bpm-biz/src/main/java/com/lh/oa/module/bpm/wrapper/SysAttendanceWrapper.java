package com.lh.oa.module.bpm.wrapper;

import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.system.api.sysAttendanceRule.SysAttendanceRecordApi;
import com.lh.oa.module.system.api.sysAttendanceRule.SysAttendanceRuleApi;
import com.lh.oa.module.system.api.sysAttendanceRule.to.SysAttendanceRecordQueryParam;
import com.lh.oa.module.system.api.sysAttendanceRule.to.SysAttendanceRuleTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2023/10/31
 */
@Component
@Slf4j
public class SysAttendanceWrapper {

    @Resource
    private SysAttendanceRuleApi sysAttendanceRuleApi;

    @Resource
    private SysAttendanceRecordApi sysAttendanceRecordApi;

    /**
     * 通过项目id获取项目考勤规则
     *
     * @param projectIds 项目ids
     * @return 项目考勤规则信息
     */
    public List<SysAttendanceRuleTO> getAttendanceRuleByProjectId(Set<Integer> projectIds) {
        log.info("通过项目ids获取项目考勤规则, projectIds:{}", projectIds);
        CommonResult<List<SysAttendanceRuleTO>> ruleResult = sysAttendanceRuleApi.getAttendanceRuleByProjectIds(projectIds);
        log.info("通过项目ids获取项目考勤规则-获得结果, projectIds:{}, result:{}", projectIds, JsonUtils.toJsonString(ruleResult));
        ExceptionThrowUtils.throwIfTrue(!ruleResult.isSuccess() || Objects.isNull(ruleResult.getData()), "系统内部错误，请联系管理员");
        return ruleResult.getData();
    }

    /**
     * 通过用户ids和项目ids获取项目考勤记录
     *
     * @param param 考勤参数
     * @return 项目考勤记录信息
     */
    public Map<Integer, Map<Integer, List<Long>>> getAttendanceDateList(SysAttendanceRecordQueryParam param) {
        log.info("通过用户ids和项目ids获取项目考勤记录, param:{}", JsonUtils.toJsonString(param));
        CommonResult<Map<Integer, Map<Integer, List<Long>>>> recordResult = sysAttendanceRecordApi.getRecentlyMonthAttendanceDateList(param);
        log.info("通过用户ids和项目ids获取项目考勤记录-获得结果, param:{}, result:{}", JsonUtils.toJsonString(param), JsonUtils.toJsonString(recordResult));
        ExceptionThrowUtils.throwIfTrue(!recordResult.isSuccess() || Objects.isNull(recordResult.getData()), "系统内部错误，请联系管理员");
        return recordResult.getData();
    }

}
