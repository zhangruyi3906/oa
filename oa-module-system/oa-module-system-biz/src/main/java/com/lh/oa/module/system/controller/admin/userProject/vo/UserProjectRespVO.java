package com.lh.oa.module.system.controller.admin.userProject.vo;

import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 人员项目 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserProjectRespVO extends UserProjectBaseVO {

    @Schema(description = "人员项目表id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    private LocalDateTime createTime;


    private AdminUserDO adminUserDO;


    private InformationDO informationDO;

}
