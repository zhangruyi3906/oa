package com.lh.oa.module.system.controller.admin.meeting;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.meeting.vo.*;
import com.lh.oa.module.system.convert.meeting.MeetingConvert;
import com.lh.oa.module.system.dal.dataobject.meeting.MeetingDO;
import com.lh.oa.module.system.service.meeting.MeetingService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 会议组织")
@RestController
@RequestMapping("/system/meeting")
@Validated
public class MeetingController {

    @Resource
    private MeetingService meetingService;

    @PostMapping("/create")
    //@Operation(summary = "创建会议组织")
    @PreAuthorize("@ss.hasPermission('system:meeting:create')")
    public CommonResult<Long> createMeeting(@Valid @RequestBody MeetingCreateReqVO createReqVO) {
        return success(meetingService.createMeeting(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新会议组织")
    @PreAuthorize("@ss.hasPermission('system:meeting:update')")
    public CommonResult<Boolean> updateMeeting(@Valid @RequestBody MeetingUpdateReqVO updateReqVO) {
        meetingService.updateMeeting(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除会议组织")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:meeting:delete')")
    public CommonResult<Boolean> deleteMeeting(@RequestParam("id") Long id) {
        meetingService.deleteMeeting(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得会议组织")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:meeting:query')")
    public CommonResult<MeetingRespVO> getMeeting(@RequestParam("id") Long id) {
        MeetingDO meeting = meetingService.getMeeting(id);
        return success(MeetingConvert.INSTANCE.convert(meeting));
    }

    @GetMapping("/list")
    //@Operation(summary = "获得会议组织列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:meeting:query')")
    public CommonResult<List<MeetingRespVO>> getMeetingList(@RequestParam("ids") Collection<Long> ids) {
        List<MeetingDO> list = meetingService.getMeetingList(ids);
        return success(MeetingConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得会议组织分页")
    @PreAuthorize("@ss.hasPermission('system:meeting:query')")
    public CommonResult<PageResult<MeetingRespVO>> getMeetingPage(@Valid MeetingPageReqVO pageVO) {
        PageResult<MeetingDO> pageResult = meetingService.getMeetingPage(pageVO);
        return success(MeetingConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出会议组织 Excel")
    @PreAuthorize("@ss.hasPermission('system:meeting:export')")
    //@Operation(type = EXPORT)
    public void exportMeetingExcel(@Valid MeetingExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<MeetingDO> list = meetingService.getMeetingList(exportReqVO);
        // 导出 Excel
        List<MeetingExcelVO> datas = MeetingConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "会议组织.xls", "数据", MeetingExcelVO.class, datas);
    }


    @GetMapping("/dayList")
    //@Operation(summary = "根据日期获得会议列表")
    @PreAuthorize("@ss.hasPermission('system:schedule:query')")
    public CommonResult<List<MeetingDayVO>> getMeetList() {
        List<MeetingDO> meetingDOS = meetingService.getMeetingList();
        Map<Date, List<MeetingDO>> collect = meetingDOS.stream().collect(Collectors.groupingBy(MeetingDO::getStartTime));
        List<MeetingDayVO> collect1 = collect.entrySet().stream()
                .map(s -> {
                    MeetingDayVO meetingDayVO = new MeetingDayVO();
                    meetingDayVO.setMeetingList(s.getValue());
                    meetingDayVO.setMeetingDate(s.getKey());
                    return meetingDayVO;
                })
                .collect(Collectors.toList());
        return success(collect1);
    }

}
