package com.lh.oa.module.system.controller.admin.projectworktype.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 项目工种 Excel 导出 Request VO，参数和 ProjectWorkTypePageReqVO 是一致的")
@Data
public class ProjectWorkTypeExportReqVO {

    @Schema(description = "项目ID")
    private Integer projectId;

    @Schema(description = "代码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "来源")
    private String dataSource;

    @Schema(description = "排序")
    private Integer orderNumber;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] createdTime;

    @Schema(description = "创建人")
    private Integer createdBy;

    @Schema(description = "修改时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] modifiedTime;

    @Schema(description = "修改人")
    private Integer modifiedBy;

    @Schema(description = "统一标志")
    private Integer flag;

}
