package com.lh.oa.module.report.controller.admin.goview.vo.project;

import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.validation.InEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

//@Schema(description = "管理后台 - GoView 项目更新 Request VO")
@Data
public class GoViewProjectUpdateReqVO {

//    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "18993")
    @NotNull(message = "编号不能为空")
    private Long id;

//    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    private String name;

//    @Schema(description = "发布状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(value = CommonStatusEnum.class, message = "发布状态必须是 {value}")
    private Integer status;

//    @Schema(description = "报表内容") // JSON 格式
    private String content;

//    @Schema(description = "预览图片 URL", example = "https://www.iocoder.cn")
    private String picUrl;

//    @Schema(description = "项目备注", example = "你猜")
    private String remark;

}
