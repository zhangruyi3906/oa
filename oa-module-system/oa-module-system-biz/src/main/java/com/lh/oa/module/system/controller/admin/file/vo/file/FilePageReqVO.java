package com.lh.oa.module.system.controller.admin.file.vo.file;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 文件分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FilePageReqVO extends PageParam {

    @Schema(description = "文件路径,模糊匹配", example = "yudao")
    private String path;

    @Schema(description = "文件类型,模糊匹配", example = "jpg")
    private String type;

    @Schema(description = "创建时间", example = "[2022-07-01 00:00:00, 2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "创建时间", example = "时间戳")
    private Long startTime;

    @Schema(description = "创建时间", example = "时间戳")
    private Long endTime;
//
    @Schema(description = "是否为个人可公开文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean userPublic;

    @Schema(description = "是否为部门可公开文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean deptPublic;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String typeName;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "分类枚举")
    private Integer typeEn;

    @Schema(description = "是否为部门可公开文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean projectPublic;

    @Schema(description = "部门ID")
    private Long projectId;

    @Schema(description = "源文件类型")
    private String source;

    @Schema(description = "客户名字")
    private String customerName;

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "流程实例id Set")
    private Set<String> processInstanceIdSet;

    @Schema(description = "流程名")
    private String instanceName;

}