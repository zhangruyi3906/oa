package com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 补卡流程创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CorrectionCreateReqVO extends CorrectionBaseVO {

}
