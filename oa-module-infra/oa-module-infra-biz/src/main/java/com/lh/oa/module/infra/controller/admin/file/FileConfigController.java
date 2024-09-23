package com.lh.oa.module.infra.controller.admin.file;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.infra.controller.admin.file.vo.config.FileConfigCreateReqVO;
import com.lh.oa.module.infra.controller.admin.file.vo.config.FileConfigPageReqVO;
import com.lh.oa.module.infra.controller.admin.file.vo.config.FileConfigRespVO;
import com.lh.oa.module.infra.controller.admin.file.vo.config.FileConfigUpdateReqVO;
import com.lh.oa.module.infra.convert.file.FileConfigConvert;
import com.lh.oa.module.infra.dal.dataobject.file.FileConfigDO;
import com.lh.oa.module.infra.service.file.FileConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name =  "管理后台 - 文件配置")
@RestController
@RequestMapping("/infra/file-config")
@Validated
public class FileConfigController {

    @Resource
    private FileConfigService fileConfigService;

    @PostMapping("/create")
    //@Operation(summary = "创建文件配置")
    @PreAuthorize("@ss.hasPermission('infra:file-config:create')")
    public CommonResult<Long> createFileConfig(@Valid @RequestBody FileConfigCreateReqVO createReqVO) {
        return success(fileConfigService.createFileConfig(createReqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "更新文件配置")
    @PreAuthorize("@ss.hasPermission('infra:file-config:update')")
    public CommonResult<Boolean> updateFileConfig(@Valid @RequestBody FileConfigUpdateReqVO updateReqVO) {
        fileConfigService.updateFileConfig(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-master")
    //@Operation(summary = "更新文件配置为 Master")
    @PreAuthorize("@ss.hasPermission('infra:file-config:update')")
    public CommonResult<Boolean> updateFileConfigMaster(@RequestParam("id") Long id) {
        fileConfigService.updateFileConfigMaster(id);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除文件配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('infra:file-config:delete')")
    public CommonResult<Boolean> deleteFileConfig(@RequestParam("id") Long id) {
        fileConfigService.deleteFileConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    //@Operation(summary = "获得文件配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:file-config:query')")
    public CommonResult<FileConfigRespVO> getFileConfig(@RequestParam("id") Long id) {
        FileConfigDO fileConfig = fileConfigService.getFileConfig(id);
        return success(FileConfigConvert.INSTANCE.convert(fileConfig));
    }

    @GetMapping("/page")
    //@Operation(summary = "获得文件配置分页")
    @PreAuthorize("@ss.hasPermission('infra:file-config:query')")
    public CommonResult<PageResult<FileConfigRespVO>> getFileConfigPage(@Valid FileConfigPageReqVO pageVO) {
        PageResult<FileConfigDO> pageResult = fileConfigService.getFileConfigPage(pageVO);
        return success(FileConfigConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/test")
    //@Operation(summary = "测试文件配置是否正确")
    @PreAuthorize("@ss.hasPermission('infra:file-config:query')")
    public CommonResult<String> testFileConfig(@RequestParam("id") Long id) throws Exception {
        String url = fileConfigService.testFileConfig(id);
        return success(url);
    }
}