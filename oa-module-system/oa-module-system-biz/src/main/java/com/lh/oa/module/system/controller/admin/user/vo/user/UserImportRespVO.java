package com.lh.oa.module.system.controller.admin.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 用户导入 Response VO")
@Data
public class UserImportRespVO implements Serializable {

    @Schema(description = "创建成功的用户名数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> successList;

    @Schema(description = "导入失败的用户集合，key 为用户名，value 为失败原因", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, String> failMap;

    public UserImportRespVO() {
    }

    public UserImportRespVO(List<String> successList, Map<String, String> failMap) {
        this.successList = successList;
        this.failMap = failMap;
    }
}