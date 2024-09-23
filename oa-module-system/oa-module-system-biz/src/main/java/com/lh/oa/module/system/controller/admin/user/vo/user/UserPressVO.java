package com.lh.oa.module.system.controller.admin.user.vo.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPressVO {
    private String label;
    private Long value;

    private Long deptId;
    private String deptName;
    private Set<Long> postIds;
    private String postName;
    @Schema(description = "身份证号")
    private String identityCard;
    @Schema(description = "银行")
    private String bankAccount;

    @Schema(description = "银行卡号")
    private String bankAccountNumber;
    @Schema(description = "手机号")
    private String mobile;
    @Schema(description = "性别")
    private String sex;
}
