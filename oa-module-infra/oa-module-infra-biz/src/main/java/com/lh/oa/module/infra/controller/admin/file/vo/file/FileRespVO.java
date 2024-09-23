package com.lh.oa.module.infra.controller.admin.file.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 文件 Response VO,不返回 content 字段，太大")
@Data
public class FileRespVO {

    @Schema(description = "文件编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "文件路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "oa.jpg")
    private String path;

    @Schema(description = "文件 URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/yudao.jpg")
    private String url;

    @Schema(description = "文件类型", example = "jpg")
    private String type;

    @Schema(description = "文件大小", example = "2048", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer size;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "是否为个人可公开文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean userPublic;

    @Schema(description = "是否为部门可公开文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean deptPublic;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "分类名称不能为空")
    private String typeName;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "分类枚举")
    private Integer typeEn;

    @Schema(description = "是否为部门可公开文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean projectPublic;

    @Schema(description = "部门ID")
    private Long projectId;

}