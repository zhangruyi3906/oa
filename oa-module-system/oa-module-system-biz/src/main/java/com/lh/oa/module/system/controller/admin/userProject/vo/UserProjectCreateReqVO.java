package com.lh.oa.module.system.controller.admin.userProject.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - 人员项目创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserProjectCreateReqVO extends UserProjectBaseVO {

    private List<User> list;

    @Schema(description = "菜单")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class User {
        @Schema(description = "人员id")
        @NotNull(message = "人员id不能为空")
        private Long userId;

        @Schema(description = "人员名称")
        @NotNull(message = "人员id不能为空")
        private String userName;
    }
}
