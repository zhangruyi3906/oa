package com.lh.oa.module.system.controller.admin.schedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 日程管理 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ScheduleBaseVO {

    @Schema(description = "日程标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "日程描述")
    private String description;

    @Schema(description = "日程时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime expireTime;

    @Schema(description = "是否过期")
    private Boolean expired;

    @Schema(description = "创建者id", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "创建者id不能为空")
    private Long userId;

    @Schema(description = "日程日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private String expireDate;

    private Date expireDateDay;

    @Schema(description = "开始时间")
    private Date scheStartTime;

    @Schema(description = "结束时间")
    private Date scheEndTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "日程状态")
    private Integer status;


}
