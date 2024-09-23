package com.lh.oa.module.system.controller.admin.userProject.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 人员项目 获取项目人员 Response VO")
@Data
public class UserProjectUserVO {

    @Schema(description = "人员id")
    private Long userId;

    @Schema(description = "人员名称")
    private String userName;
}
