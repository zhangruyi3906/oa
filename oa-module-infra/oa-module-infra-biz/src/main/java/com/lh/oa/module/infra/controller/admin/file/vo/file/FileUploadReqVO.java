package com.lh.oa.module.infra.controller.admin.file.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Schema(description =  "管理后台 - 上传文件 Request VO")
@Data
public class FileUploadReqVO {

    @Schema(description = "文件附件", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "文件附件不能为空")
    private MultipartFile file;

    @Schema(description = "文件路径")
    private String path;

    @Schema(description = "是否为个人可公开文件", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "是否为个人可公开文件不能为空")
    private Boolean userPublic;

    @Schema(description = "是否为部门可公开文件", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "是否为部门可公开文件不能为空")
    private Boolean deptPublic;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "分类名称不能为空")
    private String typeName;

    @Schema(description = "")
    private Integer type;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "分类枚举")
    private Integer typeEn;

    @Schema(description = "是否为项目可公开文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean projectPublic;

    @Schema(description = "项目ID")
    private Long projectId;
}