package com.lh.oa.module.system.controller.admin.information;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.controller.admin.information.vo.*;
import com.lh.oa.module.system.convert.information.InformationConvert;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;
import com.lh.oa.module.system.service.information.InformationService;
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

@Tag(name = "管理后台 - 员工信息")
@RestController
@RequestMapping("/system/information")
@Validated
public class InformationController {

    @Resource
    private InformationService informationService;

    @PostMapping("/create")
    //@Operation(summary = "创建员工信息")
    @PreAuthorize("@ss.hasPermission('system:information:create')")
    public CommonResult<Long> createInformation(@Valid @RequestBody InformationCreateReqVO createReqVO) {
        return success(informationService.createInformation(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新员工信息")
    @PreAuthorize("@ss.hasPermission('system:information:update')")
    public CommonResult<Boolean> updateInformation(@Valid @RequestBody InformationUpdateReqVO updateReqVO) {
        informationService.updateInformation(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除员工信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:information:delete')")
    public CommonResult<Boolean> deleteInformation(@RequestParam("id") Long id) {
        informationService.deleteInformation(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得员工信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:information:query')")
    public CommonResult<InformationDO> getInformation(@RequestParam("id") Long id) {
        InformationDO information = informationService.getInformation(id);
        return success(information);
    }

    @GetMapping("/list")
    //@Operation(summary = "获得员工信息列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:information:query')")
    public CommonResult<List<InformationRespVO>> getInformationList(@RequestParam("ids") Collection<Long> ids) {
        List<InformationDO> list = informationService.getInformationList(ids);
        return success(InformationConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得员工信息分页")
    @PreAuthorize("@ss.hasPermission('system:information:query')")
    public CommonResult<PageResult<InformationDO>> getInformationPage(@Valid InformationPageReqVO pageVO) {
        PageResult<InformationDO> pageResult = informationService.getInformationPage(pageVO);
        return success(pageResult);
    }

    @GetMapping("/page-project")
    //@Operation(summary = "获得员工项目信息")
    @PreAuthorize("@ss.hasPermission('system:information:query')")
    public CommonResult<List<UserProjectDO>> getInforProject(@Valid InformationPageReqVO pageVO) {
        return success(informationService.getInforProject(pageVO));
    }

    @GetMapping("/export-excel")
    //@Operation(summary = "导出员工信息 Excel")
    @PreAuthorize("@ss.hasPermission('system:information:export')")
    public void exportInformationExcel(@Valid InformationExportReqVO exportReqVO,
                                       HttpServletResponse response) throws IOException {
        List<InformationDO> list = informationService.getInformationList(exportReqVO);
        // 导出 Excel
        List<InformationExcelVO> datas = InformationConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "员工信息.xls", "数据", InformationExcelVO.class, datas);
    }

}
