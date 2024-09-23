package com.lh.oa.module.bpm.controller.admin.task;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.task.vo.save.BpmSaveProcessInstancePageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.save.BpmSaveProcessInstanceResVO;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmSaveInstanceDO;
import com.lh.oa.module.bpm.service.task.SaveProcessInstanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name =  "管理后台 - 保存和撤回的流程任务实例")
@RestController
@RequestMapping("/bpm/saveInstance")
@Validated
public class BpmSaveInstanceController {
    @Resource
    private SaveProcessInstanceService saveProcessInstanceService;
    @GetMapping("/my-page")
    public CommonResult<PageResult<BpmSaveProcessInstanceResVO>> getMySaveProcessInstancePage(
            @Valid BpmSaveProcessInstancePageReqVO pageReqVO) {
        return success(saveProcessInstanceService.getMySaveProcessInstancePage(getLoginUserId(), pageReqVO));
    }

    @GetMapping("/get")
    public CommonResult<BpmSaveInstanceDO> getSaveProcessInstance(Long saveId) {
        return success(saveProcessInstanceService.getSaveProcessInstance(saveId));
    }


    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteSaveProcessInstance(Long saveId) {
        saveProcessInstanceService.deleteSaveProcessInstance(saveId);
        return success(true);
    }

}
