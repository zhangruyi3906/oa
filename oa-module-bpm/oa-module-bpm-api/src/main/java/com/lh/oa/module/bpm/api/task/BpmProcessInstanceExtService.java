package com.lh.oa.module.bpm.api.task;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceParam;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(value = "bpm-server", path = "/admin-api/bpm/process-instance", contextId = "BpmProcessInstanceExtService")
public interface BpmProcessInstanceExtService {

    @PostMapping("/getProcessByUserIdAndMonth")
    CommonResult<List<BpmProcessInstanceTo>> getProcessByUserIdAndMonth(@RequestBody BpmProcessInstanceParam param);

    @PostMapping("/getProcessByParamPage")
    CommonResult<PageResult<BpmProcessInstanceTo>> getProcessByParamPage(@RequestBody BpmProcessInstanceParam param);

    /**
     * 获取用户的所有涉及的流程实例编号
     *
     * @param userId 用户编号
     * @return 实例的编号
     */
    @GetMapping("/getAllProcessInstanceIdsByUserId")
    CommonResult<Set<String>> getAllProcessInstanceIdsByUserId(@RequestParam("userId") Long userId);

}