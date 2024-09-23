package com.lh.oa.module.system.controller.admin.dept.vo.dept;

import com.lh.oa.module.system.enums.DeptTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 部门列表 Request VO")
@Data
public class DeptListReqVO {

    @Schema(description = "部门名称,模糊匹配", example = "芋道")
    private String name;

    private DeptTypeEnum type;

    @Schema(description = "展示状态,参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;

    public String getTypeVal() {
        return type == null ? null : type.getVal();
    }
}