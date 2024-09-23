package com.lh.oa.module.bpm.controller.admin.task;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.bpm.controller.admin.task.vo.copy.BpmCopy;
import com.lh.oa.module.bpm.service.task.BpmCopyService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@RestController
@RequestMapping("/bpm/copy")
public class BpmCopyController {
    @Resource
    private BpmCopyService bpmCopyService;
    @GetMapping
    public BpmCopy get(@Param("copy_id") Long id){
        BpmCopy bpmCopy = bpmCopyService.queryById(id);
        return bpmCopy;
    }

    @GetMapping("/read")
    @PermitAll
    public CommonResult<Boolean> readTodoTask(@RequestParam("copyId") Long copyId) {
        bpmCopyService.readCopy(copyId);
        return success(true);
    }

    @GetMapping("/unreadCount")
    @PermitAll
    public CommonResult<Integer> unreadCount(@RequestParam(required = false) Long userId) {
        Integer count = bpmCopyService.unreadCount(userId == 0 ? getLoginUserId() : userId);
        return success(count);
    }
}
