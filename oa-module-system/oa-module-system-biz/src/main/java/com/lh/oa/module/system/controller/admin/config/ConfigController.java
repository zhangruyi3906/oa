package com.lh.oa.module.system.controller.admin.config;


import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.framework.operatelog.core.annotations.OperateLog;
import com.lh.oa.module.system.controller.admin.config.vo.*;
import com.lh.oa.module.system.convert.config.ConfigConvert;
import com.lh.oa.module.system.dal.dataobject.config.ConfigDO;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.service.config.ConfigService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name =  "管理后台 - 参数配置")
@RestController
@RequestMapping("/system/config")
@Validated
public class ConfigController {

    @Resource
    private ConfigService configService;

    @PostMapping("/create")
    //@Operation(summary = "创建参数配置")
    @PreAuthorize("@ss.hasPermission('infra:config:create')")
    public CommonResult<Long> createConfig(@Valid @RequestBody ConfigCreateReqVO reqVO) {
        return success(configService.createConfig(reqVO));
    }

    @PutMapping("/update")
    //@Operation(summary = "修改参数配置")
    @PreAuthorize("@ss.hasPermission('infra:config:update')")
    public CommonResult<Boolean> updateConfig(@Valid @RequestBody ConfigUpdateReqVO reqVO) {
        configService.updateConfig(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    //@Operation(summary = "删除参数配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:config:delete')")
    public CommonResult<Boolean> deleteConfig(@RequestParam("id") Long id) {
        configService.deleteConfig(id);
        return success(true);
    }

    @GetMapping(value = "/get")
    //@Operation(summary = "获得参数配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:config:query')")
    public CommonResult<ConfigRespVO> getConfig(@RequestParam("id") Long id) {
        return success(ConfigConvert.INSTANCE.convert(configService.getConfig(id)));
    }

    @GetMapping(value = "/get-value-by-key")
    //@Operation(summary = "根据参数键名查询参数值", description = "不可见的配置，不允许返回给前端")
    @Parameter(name = "key", description = "参数键", required = true, example = "yunai.biz.username")
    public CommonResult<String> getConfigKey(@RequestParam("key") String key) {
        ConfigDO config = configService.getConfigByKey(key);
        if (config == null) {
            return success(null);
        }
        if (!config.getVisible()) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CONFIG_GET_VALUE_ERROR_IF_VISIBLE);
        }
        return success(config.getValue());
    }

    @GetMapping("/page")
    //@Operation(summary = "获取参数配置分页")
    @PreAuthorize("@ss.hasPermission('infra:config:query')")
    public CommonResult<PageResult<ConfigRespVO>> getConfigPage(@Valid ConfigPageReqVO reqVO) {
        PageResult<ConfigDO> page = configService.getConfigPage(reqVO);
        return success(ConfigConvert.INSTANCE.convertPage(page));
    }

    @GetMapping("/export")
    //@Operation(summary = "导出参数配置")
    @PreAuthorize("@ss.hasPermission('infra:config:export')")
    @OperateLog(type = EXPORT)
    public void exportConfig(@Valid ConfigExportReqVO reqVO,
                             HttpServletResponse response) throws IOException {
        List<ConfigDO> list = configService.getConfigList(reqVO);
        // 拼接数据
        List<ConfigExcelVO> datas = ConfigConvert.INSTANCE.convertList(list);
        // 输出
        ExcelUtils.write(response, "参数配置.xls", "数据", ConfigExcelVO.class, datas);
    }

}
