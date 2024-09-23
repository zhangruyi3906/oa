package com.lh.oa.module.system.controller.admin.wrapper;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceExtService;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceParam;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tanghanlin
 * @since 2023/10/28
 */
@Slf4j
@Component
public class ProcessInstanceExtWrapper {

    @Autowired
    private BpmProcessInstanceExtService bpmProcessInstanceExtService;

    public List<BpmProcessInstanceTo> getProcessByUserIdAndMonth(Set<Long> userIds, Integer attendMonth, String processName) {
        log.info("获取用户发起的流程列表, userIds:{}, attendMonth:{}, processName:{}", userIds, attendMonth, processName);
        BpmProcessInstanceParam param = new BpmProcessInstanceParam(userIds, attendMonth, processName);
        CommonResult<List<BpmProcessInstanceTo>> processResult = bpmProcessInstanceExtService.getProcessByUserIdAndMonth(param);
        log.info("获取用户发起的流程列表-返回结果，param:{}, result:{}",
                JsonUtils.toJsonString(param), JsonUtils.toJsonString(processResult.getData().stream().map(BpmProcessInstanceTo::getId).collect(Collectors.toSet())));
        if (!processResult.isSuccess() || CollectionUtils.isEmpty(processResult.getData())) {
            return Collections.emptyList();
        }
        return processResult.getData();
    }

}
