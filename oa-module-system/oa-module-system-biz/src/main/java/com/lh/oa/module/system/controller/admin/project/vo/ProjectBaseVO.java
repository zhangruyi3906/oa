package com.lh.oa.module.system.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import javax.validation.constraints.*;

/**
 * 项目 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ProjectBaseVO {

    @Schema(description = "所属组织ID")
    private Integer orgId;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "名称不能为空")
    private String name;

    @Schema(description = "简称")
    private String simpleName;

    @Schema(description = "简介")
    private String simpleDescription;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "类型不能为空")
    private String type;

    private String typeVal;

    @Schema(description = "效果图ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "效果图ID不能为空")
    private Integer effectGraphFileId;

    @Schema(description = "效果图URL")
    private String effectGraphFileUrl;

    @Schema(description = "平面图ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "平面图ID不能为空")
    private Integer planarGraphFileId;

    @Schema(description = "平面图URL")
    private String planarGraphFileUrl;

    @Schema(description = "计划开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划开始时间不能为空")
    private Integer planStartTime;

    @Schema(description = "计划结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划结束时间不能为空")
    private Integer planEndTime;

    @Schema(description = "负责人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "负责人不能为空")
    private String directorName;

    @Schema(description = "负责人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "负责人电话不能为空")
    private String directorMobile;

    @Schema(description = "地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "地址不能为空")
    private String address;

    @Schema(description = "经度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    @Schema(description = "纬度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    @Schema(description = "施工单位")
    private String constructUnit;

    @Schema(description = "建设单位")
    private String workUnit;

    @Schema(description = "监理单位")
    private String superviseUnit;

    @Schema(description = "设计单位")
    private String designUnit;

    @Schema(description = "勘察单位")
    private String surveyUnit;

    @Schema(description = "微官网ID")
    private Integer microWebsiteId;

    @Schema(description = "排序")
    private Integer orderNumber;


    @Schema(description = "修改时间")
    private Integer modifiedTime;

    @Schema(description = "修改人")
    private Integer modifiedBy;

    @Schema(description = "统一标志")
    private Integer flag;

    @Schema(description = "是否禁用")
    private Boolean isDisabled;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "完成日期")
    private Date finishDate;

    @Schema(description = "项目分类",defaultValue = "SYSTEM_PROJECT")
    private String category;

}
