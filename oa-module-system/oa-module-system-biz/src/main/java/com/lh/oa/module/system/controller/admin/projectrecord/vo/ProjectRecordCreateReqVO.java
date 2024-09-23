package com.lh.oa.module.system.controller.admin.projectrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 打卡记录创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectRecordCreateReqVO extends ProjectRecordBaseVO{
    private Double latitude;
    private Double longitude;
    private String remarkOut;
    private Long projectId;

    private String photoUrl;

    /**
     * 是否离线打卡，默认在线打卡
     */
    private boolean online = true;
}
