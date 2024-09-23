package com.lh.oa.module.system.controller.admin.meetingcontent;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.*;
import com.lh.oa.module.system.controller.admin.meetingcontent.vo.*;
import com.lh.oa.module.system.convert.meetingcontent.MeetingContentConvert;
import com.lh.oa.module.system.dal.dataobject.meetingcontent.MeetingContentDO;
import com.lh.oa.module.system.service.meetingcontent.MeetingContentService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 会议记录")
@RestController
@RequestMapping("/system/meeting-content")
@Validated
public class MeetingContentController {

    @Resource
    private MeetingContentService meetingContentService;

    @PostMapping("/create")
    //@Operation(summary = "创建会议记录")
    @PreAuthorize("@ss.hasPermission('system:meeting-content:create')")
    public CommonResult<Long> createMeetingContent(@Valid @RequestBody MeetingContentCreateReqVO createReqVO) {
        return success(meetingContentService.createMeetingContent(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新会议记录")
    @PreAuthorize("@ss.hasPermission('system:meeting-content:update')")
    public CommonResult<Boolean> updateMeetingContent(@Valid @RequestBody MeetingContentUpdateReqVO updateReqVO) {
        meetingContentService.updateMeetingContent(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除会议记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:meeting-content:delete')")
    public CommonResult<Boolean> deleteMeetingContent(@RequestParam("id") Long id) {
        meetingContentService.deleteMeetingContent(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得会议记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:meeting-content:query')")
    public CommonResult<MeetingContentRespVO> getMeetingContent(@RequestParam("id") Long id) {
        MeetingContentDO meetingContent = meetingContentService.getMeetingContent(id);
        return success(MeetingContentConvert.INSTANCE.convert(meetingContent));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得会议记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:meeting-content:query')")
    public CommonResult<List<MeetingContentRespVO>> getMeetingContentList(@RequestParam("ids") Collection<Long> ids) {
        List<MeetingContentDO> list = meetingContentService.getMeetingContentList(ids);
        return success(MeetingContentConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得会议记录分页")
    @PreAuthorize("@ss.hasPermission('system:meeting-content:query')")
    public CommonResult<PageResult<MeetingContentRespVO>> getMeetingContentPage(@Valid MeetingContentPageReqVO pageVO) {
        PageResult<MeetingContentDO> pageResult = meetingContentService.getMeetingContentPage(pageVO);
        return success(MeetingContentConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出会议记录 Excel")
    @PreAuthorize("@ss.hasPermission('system:meeting-content:export')")
    //@Operation(type = EXPORT)
    public void exportMeetingContentExcel(@Valid MeetingContentExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<MeetingContentDO> list = meetingContentService.getMeetingContentList(exportReqVO);
        // 导出 Excel
        List<MeetingContentExcelVO> datas = MeetingContentConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "会议记录.xls", "数据", MeetingContentExcelVO.class, datas);
    }

}
