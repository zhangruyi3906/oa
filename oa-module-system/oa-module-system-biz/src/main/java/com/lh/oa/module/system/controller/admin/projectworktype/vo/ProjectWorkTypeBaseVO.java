package com.lh.oa.module.system.controller.admin.projectworktype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

/**
 * 项目工种 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ProjectWorkTypeBaseVO {

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目ID不能为空")
    private Integer projectId;

    @Schema(description = "代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "代码不能为空")
    private String code;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "名称不能为空")
    private String name;

    @Schema(description = "来源")
    private String dataSource;

    @Schema(description = "排序")
    private Integer orderNumber;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "创建时间不能为空")
    private Integer createdTime;

    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "创建人不能为空")
    private Integer createdBy;

    @Schema(description = "修改时间")
    private Integer modifiedTime;

    @Schema(description = "修改人")
    private Integer modifiedBy;

    @Schema(description = "统一标志")
    private Integer flag;

}
